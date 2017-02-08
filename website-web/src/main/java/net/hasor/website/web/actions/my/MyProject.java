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
package net.hasor.website.web.actions.my;
import net.hasor.web.Invoker;
import net.hasor.web.annotation.MappingTo;
import net.hasor.web.annotation.ReqParam;
import net.hasor.website.domain.Owner;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.ProjectVersionDO;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.utils.LoggerUtils;
import org.more.bizcommon.Message;
import org.more.bizcommon.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 我的项目，项目详情。
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/my/projects.htm")
public class MyProject extends BaseMyProject {
    //
    public void execute(@ReqParam("curProjectID") long curProjectID, Invoker data) throws IOException {
        // .need login
        if (needLogin()) {
            return;
        }
        // .need login
        if (!super.fillProjectInfo(curProjectID)) {
            return;
        }
        // .页面必须的列表元素
        super.fillInfo();
        //
        // .查询curProjectID项目
        Result<ProjectInfoDO> result = this.projectManager.queryProjectByID(curProjectID);
        int notExist = ErrorCodes.P_PROJECT_NOT_EXIST.getMsg().getType();
        Message resultMsg = result.firstMessage();
        if (resultMsg != null && resultMsg.getType() != notExist) {
            logger.error(LoggerUtils.create("ERROR_006_0011")//
                    .addLog("result", result) //
                    .addLog("currentUserID", curProjectID)//
                    .addLog("errorMessage", resultMsg)//
                    .toJson());
            sendError(resultMsg);
            return;
        }
        // .判断项目归属
        Owner userOwner = getUser();
        ProjectInfoDO infoDO = result.getResult();
        if (infoDO != null) {
            if (infoDO.getOwnerID() != userOwner.getOwnerID() || !infoDO.getOwnerType().equals(userOwner.getOwnerType())) {
                logger.error(LoggerUtils.create("ERROR_006_0013")//
                        .addLog("result", result) //
                        .addLog("currentUserID", curProjectID)//
                        .addLog("errorMessage", resultMsg)//
                        .toJson());
                sendError(resultMsg);
                return;
            }
        }
        //
        // .版本列表
        List<ProjectVersionDO> versionList = new ArrayList<ProjectVersionDO>(0);
        if (infoDO != null) {
            Result<List<ProjectVersionDO>> versionResult = this.projectManager.queryVersionListByProject(infoDO.getId());
            if (!versionResult.isSuccess()) {
                logger.error(LoggerUtils.create("ERROR_003_0008")//
                        .addLog("result", versionResult) //
                        .addLog("currentUserID", userOwner.getOwnerID())//
                        .addLog("errorMessage", "queryVersionListByProject -> " + versionResult.firstMessage().getMessage())//
                        .toJson());
                putData("versionErrorMessage", versionResult.firstMessage());
            } else {
                versionList = versionResult.getResult();
            }
        }
        putData("versionList", versionList);
        //
    }
}