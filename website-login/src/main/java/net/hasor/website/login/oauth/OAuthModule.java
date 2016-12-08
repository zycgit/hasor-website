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
import net.hasor.core.ApiBinder;
import net.hasor.core.Module;
import net.hasor.core.Settings;
import net.hasor.website.login.oauth.github.GithubAccessInfo;
import net.hasor.website.login.oauth.github.GithubOAuth;
import net.hasor.website.login.oauth.tencent.TencentAccessInfo;
import net.hasor.website.login.oauth.tencent.TencentOAuth;
import net.hasor.website.login.oauth.weibo.WeiboAccessInfo;
import net.hasor.website.login.oauth.weibo.WeiboOAuth;
/**
 * 集成第三方登陆 & CAS 等
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
public class OAuthModule implements Module {
    @Override
    public void loadModule(ApiBinder apiBinder) throws Throwable {
        Settings settings = apiBinder.getEnvironment().getSettings();
        //
        if (settings.getBoolean("website-oauth.tencent.enable", true)) {
            apiBinder.bindType(AbstractOAuth.class).nameWith(TencentOAuth.PROVIDER_NAME).to(TencentOAuth.class);
            new TencentOAuth(apiBinder);// .Tencent
            new TencentAccessInfo();
        }
        if (settings.getBoolean("website-oauth.github.enable", true)) {
            apiBinder.bindType(AbstractOAuth.class).nameWith(GithubOAuth.PROVIDER_NAME).to(GithubOAuth.class);
            new GithubOAuth(apiBinder); // .Github
            new GithubAccessInfo();
        }
        if (settings.getBoolean("website-oauth.weibo.enable", true)) {
            apiBinder.bindType(AbstractOAuth.class).nameWith(WeiboOAuth.PROVIDER_NAME).to(WeiboOAuth.class);
            new WeiboOAuth(apiBinder);  // .Weibo
            new WeiboAccessInfo();
        }
    }
}