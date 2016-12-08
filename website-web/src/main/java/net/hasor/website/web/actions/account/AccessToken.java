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
import net.hasor.restful.api.MappingTo;
import net.hasor.restful.api.Params;
import net.hasor.restful.api.Valid;
import net.hasor.website.domain.UserDO;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.login.oauth.OAuthManager;
import net.hasor.website.manager.UserManager;
import net.hasor.website.web.core.Action;
import net.hasor.website.web.forms.LoginCallBackForm;
import org.more.bizcommon.Result;
import org.more.bizcommon.log.LogUtils;
import org.more.util.StringUtils;

import java.io.IOException;
/**
 * OAuth : 服务器获取 AccessToken
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/account/access_token.do")
public class AccessToken extends Action {
    @Inject
    private OAuthManager oauthManager;
    @Inject
    private UserManager  userManager;
    //
    public void execute(@Valid("AccessToken") @Params LoginCallBackForm loginForm) throws IOException {
        String authCode = loginForm.getCode();
        String provider = loginForm.getProvider();
        // .ajax
        String ajaxTo = this.getRequest().getHeader("ajaxTo");
        if (StringUtils.isBlank(ajaxTo) || !StringUtils.equalsIgnoreCase(ajaxTo, "true")) {
            logger.error(LogUtils.create("ERROR_003_0007")//
                    .addLog("authCode", authCode)//
                    .addLog("provider", provider)//
                    .addLog("error", "login_error : request not accepted.")//
                    .toJson());
            sendError(ErrorCodes.BAD_REQUEST.getMsg());
            return;
        }
        // .csrf
        if (!this.csrfTokenTest()) {
            logger.error(LogUtils.create("ERROR_999_0005")//
                    .addLog("authCode", authCode)//
                    .addLog("provider", provider)//
                    .addLog("error", "login_error : csrfToken failed.")//
                    .toJson());
            sendJsonError(ErrorCodes.V_CSRF_INVALID.getMsg());
            return;
        }
        // .code
        if (StringUtils.isBlank(authCode)) {
            logger.error(LogUtils.create("ERROR_004_0010")//
                    .addLog("authCode", authCode)//
                    .addLog("provider", provider)//
                    .addLog("error", "login_error : get access_token failed, response is empty.")//
                    .toJson());
            sendJsonError(ErrorCodes.BAD_REQUEST.getMsg(authCode));
            return;
        }
        // .form valid
        if (!this.isValid()) {
            logger.error(LogUtils.create("ERROR_004_0011")//
                    .addLog("provider", provider)//
                    .addLog("authCode", authCode)//
                    .addLog("error", "login_error : form valid failed.")//
                    .toJson());
            sendJsonError(ErrorCodes.V_OAUTH_CALLBACK_FAILED.getMsg(authCode));
            return;
        }
        // .oauth info
        Result<UserDO> infoResult = oauthManager.evalUserInfo(provider, authCode, loginForm.getState());
        if (infoResult == null || (infoResult.isSuccess() && infoResult.getResult() == null)) {
            logger.error(LogUtils.create("ERROR_999_0001")//
                    .addLog("provider", provider)//
                    .addLog("authCode", authCode)//
                    .addLog("error", "login_error : result is null.")//
                    .toJson());
            sendJsonError(ErrorCodes.LOGIN_ERROR.getMsg());
            return;
        }
        if (!infoResult.isSuccess()) {
            logger.error(LogUtils.create("ERROR_004_0003")//
                    .addLog("provider", provider)//
                    .addLog("authCode", authCode)//
                    .addLog("error", "login_error : access process failed.")//
                    .toJson());
            sendJsonError(infoResult.firstMessage());
            return;
        }
        // .login
        UserDO userDO = null;
        try {
            // .登陆
            UserDO oriUserInfo = infoResult.getResult();
            String uniqueID = oriUserInfo.getUserSourceList().get(0).getAccessInfo().getExternalUserID();
            userDO = this.userManager.getUserByProvider(provider, uniqueID);
            Result<Long> dataResult = null;
            if (userDO == null) {
                dataResult = this.userManager.newUser(oriUserInfo);//插入新用户
            } else {
                dataResult = this.userManager.updateAccessInfo(userDO, provider, userDO.getUserSourceList().get(0));
            }
            if (!dataResult.isSuccess() || dataResult.getResult() == null) {
                logger.error(LogUtils.create("ERROR_003_0008")//
                        .addLog("result", dataResult) //
                        .addLog("provider", provider)//
                        .addLog("authCode", authCode)//
                        .addLog("errorMessage", dataResult.firstMessage())//
                        .addLog("error", "login success , but user save to db failed.")//
                        .toJson());
                sendJsonError(dataResult.firstMessage());
                return;
            }
            // .save or update
            long userID = dataResult.getResult();
            if (userID <= 0) {
                logger.error(LogUtils.create("ERROR_002_0006")//
                        .addLog("result", dataResult) //
                        .addLog("provider", provider)//
                        .addLog("authCode", authCode)//
                        .addLog("error", "login success , but user save to db failed.")//
                        .toJson());
                sendJsonError(ErrorCodes.U_SAVE_USER_FAILED.getMsg());
                return;
            }
            // .更新之后反查数据
            userDO = this.userManager.getUserByProvider(provider, uniqueID);
            if (userDO == null) {
                logger.error(LogUtils.create("ERROR_002_0001")//
                        .addLog("result", dataResult) //
                        .addLog("provider", provider)//
                        .addLog("authCode", authCode)//
                        .addLog("error", "login_error : query user by id result is null.")//
                        .toJson());
                sendJsonError(ErrorCodes.U_GET_USER_NOT_EXIST.getMsg());
                return;
            }
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_003_0009")//
                    .addLog("provider", provider)//
                    .addLog("authCode", authCode)//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            sendJsonError(ErrorCodes.LOGIN_ERROR.getMsg());
            return;
        }
        // .跳转到目标页面
        String redirectURI = this.userManager.startQuickLogin(userDO.getUserID(), provider, loginForm.getRedirectURI());
        sendJsonData(redirectURI);
    }
}