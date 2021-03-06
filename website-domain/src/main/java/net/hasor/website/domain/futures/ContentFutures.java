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
package net.hasor.website.domain.futures;
import java.util.List;
/**
 *
 * @version : 2016年08月08日
 * @author 赵永春 (zyc@hasor.net)
 */
public class ContentFutures {
    private String       oriAuthor = null;  // 原作者
    private String       oriURL    = null;  // 原作URL
    private String       tags      = null;  // 标签
    private List<String> pushList  = null;  // 站外推送列表
    //
    public String getOriAuthor() {
        return oriAuthor;
    }
    public void setOriAuthor(String oriAuthor) {
        this.oriAuthor = oriAuthor;
    }
    public String getOriURL() {
        return oriURL;
    }
    public void setOriURL(String oriURL) {
        this.oriURL = oriURL;
    }
    public String getTags() {
        return tags;
    }
    public void setTags(String tags) {
        this.tags = tags;
    }
    public List<String> getPushList() {
        return pushList;
    }
    public void setPushList(List<String> pushList) {
        this.pushList = pushList;
    }
}