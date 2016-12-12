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
import net.hasor.db.transaction.TransactionCallback;
import net.hasor.db.transaction.TransactionStatus;
import net.hasor.db.transaction.TransactionTemplate;
import net.hasor.website.core.AppConstant;
import net.hasor.website.core.Cache;
import net.hasor.website.datadao.UserDAO;
import net.hasor.website.datadao.UserSourceDAO;
import net.hasor.website.domain.UserDO;
import net.hasor.website.domain.UserSourceDO;
import net.hasor.website.domain.beans.QuickLoginResult;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.domain.enums.UserType;
import net.hasor.website.utils.JsonUtils;
import org.more.bizcommon.Result;
import org.more.bizcommon.ResultDO;
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
    @Inject(AppConstant.CACHE_USER)
    private Cache               userCache;
    @Inject
    private UserDAO             userDAO;
    @Inject
    private UserSourceDAO       userSourceDAO;
    @Inject(AppConstant.DB_MYSQL)
    private TransactionTemplate transactionTemplate;
    //
    //
    public UserDO getUserByProvider(String provider, String uniqueID) {
        try {
            UserSourceDO sourceDO = this.userSourceDAO.queryByUnique(provider, uniqueID);
            if (sourceDO == null || sourceDO.getUserID() <= 0) {
                logger.error(LogUtils.create("ERROR_002_0002")//
                        .addLog("queryType", "userSourceDAO.queryByUnique") //
                        .addLog("provider", provider) //
                        .addLog("uniqueID", uniqueID) //
                        .toJson());
                return null;
            }
            UserDO userDO = this.userDAO.queryById(sourceDO.getUserID());
            if (userDO == null) {
                logger.error(LogUtils.create("ERROR_002_0001")//
                        .addLog("queryType", "userDAO.queryById") //
                        .addLog("userID", sourceDO.getUserID()) //
                        .addLog("source_provider", provider) //
                        .addLog("source_uniqueID", uniqueID) //
                        .toJson());
                return null;
            }
            userDO.setUserSourceList(new ArrayList<UserSourceDO>());
            userDO.getUserSourceList().add(sourceDO);
            return userDO;
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_002_0001")//
                    .addLog("queryType", "userDAO.queryById") //
                    .addLog("source_provider", provider) //
                    .addLog("source_uniqueID", uniqueID) //
                    .addLog("error", "query error -> " + e.getMessage()) //
                    .toJson(), e);
            return null;
        }
    }
    //
    public UserDO getUserByID(long userID) {
        if (userID <= 0) {
            logger.error(LogUtils.create("ERROR_002_0001")//
                    .addLog("userID", userID) //
                    .addLog("error", "userID error.") //
                    .toJson());
            return null;
        }
        try {
            UserDO userDO = this.userDAO.queryById(userID);
            if (userDO == null) {
                logger.error(LogUtils.create("ERROR_002_0001")//
                        .addLog("queryType", "userDAO.queryById") //
                        .addLog("userID", userID) //
                        .addLog("error", "query result is empty.") //
                        .toJson());
            }
            return userDO;
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_002_0001")//
                    .addLog("queryType", "userDAO.queryById") //
                    .addLog("userID", userID) //
                    .addLog("error", "query error -> " + e.getMessage()) //
                    .toJson(), e);
            return null;
        }
    }
    //
    public UserDO queryByLogin(String login) {
        if (StringUtils.isBlank(login)) {
            logger.error(LogUtils.create("ERROR_002_0001")//
                    .addLog("error", "login input is blank.") //
                    .toJson());
            return null;
        }
        try {
            UserDO userDO = this.userDAO.queryByLogin(login);
            if (userDO == null) {
                logger.error(LogUtils.create("ERROR_002_0001")//
                        .addLog("queryType", "userDAO.queryByLogin") //
                        .addLog("login", login) //
                        .addLog("error", "query result is empty.") //
                        .toJson());
            }
            return userDO;
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_002_0001")//
                    .addLog("queryType", "userDAO.queryByLogin") //
                    .addLog("login", login) //
                    .addLog("error", "query error -> " + e.getMessage()) //
                    .toJson(), e);
            return null;
        }
    }
    //
    public UserSourceDO queryUserSourceByUserID(long userID, String proivter) {
        if (StringUtils.isBlank(proivter) || userID <= 0) {
            logger.error(LogUtils.create("ERROR_002_0002")//
                    .addLog("proivter", proivter) //
                    .addLog("userID", userID) //
                    .addLog("error", "proivter is blank or userID <=0.") //
                    .toJson());
            return null;
        }
        try {
            UserSourceDO userSourceDO = this.userSourceDAO.queryByUserID(proivter, userID);
            if (userSourceDO == null) {
                logger.error(LogUtils.create("ERROR_002_0002")//
                        .addLog("queryType", "userSourceDAO.queryByUserID") //
                        .addLog("proivter", proivter) //
                        .addLog("userID", userID) //
                        .addLog("error", "query result is empty.") //
                        .toJson());
            }
            return userSourceDO;
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_002_0002")//
                    .addLog("queryType", "userSourceDAO.queryByUserID") //
                    .addLog("proivter", proivter) //
                    .addLog("userID", userID) //
                    .addLog("error", "query error -> " + e.getMessage()) //
                    .toJson(), e);
            return null;
        }
    }
    //
    public UserDO getFullUserDataByID(long userID) {
        UserDO userDO = this.getUserByID(userID);
        if (userDO == null) {
            logger.error(LogUtils.create("ERROR_002_0001")//
                    .addLog("userID", userID) //
                    .addLog("error", "query failed.") //
                    .toJson());
            return null;
        }
        userDO.setUserSourceList(Collections.EMPTY_LIST);
        try {
            List<UserSourceDO> sourceList = this.userSourceDAO.queryListByUserID(userID);
            if (sourceList != null) {
                userDO.setUserSourceList(sourceList);
            } else {
                logger.info(LogUtils.create("ERROR_002_0003")//
                        .addLog("queryType", "userSourceDAO.queryListByUserID") //
                        .addLog("userID", userID) //
                        .toJson());
            }
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_999_0003")//
                    .addLog("queryType", "userSourceDAO.queryListByUserID") //
                    .addLog("userID", userID) //
                    .addLog("error", "query error -> " + e.getMessage()) //
                    .toJson(), e);
        }
        return userDO;
    }
    //
    public Result<Long> newUser(final UserDO userDO) {
        // .检查是否被占用
        String loginData = "";
        try {
            UserDO query = null;
            if (query == null && StringUtils.isNotBlank(userDO.getAccount())) {
                loginData = "account:" + userDO.getAccount();
                query = this.userDAO.queryByLogin(userDO.getAccount());
            }
            if (query == null && StringUtils.isNotBlank(userDO.getEmail())) {
                loginData = "email:" + userDO.getEmail();
                query = this.userDAO.queryByLogin(userDO.getEmail());
            }
            if (query == null && StringUtils.isNotBlank(userDO.getMobilePhone())) {
                loginData = "mobile:" + userDO.getMobilePhone();
                query = this.userDAO.queryByLogin(userDO.getMobilePhone());
            }
            if (query != null) {
                ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                        .setSuccess(false)//
                        .addMessage(ErrorCodes.U_SAVE_USER_EXIST.getMsg(loginData))//
                        .setResult(0L);
                logger.error(LogUtils.create("ERROR_002_0004")//
                        .addLog("loginData", loginData) //
                        .addLog("error", resultDO.firstMessage().getMessage()) //
                        .toJson());
                return resultDO;
            }
        } catch (Exception e) {
            ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                    .setSuccess(false)//
                    .setThrowable(e)//
                    .addMessage(ErrorCodes.U_GET_USER_FAILED.getMsg(loginData))//
                    .setResult(0L);
            logger.error(LogUtils.create("ERROR_999_0003")//
                    .addLog("loginData", loginData) //
                    .addLog("error", resultDO.firstMessage().getMessage()) //
                    .toJson(), e);
            return resultDO;
        }
        //
        // .保存用户数据
        try {
            return this.transactionTemplate.execute(new TransactionCallback<Result<Long>>() {
                public Result<Long> doTransaction(TransactionStatus tranStatus) throws Throwable {
                    return saveNewUser(tranStatus, userDO);
                }
            });
        } catch (Throwable e) {
            ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.U_SAVE_USER_FAILED.getMsg())//
                    .setThrowable(e)//
                    .setResult(0L);
            logger.error(LogUtils.create("ERROR_002_0005")//
                    .addLog("error", resultDO.firstMessage().getMessage()) //
                    .toJson());
            return resultDO;
        }
    }
    private Result<Long> saveNewUser(TransactionStatus tranStatus, UserDO userDO) throws SQLException {
        // .保存用户数据
        try {
            long userResult = this.userDAO.insertUser(userDO);
            if (userResult <= 0) {
                ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                        .setSuccess(false)//
                        .addMessage(ErrorCodes.U_SAVE_USER_FAILED.getMsg())//
                        .setResult(0L);
                logger.error(LogUtils.create("ERROR_002_0006")//
                        .addLog("error", resultDO.firstMessage().getMessage()) //
                        .toJson());
                tranStatus.setRollbackOnly(); //回滚事务
                return resultDO;
            }
        } catch (Exception e) {
            ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.U_SAVE_USER_FAILED.getMsg())//
                    .setResult(0L);
            logger.error(LogUtils.create("ERROR_002_0005")//
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            tranStatus.setRollbackOnly(); //回滚事务
            return resultDO;
        }
        // .保存外部登陆
        try {
            List<UserSourceDO> sourceList = userDO.getUserSourceList();
            if (sourceList != null) {
                for (UserSourceDO sourceDO : sourceList) {
                    sourceDO.setUserID(userDO.getUserID());
                    sourceDO.setOriUserID(userDO.getUserID());
                    long sourceResult = this.userSourceDAO.insertUserSource(sourceDO);
                    if (sourceResult <= 0) {
                        ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                                .setSuccess(false)//
                                .addMessage(ErrorCodes.U_SAVE_USER_FAILED.getMsg())//
                                .setResult(0L);
                        logger.error(LogUtils.create("ERROR_002_0007")//
                                .addLog("error", resultDO.firstMessage().getMessage()) //
                                .toJson());
                        tranStatus.setRollbackOnly(); //回滚事务
                        return resultDO;
                    }
                }
            }
            // .返回用户ID
            return new ResultDO<Long>(false)//
                    .setSuccess(true)//
                    .setResult(userDO.getUserID());
        } catch (Exception e) {
            ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.U_SAVE_USER_FAILED.getMsg())//
                    .setResult(0L);
            logger.error(LogUtils.create("ERROR_002_0008")//
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            tranStatus.setRollbackOnly(); //回滚事务
            return resultDO;
        }
    }
    //
    public Result<Long> updateAccessInfo(UserDO userDO, String provider, UserSourceDO result) {
        try {
            int res = this.userSourceDAO.updateUserSource(provider, userDO.getUserID(), result);
            if (res <= 0) {
                ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                        .setSuccess(false)//
                        .addMessage(ErrorCodes.U_SAVE_SOURCE_FAILED.getMsg())//
                        .setResult((long) res);
                logger.error(LogUtils.create("ERROR_002_0009")//
                        .addLog("provider", provider) //
                        .addLog("userID", userDO.getUserID()) //
                        .toJson());
                return resultDO;
            } else {
                return new ResultDO<Long>(true)//
                        .setSuccess(true)//
                        .setResult(userDO.getUserID());
            }
        } catch (Exception e) {
            ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.U_SAVE_SOURCE_FAILED.getMsg())//
                    .setResult(0L);
            logger.error(LogUtils.create("ERROR_999_0003")//
                    .addLog("provider", provider) //
                    .addLog("userID", userDO.getUserID()) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            return resultDO;
        }
    }
    //
    @Transactional
    public void loginUpdate(UserDO userDO, String provider) {
        try {
            this.userDAO.loginUpdate(userDO.getUserID());
            if (StringUtils.isNotBlank(provider)) {
                this.userSourceDAO.loginUpdateByUserID(provider, userDO.getUserID());
            }
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_999_0003")//
                    .addLog("provider", provider) //
                    .addLog("userID", userDO.getUserID()) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
        }
    }
    //
    /** 开始一个快速登陆（快速登陆只需要一个URL即可完成登陆，但是快速登陆有有效期时间限制），方法返回登陆地址（从生成登陆链接之后必须在6秒内访问） */
    public String startQuickLogin(long userID, String provider, String redirectURL) {
        Map<String, String> quickInfo = new HashMap<String, String>();
        quickInfo.put("userID", String.valueOf(userID));
        quickInfo.put("provider", provider);
        quickInfo.put("redirectURL", redirectURL);
        quickInfo.put("atTime", String.valueOf(new Date().getTime()));
        quickInfo.put("lostTime", String.valueOf(new Date().getTime() + 6 * 1000));
        String quickJson = JSON.toString(quickInfo);
        //
        String userIDHex = toHexString(userID);
        String key = null;
        try {
            key = CommonCodeUtils.MD5.getMD5(userIDHex + "-" + quickJson);
        } catch (NoSuchAlgorithmException e) {
            logger.error(LogUtils.create("ERROR_003_0001")//
                    .addLog("provider", provider) //
                    .addLog("userID", userID) //
                    .addLog("error", "NoSuchAlgorithm 'MD5' -> " + e.getMessage()) //
                    .toJson(), e);
        }
        this.userCache.put(key, quickJson, 6000);
        return "/account/loginJump.do?userCode=" + userIDHex + "&quickCode=" + key;
    }
    //
    /** 执行快速登陆 */
    public QuickLoginResult doQuickLogin(String userCode, String quickCode) {
        // .空数据
        String quickInfo = (String) this.userCache.get(quickCode);
        if (StringUtils.isBlank(quickInfo)) {
            logger.error(LogUtils.create("ERROR_003_0002")//
                    .addLog("userCode", userCode) //
                    .addLog("quickCode", quickCode) //
                    .addLog("error", "no such data") //
                    .toJson());
            return null;
        }
        // .关键字段检测
        Map<String, Object> quickMap = JsonUtils.toMap(quickInfo);
        String userIDStr = (String) quickMap.get("userID");
        String provider = (String) quickMap.get("provider");
        String redirectURL = (String) quickMap.get("redirectURL");
        String lostTimeStr = (String) quickMap.get("lostTime");
        if (StringUtils.isBlank(userIDStr) || StringUtils.isBlank(lostTimeStr) || StringUtils.isBlank(redirectURL)) {
            logger.error(LogUtils.create("ERROR_003_0003")//
                    .addLog("userCode", userCode) //
                    .addLog("quickCode", quickCode) //
                    .addLog("userIDStr", userIDStr) //
                    .addLog("lostTimeStr", lostTimeStr) //
                    .addLog("redirectURL", redirectURL) //
                    .toJson());
            return null;
        }
        // .超时判断
        long lostTime = Long.parseLong(lostTimeStr);
        long atTime = System.currentTimeMillis();
        if (lostTime < atTime) {
            logger.error(LogUtils.create("ERROR_003_0004")//
                    .addLog("userCode", userCode) //
                    .addLog("quickCode", quickCode) //
                    .addLog("userIDStr", userIDStr) //
                    .addLog("lostTime", lostTime) //
                    .addLog("atTime", atTime) //
                    .toJson());
            return null;
        }
        // .用户ID校验
        String extUserCode = userCode;
        long userID = Long.parseLong(userIDStr);
        String cacheUserCode = toHexString(userID);
        if (!StringUtils.equalsIgnoreCase(extUserCode, cacheUserCode)) {
            logger.error(LogUtils.create("ERROR_003_0005")//
                    .addLog("userCode", userCode) //
                    .addLog("quickCode", quickCode) //
                    .addLog("userIDStr", userIDStr) //
                    .addLog("lostTime", lostTime) //
                    .addLog("atTime", atTime) //
                    .toJson());
            return null;
        }
        //
        UserDO userDO = getUserByID(userID);
        this.loginUpdate(userDO, provider);
        QuickLoginResult result = new QuickLoginResult();
        result.setUserDO(userDO);
        result.setProvider(provider);
        result.setRedirectURL(redirectURL);
        logger.info(LogUtils.create("INFO_003_0006")//
                .addLog("userCode", userCode) //
                .addLog("quickCode", quickCode) //
                .addLog("userID", userDO.getUserID()) //
                .addLog("provider", provider) //
                .addLog("redirectURL", redirectURL) //
                .addLog("atTime", atTime) //
                .toJson());
        //
        return result;
    }
    private String toHexString(long userID) {
        String userIDHex = Long.toHexString(userID).toLowerCase();
        return StringUtils.leftPad(userIDHex, 32, "0").toLowerCase();
    }
    //
    /** 将source 绑定到 指定userID上，source最初绑定的 userDO 如果不在存在其它登陆项目，并且为临时账号的话。账号会被置为失效 */
    public Result<Boolean> reBindLogin(final long targetUserID, final String targetProivter, final long bindToUserID) {
        //
        // .获取接受绑定的账号
        UserDO currentUser = this.getFullUserDataByID(bindToUserID);
        if (currentUser == null) {
            ResultDO<Boolean> result = new ResultDO<Boolean>()//
                    .setSuccess(false)//
                    .setResult(false)//
                    .addMessage(ErrorCodes.U_GET_USER_NOT_EXIST.getMsg());
            logger.error(LogUtils.create("ERROR_002_0001")//
                    .addLog("userID", targetUserID) //
                    .toJson());
            return result;
        }
        // .检测是否已经拥有同类型登陆方式，如果有则放弃后续绑定。一个账号同一个proivter只能绑定一个外部账号。
        UserSourceDO sourceProivter = null;
        for (UserSourceDO sourceDO : currentUser.getUserSourceList()) {
            if (StringUtils.equalsIgnoreCase(sourceDO.getProvider(), targetProivter)) {
                sourceProivter = sourceDO;
            }
        }
        if (sourceProivter != null) {
            ResultDO<Boolean> result = new ResultDO<Boolean>()//
                    .setSuccess(false)//
                    .setResult(false)//
                    .addMessage(ErrorCodes.U_PROIVTER_EXIST.getMsg());
            logger.error(LogUtils.create("ERROR_002_0010")//
                    .addLog("bindToUserID", bindToUserID) //
                    .addLog("targetUserID", targetUserID) //
                    .addLog("targetProivter", targetProivter) //
                    .addLog("sourceID", sourceProivter.getSourceID()) //
                    .toJson());
            return result;
        }
        // .检测即将绑定的 source 是否存在
        UserSourceDO sourceDO = this.queryUserSourceByUserID(targetUserID, targetProivter);
        if (sourceDO == null) {
            ResultDO<Boolean> result = new ResultDO<Boolean>()//
                    .setSuccess(false)//
                    .setResult(false);
            logger.error(LogUtils.create("ERROR_002_0002")//
                    .addLog("bindToUserID", bindToUserID) //
                    .addLog("targetUserID", targetUserID) //
                    .addLog("targetProivter", targetProivter) //
                    .toJson());
            return result;
        }
        // .下列情景必须先解绑：1.主账号、2.绑定多个登陆方式
        UserDO targetUser = this.getFullUserDataByID(targetUserID);
        if (targetUser == null) {
            ResultDO<Boolean> result = new ResultDO<Boolean>()//
                    .setSuccess(false)//
                    .setResult(false)//
                    .addMessage(ErrorCodes.U_GET_USER_NOT_EXIST.getMsg());
            logger.error(LogUtils.create("ERROR_002_0001")//
                    .addLog("userID", targetUserID) //
                    .toJson());
            return result;
        }
        if (!(targetUser.getType() == UserType.Temporary && targetUser.getUserSourceList().size() == 1)) {
            ResultDO<Boolean> result = new ResultDO<Boolean>()//
                    .setSuccess(false)//
                    .setResult(false)//
                    .addMessage(ErrorCodes.U_PROIVTER_MAST_UNBIND.getMsg());
            logger.error(LogUtils.create("ERROR_002_0011")//
                    .addLog("bindToUserID", bindToUserID) //
                    .addLog("targetUserID", targetUserID) //
                    .addLog("targetProivter", targetProivter) //
                    .addLog("targetUserType", targetUser.getType().name()) //
                    .addLog("targetSize", targetUser.getUserSourceList().size()) //
                    .toJson());
            return result;
        }
        //
        // .重新绑定
        try {
            return this.transactionTemplate.execute(new TransactionCallback<Result<Boolean>>() {
                public Result<Boolean> doTransaction(TransactionStatus tranStatus) throws Throwable {
                    return rebind(tranStatus, targetUserID, targetProivter, bindToUserID);
                }
            });
        } catch (Throwable e) {
            ResultDO<Boolean> result = new ResultDO<Boolean>()//
                    .setThrowable(e)//
                    .setSuccess(false)//
                    .setResult(false);
            logger.error(LogUtils.create("ERROR_002_0012")//
                    .addLog("bindToUserID", bindToUserID) //
                    .addLog("targetUserID", targetUserID) //
                    .addLog("targetProivter", targetProivter) //
                    .addLog("targetUserType", targetUser.getType().name()) //
                    .addLog("targetSize", targetUser.getUserSourceList().size()) //
                    .addLog("error", e.getMessage()) //
                    .toJson());
            return result;
        }
    }
    private Result<Boolean> rebind(TransactionStatus tranStatus, long targetUserID, String targetProivter, long bindToUserID) throws SQLException {
        // .更新绑定关系
        try {
            int res = this.userSourceDAO.updateBindUser(targetUserID, targetProivter, bindToUserID);
            if (res != 1) {
                ResultDO<Boolean> result = new ResultDO<Boolean>()//
                        .setSuccess(false)//
                        .setResult(false)//
                        .addMessage(ErrorCodes.U_PROIVTER_REBIND_FAILED.getMsg());
                logger.error(LogUtils.create("ERROR_002_0013")//
                        .addLog("res", res) //
                        .addLog("targetUserID", targetUserID) //
                        .addLog("targetProivter", targetProivter) //
                        .addLog("bindToUserID", bindToUserID) //
                        .toJson());
                tranStatus.setRollbackOnly();
                return result;
            }
        } catch (Exception e) {
            ResultDO<Boolean> resultDO = new ResultDO<Boolean>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.U_PROIVTER_REBIND_FAILED.getMsg())//
                    .setResult(false);
            logger.error(LogUtils.create("ERROR_999_0003")//
                    .addLog("targetUserID", targetUserID) //
                    .addLog("targetProivter", targetProivter) //
                    .addLog("bindToUserID", bindToUserID) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            tranStatus.setRollbackOnly(); //回滚事务
            return resultDO;
        }
        // .更新账号为失效
        try {
            int res = this.userDAO.invalidUser(targetUserID);
            if (res != 1) {
                ResultDO<Boolean> result = new ResultDO<Boolean>()//
                        .setSuccess(false)//
                        .setResult(false)//
                        .addMessage(ErrorCodes.U_UPDATE_FAILED.getMsg());
                logger.error(LogUtils.create("ERROR_002_0014")//
                        .addLog("res", res) //
                        .addLog("userID", targetUserID) //
                        .toJson());
                tranStatus.setRollbackOnly();
                return result;
            }
        } catch (Exception e) {
            ResultDO<Boolean> resultDO = new ResultDO<Boolean>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.U_UPDATE_FAILED.getMsg())//
                    .setResult(false);
            logger.error(LogUtils.create("ERROR_999_0003")//
                    .addLog("targetUserID", targetUserID) //
                    .addLog("error", "userDAO.invalidUser -> " + e.getMessage()) //
                    .toJson(), e);
            tranStatus.setRollbackOnly(); //回滚事务
            return resultDO;
        }
        //
        ResultDO<Boolean> result = new ResultDO<Boolean>()//
                .setSuccess(true)//
                .setResult(true);
        return result;
    }
}