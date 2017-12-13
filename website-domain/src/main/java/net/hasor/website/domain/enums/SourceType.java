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
 * 源码类型,开源 or 闭源
 * @version : 2016年08月11日
 * @author 赵永春 (zyc@hasor.net)
 */
public enum SourceType implements GeneralEnumParsing<SourceType> {
    Open(0, "开源"),//
    Close(1, "闭源");
    //
    private int    type;
    private String desc;
    SourceType(int type, String desc) {
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
    public SourceType formType(int type) {
        for (SourceType item : SourceType.values()) {
            if (item.getType() == type) {
                return item;
            }
        }
        return null;
    }
    public SourceType formName(String name) {
        for (SourceType item : SourceType.values()) {
            if (item.name().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}