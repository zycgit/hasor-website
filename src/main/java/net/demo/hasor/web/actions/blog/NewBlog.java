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
package net.demo.hasor.web.actions.blog;
import net.demo.hasor.core.Action;
import net.demo.hasor.web.forms.LoginCallBackForm;
import net.hasor.restful.api.MappingTo;
import net.hasor.restful.api.Params;
import net.hasor.restful.api.Valid;
import org.more.fileupload.real.FileItemStream;
import org.more.fileupload.real.FileUpload;
import org.more.fileupload.real.util.Streams;
import org.more.util.io.IOUtils;

import java.io.*;
/**
 * OAuth : 服务器获取 AccessToken
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
@MappingTo("/blog/save.do")
public class NewBlog extends Action {
    //
    public void execute(@Valid("AccessToken") @Params LoginCallBackForm loginForm) throws IOException {
        loginForm.getCode();
        boolean isMultipart = FileUpload.isMultipartContent(this.getRequest());
        // Create a new file upload handler
        FileUpload upload = new FileUpload();
        // Parse the request
        FileUpload.FileItemIteratorImpl iter = upload.getItemIterator(this.getRequest());
        while (iter.hasNext()) {
            FileItemStream item = iter.next();
            String name = item.getFieldName();
            InputStream stream = item.openStream();
            if (item.isFormField()) {
                System.out.println("Form field " + name + " with value " + Streams.asString(stream) + " detected.");
            } else {
                String fileDir = this.getAppContext().getEnvironment().evalString("%HASOR_TEMP_PATH%");
                File f = new File(fileDir, item.getName());
                f.getParentFile().mkdirs();
                OutputStream ostream = new FileOutputStream(f);
                IOUtils.copy(stream, ostream);
                ostream.flush();
                ostream.close();
                System.out.println("File field " + name + " with file name " + item.getName() + " detected.");
            }
        }
    }
}