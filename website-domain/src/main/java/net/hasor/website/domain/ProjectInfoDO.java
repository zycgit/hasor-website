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
package net.hasor.website.domain;
import net.hasor.website.domain.enums.ContentFormat;
import net.hasor.website.domain.enums.OwnerType;
import net.hasor.website.domain.enums.ProjectStatus;
import net.hasor.website.domain.enums.SourceType;
import net.hasor.website.domain.futures.ProjectFutures;

import java.util.Date;
/**
 *
 * @version : 2016年10月07日
 * @author 赵永春(zyc@hasor.net)
 */
public class ProjectInfoDO {
    private long           id            = 0;     //项目ID（PK，自增）
    private long           ownerID       = 0;     //owner id
    private OwnerType      ownerType     = null;  //owner 类型
    private Long           parentID      = null;  //归属父项目 id
    private ProjectStatus  status        = null;  //状态
    private String         name          = null;  //项目名称
    private String         subtitle      = null;  //小标题
    private String         present       = null;  //介绍正文
    private ContentFormat  contentFormat = null;  //介绍内容格式
    private String         homePage      = null;  //项目主页
    private String         downPage      = null;  //下载连接 or 页面
    private SourceType     sourceType    = null;  //项目类型(开源项目 or 闭源)
    private String         language      = null;  //主要使用语言
    private String         license       = null;  //授权协议
    private ProjectFutures futures       = null;  //扩展信息(json格式)
    private Date           createTime    = null;  //创建时间
    private Date           modifyTime    = null;  //修改时间
    //
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getOwnerID() {
        return ownerID;
    }
    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }
    public OwnerType getOwnerType() {
        return ownerType;
    }
    public void setOwnerType(OwnerType ownerType) {
        this.ownerType = ownerType;
    }
    public Long getParentID() {
        return parentID;
    }
    public void setParentID(Long parentID) {
        this.parentID = parentID;
    }
    public ProjectStatus getStatus() {
        return status;
    }
    public void setStatus(ProjectStatus status) {
        this.status = status;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSubtitle() {
        return subtitle;
    }
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    public String getPresent() {
        return present;
    }
    public void setPresent(String present) {
        this.present = present;
    }
    public ContentFormat getContentFormat() {
        return contentFormat;
    }
    public void setContentFormat(ContentFormat contentFormat) {
        this.contentFormat = contentFormat;
    }
    public String getHomePage() {
        return homePage;
    }
    public void setHomePage(String homePage) {
        this.homePage = homePage;
    }
    public String getDownPage() {
        return downPage;
    }
    public void setDownPage(String downPage) {
        this.downPage = downPage;
    }
    public SourceType getSourceType() {
        return sourceType;
    }
    public void setSourceType(SourceType sourceType) {
        this.sourceType = sourceType;
    }
    public String getLanguage() {
        return language;
    }
    public void setLanguage(String language) {
        this.language = language;
    }
    public String getLicense() {
        return license;
    }
    public void setLicense(String license) {
        this.license = license;
    }
    public ProjectFutures getFutures() {
        return futures;
    }
    public void setFutures(ProjectFutures futures) {
        this.futures = futures;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getModifyTime() {
        return modifyTime;
    }
    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }
}