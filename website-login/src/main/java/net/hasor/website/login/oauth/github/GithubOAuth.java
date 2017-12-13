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
package net.hasor.website.login.oauth.github;
import com.qq.connect.utils.http.Response;
import net.hasor.core.ApiBinder;
import net.hasor.core.InjectSettings;
import net.hasor.core.Singleton;
import net.hasor.utils.ExceptionUtils;
import net.hasor.website.domain.AccessInfo;
import net.hasor.website.domain.UserDO;
import net.hasor.website.domain.UserSourceDO;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.domain.enums.GenderType;
import net.hasor.website.domain.enums.UserStatus;
import net.hasor.website.domain.enums.UserType;
import net.hasor.website.domain.futures.UserContactInfo;
import net.hasor.website.domain.futures.UserFutures;
import net.hasor.website.domain.result.ResultDO;
import net.hasor.website.login.oauth.AbstractOAuth;
import net.hasor.website.utils.JsonUtils;
import net.hasor.website.utils.LoggerUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static net.hasor.website.utils.ResultUtils.failed;
import static net.hasor.website.utils.ResultUtils.success;
/**
 * 封装Github登陆
 * https://status.github.com/
 * https://developer.github.com/v3/oauth/
 * @version : 2016年1月10日
 * @author 赵永春 (zyc@hasor.net)
 */
@Singleton
public class GithubOAuth extends AbstractOAuth {
    public static final String PROVIDER_NAME = "Github";
    public static final String URL_DATA      = "provider=" + PROVIDER_NAME + "&type=website";
    //
    //应用ID
    @InjectSettings("website-oauth.github.app_id")
    private             String appID         = null;
    //应用Key
    @InjectSettings("website-oauth.github.app_key")
    private             String appKey        = null;
    @InjectSettings("website-oauth.github.oauth_scope")
    private             String scope         = null;
    //
    //
    public String getAppID() {
        return appID;
    }
    public String getAppKey() {
        return appKey;
    }
    //
    //
    public GithubOAuth() {
        super();
    }
    public GithubOAuth(ApiBinder apiBinder) {
        super(apiBinder);
    }
    //
    //
    @Override
    public void configOAuth(ApiBinder apiBinder) {
    }
    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
    //
    /**首次登录的跳转地址(参数为回跳地址)*/
    public String evalLoginURL(String status, String redirectTo) {
        try {
            String redirectURI = this.getRedirectURI() + "?" + URL_DATA + "&redirectURI=" + redirectTo;
            return "https://github.com/login/oauth/authorize?response_type=code" //
                    + "&client_id=" + this.appID //
                    + "&redirect_uri=" + URLEncoder.encode(redirectURI, "utf-8") //
                    + "&scope=" + this.scope;//
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_004_0004")//
                    .addLog("oauth_provider", this.getProviderName())//
                    .addLog("oauth_appID", this.appID)//
                    .addLog("oauth_redirectURI", this.getRedirectURI())//
                    .addLog("oauth_urlData", URL_DATA)//
                    .addLog("oauth_redirectTo", redirectTo)//
                    .addLog("oauth_appID", this.appID)//
                    .addLog("oauth_scope", this.scope)//
                    .addLog("param_status", status)//
                    .addLog("param_redirectTo", redirectTo)//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw ExceptionUtils.toRuntimeException(e);
        }
    }
    //
    /**拿到远程Code之后通过code获取 AccessInfo 认证信息对象。*/
    public ResultDO<AccessInfo> evalToken(String status, String authCode) {
        //
        // .生成获取 token 的 url
        String tokenURL = null;
        try {
            tokenURL = "https://github.com/login/oauth/access_token?1=1" //
                    + "&client_id=" + this.appID //
                    + "&client_secret=" + this.appKey//
                    + "&code=" + authCode//
                    + "&state=" + (status == null ? "" : status) //
                    + "&redirect_uri=" + URLEncoder.encode(this.getRedirectURI() + "?" + URL_DATA, "utf-8");
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_004_0005")//
                    .addLog("oauth_provider", this.getProviderName())//
                    .addLog("oauth_appID", this.appID)//
                    .addLog("oauth_redirectURI", this.getRedirectURI())//
                    .addLog("oauth_urlData", URL_DATA)//
                    .addLog("oauth_appID", this.appID)//
                    .addLog("oauth_scope", this.scope)//
                    .addLog("param_status", status)//
                    .addLog("param_authCode", authCode)//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            return failed(e);
        }
        //
        // .获取 token
        Map<String, String> dataMaps = new HashMap<String, String>();
        try {
            logger.error("github_access_token :authCode = {} , build token URL -> {}.", authCode, tokenURL);
            Response response = this.httpClient.httpGet(tokenURL);
            String data = response.getResponseAsString();
            if (StringUtils.isBlank(data)) {
                logger.error(LoggerUtils.create("ERROR_004_0006")//
                        .addLog("oauth_provider", this.getProviderName())//
                        .addLog("oauth_appID", this.appID)//
                        .addLog("oauth_redirectURI", this.getRedirectURI())//
                        .addLog("oauth_urlData", URL_DATA)//
                        .addLog("oauth_appID", this.appID)//
                        .addLog("oauth_scope", this.scope)//
                        .addLog("param_status", status)//
                        .addLog("param_authCode", authCode)//
                        .addLog("tokenURL", tokenURL)//
                        .toJson());//结果为空
                return failed(ErrorCodes.OA_TOKEN_EXT_EMPTY);
            }
            //
            String[] dataItems = data.split("&");
            for (String dataItem : dataItems) {
                //
                String[] arrs = dataItem.split("=");
                String keyStr = URLDecoder.decode(arrs[0], "utf-8");
                String varStr = URLDecoder.decode(arrs[1], "utf-8");
                dataMaps.put(keyStr, varStr);
            }
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_004_0007")//
                    .addLog("oauth_provider", this.getProviderName())//
                    .addLog("oauth_appID", this.appID)//
                    .addLog("oauth_redirectURI", this.getRedirectURI())//
                    .addLog("oauth_urlData", URL_DATA)//
                    .addLog("oauth_appID", this.appID)//
                    .addLog("oauth_scope", this.scope)//
                    .addLog("param_status", status)//
                    .addLog("param_authCode", authCode)//
                    .addLog("tokenURL", tokenURL)//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            return failed(ErrorCodes.OA_TOKEN_EXT_ERROR);
        }
        //
        // .认证失败
        if (dataMaps.containsKey("error")) {
            //        0 = "error=bad_verification_code"
            //        1 = "error_description=The+code+passed+is+incorrect+or+expired."
            //        2 = "error_uri=https%3A%2F%2Fdeveloper.github.com%2Fv3%2Foauth%2F%23bad-verification-code"
            //返回结果失败
            String errorCoe = dataMaps.get("error").toString();
            String errorDesc = dataMaps.get("error_description").toString();
            String errorRUL = dataMaps.get("error_uri").toString();
            //
            logger.error(LoggerUtils.create("ERROR_004_0008")//
                    .addLog("oauth_provider", this.getProviderName())//
                    .addLog("oauth_appID", this.appID)//
                    .addLog("oauth_redirectURI", this.getRedirectURI())//
                    .addLog("oauth_urlData", URL_DATA)//
                    .addLog("oauth_appID", this.appID)//
                    .addLog("oauth_scope", this.scope)//
                    .addLog("param_status", status)//
                    .addLog("param_authCode", authCode)//
                    .addLog("tokenURL", tokenURL)//
                    .addLog("errorCoe", errorCoe)//
                    .addLog("errorDesc", errorDesc)//
                    .addLog("errorRUL", errorRUL)//
                    .toJson());
            return failed(ErrorCodes.OA_TOKEN_EXT_FAILED);
        }
        //
        // .获取用户信息
        String data = "{}";
        String access_token = null;
        try {
            access_token = (String) dataMaps.get("access_token");
            Response response = this.httpClient.httpGet("https://api.github.com/user?access_token=" + access_token);
            data = response.getResponseAsString();
            if (StringUtils.isBlank(data)) {
                logger.error(LoggerUtils.create("ERROR_004_0006")//
                        .addLog("oauth_provider", this.getProviderName())//
                        .addLog("oauth_appID", this.appID)//
                        .addLog("oauth_redirectURI", this.getRedirectURI())//
                        .addLog("oauth_urlData", URL_DATA)//
                        .addLog("oauth_appID", this.appID)//
                        .addLog("oauth_scope", this.scope)//
                        .addLog("param_status", status)//
                        .addLog("param_authCode", authCode)//
                        .addLog("access_token", access_token)//
                        .addLog("tokenURL", tokenURL)//
                        .toJson());//结果为空
                return failed(ErrorCodes.OA_TOKEN_EXT_EMPTY);
            }
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_004_0009")//
                    .addLog("oauth_provider", this.getProviderName())//
                    .addLog("oauth_appID", this.appID)//
                    .addLog("oauth_redirectURI", this.getRedirectURI())//
                    .addLog("oauth_urlData", URL_DATA)//
                    .addLog("oauth_appID", this.appID)//
                    .addLog("oauth_scope", this.scope)//
                    .addLog("param_status", status)//
                    .addLog("param_authCode", authCode)//
                    .addLog("access_token", access_token)//
                    .addLog("tokenURL", tokenURL)//
                    .toJson());
            return failed(ErrorCodes.OA_TOKEN_EXT_ERROR, e);
        }
        //
        // .数据解析
        GithubAccessInfo accessInfo = JsonUtils.toObject(data, GithubAccessInfo.class);
        accessInfo.setAccessToken(access_token);
        logger.info("access_token : success -> token : {} , sourceID : {}.", accessInfo.getAccessToken(), accessInfo.getSource());
        return success(accessInfo);
    }
    @Override
    public UserDO convertTo(AccessInfo result) {
        GithubAccessInfo accessInfo = (GithubAccessInfo) result;
        UserDO userDO = new UserDO();
        userDO.setAccount("");
        userDO.setPassword("-");
        userDO.setEmail("");
        userDO.setMobilePhone("");
        userDO.setNick(accessInfo.getName());
        userDO.setAvatar(accessInfo.getAvatar_url());
        if (StringUtils.isBlank(userDO.getNick())) {
            if (StringUtils.isBlank(accessInfo.getLogin())) {
                userDO.setNick(PROVIDER_NAME + "_" + System.currentTimeMillis());
            } else {
                userDO.setNick(accessInfo.getLogin());
            }
        }
        //
        if (userDO.getUserSourceList() == null) {
            userDO.setUserSourceList(new ArrayList<UserSourceDO>());
        }
        userDO.getUserSourceList().add(convertAccessInfo(result));
        userDO.setGender(GenderType.None);
        userDO.setStatus(UserStatus.Normal);
        userDO.setType(UserType.Temporary);
        //
        userDO.setFutures(new UserFutures());
        userDO.getFutures().setName(accessInfo.getName());
        userDO.getFutures().setPresent(accessInfo.getBio());
        //
        userDO.setContactInfo(new UserContactInfo());
        userDO.getContactInfo().setBlogHome(accessInfo.getBlog());
        return userDO;
    }
}