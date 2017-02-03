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
import net.hasor.website.domain.ProjectVersionDO;
/**
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
public class ProjectVersionForm extends ProjectVersionDO {
    @ReqParam("contentFormatType")
    private int    changelogFormatType = -1;
    @ReqParam("changelogEditor_Content")
    private String changelogContent    = null;
    @ReqParam("downloadURL")
    private String futuresDownloadURL  = null;
    @ReqParam("sourceURL")
    private String futuresSourceURL    = null;
    @ReqParam("apiURL")
    private String futuresApiURL       = null;
    //
    public int getChangelogFormatType() {
        return changelogFormatType;
    }
    public void setChangelogFormatType(int changelogFormatType) {
        this.changelogFormatType = changelogFormatType;
    }
    public String getChangelogContent() {
        return changelogContent;
    }
    public void setChangelogContent(String changelogContent) {
        this.changelogContent = changelogContent;
    }
    public String getFuturesDownloadURL() {
        return futuresDownloadURL;
    }
    public void setFuturesDownloadURL(String futuresDownloadURL) {
        this.futuresDownloadURL = futuresDownloadURL;
    }
    public String getFuturesSourceURL() {
        return futuresSourceURL;
    }
    public void setFuturesSourceURL(String futuresSourceURL) {
        this.futuresSourceURL = futuresSourceURL;
    }
    public String getFuturesApiURL() {
        return futuresApiURL;
    }
    public void setFuturesApiURL(String futuresApiURL) {
        this.futuresApiURL = futuresApiURL;
    }
}