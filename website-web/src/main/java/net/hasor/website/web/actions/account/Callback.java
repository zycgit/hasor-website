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
import net.hasor.web.annotation.MappingTo;
import net.hasor.web.annotation.Params;
import net.hasor.web.valid.Valid;
import net.hasor.web.valid.ValidInvoker;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.web.core.Action;
import net.hasor.website.web.forms.LoginCallBackForm;
import net.hasor.rsf.utils.LogUtils;
/**
 * OAuth : 登录回调地址
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/account/callback.do")
public class Callback extends Action {
    public void execute(@Valid("Callback") @Params LoginCallBackForm loginForm, ValidInvoker valid) {
        //
        if (!valid.isValid()) {
            logger.error(LogUtils.create("ERROR_999_0004")//
                    .addLog("provider", loginForm.getProvider())//
                    .addLog("code", loginForm.getCode())//
                    .addLog("validName", "Callback")//
                    .addLog("error", "login_error : form valid failed.")//
                    .toJson());
            sendError(ErrorCodes.V_OAUTH_CALLBACK_FAILED.getMsg());
            return;
        }
        //
        // .跳转回来立刻展示一个登录中的页面,由这个承接页展现"登陆中...", 然后异步请求后台进行登陆。
        this.putData("loginForm", loginForm);
        this.putData("csrfToken", this.csrfTokenString());
        this.renderTo("htm", "/account/callback.htm");
        return;
    }
}