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
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.ProjectVersionDO;
import net.hasor.website.domain.enums.ContentFormat;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.domain.enums.VersionStatus;
import net.hasor.website.domain.futures.ProjectVersionFutures;
import net.hasor.website.domain.result.Result;
import net.hasor.website.web.forms.ProjectVersionForm;

import java.io.IOException;
/**
 * 版本详情 & 版本更新
 * @version : 2016年1月1日
 * @author 赵永春 (zyc@hasor.net)
 */
@MappingTo("/my/updateVersion.htm")
public class UpdateVersion extends BaseMyProject {
    //
    @Get
    public void doShow(@ReqParam("projectID") long projectID, @ReqParam("versionID") long versionID) throws IOException {
        // .need login
        if (needLogin()) {
            return;
        }
        //
        // .need login
        if (!super.fillProjectInfo(projectID)) {
            return;
        }
        //
        super.fillInfo();
        //
        //
        Result<ProjectInfoDO> projectResult = this.projectManager.queryProjectByID(projectID);
        if (!projectResult.isSuccess()) {
            sendError(projectResult.firstMessage());
            return;
        }
        final ProjectInfoDO infoDO = projectResult.getResult();
        //
        // .判断项目归属
        if (!super.isMyProject(infoDO)) {
            sendError(ErrorCodes.P_OWNER_NOT_YOU.getMsg());
            return;
        }
        //
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
            sendError(versionResult.firstMessage());
            return;
        }
        //
        ProjectVersionDO versionDO = versionResult.getResult();
        putData("version", versionDO);
    }
    //
    @Post
    public void doUpdate(@Params ProjectVersionForm versionInfoDO, Invoker data) throws IOException {
        //
        // .need login
        if (needLoginAjax())
            return;
        //
        long projectID = versionInfoDO.getProjectID();
        long versionID = versionInfoDO.getId();
        Result<ProjectInfoDO> projectResult = this.projectManager.queryProjectByID(projectID);
        if (!projectResult.isSuccess()) {
            sendError(projectResult.firstMessage());
            return;
        }
        final ProjectInfoDO infoDO = projectResult.getResult();
        //
        // .判断项目归属
        if (!super.isMyProject(infoDO)) {
            sendError(ErrorCodes.P_OWNER_NOT_YOU.getMsg());
            return;
        }
        //
        Result<ProjectVersionDO> versionResult = this.projectManager.queryVersionByID(projectID, versionID);
        if (!versionResult.isSuccess()) {
            sendError(versionResult.firstMessage());
            return;
        }
        ProjectVersionDO versionDO = versionResult.getResult();
        if (VersionStatus.Delete.equals(versionDO.getStatus())) {
            sendError(ErrorCodes.BAD_RESOURCE_DELETE.getMsg());
            return;
        }
        //
        // .更新版本信息
        versionDO.setSubtitle(versionInfoDO.getSubtitle());
        versionDO.setVersion(versionInfoDO.getVersion());
        if (versionDO.getFutures() == null)
            versionDO.setFutures(new ProjectVersionFutures());
        versionDO.getFutures().setDownloadURL(versionInfoDO.getFuturesDownloadURL());
        versionDO.getFutures().setSourceURL(versionInfoDO.getFuturesSourceURL());
        versionDO.getFutures().setApiURL(versionInfoDO.getFuturesApiURL());
        versionDO.setChangelog(versionInfoDO.getChangelogContent());
        versionDO.setChangelogFormat(ContentFormat.MD.formType(versionInfoDO.getChangelogFormatType()));
        versionDO.setReleaseTime(versionInfoDO.getReleaseTime());
        //
        // .更新数据(启动事务)
        Result<Boolean> result = this.projectManager.updateVersionInfo(versionDO);
        if (!result.isSuccess()) {
            sendError(result.firstMessage());
            return;
        }
        //
        Boolean aBoolean = result.getResult();
        if (aBoolean == null || !aBoolean) {
            sendError(ErrorCodes.P_VERSION_UPDATE_FAILED.getMsg());
            return;
        }
        //
        data.getHttpResponse().sendRedirect("/my/updateVersion.htm?projectID=" + projectID + "&versionID=" + versionID);
    }
}