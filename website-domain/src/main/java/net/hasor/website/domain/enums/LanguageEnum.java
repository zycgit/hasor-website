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
 * 语言类型
 * @version : 2016年08月11日
 * @author 赵永春 (zyc@hasor.net)
 */
public enum LanguageEnum implements GeneralEnumParsing<LanguageEnum> {
    Java(1, "Java"),//
    C(2, "C/C++"), //
    JavaScript(3, "JavaScript"), //
    Python(4, "Python"),//
    Other(999, "其它"),//
    ;
    //
    private int    type;
    private String desc;
    LanguageEnum(int type, String desc) {
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
    public LanguageEnum formType(int type) {
        for (LanguageEnum item : LanguageEnum.values()) {
            if (item.getType() == type) {
                return item;
            }
        }
        return null;
    }
    public LanguageEnum formName(String name) {
        for (LanguageEnum item : LanguageEnum.values()) {
            if (item.name().equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}