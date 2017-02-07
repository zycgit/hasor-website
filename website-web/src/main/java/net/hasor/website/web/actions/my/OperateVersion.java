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
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.ProjectVersionDO;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.domain.enums.VersionStatus;
import org.more.bizcommon.Result;

import java.io.IOException;
/**
 * 版本相关的高级操作
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/my/operateVersion.do")
public class OperateVersion extends BaseMyProject {
    //
    private boolean testVersion(long projectID, long versionID) {
        //
        Result<ProjectInfoDO> projectResult = this.projectManager.queryProjectByID(projectID);
        if (!projectResult.isSuccess()) {
            sendError(projectResult.firstMessage());
            return false;
        }
        ProjectInfoDO infoDO = projectResult.getResult();
        //
        // .项目归属
        if (!super.isMyProject(infoDO)) {
            sendError(ErrorCodes.P_OWNER_NOT_YOU.getMsg());
            return false;
        }
        if (infoDO == null) {
            sendError(ErrorCodes.P_PROJECT_NOT_EXIST.getMsg());
            return false;
        }
        //
        // .版本
        Result<ProjectVersionDO> versionResult = this.projectManager.queryVersionByID(projectID, versionID);
        if (!versionResult.isSuccess() || versionResult.getResult() == null) {
            sendError(versionResult.firstMessage());
            return false;
        }
        return true;
    }
    //
    public void execute(Invoker data,//
            @ReqParam("method") String method, //
            @ReqParam("projectID") long projectID, //
            @ReqParam("versionID") long versionID, //
            @ReqParam("target") String target) throws IOException {
        //
        // .need login
        if (needLogin()) {
            return;
        }
        //
        boolean onError = false;
        if ("delete".equalsIgnoreCase(method)) {
            // - 删除，正式删除在7日后
            onError = this.doDelete(projectID, versionID);
        }
        if ("recover".equalsIgnoreCase(method)) {
            // - 恢复删除的数据
            onError = this.doRecover(projectID, versionID);
        }
        if ("publish".equalsIgnoreCase(method)) {
            // - 版本发布
            onError = this.doPublish(projectID, versionID);
        }
        //
        if (onError) {
            if ("detail".equalsIgnoreCase(target)) {
                getResponse().sendRedirect("/my/updateVersion.htm?projectID=" + projectID + "&versionID=" + versionID);
            } else {
                getResponse().sendRedirect("/my/projects.htm?curProjectID=" + projectID + "#version_" + versionID);
            }
            return;
        }
        sendError(ErrorCodes.BAD_PARAMS.getMsg());
    }
    //
    //
    private boolean doPublish(long projectID, long versionID) {
        if (!testVersion(projectID, versionID)) {
            return false;
        }
        // .执行删除
        Result<ProjectVersionDO> versionResult = this.projectManager.queryVersionByID(projectID, versionID);
        ProjectVersionDO versionDO = versionResult.getResult();
        if (!VersionStatus.Delete.equals(versionDO.getStatus())) {
            Result<Boolean> result = this.projectManager.publishVersion(this.getUser(), projectID, versionID);
            if (!result.isSuccess()) {
                sendError(result.firstMessage());
                return false;
            }
        }
        return true;
    }
    private boolean doDelete(long projectID, long versionID) throws IOException {
        // .是否可删除
        if (!testVersion(projectID, versionID)) {
            return false;
        }
        // .执行删除
        Result<ProjectVersionDO> versionResult = this.projectManager.queryVersionByID(projectID, versionID);
        ProjectVersionDO versionDO = versionResult.getResult();
        if (!VersionStatus.Delete.equals(versionDO.getStatus())) {
            Result<Boolean> result = this.projectManager.deleteVersion(this.getUser(), projectID, versionID);
            if (!result.isSuccess()) {
                sendError(result.firstMessage());
                return false;
            }
        }
        return true;
    }
    private boolean doRecover(long projectID, long versionID) throws IOException {
        if (!testVersion(projectID, versionID)) {
            return false;
        }
        // .执行恢复
        Result<ProjectVersionDO> versionResult = this.projectManager.queryVersionByID(projectID, versionID);
        ProjectVersionDO versionDO = versionResult.getResult();
        if (!VersionStatus.Delete.equals(versionDO.getStatus())) {
            Result<Boolean> result = this.projectManager.recoverVersion(this.getUser(), projectID, versionID);
            if (!result.isSuccess()) {
                sendError(result.firstMessage());
                return false;
            }
        }
        return true;
    }
}