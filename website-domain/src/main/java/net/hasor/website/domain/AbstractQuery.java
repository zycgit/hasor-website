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
import org.more.bizcommon.Paginator;
import org.more.util.ClassUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 *
 * @version : 2016年12月13日
 * @author 赵永春(zyc@hasor.net)
 */
public class AbstractQuery<T> extends Paginator {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    private T example;
    //
    public AbstractQuery() {
        this.initExample();
    }
    private void initExample() {
        try {
            Class<?> exampleType = ClassUtils.getSuperClassGenricType(this.getClass(), 0);
            this.example = (T) exampleType.newInstance();
        } catch (Exception e) {
            logger.error("initExample failed ->" + e.getMessage(), e);
        }
    }
    //
    public T getExample() {
        return example;
    }
}