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
public enum ProjectStatus implements GeneralEnumParsing<ProjectStatus> {
    // .普通项目
    Init(0, "初始筹备"),//
    Publish(1, "正常"),//变迁：0 -> 1 or 6 -> 1
    //
    // .孵化项目
    ApplyIncubator(2, "申请孵化"),// 变迁：1 -> 2
    Incubator(3, "孵化中"),// 变迁：2 -> 3 or 6 -> 3
    ApplyGraduate(4, "申请毕业"),// 变迁：3 -> 4
    Graduate(5, "已毕业"),// 变迁：4 -> 5
    Clearup(6, "整理期"),// 变迁：4 -> 6
    Archives(7, "已归档"),// 变迁：3,4,6 -> 7
    //
    // .尘归尘土归土
    Recovery(8, "回收"),// 变迁：0,1,6  ->  7
    Invalid(-1, "失效"),// 变迁：9 -> -1
    ;
    //
    private int    type;
    private String desc;
    ProjectStatus(int type, String desc) {
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
    public ProjectStatus formType(int type) {
        for (ProjectStatus item : ProjectStatus.values()) {
            if (item.getType() == type) {
                return item;
            }
        }
        return null;
    }
    public ProjectStatus formName(String name) {
        for (ProjectStatus item : ProjectStatus.values()) {
            if (StringUtils.equalsIgnoreCase(item.name(), name)) {
                return item;
            }
        }
        return null;
    }
}