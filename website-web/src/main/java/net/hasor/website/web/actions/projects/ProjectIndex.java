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
import net.hasor.web.render.RenderInvoker;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.ProjectVersionDO;
import org.more.bizcommon.Result;
import org.pegdown.Extensions;
import org.pegdown.PegDownProcessor;

import java.util.List;
/**
 * 项目的首页入口
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/projects/{projectID}")
public class ProjectIndex extends BaseProjects {
    //
    public void execute(@PathParam("projectID") long projectID, RenderInvoker data) {
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
        PegDownProcessor processor = new PegDownProcessor(Extensions.ALL);
        String htmlData = processor.markdownToHtml(projectInfo.getPresent());
        projectInfo.setPresent(htmlData);
        //
        Result<List<ProjectVersionDO>> versionResult = super.versionListToIndex(projectID);
        List<ProjectVersionDO> result = versionResult.getResult();
        if (!result.isEmpty()) {
            putData("newVersion", result.get(0));
        }
        //
        data.renderTo("htm", "/projects/projectIndex.htm");
    }
}