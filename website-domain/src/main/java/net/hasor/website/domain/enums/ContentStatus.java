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
package net.hasor.website.domain.enums;
import net.hasor.website.domain.GeneralEnumParsing;
/**
 * 帖子状态
 * @version : 2016年08月11日
 * @author 赵永春 (zyc@hasor.net)
 */
public enum ContentStatus implements GeneralEnumParsing<ContentStatus> {
    OK(0, "正常"), Delete(1, "删除"), Draft(2, "草稿"),;
    //
    private int    type;
    private String desc;
    ContentStatus(int type, String desc) {
        this.type = type;
        this.desc = desc;
    }
    public int getType() {
        return type;
    }
    public String getDesc() {
        return desc;
    }
    //
    public ContentStatus formType(int type) {
        for (ContentStatus item : ContentStatus.values()) {
            if (item.getType() == type) {
                return item;
            }
        }
        return null;
    }
    @Override
    public ContentStatus formName(String name) {
        for (ContentStatus item : ContentStatus.values()) {
            if (item.name().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}