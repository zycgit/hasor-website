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
package net.hasor.website.datadao;
import net.hasor.website.core.AbstractDao;
import net.hasor.website.domain.UserDO;
import net.hasor.website.utils.LoggerUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @version : 2016年08月08日
 * @author 赵永春 (zyc@hasor.net)
 */
public class UserDAO extends AbstractDao {
    //
    /** 新增用户 */
    public long insertUser(UserDO userDO) throws SQLException {
        try {
            int result = this.getSqlExecutor().insertStatement("user_insert", userDO);
            if (result > 0) {
                return userDO.getUserID();
            }
            return 0L;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "user_dao")//
                    .addLog("method", "insertUser")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 更新用户数据 */
    public long updateUser(long userID, UserDO userDO) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("userID", userID);
            parameter.put("userInfo", userDO);
            int result = this.getSqlExecutor().updateStatement("user_updateInfo", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "user_dao")//
                    .addLog("method", "updateUser")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 登录 */
    public int loginUpdate(long userID) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("userID", userID);
            int result = this.getSqlExecutor().updateStatement("user_loginUpdate", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "user_dao")//
                    .addLog("method", "loginUpdate")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    /** 将正常状态的用户状态设为失效 */
    public int invalidUser(long userID) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("userID", userID);
            int result = this.getSqlExecutor().updateStatement("user_invalidUser", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "user_dao")//
                    .addLog("method", "invalidUser")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 根据ID查询 */
    public UserDO queryById(long userID) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("userID", userID);
            UserDO result = this.getSqlExecutor().selectOne("user_queryById", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "user_dao")//
                    .addLog("method", "queryById")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 根据登陆信息查询用户 */
    public UserDO queryByLogin(String login) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("login", login);
            UserDO result = this.getSqlExecutor().selectOne("user_queryByLogin", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "user_dao")//
                    .addLog("method", "queryByLogin")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
}