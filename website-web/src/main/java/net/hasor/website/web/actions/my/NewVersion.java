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
import net.hasor.website.web.forms.ProjectVersionForm;
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
    @Get
    public void execute(@ReqParam("projectID") long projectID, Invoker data) throws IOException {
        //
        // .need login
        if (!super.fillProjectInfo(projectID)) {
            return;
        }
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
    //
    @Post
    public void insertVersion(@Params ProjectVersionForm versionInfoDO, Invoker data) throws IOException {
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
        // .补充信息
        if (versionInfoDO.getFutures() == null)
            versionInfoDO.setFutures(new ProjectVersionFutures());
        versionInfoDO.getFutures().setDownloadURL(versionInfoDO.getFuturesDownloadURL());
        versionInfoDO.getFutures().setSourceURL(versionInfoDO.getFuturesSourceURL());
        versionInfoDO.getFutures().setApiURL(versionInfoDO.getFuturesApiURL());
        versionInfoDO.setChangelog(versionInfoDO.getChangelogContent());
        versionInfoDO.setChangelogFormat(ContentFormat.MD.formType(versionInfoDO.getChangelogFormatType()));
        versionInfoDO.setStatus(VersionStatus.DesignPlan);
        //
        Result<Long> newVersion = this.projectManager.newVersion(infoDO, versionInfoDO);
        //
        data.getHttpResponse().sendRedirect("/my/updateVersion.htm?projectID=" + projectID + "&versionID=" + newVersion.getResult());
    }
}