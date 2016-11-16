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
import org.more.util.StringUtils;

import java.util.Set;
/**
 * 公开类型
 * @version : 2016年08月11日
 * @author 赵永春(zyc@hasor.net)
 */
public class ContentModifier {
    private Set<String> memberSet;
    private String      modifier;
    //
    public Set<String> getMemberSet() {
        return memberSet;
    }
    public void setMemberSet(Set<String> memberSet) {
        this.memberSet = memberSet;
    }
    public String getModifier() {
        return modifier;
    }
    public void setModifier(String modifier) {
        this.modifier = modifier;
    }
    //
    //
    //
    public boolean isPublic() {
        return StringUtils.endsWithIgnoreCase(this.modifier, "public");
    }
    public boolean isPrivate() {
        return StringUtils.endsWithIgnoreCase(this.modifier, "private");
    }
    public boolean isFollow() {
        return StringUtils.endsWithIgnoreCase(this.modifier, "follow");
    }
    public boolean isFollow(String followMenber) {
        if (this.isPublic()) {
            return true;
        }
        if (this.isFollow() && this.memberSet != null) {
            return this.memberSet.contains(followMenber);
        }
        return false;
    }
}