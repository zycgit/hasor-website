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
import net.hasor.website.domain.result.Paginator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
/**
 *
 * @version : 2016年12月13日
 * @author 赵永春 (zyc@hasor.net)
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
            Class<?> exampleType = getSuperClassGenricType(this.getClass(), 0);
            this.example = (T) exampleType.newInstance();
        } catch (Exception e) {
            logger.error("initExample failed ->" + e.getMessage(), e);
        }
    }
    //
    public T getExample() {
        return example;
    }
    /**获取泛型类型。*/
    public static Class<?> getSuperClassGenricType(final Class<?> clazz, final int index) {
        //返回表示此 Class 所表示的实体（类、接口、基本类型或 void）的直接超类的 Type。
        Type genType = clazz.getGenericSuperclass();
        if (!(genType instanceof ParameterizedType)) {
            return Object.class;
        }
        //返回表示此类型实际类型参数的 Type 对象的数组。
        Type[] params = ((ParameterizedType) genType).getActualTypeArguments();
        if (index >= params.length || index < 0) {
            return Object.class;
        }
        if (!(params[index] instanceof Class)) {
            return Object.class;
        }
        return (Class<?>) params[index];
    }
}