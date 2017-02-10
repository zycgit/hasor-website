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
import hprose.client.HproseClient;
import hprose.common.HproseResultMode;
import hprose.common.InvokeSettings;
import net.hasor.website.client.RsfResultDO;
import net.hasor.website.domain.enums.OwnerType;
import net.hasor.website.domain.owner.SimpleOwner;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
/**
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
public class HproseRpcTest {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    protected HproseClient client;
    //
    @Before
    public void init() throws IOException, URISyntaxException {
        this.client = HproseClient.create("http://127.0.0.1:2162/");
    }
    @Test
    public void sayHelloRPCTest() throws Throwable {
        InvokeSettings settings = new InvokeSettings();
        settings.setMode(HproseResultMode.Normal);
        System.out.println(client.invoke(//
                "call://[RSF]net.hasor.website.client.EchoService-1.0.0/sayHello",//
                new Object[] { "Hello Word." }, String.class, settings));
    }
    @Test
    public void queryProjectRPCTest() throws Throwable {
        InvokeSettings settings = new InvokeSettings();
        settings.setMode(HproseResultMode.Normal);
        System.out.println(client.invoke(//
                "call://[RSF]net.hasor.website.client.ProjectService-1.0.0/queryProjectByID",//
                new Object[] { 1L }, RsfResultDO.class, settings));
    }
    @Test
    public void queryByOwner() throws Throwable {
        SimpleOwner owner = new SimpleOwner();
        owner.setOwnerID(1L);
        owner.setOwnerType(OwnerType.Personal);
        //
        InvokeSettings settings = new InvokeSettings();
        settings.setMode(HproseResultMode.Normal);
        System.out.println(client.invoke(//
                "call://[RSF]net.hasor.website.client.ProjectService-1.0.0/queryProjectByOwner",//
                new Object[] { owner }, RsfResultDO.class, settings));
    }
    @Test
    public void queryAllProjectTest() throws Throwable {
        InvokeSettings settings = new InvokeSettings();
        settings.setMode(HproseResultMode.Normal);
        System.out.println(client.invoke(//
                "call://[RSF]net.hasor.website.client.ProjectService-1.0.0/queryPublicProject",//
                new Object[0], RsfResultDO.class, settings));
    }
}