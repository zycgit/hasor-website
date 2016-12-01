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
import net.hasor.website.domain.UserDO;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.manager.UserManager;
import net.hasor.website.login.oauth.OAuthManager;
import net.hasor.website.web.core.Action;
import net.hasor.website.web.model.UserBindInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 个人首页
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/account/my.htm")
public class My extends Action {
    @Inject
    private OAuthManager oauthManager;
    @Inject
    private UserManager  userManager;
    //
    public void execute(RenderData data) throws IOException {
        // .need login
        String ctx_path = data.getHttpRequest().getContextPath();
        if (!isLogin()) {
            data.getHttpResponse().sendRedirect(ctx_path + "/account/login.htm?redirectURI=" + ctx_path + "/account/my.htm");
        }
        // .user info
        UserDO user = this.userManager.getFullUserDataByID(this.getUserID());
        if (user == null) {
            sendError(ErrorCodes.RESULT_NULL.getMsg());
            return;
        }
        this.putData("userData", user);
        // .
        List<String> providerList = this.oauthManager.getProviderList();
        List<UserBindInfo> infoList = new ArrayList<UserBindInfo>();
        for (String provider : providerList) {
            UserBindInfo info = new UserBindInfo();
            info.setAllow(true);
            info.setProvider(provider);
            info.setHtml_css(provider.toLowerCase());
            info.setHtml_id(provider.toLowerCase() + "AuthorizationUrl");
            info.setHtml_href(this.oauthManager.evalLoginURL(provider, "", ctx_path + "/account/bind.htm"));
            infoList.add(info);
        }
        this.putData("infoList", infoList);
    }
}