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
import net.hasor.website.domain.ProjectVersionDO;
import net.hasor.website.domain.enums.ErrorCodes;
import net.hasor.website.domain.enums.OwnerType;
import net.hasor.website.domain.enums.ProjectStatus;
import org.more.bizcommon.Result;
import org.more.bizcommon.ResultDO;
import net.hasor.rsf.utils.LogUtils;
import org.more.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 *
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
@Singleton
public class ProjectManager {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Inject
    private UserManager       userManager;
    @Inject
    private ProjectInfoDAO    projectInfoDAO;
    @Inject
    private ProjectVersionDAO projectVersionDAO;
    //
    /** 新项目 */
    public Result<Long> newProject(Owner owner, ProjectInfoDO newProject) {
        // .入参判断
        if (owner == null || newProject == null || owner.getOwnerID() < 0 || owner.getOwnerType() == null) {
            ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.P_OWNER_ERROR.getMsg())//
                    .setResult(0L);
            logger.error(LogUtils.create("ERROR_006_0006")//
                    .addLog("errorCode", resultDO.firstMessage().getMessage()) //
                    .toJson());
            return resultDO;
        }
        // .数据有效性判断
        if (StringUtils.isBlank(newProject.getName())) {
            ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.V_FORM_PROJECT_INVALID.getMsg())//
                    .setResult(0L);
            logger.error(LogUtils.create("ERROR_006_0001")//
                    .addLog("ownerID", owner.getOwnerID()) //
                    .addLog("ownerType", owner.getOwnerType().name()) //
                    .addLog("errorCode", resultDO.firstMessage().getMessage()) //
                    .addLog("error", "project.name is blank.") //
                    .toJson());
            return resultDO;
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
            ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.P_OWNER_TYPE_FAILED.getMsg())//
                    .setResult(0L);
            logger.error(LogUtils.create("ERROR_006_0002")//
                    .addLog("ownerID", owner.getOwnerID()) //
                    .addLog("ownerType", owner.getOwnerType().name()) //
                    .addLog("error", resultDO.firstMessage().getMessage()) //
                    .toJson());
            return resultDO;
        }
        if (ownerFormDB == null) {
            ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.P_OWNER_NOT_EXIST.getMsg())//
                    .setResult(0L);
            logger.error(LogUtils.create("ERROR_006_0003")//
                    .addLog("ownerID", owner.getOwnerID()) //
                    .addLog("ownerType", owner.getOwnerType().name()) //
                    .addLog("error", resultDO.firstMessage().getMessage()) //
                    .toJson());
            return resultDO;
        }
        // .将project存入owner下
        long projectID = 0L;
        try {
            newProject.setOwnerID(ownerFormDB.getOwnerID());
            newProject.setOwnerType(ownerFormDB.getOwnerType());
            newProject.setCreateTime(new Date());
            newProject.setModifyTime(new Date());
            newProject.setStatus(ProjectStatus.Auditing);
            projectID = this.projectInfoDAO.insertProject(newProject);
            if (projectID <= 0) {
                ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                        .setSuccess(false)//
                        .addMessage(ErrorCodes.P_SAVE_PROJECT_FAILED.getMsg())//
                        .setResult(0L);
                logger.error(LogUtils.create("ERROR_006_0005")//
                        .addLog("ownerID", owner.getOwnerID()) //
                        .addLog("ownerType", owner.getOwnerType().name()) //
                        .addLog("errorCode", resultDO.firstMessage().getMessage()) //
                        .addLog("error", resultDO.firstMessage().getMessage()) //
                        .toJson());
                return resultDO;
            }
        } catch (Exception e) {
            ResultDO<Long> resultDO = new ResultDO<Long>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.P_SAVE_PROJECT_FAILED.getMsg())//
                    .setResult(0L);
            logger.error(LogUtils.create("ERROR_006_0004")//
                    .addLog("ownerID", owner.getOwnerID()) //
                    .addLog("ownerType", owner.getOwnerType().name()) //
                    .addLog("errorCode", resultDO.firstMessage().getMessage()) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            return resultDO;
        }
        // .返回值
        return new ResultDO<Long>(true)//
                .setSuccess(true)//
                .setResult(projectID);
    }
    //
    /** 查询我的项目列表 */
    public Result<List<ProjectInfoDO>> queryMyProjectList(Owner owner) {
        // .owner判断
        if (owner == null) {
            logger.error(LogUtils.create("ERROR_006_0006")//
                    .addLog("error", ErrorCodes.P_OWNER_ERROR.getMsg().getMessage()) //
                    .toJson());
            return new ResultDO<List<ProjectInfoDO>>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.P_OWNER_ERROR.getMsg())//
                    .setResult(null);
        }
        // .查询数据
        try {
            List<ProjectInfoDO> projectList = this.projectInfoDAO.queryByOwner(owner.getOwnerID(), owner.getOwnerType());
            projectList = (projectList == null) ? new ArrayList<ProjectInfoDO>(0) : projectList;
            return new ResultDO<List<ProjectInfoDO>>(true)//
                    .setSuccess(true)//
                    .setResult(projectList);
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_999_0003")//
                    .addLog("queryType", "projectInfoDAO.queryByOwner") //
                    .addLog("ownerID", owner.getOwnerID()) //
                    .addLog("ownerType", owner.getOwnerType().name()) //
                    .addLog("error", "query error -> " + e.getMessage()) //
                    .toJson(), e);
            return new ResultDO<List<ProjectInfoDO>>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.P_OWNER_ERROR.getMsg())//
                    .setResult(null);
        }
    }
    //
    /** 查询首页项目 */
    public Result<List<ProjectInfoDO>> queryTopProjectList() {
        return null;// TODO
    }
    //
    /** 更新项目信息（不包含：介绍正文、正文格式） */
    public Result<Boolean> updateProjectWithoutContent(ProjectInfoDO project) {
        // .验证
        boolean test1 = project.getId() <= 0;
        boolean test2 = project.getOwnerID() < 0 || project.getOwnerType() == null;
        boolean test3 = StringUtils.isBlank(project.getName());
        if (test1 || test2 || test3) {
            ResultDO<Boolean> resultDO = new ResultDO<Boolean>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.P_V_PROJECT_INFO_FAILED.getMsg())//
                    .setResult(false);
            logger.error(LogUtils.create("ERROR_006_0009")//
                    .addLog("ownerID", project.getOwnerID()) //
                    .addLog("ownerType", project.getOwnerType()) //
                    .addLog("error", resultDO.firstMessage().getMessage()) //
                    .toJson());
        }
        // .保存
        try {
            int res = this.projectInfoDAO.updateWithoutContent(project);
            if (res <= 0) {
                ResultDO<Boolean> resultDO = new ResultDO<Boolean>(false)//
                        .setSuccess(false)//
                        .addMessage(ErrorCodes.P_SAVE_PROJECT_FAILED.getMsg())//
                        .setResult(false);
                logger.error(LogUtils.create("ERROR_006_0007")//
                        .addLog("ownerID", project.getOwnerID()) //
                        .addLog("ownerType", project.getOwnerType()) //
                        .addLog("error", resultDO.firstMessage().getMessage()) //
                        .toJson());
                return resultDO;
            } else {
                return new ResultDO<Boolean>(true)//
                        .setSuccess(true)//
                        .setResult(true);
            }
        } catch (Exception e) {
            ResultDO<Boolean> resultDO = new ResultDO<Boolean>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.P_SAVE_PROJECT_FAILED.getMsg())//
                    .setResult(false);
            logger.error(LogUtils.create("ERROR_999_0003")//
                    .addLog("ownerID", project.getOwnerID()) //
                    .addLog("ownerType", project.getOwnerType()) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            return resultDO;
        }
    }
    //
    /** 更新项目信息（仅包含：介绍正文、正文格式） */
    public Result<Boolean> updateProjectContent(ProjectInfoDO project) {
        // .验证
        boolean test1 = project.getId() <= 0;
        boolean test2 = project.getOwnerID() < 0 || project.getOwnerType() == null;
        boolean test4 = project.getContentFormat() == null;
        if (test1 || test2 || test4) {
            ResultDO<Boolean> resultDO = new ResultDO<Boolean>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.P_V_PROJECT_INFO_FAILED.getMsg())//
                    .setResult(false);
            logger.error(LogUtils.create("ERROR_006_0009")//
                    .addLog("ownerID", project.getOwnerID()) //
                    .addLog("ownerType", project.getOwnerType()) //
                    .addLog("error", resultDO.firstMessage().getMessage()) //
                    .toJson());
        }
        // .保存
        try {
            int res = this.projectInfoDAO.updateContent(project);
            if (res <= 0) {
                ResultDO<Boolean> resultDO = new ResultDO<Boolean>(false)//
                        .setSuccess(false)//
                        .addMessage(ErrorCodes.P_SAVE_PROJECT_FAILED.getMsg())//
                        .setResult(false);
                logger.error(LogUtils.create("ERROR_006_0008")//
                        .addLog("ownerID", project.getOwnerID()) //
                        .addLog("ownerType", project.getOwnerType()) //
                        .addLog("error", resultDO.firstMessage().getMessage()) //
                        .toJson());
                return resultDO;
            } else {
                return new ResultDO<Boolean>(true)//
                        .setSuccess(true)//
                        .setResult(true);
            }
        } catch (Exception e) {
            ResultDO<Boolean> resultDO = new ResultDO<Boolean>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.P_SAVE_PROJECT_FAILED.getMsg())//
                    .setResult(false);
            logger.error(LogUtils.create("ERROR_999_0003")//
                    .addLog("ownerID", project.getOwnerID()) //
                    .addLog("ownerType", project.getOwnerType()) //
                    .addLog("error", e.getMessage()) //
                    .toJson(), e);
            return resultDO;
        }
    }
    //
    /** 查询项目的版本列表 */
    public Result<List<ProjectVersionDO>> queryVersionListByProject(long projectID) {
        // .id判断
        if (projectID <= 0) {
            logger.error(LogUtils.create("ERROR_006_0010")//
                    .addLog("projectID", projectID) //
                    .addLog("error", ErrorCodes.P_PROJECT_NOT_EXIST.getMsg().getMessage()) //
                    .toJson());
            return new ResultDO<List<ProjectVersionDO>>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.P_PROJECT_NOT_EXIST.getMsg())//
                    .setResult(null);
        }
        // .查询数据
        try {
            List<ProjectVersionDO> versionList = this.projectVersionDAO.queryByProject(projectID);
            versionList = (versionList == null) ? new ArrayList<ProjectVersionDO>(0) : versionList;
            return new ResultDO<List<ProjectVersionDO>>(true)//
                    .setSuccess(true)//
                    .setResult(versionList);
        } catch (Exception e) {
            logger.error(LogUtils.create("ERROR_999_0003")//
                    .addLog("queryType", "projectVersionDAO.queryByProject") //
                    .addLog("projectID", projectID) //
                    .addLog("error", "query error -> " + e.getMessage()) //
                    .toJson(), e);
            return new ResultDO<List<ProjectVersionDO>>(false)//
                    .setSuccess(false)//
                    .addMessage(ErrorCodes.P_OWNER_ERROR.getMsg())//
                    .setResult(null);
        }
    }
}