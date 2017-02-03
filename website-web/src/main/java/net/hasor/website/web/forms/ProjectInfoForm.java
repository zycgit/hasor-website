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
package net.hasor.website.web.forms;
import net.hasor.web.annotation.ReqParam;
import net.hasor.website.domain.ProjectInfoDO;
/**
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
public class ProjectInfoForm extends ProjectInfoDO {
    @ReqParam("contentFormatType")
    private int    contentFormatType = -1;
    @ReqParam("presentEditor_Content")
    private String presentContent    = null;
    @ReqParam("sourceTypeEnum")
    private int    sourceTypeEnum    = -1;
    //
    public int getContentFormatType() {
        return contentFormatType;
    }
    public void setContentFormatType(int contentFormatType) {
        this.contentFormatType = contentFormatType;
    }
    public int getSourceTypeEnum() {
        return sourceTypeEnum;
    }
    public void setSourceTypeEnum(int sourceTypeEnum) {
        this.sourceTypeEnum = sourceTypeEnum;
    }
    public String getPresentContent() {
        return presentContent;
    }
    public void setPresentContent(String presentContent) {
        this.presentContent = presentContent;
    }
}