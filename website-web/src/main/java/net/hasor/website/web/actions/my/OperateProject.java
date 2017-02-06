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
import net.hasor.website.domain.enums.ErrorCodes;
import org.more.bizcommon.Result;

import java.io.IOException;
/**
 * 项目相关的高级操作
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/my/operateProject.do")
public class OperateProject extends BaseMyProject {
    //
    private boolean testOwner(long projectID) {
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
        return true;
    }
    //
    public void execute(Invoker data, @ReqParam("method") String method, @ReqParam("projectID") long projectID) throws IOException {
        //
        // .need login
        if (needLogin()) {
            return;
        }
        //
        boolean onError = false;
        if ("delete".equalsIgnoreCase(method)) {
            // - 删除，正式删除在7日后
            onError = this.doDelete(projectID);
        }
        if ("recover".equalsIgnoreCase(method)) {
            // - 恢复删除的数据
            onError = this.doRecover(projectID);
        }
        //
        if (onError) {
            getResponse().sendRedirect("/my/projects.htm?curProjectID=" + projectID);
        }
        sendError(ErrorCodes.BAD_PARAMS.getMsg());
    }
    //
    //
    private boolean doRecover(long projectID) {
        if (!testOwner(projectID)) {
            return false;
        }
        //
        Result<ProjectInfoDO> projectResult = this.projectManager.queryProjectByID(projectID);
        ProjectInfoDO infoDO = projectResult.getResult();
        Result<Boolean> result = this.projectManager.doRecoverProject(getUser(), infoDO);
        //
        if (!result.isSuccess()) {
            sendError(result.firstMessage());
            return false;
        }
        return true;
    }
    private boolean doDelete(long projectID) {
        if (!testOwner(projectID)) {
            return false;
        }
        //
        Result<ProjectInfoDO> projectResult = this.projectManager.queryProjectByID(projectID);
        ProjectInfoDO infoDO = projectResult.getResult();
        Result<Boolean> result = this.projectManager.doDeleteProject(getUser(), infoDO);
        //
        if (!result.isSuccess()) {
            sendError(result.firstMessage());
            return false;
        }
        return true;
    }
}