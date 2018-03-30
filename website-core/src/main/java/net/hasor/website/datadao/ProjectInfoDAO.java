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
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.enums.OwnerType;
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
public class ProjectInfoDAO extends AbstractDao {
    //
    /** 新增项目 */
    public long insertProject(ProjectInfoDO projectDO) throws SQLException {
        try {
            int result = this.getSqlExecutor().insertStatement("projectInfo_insert", projectDO);
            if (result > 0) {
                return projectDO.getId();
            }
            return 0L;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "project_dao")//
                    .addLog("method", "insertProject")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 根据项目ID查询项目 */
    public ProjectInfoDO queryByID(long projectID) throws SQLException {
        if (projectID <= 0) {
            return null;
        }
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("projectID", projectID);
            ProjectInfoDO result = this.getSqlExecutor().selectOne("projectInfo_queryByID", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "project_dao")//
                    .addLog("method", "queryByID")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 根据owner查询项目列表 */
    public List<ProjectInfoDO> queryByOwner(long ownerID, OwnerType ownerType) throws SQLException {
        if (ownerID <= 0 || ownerType == null) {
            return new ArrayList<ProjectInfoDO>(0);
        }
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("ownerID", ownerID);
            parameter.put("ownerType", ownerType.getType());
            List<ProjectInfoDO> result = this.getSqlExecutor().selectList("projectInfo_queryByOwner", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "project_dao")//
                    .addLog("method", "queryListByOwner")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 更新项目信息（不包含：介绍正文、正文格式） */
    public int updateWithoutContent(ProjectInfoDO project) throws SQLException {
        if (project.getId() <= 0) {
            return 0;
        }
        if (project.getOwnerID() <= 0 || project.getOwnerType() == null) {
            return 0;
        }
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("info", project);
            int result = this.getSqlExecutor().updateStatement("projectInfo_updateWithoutContent", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "project_dao")//
                    .addLog("method", "updateWithoutContent")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 更新项目信息（仅包含：介绍正文、正文格式） */
    public int updateContent(ProjectInfoDO project) throws SQLException {
        if (project.getId() <= 0) {
            return 0;
        }
        if (project.getOwnerID() <= 0 || project.getOwnerType() == null) {
            return 0;
        }
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("info", project);
            int result = this.getSqlExecutor().updateStatement("projectInfo_updateContent", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "project_dao")//
                    .addLog("method", "updateContent")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    /** 按照项目名称排序，获取所有已经发布的项目列表。如果传递了owner参数，那么将会根据owner进行过滤 */
    public List<ProjectInfoDO> queryPublishList(long ownerID, OwnerType ownerType) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("ownerID", null);
            parameter.put("ownerType", null);
            if (ownerType != null && ownerID > 0L) {
                parameter.put("ownerID", ownerID);
                parameter.put("ownerType", ownerType.getType());
            }
            //
            List<ProjectInfoDO> result = this.getSqlExecutor().selectList("projectInfo_queryPublishList", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "project_dao")//
                    .addLog("method", "queryPublishList")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
    /** 更新状态和扩展信息字段。 */
    public long updateStatusAndFutures(long projectID, ProjectInfoDO projectInfo) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("projectID", projectID);
            parameter.put("projectInfo", projectInfo);
            return this.getSqlExecutor().updateStatement("projectInfo_updateStatusAndFutures", parameter);
        } catch (SQLException e) {
            logger.error(LoggerUtils.create("ERROR_999_0003").addLog("dao", "project_dao")//
                    .addLog("method", "updateStatusAndFutures")//
                    .addLog("error", e.getMessage())//
                    .toJson(), e);
            throw e;
        }
    }
}