/*
 * Copyright 2008-2009 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.website.web.actions.uploader;
import com.alibaba.fastjson.JSON;
import com.aliyun.oss.model.PutObjectResult;
import net.hasor.core.Inject;
import net.hasor.restful.api.Async;
import net.hasor.restful.api.MappingTo;
import net.hasor.web.FileItem;
import net.hasor.website.core.AliyunOSSClient;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.manager.EnvironmentConfig;
import net.hasor.website.web.core.Action;
import org.more.bizcommon.Result;
import org.more.bizcommon.ResultDO;
import org.more.bizcommon.log.LogUtils;
import org.more.util.StringUtils;
import org.more.util.io.FilenameUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
/**
 * OAuth : 服务器获取 AccessToken
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@Async
@MappingTo("/uploader/upload_to_temp.do")
public class UploadToTemp extends Action {
    @Inject
    private EnvironmentConfig envConfig;
    @Inject
    private AliyunOSSClient   ossClient;
    //
    public void execute() throws IOException {
        //
        if (!isLogin()) {
            sendError(ErrorCodes.U_NEED_LOGIN.getMsg().getMessage());
            return;
        }
        if (!this.csrfTokenTest()) {
            sendError(ErrorCodes.V_TOKEN_NEED_LOGIN.getMsg().getMessage());
            return;
        }
        //
        FileItem fileItem = null;
        try {
            Integer maxSize = this.envConfig.getMaxSize();
            if (maxSize <= 0) {
                maxSize = null;
            }
            fileItem = this.getOneMultipart("editormd-image-file", maxSize);//500KB
            if (fileItem == null) {
                sendError("没有发现上传的文件。");
                return;
            }
            //
            if (fileItem.isFormField()) {
                sendError("需要上传文件,而非表单。");
                return;
            } else {
                Result<String> saveToAddress = saveToOSS(fileItem, 3);
                if (!saveToAddress.isSuccess()) {
                    sendError("数据保存失败,请重试!");
                    return;
                } else {
                    this.sendResult(saveToAddress.getResult());
                    return;
                }
            }
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_005_0001")//
                    .addLog("fileName", (fileItem != null) ? fileItem.getName() : "")//
                    .addLog("userID", this.getUserID())//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            sendError("上传过程中发生错误:" + e.getMessage());
            return;
        } finally {
            if (fileItem != null) {
                fileItem.deleteOrSkip();
            }
        }
    }
    //
    private Result<String> saveToOSS(FileItem fileItem, int tryCount) throws IOException {
        String dataStr = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String timeStr = new SimpleDateFormat("HHmmss").format(new Date());
        String randomStr = String.valueOf(new Random(System.currentTimeMillis()).nextInt(9999));
        randomStr = StringUtils.leftPad(randomStr, 4, "0");
        String fileName = genPath(Long.parseLong(dataStr + timeStr + randomStr)) + "." + FilenameUtils.getExtension(fileItem.getName());
        String filePath = "/" + ossClient.getBucketPath() + "/" + dataStr + "/" + timeStr;
        //
        String uploaderPath = new File(filePath, fileName).getPath();
        String ossKey = uploaderPath.substring(1);
        String bucketName = this.ossClient.getBucketName();
        //
        try {
            // .3次名称冲突检测
            if (this.ossClient.doesObjectExist(bucketName, ossKey)) {
                return saveToOSS(fileItem, tryCount--);
            } else if (tryCount <= 0) {
                logger.error(LogUtils.create("ERROR_005_0003")//
                        .addLog("bucketName", bucketName)//
                        .addLog("fileName", fileItem.getName())//
                        .addLog("tryCount", tryCount)//
                        .addLog("ossKey", ossKey)//
                        .addLog("userID", this.getUserID())//
                        .addLog("error", "run out of tryCount.")//
                        .toJson());
                return new ResultDO<String>(false);
            }
            // .数据保存
            PutObjectResult result = this.ossClient.putObject(bucketName, ossKey, fileItem.openStream());
            if (result == null) {
                logger.error(LogUtils.create("ERROR_005_0002")//
                        .addLog("bucketName", bucketName)//
                        .addLog("fileName", fileItem.getName())//
                        .addLog("tryCount", tryCount)//
                        .addLog("ossKey", ossKey)//
                        .addLog("userID", this.getUserID())//
                        .addLog("error", "oss result is null.")//
                        .toJson());
                return new ResultDO<String>(false);
            }
            // .返回URL
            String urlPath = this.envConfig.getStaticFilesHost() + "/" + ossKey;
            logger.error("upload : {} -> {}.", bucketName, urlPath);
            return new ResultDO<String>(urlPath).setSuccess(true);
        } catch (Throwable e) {
            logger.error(LogUtils.create("ERROR_005_0001")//
                    .addLog("bucketName", bucketName)//
                    .addLog("fileName", fileItem.getName())//
                    .addLog("tryCount", tryCount)//
                    .addLog("ossKey", ossKey)//
                    .addLog("userID", this.getUserID())//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            return new ResultDO<String>(false).setThrowable(e);
        }
    }
    /**
     * 生成路径算法生成一个Path
     * @param number 参考数字
     */
    public String genPath(long number) {
        StringBuilder buffer = new StringBuilder();
        long b = 0xFFFF;//每个目录下可以拥有的子目录或文件数目。
        long c = number;
        do {
            long m = number % b;
            buffer.append(Long.toHexString(m).toUpperCase());
            c = number / b;
            number = c;
            //
            if (c > 0) {
                buffer.append("_");
            }
        } while (c > 0);
        return buffer.reverse().toString();
    }
    //
    protected void sendError(String value) throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("success", 0);
        jsonMap.put("message", value);
        this.getResponse().getWriter().write("<body>" + JSON.toJSONString(jsonMap) + "</body>");
    }
    protected void sendResult(String url) throws IOException {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("success", 1);
        jsonMap.put("message", true);
        jsonMap.put("url", url);
        this.getResponse().getWriter().write("<body>" + JSON.toJSONString(jsonMap) + "</body>");
    }
}