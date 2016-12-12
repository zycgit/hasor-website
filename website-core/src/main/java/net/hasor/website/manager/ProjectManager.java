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
package net.hasor.website.manager;
import net.hasor.core.Inject;
import net.hasor.core.Singleton;
import net.hasor.website.datadao.ProjectInfoDAO;
import net.hasor.website.datadao.ProjectVersionDAO;
import net.hasor.website.domain.Owner;
import net.hasor.website.domain.ProjectInfoDO;
import org.more.bizcommon.Result;
import org.more.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
@Singleton
public class ProjectManager {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Inject
    private ProjectInfoDAO    projectInfoDAO;
    @Inject
    private ProjectVersionDAO projectVersionDAO;
    //
    //
    /** 新项目。*/
    public Result<Long> newProject(Owner owner, ProjectInfoDO newProject) throws Exception {
        if (owner == null || newProject == null) {
            //
        }
        //
        if (StringUtils.isBlank(newProject.getName())) {
            //
        }
        //s
        return null;
    }
}