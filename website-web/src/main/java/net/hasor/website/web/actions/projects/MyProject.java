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
package net.hasor.website.web.actions.projects;
import net.hasor.core.Inject;
import net.hasor.restful.RenderData;
import net.hasor.restful.api.MappingTo;
import net.hasor.website.domain.Owner;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.manager.ProjectManager;
import net.hasor.website.web.core.Action;
import org.more.bizcommon.Result;
import org.more.bizcommon.log.LogUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
/**
 * 我的项目列表
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/projects/my.htm")
public class MyProject extends Action {
    @Inject
    private ProjectManager projectManager;
    //
    public void execute(RenderData data) throws IOException {
        // .need login
        if (needLogin()) {
            return;
        }
        //
        Owner userOwner = getUser();
        Result<List<ProjectInfoDO>> result = this.projectManager.queryMyProjectList(userOwner);
        //
        if (!result.isSuccess()) {
            logger.error(LogUtils.create("ERROR_003_0008")//
                    .addLog("result", result) //
                    .addLog("currentUserID", userOwner.getOwnerID())//
                    .addLog("errorMessage", "queryMyProjectList -> " + result.firstMessage())//
                    .toJson());
            sendError(result.firstMessage());
            return;
        }
        //
        List<ProjectInfoDO> list = result.getResult();
        if (list == null) {
            list = new ArrayList<ProjectInfoDO>(0);
        }
        putData("projectList", list);
    }
}