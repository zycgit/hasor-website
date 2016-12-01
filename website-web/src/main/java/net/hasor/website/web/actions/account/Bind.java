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
import net.hasor.restful.api.Params;
import net.hasor.restful.api.PathParam;
import net.hasor.restful.api.Valid;
import net.hasor.website.manager.UserManager;
import net.hasor.website.web.core.Action;
import net.hasor.website.web.forms.LoginForm;

import java.io.IOException;
/**
 * 登陆跳转
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/account/bind.do")
public class Bind extends Action {
    @Inject
    private UserManager userManager;
    //
    public void execute(@PathParam("action") String action, @Valid("SignIn") @Params LoginForm loginForm, RenderData data) throws IOException {
        String ctx_path = data.getHttpRequest().getContextPath();
    }
}