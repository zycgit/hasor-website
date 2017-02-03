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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
/**
 * 新版本
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/my/newVersion.htm")
public class NewVersion extends BaseMyProject {
    //
    public void execute(@ReqParam("projectID") long projectID, Invoker data) throws IOException {
        // .need login
        if (!super.fillProjectInfo(projectID)) {
            return;
        }
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
        //
        // .版本列表
        String newVersion = "0.0.1";
        if (infoDO != null) {
            Result<List<ProjectVersionDO>> versionResult = this.projectManager.queryVersionListByProject(infoDO.getId());
            List<ProjectVersionDO> verList = versionResult.getResult();
            if (versionResult.isSuccess() && verList != null && !verList.isEmpty()) {
                Collections.sort(verList, new Comparator<ProjectVersionDO>() {
                    @Override
                    public int compare(ProjectVersionDO o1, ProjectVersionDO o2) {
                        return -o1.getVersion().compareToIgnoreCase(o2.getVersion());
                    }
                });
                ProjectVersionDO versionDO = verList.get(0);
                String version = versionDO.getVersion();
                newVersion = version;
            }
        }
        putData("newVersion", newVersion);
    }
}