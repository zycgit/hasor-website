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
package net.hasor.website.login.oauth.weibo;
import com.qq.connect.utils.http.Response;
import net.hasor.core.ApiBinder;
import net.hasor.core.InjectSettings;
import net.hasor.core.Singleton;
import net.hasor.website.domain.AccessInfo;
import net.hasor.website.domain.UserDO;
import net.hasor.website.domain.UserSourceDO;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.domain.enums.GenderType;
import net.hasor.website.domain.enums.UserStatus;
import net.hasor.website.domain.enums.UserType;
import net.hasor.website.domain.futures.ContactAddressInfo;
import net.hasor.website.domain.futures.UserContactInfo;
import net.hasor.website.domain.futures.UserFutures;
import net.hasor.website.login.oauth.AbstractOAuth;
import net.hasor.website.utils.JsonUtils;
import net.hasor.website.utils.LoggerUtils;
import org.more.bizcommon.ResultDO;
import org.more.util.ExceptionUtils;
import org.more.util.StringUtils;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Map;

import static net.hasor.website.utils.ResultUtils.failed;
import static net.hasor.website.utils.ResultUtils.success;
/**
 * 封装新浪微博登陆
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
@Singleton
public class WeiboOAuth extends AbstractOAuth {
    public static final String PROVIDER_NAME = "Weibo";
    public static final String URL_DATA      = "provider=" + PROVIDER_NAME + "&type=website";
    //
    //新浪域名所有权验证
    @InjectSettings("website-oauth.weibo.admins")
    private             String adminsCode    = null;
    //应用ID
    @InjectSettings("website-oauth.weibo.app_id")
    private             String appID         = null;
    //应用Key
    @InjectSettings("website-oauth.weibo.app_key")
    private             String appKey        = null;
    //权限
    @InjectSettings("website-oauth.weibo.oauth_scope")
    private             String scope         = null;
    //
    //
    public String getAdmins() {
        return this.adminsCode;
    }
    public String getAppID() {
        return appID;
    }
    public String getAppKey() {
        return appKey;
    }
    //
    //
    public WeiboOAuth() {
        super();
    }
    public WeiboOAuth(ApiBinder apiBinder) {
        super(apiBinder);
    }
    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
    @Override
    public void configOAuth(ApiBinder apiBinder) {
    }
    //
    /**首次登录的跳转地址(参数为回跳地址)*/
    @Override
    public String evalLoginURL(String status, String redirectTo) {
        try {
            String redirectURI = this.getRedirectURI() + "?" + URL_DATA + "&redirectURI=" + redirectTo;
            return "https://api.weibo.com/oauth2/authorize?response_type=code" //
                    + "&display=default" //
                    + "&client_id=" + this.appID //
                    + "&redirect_uri=" + URLEncoder.encode(redirectURI, "utf-8") //
                    + "&scope=" + this.scope;
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
    @Override
    public ResultDO<AccessInfo> evalToken(String status, String authCode) {
        //
        // .生成获取 token 的 url
        String tokenURL = null;
        try {
            tokenURL = "https://api.weibo.com/oauth2/access_token?grant_type=authorization_code" //
                    + "&client_id=" + this.appID //
                    + "&client_secret=" + this.appKey//
                    + "&code=" + authCode//
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
        Map<String, Object> dataMaps = null;
        try {
            logger.error("weibo_access_token :authCode = {} , build token URL -> {}.", authCode, tokenURL);
            Response response = this.httpClient.httpPost(tokenURL);
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
            dataMaps = JsonUtils.toMap(response.getResponseAsString());
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
            return failed(ErrorCodes.OA_TOKEN_EXT_ERROR, e);
        }
        //
        // .认证失败
        if (dataMaps == null || dataMaps.containsKey("error")) {
            String errorKey = dataMaps.get("error").toString();
            String errorCode = dataMaps.get("error_code").toString();
            String errorDesc = dataMaps.get("error_description").toString();
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
                    .addLog("errorKey", errorKey)//
                    .addLog("errorCode", errorCode)//
                    .addLog("errorDesc", errorDesc)//
                    .toJson());
            return failed(ErrorCodes.OA_TOKEN_EXT_FAILED);
        }
        //
        // .获取用户信息
        String data = "{}";
        String userID = dataMaps.get("uid").toString();
        try {
            String access_token = dataMaps.get("access_token").toString();
            Response response = this.httpClient.httpGet("https://api.weibo.com/2/users/show.json"//
                    + "?access_token=" + access_token //
                    + "&uid=" + userID);//
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
                        .addLog("userID", userID)//
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
                    .addLog("userID", userID)//
                    .addLog("tokenURL", tokenURL)//
                    .toJson());
            return failed(ErrorCodes.OA_TOKEN_EXT_ERROR, e);
        }
        //
        // .数据解析
        WeiboAccessInfo accessInfo = JsonUtils.toObject(data, WeiboAccessInfo.class);
        accessInfo.setAccessToken(dataMaps.get("access_token").toString());
        accessInfo.setExpires_in(Long.parseLong(dataMaps.get("expires_in").toString()));
        accessInfo.setRemind_in(dataMaps.get("remind_in").toString());
        accessInfo.setAccessUserID(dataMaps.get("uid").toString());
        //
        logger.info("access_token : success -> token : {} , sourceID : {}.", accessInfo.getAccessToken(), accessInfo.getSource());
        return success(accessInfo);
    }
    @Override
    public UserDO convertTo(AccessInfo result) {
        WeiboAccessInfo accessInfo = (WeiboAccessInfo) result;
        UserDO userDO = new UserDO();
        userDO.setAccount("");
        userDO.setPassword("-");
        userDO.setEmail("");
        userDO.setMobilePhone("");
        userDO.setNick(accessInfo.getScreen_name());
        userDO.setAvatar(accessInfo.getAvatar_large());
        if (StringUtils.isBlank(userDO.getNick())) {
            userDO.setNick(PROVIDER_NAME + "_" + System.currentTimeMillis());
        }
        //
        userDO.setUserSourceList(new ArrayList<UserSourceDO>());
        userDO.getUserSourceList().add(convertAccessInfo(result));
        if (StringUtils.equalsIgnoreCase(accessInfo.getGender(), "m")) {
            userDO.setGender(GenderType.Male);
        } else if (StringUtils.equalsIgnoreCase(accessInfo.getGender(), "f")) {
            userDO.setGender(GenderType.Female);
        } else {
            userDO.setGender(GenderType.None);
        }
        userDO.setStatus(UserStatus.Normal);
        userDO.setType(UserType.Temporary);
        //
        userDO.setFutures(new UserFutures());
        userDO.getFutures().setName(accessInfo.getName());
        userDO.getFutures().setPresent(accessInfo.getDescription());
        //
        userDO.setContactInfo(new UserContactInfo());
        userDO.getContactInfo().setBlogHome(accessInfo.getUrl());
        //
        ContactAddressInfo userAddressInfo = new ContactAddressInfo();
        //
        userAddressInfo.setProvinceCode(String.valueOf(accessInfo.getProvince()));
        userAddressInfo.setCityCode(String.valueOf(accessInfo.getCity()));
        userAddressInfo.setLocation(accessInfo.getLocation());
        userDO.getContactInfo().setUserAddress(userAddressInfo);
        return userDO;
    }
    //
}