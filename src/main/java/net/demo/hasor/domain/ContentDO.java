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
import net.demo.hasor.domain.enums.BodyFormat;
import net.demo.hasor.domain.enums.ContentStatus;
import net.demo.hasor.domain.enums.ContentType;
import net.demo.hasor.domain.futures.ContentFutures;

import java.util.Date;
/**
 * 帖子
 * @version : 2016年08月11日
 * @author 赵永春(zyc@hasor.net)
 */
public class ContentDO {
    private long            id            = 0;      // 内容ID（PK，自增)
    private Long            categoryID    = null;   // 分类ID
    private long            userID        = 0;      // 帖子所属用户ID
    private ContentType     type          = null;   // 文章类型(首发,原创,转载)
    private ContentModifier modifier      = null;   // 公开类型json格式(所有人,指定范围,私有的)
    private String          title         = null;   // 标题
    private String          brief         = null;   // 摘要
    private String          contentBody   = null;   // 内容正文
    private BodyFormat      contentFormat = null;   // 文章格式
    private ContentFutures  futures       = null;   // 扩展信息(json格式)
    private ContentStatus   status        = null;   // 状态
    private boolean         allowComment  = false;  // 是否允许评论
    private boolean         ontop         = false;  // 置顶
    private Date            publishTime   = null;   // 文章发布时间
    private Date            createTime    = null;   // 创建时间
    private Date            modifyTime    = null;   // 修改时间
    //
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Long getCategoryID() {
        return categoryID;
    }
    public void setCategoryID(Long categoryID) {
        this.categoryID = categoryID;
    }
    public long getUserID() {
        return userID;
    }
    public void setUserID(long userID) {
        this.userID = userID;
    }
    public ContentType getType() {
        return type;
    }
    public void setType(ContentType type) {
        this.type = type;
    }
    public ContentModifier getModifier() {
        return modifier;
    }
    public void setModifier(ContentModifier modifier) {
        this.modifier = modifier;
    }
    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getBrief() {
        return brief;
    }
    public void setBrief(String brief) {
        this.brief = brief;
    }
    public String getContentBody() {
        return contentBody;
    }
    public void setContentBody(String contentBody) {
        this.contentBody = contentBody;
    }
    public BodyFormat getContentFormat() {
        return contentFormat;
    }
    public void setContentFormat(BodyFormat contentFormat) {
        this.contentFormat = contentFormat;
    }
    public ContentFutures getFutures() {
        return futures;
    }
    public void setFutures(ContentFutures futures) {
        this.futures = futures;
    }
    public ContentStatus getStatus() {
        return status;
    }
    public void setStatus(ContentStatus status) {
        this.status = status;
    }
    public boolean isAllowComment() {
        return allowComment;
    }
    public void setAllowComment(boolean allowComment) {
        this.allowComment = allowComment;
    }
    public boolean isOntop() {
        return ontop;
    }
    public void setOntop(boolean ontop) {
        this.ontop = ontop;
    }
    public Date getPublishTime() {
        return publishTime;
    }
    public void setPublishTime(Date publishTime) {
        this.publishTime = publishTime;
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