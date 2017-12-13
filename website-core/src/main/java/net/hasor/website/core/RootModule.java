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
package net.hasor.website.core;
import net.hasor.core.ApiBinder;
import net.hasor.core.Module;
import net.hasor.website.cache.CacheModule;
import net.hasor.website.oss.AliyunModule;
import net.hasor.website.provider.ConsumerModule;
import net.hasor.website.provider.ProviderModule;
import net.hasor.website.utils.CommonCodeUtils;
/**
 *
 * @version : 2015年12月25日
 * @author 赵永春 (zyc@hasor.net)
 */
public class RootModule implements Module {
    private boolean withoutRPCProvider;
    public RootModule(boolean withoutRPCProvider) {
        this.withoutRPCProvider = withoutRPCProvider;
    }
    @Override
    public void loadModule(ApiBinder apiBinder) throws Throwable {
        //
        // .测试算法是否OK
        CommonCodeUtils.MD5.getMD5(".");
        //
        // .子模块
        apiBinder.installModule(new DataSourceModule());    // 数据库
        apiBinder.installModule(new AliyunModule());        // 阿里云
        apiBinder.installModule(new CacheModule());         // Cache
        if (this.withoutRPCProvider)
            apiBinder.installModule(new ProviderModule());  // 服务提供者
        apiBinder.installModule(new ConsumerModule());      // 服务消费者
    }
}
