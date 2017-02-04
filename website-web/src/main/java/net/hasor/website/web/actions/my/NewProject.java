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
import net.hasor.web.annotation.*;
import net.hasor.website.domain.Owner;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.enums.ContentFormat;
import net.hasor.website.domain.enums.ProjectStatus;
import net.hasor.website.domain.enums.SourceType;
import net.hasor.website.domain.futures.ProjectFutures;
import net.hasor.website.web.forms.ProjectInfoForm;
import org.more.bizcommon.Result;

import java.io.IOException;
/**
 * 新项目
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/my/newProject.htm")
public class NewProject extends BaseMyProject {
    //
    @Get
    public void execute(@ReqParam("parentID") long parentID, Invoker data) throws IOException {
        // .need login
        if (!super.fillProjectInfo(0L)) {
            return;
        }
        // .页面必须的列表元素
        super.fillInfo();
        //
        Result<ProjectInfoDO> projectByID = this.projectManager.queryProjectByID(parentID);
        //
        Owner userOwner = getUser();
        putData("project", null);//清空默认选择
        putData("parentProject", projectByID.getResult());
        putData("owner", userOwner);
    }
    //
    @Post
    public void newProject(@Params ProjectInfoForm projectInfoDO, Invoker data) throws IOException {
        // .need login
        if (needLogin()) {
            return;
        }
        //
        ProjectInfoDO newProject = new ProjectInfoDO();
        Owner owner = getUser();
        newProject.setOwnerID(owner.getOwnerID());
        newProject.setOwnerType(owner.getOwnerType());
        newProject.setParentID(projectInfoDO.getParentID());
        newProject.setName(projectInfoDO.getName());
        newProject.setSubtitle(projectInfoDO.getSubtitle());
        newProject.setContentFormat(ContentFormat.MD.formType(projectInfoDO.getContentFormatType()));
        newProject.setPresent(projectInfoDO.getPresentContent());
        newProject.setHomePage(projectInfoDO.getHomePage());
        newProject.setDownPage(projectInfoDO.getDownPage());
        newProject.setLanguage(projectInfoDO.getLanguage());
        newProject.setSourceType(SourceType.Open.formType(projectInfoDO.getSourceTypeEnum()));
        newProject.setLicense(projectInfoDO.getLicense());
        newProject.setFutures(new ProjectFutures());
        newProject.setStatus(ProjectStatus.Init);
        //
        Result<Long> project = this.projectManager.newProject(owner, newProject);
        if (!project.isSuccess()) {
            sendError(project.firstMessage());
            return;
        }
        //
        getResponse().sendRedirect("/my/projects.htm?curProjectID=" + projectInfoDO.getId());
    }
}