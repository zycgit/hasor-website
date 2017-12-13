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
package net.hasor.website.web.utils;
import net.hasor.website.core.Service;
import net.hasor.website.domain.beans.AppConstant;
import net.hasor.website.domain.owner.SimpleOwner;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
/**
 *
 * @version : 2016年12月05日
 * @author 赵永春 (zyc@hasor.net)
 */
@Service("loginUtils")
public class LoginUtils {
    //
    /**获取登录用户*/
    public static SimpleOwner getUser(HttpServletRequest request) {
        SimpleOwner owner = (SimpleOwner) request.getSession(true).getAttribute(AppConstant.SESSION_KEY_USER);
        return owner;
    }
    //
    /**登录用户的Nick*/
    public static String getUserNick(HttpServletRequest request) {
        SimpleOwner owner = getUser(request);
        if (owner == null) {
            return "";
        } else {
            return owner.getOwnerName();
        }
    }
    /**获取登陆用户的头像*/
    public static String getUserAvatar(HttpServletRequest request) {
        try {
            Object userAvatar = request.getSession(true).getAttribute(AppConstant.SESSION_KEY_USER_AVATAR);
            if (userAvatar == null) {
                return "";
            } else {
                return userAvatar.toString();
            }
        } catch (Exception e) {
            return "";
        }
    }
    //
    /**登录用户的ID*/
    public static long getUserID(HttpServletRequest request) {
        SimpleOwner owner = getUser(request);
        if (owner == null) {
            return 0L;
        } else {
            return owner.getOwnerID();
        }
    }
    /**获取最后一次调用登陆系统返回的UserID*/
    public static long getTargetUserID(HttpServletRequest request) {
        try {
            Object userID = request.getSession(true).getAttribute(AppConstant.SESSION_KEY_TARGET_USER_ID);
            if (userID == null) {
                return 0;
            } else {
                return Long.parseLong(userID.toString());
            }
        } catch (Exception e) {
            return 0;
        }
    }
    /**获取最后一次调用登陆系统用的来源*/
    public static String getTargetPrivider(HttpServletRequest request) {
        try {
            Object targetPrivider = request.getSession(true).getAttribute(AppConstant.SESSION_KEY_TARGET_PROVIDER);
            if (targetPrivider == null) {
                return "";
            } else {
                return targetPrivider.toString();
            }
        } catch (Exception e) {
            return "";
        }
    }
    //
    /**是否已登录*/
    public static boolean isLogin(HttpServletRequest request) {
        return getUserID(request) > 0 && StringUtils.isNotBlank(getUserNick(request));
    }
}