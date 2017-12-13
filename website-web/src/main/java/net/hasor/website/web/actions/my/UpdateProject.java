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
import net.hasor.db.transaction.TransactionCallback;
import net.hasor.db.transaction.TransactionStatus;
import net.hasor.web.Invoker;
import net.hasor.web.annotation.MappingTo;
import net.hasor.web.annotation.Params;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.enums.ContentFormat;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.domain.enums.SourceType;
import net.hasor.website.domain.result.Result;
import net.hasor.website.web.forms.ProjectInfoForm;

import java.io.IOException;
/**
 * 更新项目
 * @version : 2016年1月1日
 * @author 赵永春 (zyc@hasor.net)
 */
@MappingTo("/my/updateProject.do")
public class UpdateProject extends BaseMyProject {
    //
    public void execute(@Params ProjectInfoForm projectInfoDO, Invoker data) throws IOException {
        //
        // .need login
        if (needLoginAjax())
            return;
        //
        Result<ProjectInfoDO> projectResult = this.projectManager.queryProjectByID(projectInfoDO.getId());
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
        // .更新项目信息
        infoDO.setName(projectInfoDO.getName());
        infoDO.setSubtitle(projectInfoDO.getSubtitle());
        infoDO.setContentFormat(ContentFormat.MD.formType(projectInfoDO.getContentFormatType()));
        infoDO.setPresent(projectInfoDO.getPresentContent());
        infoDO.setHomePage(projectInfoDO.getHomePage());
        infoDO.setDownPage(projectInfoDO.getDownPage());
        infoDO.setLanguage(projectInfoDO.getLanguage());
        infoDO.setSourceType(SourceType.Open.formType(projectInfoDO.getSourceTypeEnum()));
        infoDO.setLicense(projectInfoDO.getLicense());
        //
        // .更新数据(启动事务)
        Result<Boolean> result = this.transactionService.mysqlTransaction(new TransactionCallback<Boolean>() {
            @Override
            public Boolean doTransaction(TransactionStatus tranStatus) throws Throwable {
                Result<Boolean> result = null;
                // .1st ,更新标题等数据
                result = projectManager.updateProjectWithoutContent(infoDO);
                if (!result.isSuccess()) {
                    tranStatus.setRollbackOnly();// <-- 回滚事务
                    return false;
                }
                // .2st ,更新内容
                result = projectManager.updateProjectContent(infoDO);
                if (!result.isSuccess()) {
                    tranStatus.setRollbackOnly();// <-- 回滚事务
                    return false;
                }
                return true;
            }
        });
        //
        //
        Boolean aBoolean = result.getResult();
        if (aBoolean == null || !aBoolean) {
            sendError(ErrorCodes.P_PROJECT_UPDATE_FAILED.getMsg());
            return;
        }
        //
        getResponse().sendRedirect("/my/projects.htm?curProjectID=" + projectInfoDO.getId());
    }
}