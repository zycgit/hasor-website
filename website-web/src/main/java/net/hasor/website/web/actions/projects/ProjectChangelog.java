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
package net.hasor.website.web.actions.projects;
import net.hasor.web.annotation.MappingTo;
import net.hasor.web.annotation.PathParam;
import net.hasor.web.RenderInvoker;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.ProjectVersionDO;
import net.hasor.website.domain.result.Result;
import net.hasor.website.utils.LoggerUtils;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

import java.util.ArrayList;
import java.util.List;
/**
 * 项目的首页入口
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/projects/{projectID}/projectChangelog.htm")
public class ProjectChangelog extends BaseProjects {
    //
    public void execute(RenderInvoker data, @PathParam("projectID") long projectID) {
        //
        Result<ProjectInfoDO> projectResult = this.projectManager.queryProjectByID(projectID);
        if (!projectResult.isSuccess()) {
            sendError(projectResult.firstMessage());
            return;
        }
        //
        ProjectInfoDO projectInfo = projectResult.getResult();
        putData("project", projectInfo);
        //
        // .版本列表
        List<ProjectVersionDO> versionList = new ArrayList<ProjectVersionDO>(0);
        Result<List<ProjectVersionDO>> versionResult = super.versionListToIndex(projectID);
        if (!versionResult.isSuccess()) {
            logger.error(LoggerUtils.create("ERROR_003_0008")//
                    .addLog("result", versionResult) //
                    .addLog("errorMessage", "queryVersionListByProject -> " + versionResult.firstMessage().getMessage())//
                    .toJson());
            putData("versionErrorMessage", versionResult.firstMessage());
        } else {
            versionList = versionResult.getResult();
        }
        //
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);
        for (ProjectVersionDO versionDO : versionList) {
            String htmlData = processor.markdownToHtml(versionDO.getChangelog());
            versionDO.setChangelog(htmlData);
        }
        //
        putData("versionList", versionList);
        //
        //
        data.renderTo("htm", "/projects/projectChangelog.htm");
    }
}