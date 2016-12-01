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
import com.qq.connect.utils.http.HttpClientUtil;
import net.hasor.core.ApiBinder;
import net.hasor.core.Init;
import net.hasor.core.Inject;
import net.hasor.core.InjectSettings;
import net.hasor.website.domain.AccessInfo;
import net.hasor.website.domain.UserDO;
import net.hasor.website.domain.UserSourceDO;
import org.more.bizcommon.ResultDO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
public abstract class AbstractOAuth {
    protected Logger         logger      = LoggerFactory.getLogger(getClass());
    @InjectSettings("website-oauth.config.redirectURI")
    private   String         redirectURI = null;
    @Inject
    protected HttpClientUtil httpClient  = null;
    //
    public AbstractOAuth() {
    }
    public AbstractOAuth(ApiBinder apiBinder) {
        this.configOAuth(apiBinder);
    }
    protected UserSourceDO convertAccessInfo(AccessInfo accessInfo) {
        UserSourceDO sourceDO = new UserSourceDO();
        sourceDO.setProvider(accessInfo.getProvider());
        sourceDO.setUniqueID(accessInfo.getExternalUserID());
        sourceDO.setAccessInfo(accessInfo);
        sourceDO.setStatus(true);
        return sourceDO;
    }
    //
    @Init
    public void init() {
        this.redirectURI = this.redirectURI.replace("//", "/");
    }
    //
    protected String getRedirectURI() {
        return this.redirectURI;
    }
    //
    /**配置*/
    public abstract void configOAuth(ApiBinder apiBinder);

    /**名字*/
    public abstract String getProviderName();
    //
    /**登录的跳转地址(参数为回跳地址)*/
    public abstract String evalLoginURL(String status, String redirectTo);

    /**获取用户信息*/
    public abstract ResultDO<AccessInfo> evalToken(String status, String authCode);

    /**类型转换*/
    public abstract UserDO convertTo(AccessInfo result);
}