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
import net.hasor.website.domain.ProjectVersionDO;
import net.hasor.website.domain.enums.VersionStatus;

import java.util.Date;
/**
 *
 * @version : 2016年12月05日
 * @author 赵永春 (zyc@hasor.net)
 */
public class VersionUtils {
    /** 是否为删除状态？ */
    public static boolean isDelete(ProjectVersionDO version) {
        if (version == null)
            return false;
        return VersionStatus.Recovery.equals(version.getStatus()) || VersionStatus.Delete.equals(version.getStatus());
    }
    /** 是否为已发布状态？ */
    public static boolean isPublish(ProjectVersionDO version) {
        if (version == null)
            return false;
        return VersionStatus.Release.equals(version.getStatus());
    }
    //
    //
    //
    /** 是否可以更新版本信息？ */
    public static boolean canUpdateInfo(ProjectVersionDO version) {
        if (version == null)
            return false;
        boolean testA = VersionStatus.DesignPlan.equals(version.getStatus());
        boolean testB = VersionStatus.Developing.equals(version.getStatus());
        boolean testC = VersionStatus.Release.equals(version.getStatus());
        return testA || testB || testC;
    }
    /** 是否可以执行删除操作？ */
    public static boolean canDelete(ProjectVersionDO version) {
        if (version == null)
            return false;
        return VersionStatus.DesignPlan.equals(version.getStatus()) || VersionStatus.Developing.equals(version.getStatus()) || VersionStatus.Release.equals(version.getStatus());
    }
    /** 是否可以取消删除？ */
    public static boolean canRecovery(ProjectVersionDO version) {
        if (version == null || version.getFutures() == null)
            return false;
        if (!VersionStatus.Recovery.equals(version.getStatus())) {
            return false; //非回收状态的，不可以执行
        }
        Date recoveryTime = version.getFutures().getRecoveryEndTime();
        if (recoveryTime == null) {
            recoveryTime = version.getModifyTime();
        }
        if (System.currentTimeMillis() > recoveryTime.getTime()) {
            return false;//时间太过久远
        }
        return true;
    }
    /** 是否可以进行发布删除？ */
    public static boolean canPublish(ProjectVersionDO versionInfo) {
        boolean testDP = VersionStatus.DesignPlan.equals(versionInfo.getStatus());
        boolean testDev = VersionStatus.Developing.equals(versionInfo.getStatus());
        return testDP || testDev;
    }
}