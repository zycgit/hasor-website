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
import net.hasor.website.manager.UserManager;
import net.hasor.website.web.core.Action;
import org.more.bizcommon.Result;

import java.io.IOException;
/**
 * 给当前登陆账号绑定登陆
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/account/bind.do")
public class Bind extends Action {
    @Inject
    private UserManager userManager;
    //
    public void execute(RenderData data) throws IOException {
        // .need login
        String ctx_path = data.getHttpRequest().getContextPath();
        if (!isLogin()) {
            data.getHttpResponse().sendRedirect(ctx_path + "/account/login.htm?redirectURI=" + ctx_path + "/my/my.htm");
            return;
        }
        //
        long targetUserID = this.getTargetUserID();
        String targetProivter = this.getTargetPrivider();
        //        UserDO targetUser = this.userManager.getFullUserDataByID(targetUserID);
        //        //
        //        // 检测该登陆方式已经绑定了其它账号
        //        if (targetUser.getType() == UserType.Master && targetUser.getStatus() == UserStatus.Normal) {
        //            sendRedirectError(ErrorCodes.OAUTH_BIND_FAILED.getMsg()); // 该登陆方式已经绑定了其它账号，请先解除绑定之后在操作
        //            return;
        //        }
        //        // 检测当前账号是否已经具有了同类型绑定
        //        UserDO cunterUser = this.userManager.getFullUserDataByID(getUserID());
        //        boolean hasProivter = false;
        //        for (UserSourceDO sourceDO : cunterUser.getUserSourceList()) {
        //            if (StringUtils.equalsIgnoreCase(sourceDO.getProvider(), targetProivter)) {
        //                hasProivter = true;
        //            }
        //        }
        //        if (hasProivter) {
        //            sendRedirectError(ErrorCodes.OAUTH_BIND_HAS_PROIVTER.getMsg());
        //            return;
        //        }
        //        //
        //        UserSourceDO sourceDO = this.userManager.queryUserSourceByUserID(targetUserID, targetProivter);
        Result<Boolean> result = this.userManager.reBindLogin(targetUserID, targetProivter, this.getUserID());
    }
}