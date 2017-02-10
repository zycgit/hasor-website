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
package net.hasor.website.utils;
import net.hasor.website.client.RsfResultDO;
import net.hasor.website.domain.enums.ErrorCodes;
import org.more.bizcommon.Message;
import org.more.bizcommon.Result;
import org.more.bizcommon.ResultDO;
/**
 * @version : 2016年1月10日
 * @author 赵永春(zyc@hasor.net)
 */
public class ResultUtils {
    //
    public static <T> ResultDO<T> failed(ErrorCodes errorCode) {
        return failed(errorCode.getMsg(), null);
    }
    public static <T> ResultDO<T> failed(ErrorCodes errorCode, Throwable throwable) {
        return failed(errorCode.getMsg(), throwable);
    }
    public static <T> ResultDO<T> failed(Message errorMessage) {
        return failed(errorMessage, null);
    }
    public static <T> ResultDO<T> failed(Throwable throwable) {
        return failed(ErrorCodes.BAD_UNKNOWN, throwable);
    }
    public static <T> ResultDO<T> failed(Message errorMessage, Throwable throwable) {
        return new ResultDO<T>(false)//
                .setThrowable(throwable)//
                .addMessage(errorMessage)//
                .setSuccess(false)//
                .setResult(null);
    }
    public static <T> ResultDO<T> failed(Result<?> result) {
        return new ResultDO<T>(false)//
                .setThrowable(result.getThrowable())//
                .addMessage(result.getMessageList())//
                .setSuccess(result.isSuccess())//
                .setResult(null);
    }
    public static <T, D extends T> ResultDO<T> success(D data) {
        return new ResultDO<T>(true)//
                .setSuccess(true)//
                .setResult(data);
    }
    //
    public static <T, D extends T> RsfResultDO<T> converTo(Result<D> data) {
        RsfResultDO<T> resultDO = new RsfResultDO<>();
        resultDO.setSuccess(data.isSuccess());
        Message firstMessage = data.firstMessage();
        if (firstMessage != null) {
            resultDO.setErrorCode(firstMessage.getType());
            resultDO.setErrorMessage(firstMessage.getMessage());
        }
        resultDO.setResult(data.getResult());
        return resultDO;
    }
}
