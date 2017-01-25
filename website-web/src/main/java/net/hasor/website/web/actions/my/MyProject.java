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
import net.hasor.core.Inject;
import net.hasor.web.Invoker;
import net.hasor.web.annotation.MappingTo;
import net.hasor.web.annotation.ReqParam;
import net.hasor.website.domain.Owner;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.ProjectVersionDO;
import net.hasor.website.manager.ProjectManager;
import net.hasor.website.utils.LoggerUtils;
import net.hasor.website.web.core.Action;
import org.more.bizcommon.Result;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 我的项目列表
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/my/projects.htm")
public class MyProject extends Action {
    @Inject
    private ProjectManager projectManager;
    //
    public void execute(@ReqParam("curProjectID") long curProjectID, Invoker data) throws IOException {
        // .need login
        if (needLogin()) {
            return;
        }
        //
        // .我的项目
        Owner userOwner = getUser();
        Result<List<ProjectInfoDO>> result = this.projectManager.queryMyProjectList(userOwner);
        if (!result.isSuccess()) {
            logger.error(LoggerUtils.create("ERROR_003_0008")//
                    .addLog("result", result) //
                    .addLog("currentUserID", userOwner.getOwnerID())//
                    .addLog("errorMessage", "queryMyProjectList -> " + result.firstMessage())//
                    .toJson());
            sendError(result.firstMessage());
            return;
        }
        List<ProjectInfoDO> list = result.getResult();
        if (list == null) {
            list = new ArrayList<ProjectInfoDO>(0);
        }
        putData("projectList", list);
        //
        // .当前项目
        ProjectInfoDO infoDO = null;
        for (ProjectInfoDO info : list) {
            if (curProjectID == info.getId()) {
                infoDO = info;
            }
        }
        if (infoDO == null && !list.isEmpty()) {
            infoDO = list.get(0);
        }
        putData("project", infoDO);
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
    }
}