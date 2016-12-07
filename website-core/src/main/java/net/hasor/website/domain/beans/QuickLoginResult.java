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
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package net.hasor.website.domain.beans;
import net.hasor.website.domain.UserDO;
/**
 * 快速登陆返回值
 * @version : 2016年12月05日
 * @author 赵永春(zyc@hasor.net)
 */
public class QuickLoginResult {
    private UserDO userDO;
    private String provider;
    private String redirectURL;
    //
    public UserDO getUserDO() {
        return this.userDO;
    }
    public void setUserDO(UserDO userDO) {
        this.userDO = userDO;
    }
    public String getProvider() {
        return this.provider;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public String getRedirectURL() {
        return this.redirectURL;
    }
    public void setRedirectURL(String redirectURL) {
        this.redirectURL = redirectURL;
    }
}