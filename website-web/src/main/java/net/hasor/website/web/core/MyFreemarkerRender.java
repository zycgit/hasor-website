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
package net.hasor.website.web.core;
import freemarker.template.Configuration;
import freemarker.template.TemplateModelException;
import net.hasor.core.AppContext;
import net.hasor.plugins.render.FreemarkerRender;
import net.hasor.website.core.Service;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.ServletContext;
import java.util.Set;
/**
 *
 * @version : 2016年1月3日
 * @author 赵永春(zyc@hasor.net)
 */
public class MyFreemarkerRender extends FreemarkerRender {
    @Override
    protected void configSharedVariable(AppContext appContext, ServletContext servletContext, Configuration freemarker) throws TemplateModelException {
        super.configSharedVariable(appContext, servletContext, freemarker);
        // - 系统服务
        Set<Class<?>> serviceSet = appContext.getEnvironment().findClass(Service.class);
        for (Class<?> service : serviceSet) {
            if (service == Service.class) {
                continue;
            }
            Service ser = service.getAnnotation(Service.class);
            if (ser != null && StringUtils.isNotBlank(ser.value())) {
                freemarker.setSharedVariable(ser.value(), appContext.getInstance(service));
            }
        }
    }
}