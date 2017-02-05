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
    /** 是否为正常？ */
    public static boolean isPublish(ProjectInfoDO project) {
        if (project == null)
            return false;
        return ProjectStatus.Publish.equals(project.getStatus());
    }
    /** 是否位于孵化过程中，被孵化的项目大部分数据都不可以修改？ */
    public static boolean isIncubator(ProjectInfoDO project) {
        if (project == null)
            return false;
        ProjectStatus projectStatus = project.getStatus();
        boolean testA = ProjectStatus.ApplyIncubator.equals(projectStatus);   // 申请孵化
        boolean testB = ProjectStatus.Incubator.equals(projectStatus);  // 孵化中
        boolean testC = ProjectStatus.ApplyGraduate.equals(projectStatus);  // 申请毕业
        boolean testD = ProjectStatus.Graduate.equals(projectStatus);  // 已毕业
        boolean testE = ProjectStatus.Clearup.equals(projectStatus);  // 整理期
        boolean testF = ProjectStatus.Archives.equals(projectStatus);  // 已归档
        return testA || testB || testC || testD || testE || testF;
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
        boolean testC = ProjectStatus.Clearup.equals(projectStatus);    // 整理期
        return !(testA || testB || testC);
    }
    /** 是否可以更新项目信息？ */
    public static boolean canUpdateInfo(ProjectInfoDO project) {
        if (project == null)
            return false;
        // .不可以更新项目资料的状态有：
        ProjectStatus projectStatus = project.getStatus();
        boolean testA = ProjectStatus.ApplyIncubator.equals(projectStatus); // 申请孵化
        boolean testB = ProjectStatus.ApplyGraduate.equals(projectStatus);  // 申请毕业
        boolean testC = ProjectStatus.Clearup.equals(projectStatus);        // 整理期
        boolean testD = ProjectStatus.Archives.equals(projectStatus);       // 归档
        boolean testE = ProjectStatus.Recovery.equals(projectStatus);       // 回收
        boolean testF = ProjectStatus.Invalid.equals(projectStatus);        // 失效
        return !(testA || testB || testC || testD || testE || testF);
    }
    /** 是否可以发布新版本？ */
    public static boolean canVersion(ProjectInfoDO project) {
        if (project == null)
            return false;
        // .不可以进行版本操作状态有：
        ProjectStatus projectStatus = project.getStatus();
        boolean testB = ProjectStatus.Clearup.equals(projectStatus);   // 整理期
        boolean testC = ProjectStatus.Archives.equals(projectStatus);  // 已归档
        boolean testD = ProjectStatus.Recovery.equals(projectStatus);  // 回收
        boolean testE = ProjectStatus.Invalid.equals(projectStatus);   // 失效
        return !(testB || testC || testD || testE);
    }
    /** 是否可以，删除项目？ */
    public static boolean canDelete(ProjectInfoDO project) {
        if (project == null)
            return false;
        // 转变为 Recovery(6, "回收"),// 变迁：0,1,5  ->  6
        ProjectStatus projectStatus = project.getStatus();
        boolean testA = ProjectStatus.Init.equals(projectStatus);     // 筹备
        boolean testB = ProjectStatus.Publish.equals(projectStatus);  // 正常
        boolean testC = ProjectStatus.Clearup.equals(projectStatus);  // 已归档
        return testA || testB || testC;
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
    /** 是否可以，申请孵化？ */
    public static boolean canIncubator(ProjectInfoDO project) {
        if (project == null)
            return false;
        return ProjectStatus.Publish.equals(project.getStatus()) || ProjectStatus.Clearup.equals(project.getStatus());
    }
    /** 是否可以，取消申请孵化项目？ */
    public static boolean canCancelIncubator(ProjectInfoDO project) {
        if (project == null)
            return false;
        return ProjectStatus.ApplyIncubator.equals(project.getStatus());
    }
    /** 是否可以，申请毕业？ */
    public static boolean canGraduate(ProjectInfoDO project) {
        if (project == null)
            return false;
        return ProjectStatus.Incubator.equals(project.getStatus());
    }
    /** 是否可以，撤销毕业申请？ */
    public static boolean canCancelGraduate(ProjectInfoDO project) {
        if (project == null)
            return false;
        return ProjectStatus.ApplyGraduate.equals(project.getStatus());
    }
    /** 是否可以，不活跃的项目进行归档？ */
    public static boolean canArchives(ProjectInfoDO project) {
        if (project == null)
            return false;
        // .可以进行归档的状态有：
        ProjectStatus projectStatus = project.getStatus();
        boolean testA = ProjectStatus.Incubator.equals(projectStatus);      // 孵化中
        boolean testB = ProjectStatus.ApplyGraduate.equals(projectStatus);  // 申请毕业
        boolean testC = ProjectStatus.Clearup.equals(projectStatus);        // 整理期
        return testA || testB || testC;
    }
    /** 是否可以，重新激活整理期的项目？ */
    public static boolean canActivate(ProjectInfoDO project) {
        if (project == null)
            return false;
        return ProjectStatus.Clearup.equals(project.getStatus());
    }
}