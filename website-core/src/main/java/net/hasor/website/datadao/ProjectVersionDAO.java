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
import org.more.bizcommon.log.LogUtils;

import java.sql.SQLException;
/**
 *
 * @version : 2016年08月08日
 * @author 赵永春(zyc@hasor.net)
 */
public class ProjectVersionDAO extends AbstractDao {
    //
    /** 新增版本 */
    public long insertVersion(ProjectVersionDO versionDO) throws SQLException {
        try {
            int result = this.getSqlExecutor().insert("projectVersion_insert", versionDO);
            if (result > 0) {
                return versionDO.getId();
            }
            return 0L;
        } catch (SQLException e) {
            logger.error(LogUtils.create("ERROR_999_0003").addLog("dao", "project_version_dao")//
                    .addLog("method", "insertVersion")//
                    .logException(e) //
                    .toJson(), e);
            throw e;
        }
    }
}