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
package net.hasor.website.utils;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.enums.ProjectStatus;

import java.util.Date;
/**
 *
 * @version : 2016年12月05日
 * @author 赵永春(zyc@hasor.net)
 */
public class ProjectUtils {
    /** 是否为删除状态？ */
    public static boolean isDelete(ProjectInfoDO project) {
        if (project == null)
            return false;
        return ProjectStatus.Recovery.equals(project.getStatus()) || ProjectStatus.Invalid.equals(project.getStatus());
    }
    //
    //
    //
    /** 是否可以创建子项目？ */
    public static boolean canSubProject(ProjectInfoDO project) {
        if (project == null)
            return false;
        // .不可以创建子项目的状态有：
        ProjectStatus projectStatus = project.getStatus();
        boolean testA = ProjectStatus.Recovery.equals(projectStatus);   // 回收
        boolean testB = ProjectStatus.Invalid.equals(projectStatus);    // 失效
        return !(testA || testB);
    }
    /** 是否可以更新项目信息？ */
    public static boolean canUpdateInfo(ProjectInfoDO project) {
        if (project == null)
            return false;
        ProjectStatus projectStatus = project.getStatus();
        boolean testA = ProjectStatus.Recovery.equals(projectStatus);   // 回收
        boolean testB = ProjectStatus.Invalid.equals(projectStatus);    // 失效
        return !(testA || testB);
    }
    /** 是否可以发布新版本？ */
    public static boolean canVersion(ProjectInfoDO project) {
        if (project == null)
            return false;
        ProjectStatus projectStatus = project.getStatus();
        boolean testA = ProjectStatus.Private.equals(projectStatus);       // 回收
        boolean testB = ProjectStatus.Public.equals(projectStatus);    // 失效
        return testA || testB;
    }
    /** 是否可以，删除项目？ */
    public static boolean canDelete(ProjectInfoDO project) {
        if (project == null)
            return false;
        ProjectStatus projectStatus = project.getStatus();
        boolean testA = ProjectStatus.Private.equals(projectStatus);       // 回收
        boolean testB = ProjectStatus.Public.equals(projectStatus);    // 失效
        return testA || testB;
    }
    /** 是否可以，取消删除操作？ */
    public static boolean canRecovery(ProjectInfoDO project) {
        if (project == null || project.getFutures() == null)
            return false;
        if (!ProjectStatus.Recovery.equals(project.getStatus())) {
            return false; //非回收状态的，不可以执行
        }
        Date recoveryTime = project.getFutures().getRecoveryEndTime();
        if (recoveryTime == null) {
            recoveryTime = project.getModifyTime();
        }
        if (System.currentTimeMillis() > recoveryTime.getTime()) {
            return false;//时间太过久远
        }
        return true;
    }
    /** 是否可以，设置项目为公开？ */
    public static boolean canPublic(ProjectInfoDO project) {
        if (project == null)
            return false;
        return ProjectStatus.Private.equals(project.getStatus());
    }
    /** 是否可以，设置项目为私密？ */
    public static boolean canPrivate(ProjectInfoDO project) {
        if (project == null)
            return false;
        return ProjectStatus.Public.equals(project.getStatus());
    }
}