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
package net.hasor.website.core;
import net.hasor.core.ApiBinder;
import net.hasor.core.AppContext;
import net.hasor.core.LifeModule;
import net.hasor.core.Settings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * 阿里云 OSS
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
public class AliyunModule implements LifeModule {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private AliyunOSSClient ossClient;
    @Override
    public void loadModule(ApiBinder apiBinder) throws Throwable {
        Settings settings = apiBinder.getEnvironment().getSettings();
        String endpoint = settings.getString("aliyun.endpoint.oss");
        String accessKey = settings.getString("aliyun.accessKey");
        String accessKeySecret = settings.getString("aliyun.accessKeySecret");
        //
        try {
            //Class<?>[] clt = { ThrowableInformation.class, DefaultThrowableRenderer.class };
            //com.aliyun.oss.common.utils.LogUtils.getLog().error("TEST", new Exception());
            //
            logger.error("aliyun:oss -> endpoint : {} , accessKey= {} ,accessKeySecret = ******", endpoint, accessKey);
            this.ossClient = new AliyunOSSClient(endpoint, accessKey, accessKeySecret);
            this.ossClient.setBucketName(settings.getString("aliyun.bucketName", "www-hasor"));
            this.ossClient.setBucketPath(settings.getString("aliyun.bucketPath", "/uploader_daily/"));
            apiBinder.bindType(AliyunOSSClient.class).toInstance(this.ossClient);
        } catch (Exception e) {
            logger.error("aliyun:oss -> error : {}", e.getMessage(), e);
            throw e;
        }
    }
    @Override
    public void onStart(AppContext appContext) throws Throwable {
        //
    }
    @Override
    public void onStop(AppContext appContext) throws Throwable {
        logger.error("aliyun:oss -> shutdown.");
        if (this.ossClient != null) {
            this.ossClient.shutdown();
        }
    }
}