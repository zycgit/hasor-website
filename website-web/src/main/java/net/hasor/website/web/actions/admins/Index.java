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
package net.hasor.website.web.actions.admins;
import net.hasor.web.annotation.MappingTo;
import net.hasor.web.render.RenderInvoker;
import net.hasor.website.domain.ProjectInfoDO;
import org.more.bizcommon.Result;

import java.util.List;
/**
 * 总入口
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/admins/index.htm")
public class Index extends BaseAdmin {
    //
    public void execute(RenderInvoker data) {
        //
        Result<List<ProjectInfoDO>> projectList = this.projectManager.queryTopProjectList();
        if (!projectList.isSuccess()) {
            sendError(projectList.firstMessage());
            return;
        }
        data.put("projectList", projectList.getResult());
        //
    }
}