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
import org.more.bizcommon.log.LogUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @version : 2016年08月08日
 * @author 赵永春(zyc@hasor.net)
 */
public class ProjectInfoDAO extends AbstractDao {
    //
    /** 新增项目 */
    public long insertProject(ProjectInfoDO projectDO) throws SQLException {
        try {
            int result = this.getSqlExecutor().insert("projectInfo_insert", projectDO);
            if (result > 0) {
                return projectDO.getId();
            }
            return 0L;
        } catch (SQLException e) {
            logger.error(LogUtils.create("ERROR_999_0003").addLog("dao", "project_dao")//
                    .addLog("method", "insertProject")//
                    .logException(e) //
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 根据项目ID查询项目 */
    public ProjectInfoDO queryByID(long projectID) throws SQLException {
        if (projectID < 0) {
            return null;
        }
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("projectID", projectID);
            ProjectInfoDO result = this.getSqlExecutor().selectOne("projectInfo_queryByID", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LogUtils.create("ERROR_999_0003").addLog("dao", "project_dao")//
                    .addLog("method", "queryByID")//
                    .logException(e) //
                    .toJson(), e);
            throw e;
        }
    }
    //
    /** 根据owner查询项目列表 */
    public List<ProjectInfoDO> queryByOwner(long ownerID, OwnerType ownerType) throws SQLException {
        if (ownerID < 0 || ownerType == null) {
            return new ArrayList<ProjectInfoDO>(0);
        }
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("ownerID", ownerID);
            parameter.put("ownerType", ownerType);
            List<ProjectInfoDO> result = this.getSqlExecutor().selectList("projectInfo_queryByOwner", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LogUtils.create("ERROR_999_0003").addLog("dao", "project_dao")//
                    .addLog("method", "queryListByOwner")//
                    .logException(e) //
                    .toJson(), e);
            throw e;
        }
    }
}