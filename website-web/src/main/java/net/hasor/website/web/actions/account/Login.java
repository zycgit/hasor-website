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
import net.hasor.restful.RenderData;
import net.hasor.restful.api.MappingTo;
import net.hasor.restful.api.Params;
import net.hasor.restful.api.PathParam;
import net.hasor.restful.api.Valid;
import net.hasor.website.domain.UserDO;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.manager.UserManager;
import net.hasor.website.web.core.Action;
import net.hasor.website.web.forms.LoginForm;
import org.more.bizcommon.log.LogUtils;
import org.more.util.StringUtils;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
/**
 * 本地登陆
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/account/login.{action}")
public class Login extends Action {
    @Inject
    private UserManager userManager;
    //
    public void execute(@PathParam("action") String action, @Valid("SignIn") @Params LoginForm loginForm, RenderData data) throws IOException {
        String ctx_path = data.getHttpRequest().getContextPath();
        //
        // - 登录请求
        if (StringUtils.equalsIgnoreCase("do", action)) {
            this.putData("loginForm", loginForm);
            if (data.isValid()) {
                UserDO userDO = this.userManager.queryByLogin(loginForm.getLogin());
                if (userDO != null && StringUtils.equals(userDO.getPassword(), loginForm.getPassword())) {
                    // .跳转到目标页面
                    try {
                        String redirectURI = this.userManager.startQuickLogin(userDO.getUserID(), null, loginForm.getRedirectURI());
                        sendJsonData(redirectURI);
                        return;
                    } catch (NoSuchAlgorithmException e) {
                        //
                        logger.error(LogUtils.create("ERROR_999_0002")//
                                .logException(e)//
                                .addString("tencent_access_token : startQuickLogin failed.").toJson(), e);
                        sendJsonError(ErrorCodes.LOGIN_USER_SAVE.getMsg("startQuickLogin。"));
                    }
                }
            }
            //
            // - 验证失败
            renderTo("htm", "/account/login.htm");
            return;
        }
        //
        // - 登录页面
        data.clearValidErrors();//清空验证信息,避免瞎显示
        if (this.isLogin()) {
            data.getHttpResponse().sendRedirect(ctx_path + "/account/my.htm");
        }
    }
}