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
import net.hasor.junit.ContextConfiguration;
import net.hasor.junit.HasorUnitRunner;
import net.hasor.website.datadao.ProjectInfoDAO;
import net.hasor.website.datadao.ProjectVersionDAO;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.enums.ContentFormat;
import net.hasor.website.domain.enums.OwnerType;
import net.hasor.website.domain.enums.SourceType;
import net.hasor.website.domain.futures.ProjectFutures;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
/**
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
@ContextConfiguration(value = "hasor-config.xml", loadModules = TestModule.class)
@RunWith(HasorUnitRunner.class)
public class DaoTest extends AbstractTest {
    protected Logger logger = LoggerFactory.getLogger(getClass());
    @Inject
    protected ProjectInfoDAO    projectInfoDAO;
    @Inject
    protected ProjectVersionDAO projectVersionDAO;
    //
    @Test
    public void insert() throws SQLException {
        //
        ProjectInfoDO projectDO = new ProjectInfoDO();
        projectDO.setOwnerID(0);
        projectDO.setOwnerType(OwnerType.Personal);
        projectDO.setParentID(null);
        projectDO.setName("testProject");
        projectDO.setSubtitle("ddddd");
        projectDO.setPresent("xxx....................");
        projectDO.setContentFormat(ContentFormat.MD);
        projectDO.setHomePage("http://xxxxx");
        projectDO.setDownPage("http://yyyyy");
        projectDO.setSourceType(SourceType.Close);
        projectDO.setLanguage("java");
        projectDO.setLicense("a20");
        projectDO.setFutures(new ProjectFutures());
        //
        long i = projectInfoDAO.insertProject(projectDO);
        logger.info("i=" + i);
        //
        ProjectInfoDO infoDO = projectInfoDAO.queryByID(i);
        logger.info(JSON.toJSONString(infoDO));
    }
    //
}