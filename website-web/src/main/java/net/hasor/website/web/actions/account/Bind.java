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
package net.hasor.website.web.actions.account;
import net.hasor.core.Inject;
import net.hasor.web.Invoker;
import net.hasor.web.annotation.MappingTo;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.domain.result.Result;
import net.hasor.website.manager.UserManager;
import net.hasor.website.utils.LoggerUtils;
import net.hasor.website.web.core.Action;

import java.io.IOException;
/**
 * 给当前登陆账号绑定登陆
 * @version : 2016年1月1日
 * @author 赵永春 (zyc@hasor.net)
 */
@MappingTo("/account/bind.do")
public class Bind extends Action {
    @Inject
    private UserManager userManager;
    //
    public void execute(Invoker data) throws IOException {
        // .need login
        if (needLogin("/my/my.htm")) {
            return;
        }
        //
        long targetUserID = this.getTargetUserID();
        String targetProivter = this.getTargetPrivider();
        Result<Boolean> result = this.userManager.reBindLogin(targetUserID, targetProivter, this.getUserID());
        //
        if (!result.isSuccess() || result.getResult() == null) {
            logger.error(LoggerUtils.create("ERROR_003_0008")//
                    .addLog("result", result) //
                    .addLog("currentUserID", this.getUserID())//
                    .addLog("targetUserID", targetUserID)//
                    .addLog("targetProivter", targetProivter)//
                    .addLog("errorMessage", result.firstMessage())//
                    .toJson());
            sendError(result.firstMessage());
            return;
        }
        //
        if (!result.getResult()) {
            logger.error(LoggerUtils.create("ERROR_004_0012")//
                    .addLog("result", result) //
                    .addLog("currentUserID", this.getUserID())//
                    .addLog("targetUserID", targetUserID)//
                    .addLog("targetProivter", targetProivter)//
                    .addLog("errorMessage", result.firstMessage())//
                    .toJson());
            sendError(ErrorCodes.OA_BIND_FAILED.getMsg());
            return;
        }
        // .绑定成功
        String ctx_path = getRequest().getContextPath();
        data.getHttpResponse().sendRedirect(ctx_path + "/my/my.htm");
    }
}