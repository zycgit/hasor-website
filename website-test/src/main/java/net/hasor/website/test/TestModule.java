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
package net.hasor.website.test;
import net.hasor.core.ApiBinder;
import net.hasor.core.Module;
import net.hasor.rsf.RsfApiBinder;
import net.hasor.website.client.EchoService;
import net.hasor.website.client.ProjectService;
import net.hasor.website.core.RootModule;
/**
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
public class TestModule implements Module {
    private boolean withoutRPCProvider;
    public TestModule() {
        this(false);
    }
    public TestModule(boolean withoutRPCProvider) {
        this.withoutRPCProvider = withoutRPCProvider;
    }
    @Override
    public void loadModule(ApiBinder apiBinder) throws Throwable {
        //
        apiBinder.installModule(new RootModule(this.withoutRPCProvider));
        //
        RsfApiBinder rsfApiBinder = apiBinder.tryCast(RsfApiBinder.class);
        if (rsfApiBinder != null && !this.withoutRPCProvider) {
            String targetAddress = "rsf://127.0.0.1:2161/default";
            rsfApiBinder.bindType(EchoService.class).toProvider(rsfApiBinder.converToProvider(//
                    rsfApiBinder.rsfService(EchoService.class).bindAddress(targetAddress).register()//
            ));
            rsfApiBinder.bindType(ProjectService.class).toProvider(rsfApiBinder.converToProvider(//
                    rsfApiBinder.rsfService(ProjectService.class).bindAddress(targetAddress).register()//
            ));
        }
    }
}