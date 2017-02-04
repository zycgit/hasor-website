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
package net.hasor.website.web.actions.projects;
import net.hasor.web.annotation.MappingTo;
import net.hasor.web.annotation.PathParam;
import net.hasor.web.render.RenderInvoker;
import net.hasor.website.domain.ProjectInfoDO;
import org.commonmark.Extension;
import org.commonmark.ext.autolink.AutolinkExtension;
import org.commonmark.ext.front.matter.YamlFrontMatterExtension;
import org.commonmark.ext.gfm.strikethrough.StrikethroughExtension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.ext.heading.anchor.HeadingAnchorExtension;
import org.commonmark.ext.ins.InsExtension;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.more.bizcommon.Result;

import java.util.Arrays;
import java.util.List;
/**
 * 项目的首页入口
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/projects/{projectID}/index.htm")
public class ProjectIndex extends BaseProjects {
    //
    public void execute(@PathParam("projectID") long projectID, RenderInvoker data) {
        //
        Result<ProjectInfoDO> projectResult = this.projectManager.queryProjectByID(projectID);
        if (!projectResult.isSuccess()) {
            sendError(projectResult.firstMessage());
            return;
        }
        //
        ProjectInfoDO projectInfo = projectResult.getResult();
        putData("project", projectInfo);
        //
        List<Extension> extensions = Arrays.asList( //
                TablesExtension.create(),           // commonmark-ext-gfm-tables
                AutolinkExtension.create(),         // commonmark-ext-autolink
                StrikethroughExtension.create(),    // commonmark-ext-gfm-strikethrough
                HeadingAnchorExtension.create(),    // commonmark-ext-heading-anchor
                YamlFrontMatterExtension.create(),  // commonmark-ext-yaml-front-matter
                InsExtension.create()               // commonmark-ext-ins
        );
        Parser parser = Parser.builder().extensions(extensions).build();
        Node document = parser.parse(projectInfo.getPresent());
        HtmlRenderer renderer = HtmlRenderer.builder().extensions(extensions).build();
        String htmlData = renderer.render(document);
        putData("projectPresent", htmlData);
        //
        data.renderTo("htm", "/projects/projectDetail.htm");
    }
}