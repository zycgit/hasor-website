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
import net.hasor.core.Hasor;
import net.hasor.core.Inject;
import net.hasor.core.Singleton;
import net.hasor.website.core.TransactionService;
import net.hasor.website.datadao.ProjectInfoDAO;
import net.hasor.website.datadao.ProjectVersionDAO;
import net.hasor.website.domain.Owner;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.ProjectVersionDO;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.domain.enums.OwnerType;
import net.hasor.website.domain.enums.ProjectStatus;
import net.hasor.website.domain.enums.VersionStatus;
import net.hasor.website.domain.futures.ProjectFutures;
import net.hasor.website.domain.futures.ProjectVersionFutures;
import net.hasor.website.domain.result.Result;
import net.hasor.website.utils.LoggerUtils;
import net.hasor.website.utils.ProjectUtils;
import net.hasor.website.utils.VersionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static net.hasor.website.utils.ResultUtils.failed;
import static net.hasor.website.utils.ResultUtils.success;
/**
 *
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
@Singleton
public class ProjectManager {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Inject
    private UserManager        userManager;
    @Inject
    private EnvironmentConfig  environmentConfig;
    @Inject
    private ProjectInfoDAO     projectInfoDAO;
    @Inject
    private ProjectVersionDAO  projectVersionDAO;
    @Inject
    private TransactionService transactionService;
    //
    //
    /** 新项目 */
    public Result<Long> newProject(Owner owner, ProjectInfoDO newProject) {
        // .入参判断
        if (owner == null || newProject == null || owner.getOwnerID() < 0 || owner.getOwnerType() == null) {
            logger.error(LoggerUtils.create("ERROR_006_0006").toJson());
            return failed(ErrorCodes.P_OWNER_ERROR);
        }
        // .数据有效性判断
        if (StringUtils.isBlank(newProject.getName())) {
            logger.error(LoggerUtils.create("ERROR_006_0001")//
                    .addLog("ownerID", owner.getOwnerID()) //
                    .addLog("ownerType", owner.getOwnerType().name()) //
                    .addLog("error", "project.name is blank.") //
                    .toJson());
            return failed(ErrorCodes.V_FORM_PROJECT_INVALID);
        }
        // .owner是否真是存在
        Owner ownerFormDB = null;
        if (owner.getOwnerType() == OwnerType.Personal) {
            // -个人
            ownerFormDB = this.userManager.getUserByID(owner.getOwnerID());
        } else if (owner.getOwnerType() == OwnerType.Organization) {
            // -组织
            ownerFormDB = null;//this.userManager.getUserByID(owner.getOwnerID());
        } else {
            // -未知
            logger.error(LoggerUtils.create("ERROR_006_0002")//
                    .addLog("ownerID", owner.getOwnerID()) //
                    .addLog("ownerType", owner.getOwnerType().name()) //
                    .toJson());
            return failed(ErrorCodes.P_OWNER_TYPE_FAILED);
        }
        if (ownerFormDB == null) {
            logger.error(LoggerUtils.create("ERROR_006_0003")//
                    .addLog("ownerID", owner.getOwnerID()) //
                    .addLog("ownerType", owner.getOwnerType().name()) //
                    .toJson());
            return failed(ErrorCodes.P_OWNER_NOT_EXIST);
        }
        //
        // .将project存入owner下
        long projectID = 0L;
        try {
            newProject.setOwnerID(ownerFormDB.getOwnerID());
            newProject.setOwnerType(ownerFormDB.getOwnerType());
            newProject.setCreateTime(new Date());
            newProject.setModifyTime(new Date());
            newProject.setStatus(ProjectStatus.Private);
            projectID = this.projectInfoDAO.insertProject(newProject);
            if (projectID <= 0) {
                logger.error(LoggerUtils.create("ERROR_006_0005")//
                        .addLog("ownerID", owner.getOwnerID()) //
                        .addLog("ownerType", owner.getOwnerType().name()) //
                        .toJson());
                return failed(ErrorCodes.P_SAVE_PROJECT_FAILED);
            }
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("ownerID", owner.getOwnerID()) //
                    .addLog("ownerType", owner.getOwnerType().name()) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_SAVE_PROJECT_FAILED, e);
        }
        // .返回值
        newProject.setId(projectID);
        return success(projectID);
    }
    /** 查询我的项目列表 */
    public Result<List<ProjectInfoDO>> queryMyProjectList(Owner owner) {
        // .owner判断
        if (owner == null) {
            logger.error(LoggerUtils.create("ERROR_006_0006").toJson());
            return failed(ErrorCodes.P_OWNER_ERROR);
        }
        // .查询数据
        try {
            List<ProjectInfoDO> projectList = this.projectInfoDAO.queryByOwner(owner.getOwnerID(), owner.getOwnerType());
            projectList = (projectList == null) ? new ArrayList<ProjectInfoDO>(0) : projectList;
            return success(projectList);
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("queryType", "projectInfoDAO.queryByOwner") //
                    .addLog("ownerID", owner.getOwnerID()) //
                    .addLog("ownerType", owner.getOwnerType().name()) //
                    .addLog("error", "query error -> " + e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_QUERY_ERROR, e);
        }
    }
    /** 根据项目ID查询项目 */
    public Result<ProjectInfoDO> queryProjectByID(long projectID) {
        try {
            ProjectInfoDO projectInfo = this.projectInfoDAO.queryByID(projectID);
            if (projectInfo == null) {
                return failed(ErrorCodes.P_PROJECT_NOT_EXIST);
            } else {
                return success(projectInfo);
            }
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("queryType", "projectInfoDAO.queryByID") //
                    .addLog("projectID", projectID) //
                    .addLog("error", "query error -> " + e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_QUERY_ERROR, e);
        }
    }
    /** 查询首页项目，所有已发布的，按照名字排序。 */
    public Result<List<ProjectInfoDO>> queryTopProjectList() {
        // .查询数据
        try {
            List<ProjectInfoDO> projectList = projectInfoDAO.queryPublishList(0L, null);
            projectList = (projectList == null) ? new ArrayList<ProjectInfoDO>(0) : projectList;
            return success(projectList);
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("queryType", "projectInfoDAO.queryPublishList") //
                    .addLog("ownerID", "all") //
                    .addLog("ownerType", "all") //
                    .addLog("error", "query error -> " + e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_QUERY_ERROR, e);
        }
    }
    /** 更新项目信息（不包含：介绍正文、正文格式） */
    public Result<Boolean> updateProjectWithoutContent(ProjectInfoDO project) {
        // .验证
        boolean test1 = project.getId() <= 0;
        boolean test2 = project.getOwnerID() < 0 || project.getOwnerType() == null;
        boolean test3 = StringUtils.isBlank(project.getName());
        if (test1 || test2 || test3) {
            logger.error(LoggerUtils.create("ERROR_006_0009")//
                    .addLog("ownerID", project.getOwnerID()) //
                    .addLog("ownerType", project.getOwnerType()) //
                    .toJson());
            return failed(ErrorCodes.P_V_PROJECT_INFO_FAILED);
        }
        // .保存
        try {
            int res = this.projectInfoDAO.updateWithoutContent(project);
            if (res <= 0) {
                logger.error(LoggerUtils.create("ERROR_006_0007")//
                        .addLog("ownerID", project.getOwnerID()) //
                        .addLog("ownerType", project.getOwnerType()) //
                        .toJson());
                return failed(ErrorCodes.P_SAVE_PROJECT_FAILED);
            } else {
                return success(true);
            }
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("ownerID", project.getOwnerID()) //
                    .addLog("ownerType", project.getOwnerType()) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_SAVE_PROJECT_FAILED, e);
        }
    }
    /** 更新项目信息（仅包含：介绍正文、正文格式） */
    public Result<Boolean> updateProjectContent(ProjectInfoDO project) {
        // .验证
        boolean test1 = project.getId() <= 0;
        boolean test2 = project.getOwnerID() < 0 || project.getOwnerType() == null;
        boolean test4 = project.getContentFormat() == null;
        if (test1 || test2 || test4) {
            logger.error(LoggerUtils.create("ERROR_006_0009")//
                    .addLog("ownerID", project.getOwnerID()) //
                    .addLog("ownerType", project.getOwnerType()) //
                    .toJson());
            return failed(ErrorCodes.P_V_PROJECT_INFO_FAILED);
        }
        // .保存
        try {
            int res = this.projectInfoDAO.updateContent(project);
            if (res <= 0) {
                logger.error(LoggerUtils.create("ERROR_006_0008")//
                        .addLog("ownerID", project.getOwnerID()) //
                        .addLog("ownerType", project.getOwnerType()) //
                        .toJson());
                return failed(ErrorCodes.P_SAVE_PROJECT_FAILED);
            } else {
                return success(true);
            }
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("ownerID", project.getOwnerID()) //
                    .addLog("ownerType", project.getOwnerType()) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_SAVE_PROJECT_FAILED, e);
        }
    }
    //
    //
    /** 查询项目的版本列表 */
    public Result<List<ProjectVersionDO>> queryVersionListByProject(long projectID) {
        // .id判断
        if (projectID <= 0) {
            logger.error(LoggerUtils.create("ERROR_006_0010")//
                    .addLog("projectID", projectID) //
                    .toJson());
            return failed(ErrorCodes.P_PROJECT_NOT_EXIST);
        }
        // .查询数据
        try {
            List<ProjectVersionDO> versionList = this.projectVersionDAO.queryByProject(projectID);
            versionList = (versionList == null) ? new ArrayList<ProjectVersionDO>(0) : versionList;
            return success(versionList);
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("queryType", "projectVersionDAO.queryByProject") //
                    .addLog("projectID", projectID) //
                    .addLog("error", "query error -> " + e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_QUERY_ERROR, e);
        }
    }
    /** 根据ID查询版本信息 */
    public Result<ProjectVersionDO> queryVersionByID(long projectID, long versionID) {
        try {
            ProjectVersionDO versionDO = this.projectVersionDAO.queryByID(projectID, versionID);
            if (versionDO == null) {
                return failed(ErrorCodes.P_VERSION_NOT_EXIST);
            } else {
                return success(versionDO);
            }
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("queryType", "projectInfoDAO.queryByID") //
                    .addLog("projectID", projectID) //
                    .addLog("versionID", versionID) //
                    .addLog("error", "query error -> " + e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_QUERY_ERROR, e);
        }
    }
    /** 更新版本信息（更新项目版本信息，不会变更版本状态和隶属关系） */
    public Result<Boolean> updateVersionInfo(ProjectVersionDO version) {
        // .保存
        try {
            int res = this.projectVersionDAO.updateVersionInfo(version);
            if (res <= 0) {
                logger.error(LoggerUtils.create("ERROR_006_0007")//
                        .addLog("versionID", version.getId()) //
                        .addLog("projectID", version.getProjectID()) //
                        .toJson());
                return failed(ErrorCodes.P_VERSION_UPDATE_FAILED);
            } else {
                return success(true);
            }
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("versionID", version.getId()) //
                    .addLog("projectID", version.getProjectID()) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_VERSION_UPDATE_FAILED, e);
        }
    }
    //
    //
    /** 新增版本 */
    public Result<Long> newVersion(Owner user, long projectID, ProjectVersionDO versionInfoDO) {
        Result<ProjectInfoDO> projectByID = this.queryProjectByID(projectID);
        Result<Void> testProject = this.testProjectOwner(user, projectByID);
        if (!testProject.isSuccess()) {
            return failed(testProject);
        }
        //
        // .状态判断
        ProjectInfoDO projectInfoDO = projectByID.getResult();
        if (!ProjectUtils.canVersion(projectInfoDO)) {
            return failed(ErrorCodes.P_PROJECT_STATUS_FAILED);
        }
        //
        // .保存
        try {
            versionInfoDO.setProjectID(Hasor.assertIsNotNull(projectInfoDO).getId());
            long versionID = this.projectVersionDAO.insertVersion(versionInfoDO);
            if (versionID <= 0) {
                logger.error(LoggerUtils.create("ERROR_006_0014")//
                        .addLog("projectID", projectInfoDO.getId()) //
                        .toJson());
                return failed(ErrorCodes.P_VERSION_SAVE_FAILED);
            } else {
                versionInfoDO.setId(versionID);
                return success(versionID);
            }
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("projectID", projectInfoDO.getId()) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_VERSION_SAVE_FAILED, e);
        }
    }
    /** 标记删除版本 */
    public Result<Boolean> deleteVersion(Owner user, long projectID, long versionID) {
        Result<ProjectInfoDO> projectByID = this.queryProjectByID(projectID);
        Result<Void> testProject = this.testProjectOwner(user, projectByID);
        if (!testProject.isSuccess()) {
            return failed(testProject);
        }
        Result<ProjectVersionDO> versionByID = this.queryVersionByID(projectID, versionID);
        if (!versionByID.isSuccess()) {
            return failed(versionByID);
        }
        //
        // .状态判断
        ProjectInfoDO projectInfoDO = projectByID.getResult();
        ProjectVersionDO versionInfo = versionByID.getResult();
        if (!ProjectUtils.canVersion(projectInfoDO)) {
            return failed(ErrorCodes.P_PROJECT_STATUS_FAILED);
        }
        if (!VersionUtils.canDelete(versionInfo)) {
            return failed(ErrorCodes.P_VERSION_STATUS_FAILED);
        }
        //
        // .删除
        try {
            if (versionInfo.getFutures() == null) {
                versionInfo.setFutures(new ProjectVersionFutures());
            }
            versionInfo.getFutures().setRecoveryStartTime(new Date());
            versionInfo.getFutures().setRecoveryEndTime(this.environmentConfig.getRecoveryTime(new Date()));
            versionInfo.getFutures().setRecoveryStatus(versionInfo.getStatus().name());
            versionInfo.setStatus(VersionStatus.Recovery);
            long res = this.projectVersionDAO.updateStatusAndFutures(projectID, versionInfo);
            if (res <= 0) {
                logger.error(LoggerUtils.create("ERROR_006_0014")//
                        .addLog("projectID", projectID) //
                        .addLog("versionID", versionID) //
                        .toJson());
                return failed(ErrorCodes.P_VERSION_UPDATE_FAILED);
            } else {
                return success(true);
            }
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("projectID", projectID) //
                    .addLog("versionID", versionID) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_VERSION_UPDATE_FAILED, e);
        }
    }
    /** 恢复删除的版本 */
    public Result<Boolean> recoverVersion(Owner user, long projectID, long versionID) {
        Result<ProjectInfoDO> projectByID = this.queryProjectByID(projectID);
        Result<Void> testProject = this.testProjectOwner(user, projectByID);
        if (!testProject.isSuccess()) {
            return failed(testProject);
        }
        Result<ProjectVersionDO> versionByID = this.queryVersionByID(projectID, versionID);
        if (!versionByID.isSuccess()) {
            return failed(versionByID);
        }
        //
        // .状态判断
        ProjectInfoDO projectInfoDO = projectByID.getResult();
        ProjectVersionDO versionInfo = versionByID.getResult();
        if (!ProjectUtils.canVersion(projectInfoDO)) {
            return failed(ErrorCodes.P_PROJECT_STATUS_FAILED);
        }
        if (!VersionUtils.canRecovery(versionInfo)) {
            return failed(ErrorCodes.P_VERSION_STATUS_FAILED);
        }
        //
        // .恢复删除
        try {
            String lastStatus = versionInfo.getFutures().getRecoveryStatus();
            versionInfo.setStatus(VersionStatus.DesignPlan.formName(lastStatus));
            long res = this.projectVersionDAO.updateStatusAndFutures(projectID, versionInfo);
            if (res <= 0) {
                logger.error(LoggerUtils.create("ERROR_006_0014")//
                        .addLog("projectID", projectID) //
                        .addLog("versionID", versionID) //
                        .toJson());
                return failed(ErrorCodes.P_VERSION_UPDATE_FAILED);
            } else {
                return success(true);
            }
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("projectID", projectID) //
                    .addLog("versionID", versionID) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_VERSION_UPDATE_FAILED, e);
        }
    }
    /** 版本置为发布状态 */
    public Result<Boolean> publishVersion(Owner user, long projectID, long versionID) {
        Result<ProjectInfoDO> projectByID = this.queryProjectByID(projectID);
        Result<Void> testProject = this.testProjectOwner(user, projectByID);
        if (!testProject.isSuccess()) {
            return failed(testProject);
        }
        Result<ProjectVersionDO> versionByID = this.queryVersionByID(projectID, versionID);
        if (!versionByID.isSuccess()) {
            return failed(versionByID);
        }
        //
        // .状态判断
        ProjectInfoDO projectInfoDO = projectByID.getResult();
        ProjectVersionDO versionInfo = versionByID.getResult();
        if (!ProjectUtils.canVersion(projectInfoDO)) {
            return failed(ErrorCodes.P_PROJECT_STATUS_FAILED);
        }
        if (!VersionUtils.canPublish(versionInfo)) {
            return failed(ErrorCodes.P_VERSION_STATUS_FAILED);
        }
        //
        // .发布
        try {
            versionInfo.setStatus(VersionStatus.Release);
            long res = this.projectVersionDAO.updateStatus(projectID, versionInfo);
            if (res <= 0) {
                logger.error(LoggerUtils.create("ERROR_006_0014")//
                        .addLog("projectID", projectID) //
                        .addLog("versionID", versionID) //
                        .toJson());
                return failed(ErrorCodes.P_VERSION_UPDATE_FAILED);
            } else {
                return success(true);
            }
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("projectID", projectID) //
                    .addLog("versionID", versionID) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_VERSION_UPDATE_FAILED, e);
        }
    }
    //
    //
    /** 标记删除项目 */
    public Result<Boolean> doDeleteProject(Owner operUser, ProjectInfoDO project) {
        long projectID = project.getId();
        Result<ProjectInfoDO> projectByID = this.queryProjectByID(projectID);
        Result<Void> testProject = this.testProjectOwner(operUser, projectByID);
        if (!testProject.isSuccess()) {
            return failed(testProject);
        }
        //
        // .状态判断
        ProjectInfoDO projectInfo = projectByID.getResult();
        if (!ProjectUtils.canDelete(projectInfo)) {
            return failed(ErrorCodes.P_PROJECT_STATUS_FAILED);
        }
        if (projectInfo.getFutures() == null) {
            projectInfo.setFutures(new ProjectFutures());
        }
        projectInfo.getFutures().setRecoveryStartTime(new Date());
        projectInfo.getFutures().setRecoveryEndTime(this.environmentConfig.getRecoveryTime(new Date()));
        projectInfo.getFutures().setRecoveryStatus(projectInfo.getStatus().name());
        projectInfo.setStatus(ProjectStatus.Recovery);
        //
        // .删除
        return updateStatusAndFutures(projectID, projectInfo);
    }
    /** 恢复删除的项目 */
    public Result<Boolean> doRecoverProject(Owner operUser, ProjectInfoDO project) {
        long projectID = project.getId();
        Result<ProjectInfoDO> projectByID = this.queryProjectByID(projectID);
        Result<Void> testProject = this.testProjectOwner(operUser, projectByID);
        if (!testProject.isSuccess()) {
            return failed(testProject);
        }
        // .状态判断
        ProjectInfoDO projectInfo = projectByID.getResult();
        if (!ProjectUtils.canRecovery(projectInfo)) {
            return failed(ErrorCodes.P_PROJECT_STATUS_FAILED);
        }
        //
        String lastStatus = projectInfo.getFutures().getRecoveryStatus();
        projectInfo.setStatus(ProjectStatus.Private.formName(lastStatus));
        return updateStatusAndFutures(projectID, projectInfo);
    }
    /** 设置为私有项目 */
    public Result<Boolean> doPrivateProject(Owner operUser, ProjectInfoDO project) {
        //
        long projectID = project.getId();
        Result<ProjectInfoDO> projectByID = this.queryProjectByID(projectID);
        Result<Void> testProject = this.testProjectOwner(operUser, projectByID);
        if (!testProject.isSuccess()) {
            return failed(testProject);
        }
        // .状态判断
        ProjectInfoDO projectInfo = projectByID.getResult();
        if (!ProjectUtils.canPrivate(projectInfo)) {
            return failed(ErrorCodes.P_PROJECT_STATUS_FAILED);
        }
        projectInfo.setStatus(ProjectStatus.Private);
        return updateStatusAndFutures(projectID, projectInfo);
    }
    /** 设置为公开项目 */
    public Result<Boolean> doPublicProject(Owner operUser, ProjectInfoDO project) {
        //
        long projectID = project.getId();
        Result<ProjectInfoDO> projectByID = this.queryProjectByID(projectID);
        Result<Void> testProject = this.testProjectOwner(operUser, projectByID);
        if (!testProject.isSuccess()) {
            return failed(testProject);
        }
        // .状态判断
        ProjectInfoDO projectInfo = projectByID.getResult();
        if (!ProjectUtils.canPublic(projectInfo)) {
            return failed(ErrorCodes.P_PROJECT_STATUS_FAILED);
        }
        //
        projectInfo.setStatus(ProjectStatus.Public);
        return updateStatusAndFutures(projectID, projectInfo);
    }
    //
    //
    private Result<Void> testProjectOwner(Owner owner, Result<ProjectInfoDO> projectResult) {
        if (!projectResult.isSuccess()) {
            return failed(projectResult);
        }
        if (owner == null) {
            return failed(ErrorCodes.P_OWNER_ERROR);
        }
        ProjectInfoDO projectInfo = projectResult.getResult();
        if (projectInfo == null) {
            return failed(ErrorCodes.P_PROJECT_NOT_EXIST);
        }
        if (projectInfo.getOwnerID() != owner.getOwnerID() || !projectInfo.getOwnerType().equals(owner.getOwnerType())) {
            return failed(ErrorCodes.P_OWNER_NOT_YOU);
        }
        return success(null);
    }
    private Result<Boolean> updateStatusAndFutures(long projectID, ProjectInfoDO projectInfo) {
        try {
            long res = this.projectInfoDAO.updateStatusAndFutures(projectID, projectInfo);
            if (res <= 0) {
                logger.error(LoggerUtils.create("ERROR_006_0014")//
                        .addLog("projectID", projectID) //
                        .toJson());
                return failed(ErrorCodes.P_PROJECT_UPDATE_FAILED);
            } else {
                return success(true);
            }
        } catch (Exception e) {
            logger.error(LoggerUtils.create("ERROR_999_0003")//
                    .addLog("projectID", projectID) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            return failed(ErrorCodes.P_PROJECT_UPDATE_FAILED, e);
        }
    }
}