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
package net.hasor.website.web.actions.my;
import net.hasor.core.Inject;
import net.hasor.web.RenderData;
import net.hasor.web.annotation.MappingTo;
import net.hasor.website.domain.UserDO;
import net.hasor.website.domain.UserSourceDO;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.login.oauth.OAuthManager;
import net.hasor.website.manager.UserManager;
import net.hasor.website.web.core.Action;
import net.hasor.website.web.model.UserBindInfo;
import org.more.bizcommon.log.LogUtils;
import org.more.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 个人首页
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/my/my.htm")
public class My extends Action {
    @Inject
    private OAuthManager oauthManager;
    @Inject
    private UserManager  userManager;
    //
    public void execute(RenderData data) throws IOException {
        // .need login
        if (needLogin()) {
            return;
        }
        // .user info
        UserDO user = this.userManager.getFullUserDataByID(this.getUserID());
        if (user == null) {
            logger.error(LogUtils.create("ERROR_002_0001")//
                    .addLog("userID", this.getUserID()) //
                    .addLog("error", "result is null.") //
                    .toJson());
            sendError(ErrorCodes.U_GET_USER_NOT_EXIST.getMsg());
            return;
        }
        this.putData("userData", user);
        // .
        List<String> providerList = this.oauthManager.getProviderList();
        List<UserBindInfo> infoList = new ArrayList<UserBindInfo>();
        for (String provider : providerList) {
            UserBindInfo info = new UserBindInfo();
            info.setAllow(true);
            info.setBind(false);
            info.setProvider(provider);
            info.setHtml_css(provider.toLowerCase());
            info.setHtml_id(provider.toLowerCase() + "AuthorizationUrl");
            String ctx_path = data.getHttpRequest().getContextPath();
            info.setHtml_href(this.oauthManager.evalLoginURL(provider, "", ctx_path + "/account/bind.do"));
            infoList.add(info);
            for (UserSourceDO sourceDO : user.getUserSourceList()) {
                if (StringUtils.equalsIgnoreCase(sourceDO.getProvider(), provider)) {
                    info.setBind(true);
                    info.setNick(sourceDO.getAccessInfo().getExternalUserNick());
                }
            }
        }
        this.putData("infoList", infoList);
    }
}