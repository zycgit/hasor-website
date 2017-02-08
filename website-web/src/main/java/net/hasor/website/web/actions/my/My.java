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
import net.hasor.web.Invoker;
import net.hasor.web.annotation.MappingTo;
import net.hasor.website.domain.UserDO;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.utils.LoggerUtils;

import java.io.IOException;
/**
 * 个人首页
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/my/my.htm")
public class My extends BaseMyProject {
    //
    public void execute(Invoker data) throws IOException {
        // .need login
        if (needLogin()) {
            return;
        }
        // .need login
        if (!super.fillProjectInfo(0L)) {
            return;
        }
        // .user info
        UserDO user = super.fullUserInfo(this.getUserID());
        if (user == null) {
            logger.error(LoggerUtils.create("ERROR_002_0001")//
                    .addLog("userID", this.getUserID()) //
                    .addLog("error", "result is null.") //
                    .toJson());
            sendError(ErrorCodes.U_GET_USER_NOT_EXIST.getMsg());
            return;
        }
        this.putData("userData", user);
    }
}