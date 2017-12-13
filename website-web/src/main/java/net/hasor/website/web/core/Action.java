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
package net.hasor.website.web.core;
import net.hasor.core.Inject;
import net.hasor.web.WebController;
import net.hasor.website.core.TransactionService;
import net.hasor.website.domain.JsonResultDO;
import net.hasor.website.domain.Owner;
import net.hasor.website.domain.beans.AppConstant;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.domain.message.Message;
import net.hasor.website.utils.JsonUtils;
import net.hasor.website.web.utils.LoginUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URLEncoder;
import java.security.SecureRandom;
/**
 * 基类
 * @version : 2016年1月1日
 * @author 赵永春 (zyc@hasor.net)
 */
public class Action extends WebController {
    protected Logger       logger       = LoggerFactory.getLogger(getClass());
    private   SecureRandom secureRandom = new SecureRandom();
    @Inject
    protected TransactionService transactionService;
    //
    protected boolean needLogin() throws IOException {
        String ctx_path = getRequest().getContextPath();
        String reqURL = getRequest().getRequestURI();
        String reqQuery = getRequest().getQueryString();
        if (StringUtils.isNotBlank(reqQuery)) {
            reqURL = ctx_path + reqURL + "?" + reqQuery;
        }
        return needLogin(reqURL);
    }
    protected boolean needLogin(String redirectURI) throws IOException {
        String ctx_path = getRequest().getContextPath();
        if (!isLogin()) {
            if (StringUtils.isBlank(redirectURI)) {
                redirectURI = ctx_path + "/my/my.htm";
            }
            getResponse().sendRedirect(ctx_path + "/account/login.htm?redirectURI=" + URLEncoder.encode(redirectURI, "utf-8"));
            return true;
        }
        return false;
    }
    protected boolean needLoginAjax() throws IOException {
        if (!isLogin()) {
            sendError(ErrorCodes.U_NEED_LOGIN.getMsg());
            return true;
        }
        return false;
    }
    /** 获取 csrf Token */
    protected final String csrfTokenString() {
        String token = this.getSessionAttr(AppConstant.SESSION_KEY_CSRF_TOKEN);
        if (StringUtils.isBlank(token)) {
            token = Long.toString(this.secureRandom.nextLong(), 24);
            this.setSessionAttr(AppConstant.SESSION_KEY_CSRF_TOKEN, token);
        }
        return token;
    }
    /**
     * Csrf token test boolean.
     * @return the boolean
     */
    protected boolean csrfTokenTest() {
        String reqToken = this.getPara(AppConstant.REQ_PARAM_KEY_CSRF_TOKEN);
        return StringUtils.equals(reqToken, this.csrfTokenString());
    }
    //
    /**输出Json格式的成功结果,并指定跳转到:redirectURI地址。*/
    protected void sendJsonData(Object data) throws IOException {
        JsonResultDO jsonData = new JsonResultDO();
        jsonData.setSuccess(true);
        jsonData.setResult(data);
        String jsonStr = JsonUtils.toJsonStringSingleLine(jsonData);
        this.getResponse().getWriter().write(jsonStr);
    }
    //
    /**输出Json格式的失败结果。*/
    protected void sendJsonError(Message errorMessage) throws IOException {
        if (errorMessage == null) {
            errorMessage = ErrorCodes.BAD_UNKNOWN.getMsg("异常信息丢失。");
        }
        JsonResultDO jsonData = new JsonResultDO();
        jsonData.setSuccess(false);
        jsonData.setErrorCode(errorMessage.getType());
        jsonData.setErrorMessage(errorMessage.getMessage());
        String jsonStr = JsonUtils.toJsonStringSingleLine(jsonData);
        this.getResponse().getWriter().write(jsonStr);
    }
    //
    /**跳转到错误页。*/
    protected void sendError(Message errorMessage) {
        if (errorMessage == null) {
            errorMessage = ErrorCodes.BAD_UNKNOWN.getMsg("异常信息丢失。");
        }
        int errorCode = errorMessage.getType();
        String errorStr = errorMessage.getMessage();
        //
        this.putData("error", errorMessage);
        this.putData("errorCode", errorCode);
        this.putData("errorStr", errorStr);
        this.renderTo("htm", "/error.htm");
    }
    protected void sendRedirectError(Message errorMessage) throws IOException {
        if (errorMessage == null) {
            errorMessage = ErrorCodes.BAD_UNKNOWN.getMsg("异常信息丢失。");
        }
        int errorCode = errorMessage.getType();
        String errorStr = errorMessage.getMessage();
        this.getResponse().sendRedirect("/error.htm?errorCode=" + errorCode);
    }
    //
    /**登录用户的Nick*/
    protected Owner getUser() {
        return LoginUtils.getUser(getRequest());
    }
    //
    /**登录用户的Nick*/
    protected String getUserNick() {
        return LoginUtils.getUserNick(getRequest());
    }
    //
    /**登录用户的ID*/
    protected long getUserID() {
        return LoginUtils.getUserID(getRequest());
    }
    /**最后一次调用登录的用户的ID*/
    protected long getTargetUserID() {
        return LoginUtils.getTargetUserID(getRequest());
    }
    /**获取最后一次调用登陆系统用的来源*/
    protected String getTargetPrivider() {
        return LoginUtils.getTargetPrivider(getRequest());
    }
    //
    /**是否已登录*/
    protected boolean isLogin() {
        return LoginUtils.isLogin(getRequest());
    }
}