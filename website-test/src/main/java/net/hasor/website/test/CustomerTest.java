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
import com.alibaba.fastjson.JSON;
import net.hasor.core.Inject;
import net.hasor.plugins.junit.ContextConfiguration;
import net.hasor.plugins.junit.HasorUnitRunner;
import net.hasor.website.client.EchoService;
import net.hasor.website.client.ProjectService;
import net.hasor.website.client.RsfResultDO;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.enums.OwnerType;
import net.hasor.website.domain.owner.SimpleOwner;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
/**
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
@ContextConfiguration(value = "hasor-config.xml", loadModules = TestModule.class)
@RunWith(HasorUnitRunner.class)
public class CustomerTest extends AbstractTest {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Inject
    protected EchoService    echoService;
    @Inject
    protected ProjectService projectService;
    //
    @Test
    public void sayHelloRPCTest() {
        String res = echoService.sayHello("Hello Word");
        System.out.println(res);
    }
    @Test
    public void queryProjectRPCTest() {
        long projectID = 1;
        RsfResultDO<ProjectInfoDO> result = this.projectService.queryProjectByID(projectID);
        System.out.println(JSON.toJSONString(result));
    }
    @Test
    public void queryByOwner() {
        //
        SimpleOwner owner = new SimpleOwner();
        owner.setOwnerID(1L);
        owner.setOwnerType(OwnerType.Personal);
        RsfResultDO<List<ProjectInfoDO>> result = this.projectService.queryProjectByOwner(owner);
        System.out.println(JSON.toJSONString(result));
    }
    @Test
    public void queryAllProjectTest() {
        RsfResultDO<List<ProjectInfoDO>> result = this.projectService.queryPublicProject();
        System.out.println(JSON.toJSONString(result));
    }
}