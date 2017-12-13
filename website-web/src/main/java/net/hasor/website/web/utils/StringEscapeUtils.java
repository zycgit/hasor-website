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
package net.hasor.website.web.utils;
import net.hasor.website.core.Service;
/**
 *
 * @version : 2016年12月05日
 * @author 赵永春 (zyc@hasor.net)
 */
@Service("escapeHtml")
public class StringEscapeUtils extends org.apache.commons.lang3.StringEscapeUtils {
    public static final String escapeHtml(final String input) {
        return escapeHtml4(input);
    }
}