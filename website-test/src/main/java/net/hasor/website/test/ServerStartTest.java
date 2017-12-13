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
import net.hasor.plugins.junit.ContextConfiguration;
import net.hasor.plugins.junit.HasorUnitRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
/**
 * @version : 2016年1月10日
 * @author 赵永春 (zyc@hasor.net)
 */
@ContextConfiguration(value = "server-hasor-config.xml", loadModules = ServerStartTest.class)
@RunWith(HasorUnitRunner.class)
public class ServerStartTest extends AbstractTest implements Module {
    @Override
    public void loadModule(ApiBinder apiBinder) throws Throwable {
        apiBinder.installModule(new TestModule(true));
    }
    @Test
    public void startTest() throws IOException, InterruptedException {
        System.out.println("--- server started ---");
        while (true) {
            Thread.sleep(300);
        }
    }
}