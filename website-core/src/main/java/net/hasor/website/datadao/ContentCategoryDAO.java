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
import net.hasor.website.domain.ContentCategoryDO;
import net.hasor.website.domain.UserSourceDO;
import org.more.bizcommon.log.LogUtils;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
/**
 *
 * @version : 2016年08月08日
 * @author 赵永春(zyc@hasor.net)
 */
public class ContentCategoryDAO extends AbstractDao {
    //
    /** 新增分类登录类型 */
    public int insertCategory(ContentCategoryDO categoryDO) throws SQLException {
        try {
            int result = this.getSqlExecutor().insert("contentCategory_insert", categoryDO);
            return result;
        } catch (SQLException e) {
            logger.error(LogUtils.create("ERROR_999_0003").logException(e) //
                    .addString("content_category_dao : insertContentCategory error -> " + e.getMessage()).toJson());
            throw e;
        }
    }
    //
    /** 删除文章分类。 */
    public UserSourceDO deleteCategory(long categoryID, long userID) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("categoryID", categoryID);
            parameter.put("userID", userID);
            UserSourceDO result = this.getSqlExecutor().selectOne("contentCategory_delete", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LogUtils.create("ERROR_999_0003").logException(e) //
                    .addString("content_category_dao : deleteCategory error -> " + e.getMessage()).toJson());
            throw e;
        }
    }
    //
    /** 更新文章分类信息。*/
    public int updateContentCategory(ContentCategoryDO categoryDO) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("category", categoryDO);
            int result = this.getSqlExecutor().update("contentCategory_update", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LogUtils.create("ERROR_999_0003").logException(e) //
                    .addString("content_category_dao : updateContentCategory error -> " + e.getMessage()).toJson());
            throw e;
        }
    }
    //
    /** 更新基础信息 */
    public ContentCategoryDO queryByID(long categoryID) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("categoryID", categoryID);
            ContentCategoryDO result = this.getSqlExecutor().selectOne("contentCategory_queryById", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LogUtils.create("ERROR_999_0003").logException(e) //
                    .addString("content_category_dao : queryByID error -> " + e.getMessage()).toJson());
            throw e;
        }
    }
    //
    /** 根据用户ID查询出用户名下所有分类 */
    public List<ContentCategoryDO> queryListByUserID(long userID) throws SQLException {
        try {
            Map<String, Object> parameter = new HashMap<String, Object>();
            parameter.put("userID", userID);
            List<ContentCategoryDO> result = this.getSqlExecutor().selectList("contentCategory_queryByUser", parameter);
            return result;
        } catch (SQLException e) {
            logger.error(LogUtils.create("ERROR_999_0003").logException(e) //
                    .addString("content_category_dao : queryListByUserID error -> " + e.getMessage()).toJson());
            throw e;
        }
    }
}
