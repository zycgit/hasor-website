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
package net.hasor.website.provider;
import net.hasor.core.Inject;
import net.hasor.website.client.ProjectService;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.owner.SimpleOwner;
import net.hasor.website.manager.ProjectManager;
import org.more.bizcommon.Result;

import java.util.List;
/**
 * RSF 项目查询接口
 * @version : 2015年11月27日
 * @author 赵永春(zyc@hasor.net)
 */
public class ProjectServiceImpl implements ProjectService {
    @Inject
    private ProjectManager projectManager;
    @Override
    public Result<List<ProjectInfoDO>> queryPublicProject() {
        return this.projectManager.queryTopProjectList();
    }
    @Override
    public Result<List<ProjectInfoDO>> queryProjectByOwner(SimpleOwner owner) {
        return this.projectManager.queryMyProjectList(owner);
    }
    @Override
    public Result<ProjectInfoDO> queryProjectByID(long projectID) {
        return this.projectManager.queryProjectByID(projectID);
    }
}