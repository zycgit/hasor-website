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
import net.hasor.website.client.RsfResultDO;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.owner.SimpleOwner;
import net.hasor.website.manager.ProjectManager;

import java.util.List;

import static net.hasor.website.utils.ResultUtils.converTo;
/**
 * RSF 项目查询接口
 * @version : 2015年11月27日
 * @author 赵永春(zyc@hasor.net)
 */
public class ProjectServiceImpl implements ProjectService {
    @Inject
    private ProjectManager projectManager;
    @Override
    public RsfResultDO<List<ProjectInfoDO>> queryPublicProject() {
        return converTo(this.projectManager.queryTopProjectList());
    }
    @Override
    public RsfResultDO<List<ProjectInfoDO>> queryProjectByOwner(SimpleOwner owner) {
        return converTo(this.projectManager.queryMyProjectList(owner));
    }
    @Override
    public RsfResultDO<ProjectInfoDO> queryProjectByID(long projectID) {
        return converTo(this.projectManager.queryProjectByID(projectID));
    }
}