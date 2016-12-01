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
package net.hasor.website.manager;
import net.hasor.core.Inject;
import net.hasor.core.Singleton;
import net.hasor.db.Transactional;
import net.hasor.website.core.AppConstant;
import net.hasor.website.core.Cache;
import net.hasor.website.datadao.UserDAO;
import net.hasor.website.datadao.UserSourceDAO;
import net.hasor.website.domain.UserDO;
import net.hasor.website.domain.UserSourceDO;
import net.hasor.website.domain.beans.QuickLoginResult;
import net.hasor.website.utils.JsonUtils;
import org.more.bizcommon.json.JSON;
import org.more.bizcommon.log.LogUtils;
import org.more.util.CommonCodeUtils;
import org.more.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.*;
/**
 * 用户Manager
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
@Singleton
public class UserManager {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Inject
    private UserSourceDAO userSourceDAO;
    @Inject
    private UserDAO       userDAO;
    @Inject(AppConstant.CACHE_USER)
    private Cache         userCache;
    //
    public UserDO getUserByProvider(String provider, String uniqueID) throws SQLException {
        UserSourceDO sourceDO = this.userSourceDAO.queryByUnique(provider, uniqueID);
        if (sourceDO == null || sourceDO.getUserID() <= 0) {
            return null;
        }
        UserDO userDO = this.userDAO.queryById(sourceDO.getUserID());
        if (userDO == null) {
            return null;
        }
        userDO.setUserSourceList(new ArrayList<UserSourceDO>());
        userDO.getUserSourceList().add(sourceDO);
        return userDO;
    }
    public UserDO getUserByID(long userID) {
        if (userID <= 0) {
            return null;
        }
        try {
            return this.userDAO.queryById(userID);
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_999_0003").logException(e) //
                    .addString("UserManager : getUserByID error -> " + e.getMessage()).toJson());
            return null;
        }
    }
    //
    public UserDO queryByLogin(String login) {
        if (StringUtils.isBlank(login)) {
            return null;
        }
        try {
            return this.userDAO.queryByLogin(login);
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_999_0003").logException(e) //
                    .addString("UserManager : queryByLogin error -> " + e.getMessage()).toJson());
            return null;
        }
    }
    public UserDO getFullUserDataByID(long userID) {
        UserDO userDO = getUserByID(userID);
        if (userDO == null) {
            return null;
        }
        try {
            List<UserSourceDO> sourceList = this.userSourceDAO.queryListByUserID(userID);
            if (sourceList != null) {
                userDO.setUserSourceList(sourceList);
            }
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_999_0003").logException(e) //
                    .addString("UserManager : getFullUserDataByID error -> " + e.getMessage()).toJson());
        }
        return userDO;
    }
    //
    @Transactional
    public long newUser(UserDO userDO) throws SQLException {
        // 1. 保存用户数据 2. 保存携带的外部登录信息数据
        int userResult = this.userDAO.insertUser(userDO);
        if (userResult > 0) {
            List<UserSourceDO> sourceList = userDO.getUserSourceList();
            if (sourceList != null) {
                for (UserSourceDO sourceDO : sourceList) {
                    sourceDO.setUserID(userDO.getUserID());
                    //
                    int sourceResult = this.userSourceDAO.insertUserSource(sourceDO);
                    if (sourceResult <= 0) {
                        throw new IllegalStateException("登录信息保存失败。");
                    }
                }
            }
        }
        return userDO.getUserID();
    }
    @Transactional
    public int updateAccessInfo(UserDO userDO, String provider, UserSourceDO result) throws SQLException {
        return this.userSourceDAO.updateUserSource(provider, userDO.getUserID(), result);
    }
    @Transactional
    public void loginUpdate(UserDO userDO, String provider) {
        try {
            this.userDAO.loginUpdate(userDO.getUserID());
            if (StringUtils.isNotBlank(provider)) {
                this.userSourceDAO.loginUpdateByUserID(provider, userDO.getUserID());
            }
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_999_0003").logException(e) //
                    .addString("loginUpdate : " + e.getMessage()).toJson());
        }
    }
    /** 开始一个快速登陆（快速登陆只需要一个URL即可完成登陆，但是快速登陆有有效期时间限制），方法返回登陆地址（从生成登陆链接之后必须在6秒内访问） */
    public String startQuickLogin(long userID, String provider, String redirectURL) throws NoSuchAlgorithmException {
        Map<String, String> quickInfo = new HashMap<String, String>();
        quickInfo.put("userID", String.valueOf(userID));
        quickInfo.put("provider", provider);
        quickInfo.put("redirectURL", redirectURL);
        quickInfo.put("atTime", String.valueOf(new Date().getTime()));
        quickInfo.put("lostTime", String.valueOf(new Date().getTime() + 6 * 1000));
        String quickJson = JSON.toString(quickInfo);
        //
        String userIDHex = toHexString(userID);
        String key = CommonCodeUtils.MD5.getMD5(userIDHex + "-" + quickJson);
        this.userCache.put(key, quickJson, 6000);
        return "/account/loginJump.do?userCode=" + userIDHex + "&quickCode=" + key;
    }
    /** 执行快速登陆 */
    public QuickLoginResult doQuickLogin(String userCode, String quickCode) {
        //
        String quickInfo = (String) this.userCache.get(quickCode);
        if (quickInfo == null)
            return null;
        //
        Map<String, Object> quickMap = JsonUtils.toMap(quickInfo);
        String userIDStr = (String) quickMap.get("userID");
        String provider = (String) quickMap.get("provider");
        String redirectURL = (String) quickMap.get("redirectURL");
        String lostTimeStr = (String) quickMap.get("lostTime");
        if (StringUtils.isBlank(userIDStr) || StringUtils.isBlank(lostTimeStr) || StringUtils.isBlank(redirectURL)) {
            return null;
        }
        //
        long lostTime = Long.parseLong(lostTimeStr);
        if (lostTime < System.currentTimeMillis()) {
            return null;
        }
        //
        String extUserCode = userCode;
        long userID = Long.parseLong(userIDStr);
        String cacheUserCode = toHexString(userID);
        if (!StringUtils.equalsIgnoreCase(extUserCode, cacheUserCode)) {
            return null;
        }
        //
        UserDO userDO = getUserByID(userID);
        this.loginUpdate(userDO, provider);
        QuickLoginResult result = new QuickLoginResult();
        result.setUserDO(userDO);
        result.setRedirectURL(redirectURL);
        return result;
    }
    private String toHexString(long userID) {
        String userIDHex = Long.toHexString(userID).toLowerCase();
        return StringUtils.leftPad(userIDHex, 32, "0").toLowerCase();
    }
}