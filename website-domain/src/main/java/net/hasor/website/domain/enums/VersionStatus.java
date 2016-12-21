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
import org.more.util.StringUtils;
/**
 * 项目状态
 * @version : 2016年08月11日
 * @author 赵永春(zyc@hasor.net)
 */
public enum VersionStatus implements GeneralEnumParsing<VersionStatus> {
    DesignPlan(0, "设计"),//
    Developing(1, "开发"),//
    Release(2, "发布"),//
    ;
    //
    private int    type;
    private String desc;
    VersionStatus(int type, String desc) {
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
    public VersionStatus formType(int type) {
        for (VersionStatus item : VersionStatus.values()) {
            if (item.getType() == type) {
                return item;
            }
        }
        return null;
    }
    public VersionStatus formName(String name) {
        for (VersionStatus item : VersionStatus.values()) {
            if (StringUtils.equalsIgnoreCase(item.name(), name)) {
                return item;
            }
        }
        return null;
    }
}