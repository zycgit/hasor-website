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
package net.hasor.website.web.actions.blog;
import net.hasor.core.Inject;
import net.hasor.web.DataContext;
import net.hasor.web.annotation.MappingTo;
import net.hasor.web.annotation.PathParam;
import net.hasor.web.annotation.ReqParam;
import net.hasor.website.domain.ContentCategoryDO;
import net.hasor.website.domain.enums.ContentType;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.manager.CategoryManager;
import net.hasor.website.manager.EnvironmentConfig;
import net.hasor.website.web.core.Action;
import org.more.util.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * blog页面
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/blog/edit.{action}")
public class Edit extends Action {
    @Inject
    private EnvironmentConfig envConfig;
    @Inject
    private CategoryManager   categoryManager;
    //
    public void execute(@PathParam("action") String action, @ReqParam("content_id") long contentID, DataContext data) throws IOException {
        // .正式环境必须执行登陆
        if (!isLogin()) {
            if (StringUtils.equalsIgnoreCase(action, "htm")) {
                this.needLogin();
                return;
            } else {
                sendJsonError(ErrorCodes.U_NEED_LOGIN.getMsg());
                return;
            }
        }
        if (StringUtils.equalsIgnoreCase(action, "htm")) {
            if (contentID > 0) {
                this.showBlog(contentID);
                return;
            } else {
                this.newBlog();
                return;
            }
        } else {
            if (contentID > 0) {
                this.updateBlog(contentID);
                return;
            } else {
                this.createBlog();
                return;
            }
        }
    }
    //
    /**准备开始写一篇新的Blog*/
    private void newBlog() {
        //
        long curUser = this.getUserID();
        List<ContentCategoryDO> categoryList = this.categoryManager.queryListByUser(curUser);
        if (categoryList == null || categoryList.isEmpty()) {
            categoryList = new ArrayList<ContentCategoryDO>(0);
        }
        this.putData("csrfToken", this.csrfTokenString());
        this.putData("categoryList", categoryList);
        this.putData("contentTypeList", Arrays.asList(ContentType.values()));
        //
    }
    /**查询Blog并展现到页面上*/
    private void showBlog(long contentID) {
        this.newBlog();
        //
    }
    /**更新Blog内容*/
    private void updateBlog(long contentID) {
    }
    /**保存成一个新的 blog内容*/
    private void createBlog() {
        //
    }
}