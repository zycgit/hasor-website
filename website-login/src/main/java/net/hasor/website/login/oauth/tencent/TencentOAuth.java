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
package net.hasor.website.login.oauth.tencent;
import com.qq.connect.api.OpenID;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.Birthday;
import com.qq.connect.utils.QQConnectConfig;
import com.qq.connect.utils.http.Response;
import net.hasor.core.ApiBinder;
import net.hasor.core.InjectSettings;
import net.hasor.core.Settings;
import net.hasor.core.Singleton;
import net.hasor.utils.ExceptionUtils;
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
import net.hasor.website.domain.result.ResultDO;
import net.hasor.website.login.oauth.AbstractOAuth;
import net.hasor.website.utils.JsonUtils;
import net.hasor.website.utils.LoggerUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Map;

import static net.hasor.website.utils.ResultUtils.failed;
import static net.hasor.website.utils.ResultUtils.success;
/**
 * 封装腾讯登陆
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
@Singleton
public class TencentOAuth extends AbstractOAuth {
    public static final String PROVIDER_NAME = "Tencent";
    public static final String URL_DATA      = "provider=" + PROVIDER_NAME + "&type=website";
    //
    //QQ登录接入,授权key
    @InjectSettings("website-oauth.tencent.admins")
    private             String adminsCode    = null;
    //应用ID
    @InjectSettings("website-oauth.tencent.app_id")
    private             String appID         = null;
    //应用Key
    @InjectSettings("website-oauth.tencent.app_key")
    private             String appKey        = null;
    //权限
    @InjectSettings("website-oauth.tencent.oauth_scope")
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
    public TencentOAuth() {
        super();
    }
    public TencentOAuth(ApiBinder apiBinder) {
        super(apiBinder);
    }
    @Override
    public String getProviderName() {
        return PROVIDER_NAME;
    }
    @Override
    public void configOAuth(ApiBinder apiBinder) {
        Settings settings = apiBinder.getEnvironment().getSettings();
        String tencentAppID = settings.getString("website-oauth.tencent.app_id", "");
        QQConnectConfig.updateProperties("app_ID", tencentAppID);
        String tencentAppKey = settings.getString("website-oauth.tencent.app_key", "");
        QQConnectConfig.updateProperties("app_KEY", tencentAppKey);
        String redirectURI = settings.getString("website-oauth.config.redirectURI", "127.0.0.1");
        String tencentRedirectURI = redirectURI + "?" + TencentOAuth.URL_DATA;
        QQConnectConfig.updateProperties("redirect_URI", tencentRedirectURI);
        String oauth_scope = settings.getString("website-oauth.tencent.oauth_scope", "");
        QQConnectConfig.updateProperties("scope", oauth_scope);
    }
    //
    /**首次登录的跳转地址(参数为回跳地址)*/
    @Override
    public String evalLoginURL(String status, String redirectTo) {
        try {
            String redirectURI = this.getRedirectURI() + "?" + URL_DATA + "&redirectURI=" + redirectTo;
            return "https://graph.qq.com/oauth2.0/authorize?response_type=code" //
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
    @Override
    public ResultDO<AccessInfo> evalToken(String status, String authCode) {
        String tokenURL = null;
        try {
            tokenURL = "https://graph.qq.com/oauth2.0/token?grant_type=authorization_code" //
                    + "&client_id=" + this.appID //
                    + "&client_secret=" + this.appKey//
                    + "&code=" + authCode//
                    + "&state=" + (status == null ? "" : status) //
                    + "&redirect_uri=" + URLEncoder.encode(this.getRedirectURI() + "?" + TencentOAuth.URL_DATA, "utf-8");
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
        Response response = null;
        try {
            logger.error("tencent_access_token :authCode = {} , build token URL -> {}.", authCode, tokenURL);
            response = this.httpClient.httpGet(tokenURL);
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
            if (data.startsWith("callback(")) {
                //返回结果失败
                String jsonData = data.substring(9, data.length() - 3);//callback( {"error":100020,"error_description":"code is reused error"} );
                Map<String, Object> errorInfo = JsonUtils.toMap(jsonData);
                String errorCoe = errorInfo.get("error").toString();
                String errorDesc = errorInfo.get("error_description").toString();
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
                        .toJson());
                return failed(ErrorCodes.OA_TOKEN_EXT_FAILED);
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
            return failed(ErrorCodes.OA_TOKEN_EXT_ERROR, e);
        }
        //
        // .获取用户信息
        String access_token = "";
        String openID = "";
        try {
            AccessToken token = new AccessToken(response);
            access_token = token.getAccessToken();
            OpenID openIDObj = new OpenID(access_token);
            openID = openIDObj.getUserOpenID();
            com.qq.connect.api.qzone.UserInfo qzoneUserInfo = new com.qq.connect.api.qzone.UserInfo(access_token, openID);
            com.qq.connect.javabeans.qzone.UserInfoBean qzoneInfoBean = qzoneUserInfo.getUserInfo();
            //
            // .QQ空间信息
            TencentAccessInfo accessInfo = new TencentAccessInfo();
            accessInfo.setAccessToken(access_token);
            accessInfo.setExpiresTime(token.getExpireIn());
            accessInfo.setOpenID(openID);
            accessInfo.setOriInfo(response.getResponseAsString());
            accessInfo.setGender(qzoneInfoBean.getGender());
            accessInfo.setNickName(qzoneInfoBean.getNickname());
            accessInfo.setLevel(qzoneInfoBean.getLevel());
            accessInfo.setVip(qzoneInfoBean.isVip());
            accessInfo.setYellowYearVip(qzoneInfoBean.isYellowYearVip());
            accessInfo.setAvatarURL30(qzoneInfoBean.getAvatar().getAvatarURL30());
            accessInfo.setAvatarURL50(qzoneInfoBean.getAvatar().getAvatarURL50());
            accessInfo.setAvatarURL100(qzoneInfoBean.getAvatar().getAvatarURL100());
            //
            // .腾讯微博
            com.qq.connect.api.weibo.UserInfo weiboUserInfo = new com.qq.connect.api.weibo.UserInfo(access_token, openID);
            com.qq.connect.javabeans.weibo.UserInfoBean weiboInfoBean = weiboUserInfo.getUserInfo();
            if (weiboInfoBean.getRet() == 0) {
                //
                accessInfo.setCityCode(weiboInfoBean.getCityCode());
                accessInfo.setCountryCode(weiboInfoBean.getCountryCode());
                accessInfo.setProvinceCode(weiboInfoBean.getProvinceCode());
                accessInfo.setHomeCityCode(weiboInfoBean.getHomeCityCode());
                accessInfo.setHomeCountryCode(weiboInfoBean.getHomeCountryCode());
                accessInfo.setHomeProvinceCode(weiboInfoBean.getHomeProvinceCode());
                accessInfo.setHomeTownCode(weiboInfoBean.getHomeTownCode());
                accessInfo.setEmail(weiboInfoBean.getEmail());
                accessInfo.setWeiboLevel(weiboInfoBean.getLevel());
                accessInfo.setWeiboName(weiboInfoBean.getName());
                Birthday birthday = weiboInfoBean.getBirthday();
                if (birthday != null) {
                    String yearStr = new DecimalFormat("0000").format(birthday.getYear());
                    String monthStr = new DecimalFormat("00").format(birthday.getMonth());
                    String dayStr = new DecimalFormat("00").format(birthday.getDay());
                    accessInfo.setBirthday(yearStr + "-" + monthStr + "-" + dayStr);
                }
                accessInfo.setBlogHome(weiboInfoBean.getHomePage());
            }
            //
            logger.info("access_token : success -> token : {} , sourceID : {}.", accessInfo.getAccessToken(), accessInfo.getSource());
            return success(accessInfo);
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
                    .addLog("openID", openID)//
                    .addLog("tokenURL", tokenURL)//
                    .toJson());
            return failed(ErrorCodes.OA_TOKEN_EXT_ERROR, e);
        }
    }
    @Override
    public UserDO convertTo(AccessInfo result) {
        TencentAccessInfo accessInfo = (TencentAccessInfo) result;
        UserDO userDO = new UserDO();
        userDO.setAccount("");
        userDO.setPassword("-");
        userDO.setEmail("");
        userDO.setMobilePhone("");
        userDO.setNick(accessInfo.getNickName());
        userDO.setAvatar(accessInfo.getAvatarURL50());
        if (StringUtils.isBlank(userDO.getNick())) {
            userDO.setNick(PROVIDER_NAME + "_" + System.currentTimeMillis());
        }
        //
        userDO.setUserSourceList(new ArrayList<UserSourceDO>());
        userDO.getUserSourceList().add(convertAccessInfo(result));
        if (StringUtils.equalsIgnoreCase(accessInfo.getGender(), "男")) {
            userDO.setGender(GenderType.Male);
        } else if (StringUtils.equalsIgnoreCase(accessInfo.getGender(), "女")) {
            userDO.setGender(GenderType.Female);
        } else {
            userDO.setGender(GenderType.None);
        }
        userDO.setStatus(UserStatus.Normal);
        userDO.setType(UserType.Temporary);
        //
        userDO.setFutures(new UserFutures());
        userDO.getFutures().setBirthday(accessInfo.getBirthday());
        userDO.getFutures().setName(accessInfo.getWeiboName());
        //
        userDO.setContactInfo(new UserContactInfo());
        ContactAddressInfo userAddressInfo = new ContactAddressInfo();
        userAddressInfo.setCityCode(accessInfo.getCityCode());
        userAddressInfo.setCountryCode(accessInfo.getCountryCode());
        userAddressInfo.setProvinceCode(accessInfo.getProvinceCode());
        userAddressInfo.setTownCode("");
        userDO.getContactInfo().setUserAddress(userAddressInfo);
        ContactAddressInfo homeAddressInfo = new ContactAddressInfo();
        homeAddressInfo.setCityCode(accessInfo.getHomeCityCode());
        homeAddressInfo.setCountryCode(accessInfo.getHomeCountryCode());
        homeAddressInfo.setProvinceCode(accessInfo.getHomeProvinceCode());
        homeAddressInfo.setTownCode(accessInfo.getHomeTownCode());
        userDO.getContactInfo().setHomeAddress(homeAddressInfo);
        userDO.getContactInfo().setBlogHome(accessInfo.getBlogHome());
        return userDO;
    }
}