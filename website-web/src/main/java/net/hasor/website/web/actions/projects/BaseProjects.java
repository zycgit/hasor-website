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
import net.hasor.website.domain.ProjectVersionDO;
import net.hasor.website.domain.enums.VersionStatus;
import net.hasor.website.manager.ProjectManager;
import net.hasor.website.web.core.Action;
import org.more.bizcommon.Result;

import java.util.ArrayList;
import java.util.List;

import static net.hasor.website.utils.ResultUtils.success;
/**
 *
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
public abstract class BaseProjects extends Action {
    @Inject
    protected ProjectManager projectManager;
    //
    /** 可以用来展现的版本列表 */
    protected Result<List<ProjectVersionDO>> versionListToIndex(long projectID) {
        //
        Result<List<ProjectVersionDO>> versionResult = this.projectManager.queryVersionListByProject(projectID);
        if (!versionResult.isSuccess()) {
            return versionResult;
        }
        List<ProjectVersionDO> finalversionList = new ArrayList<ProjectVersionDO>(0);
        List<ProjectVersionDO> versionList = versionResult.getResult();
        //
        for (ProjectVersionDO versionDO : versionList) {
            if (VersionStatus.Release.equals(versionDO.getStatus())) {
                finalversionList.add(versionDO);
            }
        }
        //
        return success(finalversionList);
    }
}