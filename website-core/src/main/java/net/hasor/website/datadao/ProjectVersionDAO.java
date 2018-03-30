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
import net.hasor.website.domain.ProjectVersionDO;
import net.hasor.website.utils.LoggerUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @version : 2016年08月08日
 * @author 赵永春 (zyc@hasor.net)
 */
public class ProjectVersionDAO extends AbstractDao {
    //
    /** 新增版本 */
    public long insertVersion(ProjectVersionDO versionDO) throws SQLException {
        try {
            int result = this.getSqlExecutor().insertStatement("projectVersion_insert", versionDO);
            if (result > 0) {
                return versionDO.getId();
            }
            return 0L;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "project_version_dao")//
                    .addLog("method", "insertVersion")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 查询项目版本列表 */
    public List<ProjectVersionDO> queryByProject(long projectID) throws SQLException {
        if (projectID <= 0) {
            return new ArrayList<ProjectVersionDO>(0);
        }
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("projectID", projectID);
            List<ProjectVersionDO> result = this.getSqlExecutor().selectList("projectVersion_queryByProject", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "project_version_dao")//
                    .addLog("method", "queryByProject")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    /** 查询项目版本 */
    public ProjectVersionDO queryByID(long projectID, long versionID) throws SQLException {
        if (projectID <= 0 || versionID <= 0) {
            return null;
        }
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("projectID", projectID);
            parameter.put("versionID", versionID);
            ProjectVersionDO result = this.getSqlExecutor().selectOne("projectVersion_queryByID", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "project_version_dao")//
                    .addLog("method", "queryByID")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    /** 更新项目版本信息，不会变更版本状态和隶属关系 */
    public int updateVersionInfo(ProjectVersionDO version) throws SQLException {
        try {
            return this.getSqlExecutor().updateStatement("projectVersion_updateVersion", version);
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "project_version_dao")//
                    .addLog("method", "updateVersionInfo")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    /** 更新状态和扩展信息字段。 */
    public int updateStatusAndFutures(long projectID, ProjectVersionDO versionInfo) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("projectID", projectID);
            parameter.put("versionInfo", versionInfo);
            return this.getSqlExecutor().updateStatement("projectVersion_updateStatusAndFutures", parameter);
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "project_version_dao")//
                    .addLog("method", "updateStatusAndFutures")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    /** 仅更新状态字段。 */
    public int updateStatus(long projectID, ProjectVersionDO versionInfo) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("projectID", projectID);
            parameter.put("versionInfo", versionInfo);
            return this.getSqlExecutor().updateStatement("projectVersion_updateStatus", parameter);
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "project_version_dao")//
                    .addLog("method", "updateStatus")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
}