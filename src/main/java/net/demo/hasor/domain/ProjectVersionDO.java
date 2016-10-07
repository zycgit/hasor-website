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
package net.demo.hasor.domain;
import net.demo.hasor.domain.enums.ContentFormat;
import net.demo.hasor.domain.futures.ProjectVersionFutures;

import java.util.Date;
/**
 *
 * @version : 2016年10月07日
 * @author 赵永春(zyc@hasor.net)
 */
public class ProjectVersionDO {
    private long                  id              = 0;     //项目ID（PK，自增）
    private long                  projectID       = 0;     //归属项目ID
    private Date                  releaseTime     = null;  //发布时间
    private String                version         = null;  //版本号
    private String                subtitle        = null;  //小标题
    private String                changelog       = null;  //更新内容
    private ContentFormat         changelogFormat = null;  //更新内容格式
    private ProjectVersionFutures futures         = null;  //扩展信息(json格式)
    private Date                  createTime      = null;  //创建时间
    private Date                  modifyTime      = null;  //修改时间
    //
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getProjectID() {
        return projectID;
    }
    public void setProjectID(long projectID) {
        this.projectID = projectID;
    }
    public Date getReleaseTime() {
        return releaseTime;
    }
    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }
    public String getVersion() {
        return version;
    }
    public void setVersion(String version) {
        this.version = version;
    }
    public String getSubtitle() {
        return subtitle;
    }
    public void setSubtitle(String subtitle) {
        this.subtitle = subtitle;
    }
    public String getChangelog() {
        return changelog;
    }
    public void setChangelog(String changelog) {
        this.changelog = changelog;
    }
    public ContentFormat getChangelogFormat() {
        return changelogFormat;
    }
    public void setChangelogFormat(ContentFormat changelogFormat) {
        this.changelogFormat = changelogFormat;
    }
    public ProjectVersionFutures getFutures() {
        return futures;
    }
    public void setFutures(ProjectVersionFutures futures) {
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