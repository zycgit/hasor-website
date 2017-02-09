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
package net.hasor.website.client;
import net.hasor.website.domain.ProjectInfoDO;
import net.hasor.website.domain.owner.SimpleOwner;
import org.more.bizcommon.Result;

import java.util.List;
/**
 * 项目接口
 * @version : 2015年11月27日
 * @author 赵永春(zyc@hasor.net)
 */
public interface ProjectService {
    /** 查询所有公开的项目 */
    public Result<List<ProjectInfoDO>> queryPublicProject();

    /** 根据Owner查询项目列表 */
    public Result<List<ProjectInfoDO>> queryProjectByOwner(SimpleOwner owner);

    /** 根据 */
    public Result<ProjectInfoDO> queryProjectByID(long projectID);
}