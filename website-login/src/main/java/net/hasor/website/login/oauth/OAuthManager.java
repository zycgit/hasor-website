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
package net.hasor.website.login.oauth;
import net.hasor.core.AppContext;
import net.hasor.core.Init;
import net.hasor.core.Inject;
import net.hasor.core.Singleton;
import net.hasor.website.core.Service;
import net.hasor.website.domain.AccessInfo;
import net.hasor.website.domain.UserDO;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.utils.JsonUtils;
import org.more.bizcommon.Message;
import org.more.bizcommon.Result;
import org.more.bizcommon.ResultDO;
import org.more.bizcommon.log.LogUtils;
import org.more.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
/**
 * 集成第三方登陆 & CAS 等
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
@Singleton
@Service("oauth")
public class OAuthManager {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Inject
    private AppContext                 appContext;
    private Map<String, AbstractOAuth> oauthMap;
    //
    @Init
    public void init() {
        //初始化所有注册的Oauth
        this.oauthMap = new HashMap<String, AbstractOAuth>();
        List<AbstractOAuth> oauthList = appContext.findBindingBean(AbstractOAuth.class);
        if (oauthList == null) {
            return;
        }
        for (AbstractOAuth oauth : oauthList) {
            if (oauth == null)
                continue;
            String provider = oauth.getProviderName();
            logger.error("oauth init provider {} -> {}", provider, oauth);
            this.oauthMap.put(provider.toUpperCase(), oauth);
        }
    }
    //
    public List<String> getProviderList() {
        List<String> tmpData = new ArrayList<String>(oauthMap.keySet());
        Collections.sort(tmpData);
        return tmpData;
    }
    //
    protected AbstractOAuth getOAuthByProvider(String provider) {
        if (StringUtils.isBlank(provider)) {
            logger.error(LogUtils.create("ERROR_004_0002")//
                    .toJson());
            return null;
        }
        return this.oauthMap.get(provider.toUpperCase());
    }
    //
    /** 取得Oauth 的请求URL，用户通过请求URL到外站进行登陆 */
    public String evalLoginURL(String provider, String status, String redirectTo) {
        AbstractOAuth oauth = this.getOAuthByProvider(provider);
        if (oauth == null) {
            logger.error(LogUtils.create("ERROR_004_0001")//
                    .addLog("provider", provider)//
                    .toJson());
            return null;
        }
        return oauth.evalLoginURL(status, redirectTo);
    }
    //
    /** 根据Auth取得的授权吗，通过远程调用取得用户信息 */
    public Result<UserDO> evalUserInfo(String provider, String authCode, String status) {
        AbstractOAuth oauth = this.getOAuthByProvider(provider);
        if (oauth == null) {
            logger.error(LogUtils.create("ERROR_004_0001")//
                    .addLog("provider", provider)//
                    .toJson());
            return new ResultDO<UserDO>(false)//
                    .setSuccess(false)//
                    .setResult(null)//
                    .addMessage(ErrorCodes.OA_PROIVTER_NOT_EXIST.getMsg());
        }
        //
        ResultDO<AccessInfo> info = oauth.evalToken(status, authCode);
        if (info == null) {
            logger.error(LogUtils.create("ERROR_004_0003")//
                    .addLog("provider", provider)//
                    .addLog("authCode", authCode)//
                    .addLog("status", status)//
                    .addLog("error", "result is null.")//
                    .toJson());
            return new ResultDO<UserDO>(false)//
                    .setSuccess(false)//
                    .setResult(null)//
                    .addMessage(ErrorCodes.OA_ERROR.getMsg());
        }
        //
        if (!info.isSuccess()) {
            Message errorMsg = info.firstMessage();
            logger.error(LogUtils.create("ERROR_004_0003")//
                    .addLog("provider", provider)//
                    .addLog("authCode", authCode)//
                    .addLog("status", status)//
                    .addMessage(errorMsg)//
                    .toJson());
            return new ResultDO<UserDO>(false)//
                    .setSuccess(false)//
                    .setResult(null)//
                    .addMessage(errorMsg);
        }
        //
        if (info.getResult() == null) {
            logger.error(LogUtils.create("ERROR_004_0003")//
                    .addLog("provider", provider)//
                    .addLog("authCode", authCode)//
                    .addLog("status", status)//
                    .addLog("error", "login success , but result is null.")//
                    .toJson());
            return new ResultDO<UserDO>(false)//
                    .setSuccess(false)//
                    .setResult(null)//
                    .addMessage(ErrorCodes.OA_ERROR.getMsg());
        }
        //
        AccessInfo accessInfo = info.getResult();
        logger.info("oauth_" + provider + " : login success , accessInfo = {}.", JsonUtils.toJsonStringSingleLine(accessInfo));
        //
        UserDO userDO = oauth.convertTo(accessInfo);
        return new ResultDO<UserDO>(true)//
                .setSuccess(true)//
                .setResult(userDO);
    }
}