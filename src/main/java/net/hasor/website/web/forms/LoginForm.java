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
package net.hasor.website.web.forms;
import net.hasor.restful.api.ReqParam;
import net.hasor.restful.api.ValidBy;
import net.hasor.website.web.valids.LoginFormValidation;
/**
 * 登录表单,指定 LoginFormValidation 类为它的验证器。
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
@ValidBy(LoginFormValidation.class)
public class LoginForm {
    @ReqParam("login")
    private String login;
    @ReqParam("password")
    private String password;
    @ReqParam("redirectURI")
    private String redirectURI;
    //
    //
    public String getLogin() {
        return login;
    }
    public void setLogin(String login) {
        this.login = login;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRedirectURI() {
        return redirectURI;
    }
    public void setRedirectURI(String redirectURI) {
        this.redirectURI = redirectURI;
    }
}