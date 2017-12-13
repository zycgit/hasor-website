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
package net.hasor.website.oss;
import com.aliyun.oss.OSSClient;
/**
 *
 * @version : 2016年12月02日
 * @author 赵永春 (zyc@hasor.net)
 */
public class AliyunOSSClient extends OSSClient {
    private String bucketPath;
    private String bucketName;
    //
    public AliyunOSSClient(String endpoint, String accessKeyId, String secretAccessKey) {
        super(endpoint, accessKeyId, secretAccessKey);
    }
    //
    /**Bucket名称*/
    public String getBucketName() {
        return this.bucketName;
    }
    public void setBucketPath(String bucketPath) {
        this.bucketPath = bucketPath;
    }
    //
    /**保存上传文件的基础地址*/
    public String getBucketPath() {
        return this.bucketPath;
    }
    public void setBucketName(String bucketName) {
        this.bucketName = bucketName;
    }
}