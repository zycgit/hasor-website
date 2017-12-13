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
package net.hasor.website.provider;
import net.hasor.rsf.RsfApiBinder;
import net.hasor.rsf.RsfBindInfo;
import net.hasor.rsf.RsfModule;
import net.hasor.website.client.EchoService;
import net.hasor.website.client.MessageService;
import net.hasor.website.client.ProjectService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
/**
 * RPC 服务注册
 * @version : 2016年1月10日
 * @author 赵永春 (zyc@hasor.net)
 */
public class ProviderModule extends RsfModule {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Override
    public void loadModule(RsfApiBinder apiBinder) throws Throwable {
        // - 注册服务提供者
        RsfBindInfo<?> bind1 = apiBinder.rsfService(EchoService.class).to(EchoServiceImpl.class).register();
        RsfBindInfo<?> bind2 = apiBinder.rsfService(MessageService.class).to(MessageServiceImpl.class).register();
        RsfBindInfo<?> bind3 = apiBinder.rsfService(ProjectService.class).to(ProjectServiceImpl.class).register();
        //
        this.logger.info("rsfServiceID -> " + bind1.getBindID());
        this.logger.info("rsfServiceID -> " + bind2.getBindID());
        this.logger.info("rsfServiceID -> " + bind3.getBindID());
    }
}