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
import net.hasor.web.annotation.PathParam;
import net.hasor.web.annotation.ReqParam;
import net.hasor.website.domain.Owner;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.ProjectVersionDO;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.utils.LoggerUtils;
import org.more.bizcommon.Message;
import org.more.bizcommon.Result;

import java.io.IOException;
/**
 * 版本详情 & 版本更新
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/my/updateVersion.{action}")
public class UpdateVersion extends BaseMyProject {
    //
    public void execute(@ReqParam("projectID") long projectID, @ReqParam("versionID") long versionID,//
            @PathParam("action") String action, Invoker data) throws IOException {
        // .need login
        if (!super.fillProjectInfo(projectID)) {
            return;
        }
        //
        if ("do".equalsIgnoreCase(action)) {
            this.doUpdate(projectID, versionID, data);
        }
        this.doShow(projectID, versionID, data);
    }
    private void doShow(long projectID, long versionID, Invoker data) {
        // .查询curProjectID项目
        Result<ProjectInfoDO> result = this.projectManager.queryProjectByID(projectID);
        int notExist = ErrorCodes.P_PROJECT_NOT_EXIST.getMsg().getType();
        Message resultMsg = result.firstMessage();
        if (resultMsg != null && resultMsg.getType() != notExist) {
            logger.error(LoggerUtils.create("ERROR_006_0011")//
                    .addLog("result", result) //
                    .addLog("projectID", projectID)//
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
                        .addLog("projectID", projectID)//
                        .addLog("errorMessage", resultMsg)//
                        .toJson());
                sendError(resultMsg);
                return;
            }
        }
        // .
        if (infoDO == null) {
            sendError(ErrorCodes.P_PROJECT_NOT_EXIST.getMsg());
            return;
        } else {
            putData("project", infoDO);
        }
        //
        // .版本
        Result<ProjectVersionDO> versionResult = this.projectManager.queryVersionByID(projectID, versionID);
        if (!versionResult.isSuccess() || versionResult.getResult() == null) {
            sendError(result.firstMessage());
            return;
        }
        //
        ProjectVersionDO versionDO = versionResult.getResult();
        putData("version", versionDO);
    }
    //
    //
    private void doUpdate(long projectID, long versionID, Invoker data) {
        //
    }
}