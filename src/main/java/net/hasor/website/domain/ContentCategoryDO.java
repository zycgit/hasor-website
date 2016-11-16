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
import net.hasor.website.domain.enums.DataStatus;
import net.hasor.website.domain.futures.ContentCategoryFutures;

import java.util.Date;
/**
 * 帖子所属分类
 * @version : 2016年08月11日
 * @author 赵永春(zyc@hasor.net)
 */
public class ContentCategoryDO {
    private long                   id         = 0;      // 分类ID（PK，自增)
    private long                   userID     = 0;      // 帖子所属用户ID
    private String                 name       = null;   // 名称
    private int                    orderIndex = 0;      // 排序
    private DataStatus             status     = null;   // 状态
    private ContentCategoryFutures futures    = null;   // 扩展信息(json格式)
    private Date                   createTime = null;   // 创建时间
    private Date                   modifyTime = null;   // 修改时间
    //
    //
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public long getUserID() {
        return userID;
    }
    public void setUserID(long userID) {
        this.userID = userID;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getOrderIndex() {
        return orderIndex;
    }
    public void setOrderIndex(int orderIndex) {
        this.orderIndex = orderIndex;
    }
    public DataStatus getStatus() {
        return status;
    }
    public void setStatus(DataStatus status) {
        this.status = status;
    }
    public ContentCategoryFutures getFutures() {
        return futures;
    }
    public void setFutures(ContentCategoryFutures futures) {
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