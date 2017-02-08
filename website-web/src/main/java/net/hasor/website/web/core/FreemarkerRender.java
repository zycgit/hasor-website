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
import freemarker.cache.FileTemplateLoader;
import freemarker.cache.TemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import net.hasor.core.AppContext;
import net.hasor.web.render.Render;
import net.hasor.web.render.RenderEngine;
import net.hasor.web.render.RenderInvoker;
import net.hasor.website.core.AppConstant;
import net.hasor.website.core.Service;
import org.more.util.StringEscapeUtils;
import org.more.util.StringUtils;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.io.Writer;
import java.util.HashMap;
import java.util.Set;
/**
 *
 * @version : 2016年1月3日
 * @author 赵永春(zyc@hasor.net)
 */
@Render({ "html", "htm" })
public class FreemarkerRender implements RenderEngine {
    protected Configuration configuration;
    private   String        ctxPath;
    @Override
    public void initEngine(AppContext appContext) throws Throwable {
        String realPath = appContext.getInstance(ServletContext.class).getRealPath("/");
        TemplateLoader templateLoader = new FileTemplateLoader(new File(realPath), true);
        this.configuration = new Configuration(Configuration.VERSION_2_3_22);
        this.configuration.setTemplateLoader(templateLoader);
        this.configuration.setDefaultEncoding("utf-8");//默认页面编码UTF-8
        this.configuration.setOutputEncoding("utf-8");//输出编码格式UTF-8
        this.configuration.setLocalizedLookup(false);//是否开启国际化false
        this.configuration.setClassicCompatible(true);//null值测处理配置
        //
        // - 各种工具
        this.configuration.setSharedVariable("escapeHtml", new StringEscapeUtils());//HTML 转译,防止XSS使用。
        this.configuration.setSharedVariable("stringUtils", new StringUtils());
        //
        // - 系统服务
        Set<Class<?>> serviceSet = appContext.getEnvironment().findClass(Service.class);
        for (Class<?> service : serviceSet) {
            if (service == Service.class) {
                continue;
            }
            Service ser = service.getAnnotation(Service.class);
            if (ser != null && StringUtils.isNotBlank(ser.value())) {
                this.configuration.setSharedVariable(ser.value(), appContext.getInstance(service));
            }
        }
        //
        // - 环境变量
        this.ctxPath = appContext.findBindingBean(AppConstant.VAR_CONTEXT_PATH, String.class);
    }
    @Override
    public void process(RenderInvoker renderData, Writer writer) throws Throwable {
        Template temp = this.configuration.getTemplate(renderData.renderTo());
        //
        HashMap<String, Object> data = new HashMap<String, Object>();
        for (String key : renderData.keySet()) {
            data.put(key, renderData.get(key));
        }
        data.put(AppConstant.VAR_CONTEXT_PATH, this.ctxPath);
        data.put(AppConstant.VAR_REQUEST, renderData.getHttpRequest());
        //
        temp.process(data, writer);
    }
    @Override
    public boolean exist(String template) throws IOException {
        return this.configuration.getTemplateLoader().findTemplateSource(template) != null;
    }
}