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
import net.hasor.website.domain.UserSourceDO;
import net.hasor.website.utils.LoggerUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @version : 2016年08月08日
 * @author 赵永春 (zyc@hasor.net)
 */
public class UserSourceDAO extends AbstractDao {
    //
    /** 新增外部登录 */
    public long insertUserSource(UserSourceDO sourceDO) throws SQLException {
        try {
            int result = this.getSqlExecutor().insertStatement("userSource_insert", sourceDO);
            if (result > 0) {
                return sourceDO.getUserID();
            }
            return 0L;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "user_source_dao")//
                    .addLog("method", "insertUserSource")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 执行登录 */
    public int loginUpdateByUserID(String provider, long userID) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("provider", provider);
            parameter.put("userID", userID);
            int result = this.getSqlExecutor().updateStatement("userSource_loginUpdateByUserID", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "user_source_dao")//
                    .addLog("method", "loginUpdateByUserID")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 更新外部登陆信息 */
    public int updateUserSource(String provider, long userID, UserSourceDO sourceDO) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("provider", provider);
            parameter.put("userID", userID);
            parameter.put("sourceInfo", sourceDO);
            int result = this.getSqlExecutor().updateStatement("userSource_updateInfo", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "user_source_dao")//
                    .addLog("method", "updateUserSource")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 更新关联用户 */
    public int updateBindUser(long userID, String provider, long newUserID) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("userID", userID);
            parameter.put("provider", provider);
            parameter.put("newUserID", newUserID);
            int result = this.getSqlExecutor().updateStatement("userSource_updateBindUser", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "user_source_dao")//
                    .addLog("method", "updateBindUser")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 根据 provider 和 userID，查询外部登陆 */
    public UserSourceDO queryByUserID(String provider, long userID) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("provider", provider);
            parameter.put("userID", userID);
            UserSourceDO result = this.getSqlExecutor().selectOne("userSource_queryByUserID", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "user_source_dao")//
                    .addLog("method", "queryByUserID")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 根据 provider 和 uniqueID，查询外部登陆 */
    public UserSourceDO queryByUnique(String provider, String uniqueID) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("provider", provider);
            parameter.put("uniqueID", uniqueID);
            UserSourceDO result = this.getSqlExecutor().selectOne("userSource_queryByUnique", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "user_source_dao")//
                    .addLog("method", "queryByUnique")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 根据 userID，查询所有相关的外部登录 */
    public List<UserSourceDO> queryListByUserID(long userID) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("userID", userID);
            List<UserSourceDO> result = this.getSqlExecutor().selectList("userSource_queryListByUserID", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "user_source_dao")//
                    .addLog("method", "queryListByUserID")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
}