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
package net.hasor.website.domain.enums;
import net.hasor.website.domain.MessageTemplateString;
import org.more.bizcommon.Message;
import org.more.bizcommon.MessageTemplate;
/**
 *
 * @version : 2016年1月1日
 * @author 赵永春(zyc@hasor.net)
 */
public enum ErrorCodes {
    // .用户系统
    U_SAVE_USER_EXIST(1, "登陆账号已经被占用，请更换一个再试。"),//
    U_GET_USER_NOT_EXIST(2, "对不起错误的用户ID，该用户不存在。"),//
    U_GET_USER_FAILED(3, "用户数据查询失败，请重试。"),//
    U_SAVE_USER_FAILED(4, "保存或更新用户失败。"),//
    U_SAVE_SOURCE_FAILED(5, "保存或更新登陆方式失败。"),//
    U_PROIVTER_EXIST(6, "您已经绑定了同类型账号，或者您先行解绑在重新绑定。"),//
    U_PROIVTER_NOT_EXIST(7, "要绑定的合作帐号数据不存在，请重新执行绑定操作。"),//
    U_PROIVTER_MAST_UNBIND(8, "另账号正在使用它，请先解除绑定之后在操作。"),//
    U_PROIVTER_REBIND_FAILED(9, "绑定登陆方式失败，请重试或联系管理员。"),//
    U_UPDATE_FAILED(10, "账号更新失败，请重试或联系管理员。"),//
    U_NEED_LOGIN(11, "请先登录。"),//
    U_OPER_UNAUTHORIZED(12, "操作未被授权。"),//
    // ------------------------------------------------------------------------
    //
    // .外部登陆接入
    OA_PROIVTER_NOT_EXIST(101, "抱歉我们不支持这种方式的第三方登陆。"),//
    OA_TOKEN_EXT_ERROR(102, "OAuth方式登陆，登陆失败。"),//
    OA_TOKEN_EXT_EMPTY(103, "OAuth方式登陆，第三方合作网站没有结果。请重试或联系管理员。"),//
    OA_TOKEN_EXT_FAILED(104, "OAuth方式登陆，第三方合作网站验证失败，请重试。"),//
    OA_ERROR(105, "OAuth登陆系统内部错误，请重试或联系管理员。"),//
    OA_BIND_FAILED(106, "第三方登陆账号绑定失败，请重试"),//
    //
    LOGIN_ERROR(107, "登陆出错，请重试或联系管理员。"),//
    // ------------------------------------------------------------------------
    //
    // .表单验证
    V_OAUTH_CALLBACK_FAILED(201, "合作网站登陆回调验证失败。"),//
    V_TOKEN_NEED_LOGIN(202, "token已过期,请重新登录。"),//
    V_CSRF_INVALID(203, "请重新登陆，或者这是一个非法的请求。"),//
    V_FORM_PROJECT_INVALID(204, "项目表单验证不通过，请检查表单值是否正确。"),//
    // ------------------------------------------------------------------------
    //
    // .项目&版本
    P_OWNER_ERROR(301, "对不起，Owner参数信息错误。"),//
    P_OWNER_TYPE_FAILED(302, "Owner类型错误。"),//
    P_OWNER_NOT_EXIST(303, "对不起，Owner不存在。"),//
    P_OWNER_NOT_YOU(304, "对不起，这个项目的Owner不是您。"),//
    //
    P_VERSION_NOT_EXIST(310, "不存在的项目版本。"),//
    P_VERSION_UPDATE_FAILED(311, "更新版本信息失败，请重试。"),//
    P_VERSION_SAVE_FAILED(312, "项目新增版本失败，请重试。"),//
    P_VERSION_STATUS_FAILED(313, "该版本当前状态不支持这个操作。"),//
    //
    P_PROJECT_NOT_EXIST(320, "不存在的项目。"),//
    P_PROJECT_UPDATE_FAILED(321, "更新项目信息失败，请重试。"),//
    P_SAVE_PROJECT_FAILED(322, "项目信息保存失败，请稍后重试。"),//
    P_PROJECT_STATUS_FAILED(313, "项目的当前状态不支持这个操作。"),//
    P_V_PROJECT_INFO_FAILED(324, "项目信息不完整或错误的表单数据。"),//
    //
    P_QUERY_ERROR(380, "查询项目信息失败，请重试。"),//
    // ------------------------------------------------------------------------
    //
    SUCCESS(900, "操作成功。"),//
    BAD_PARAMS(901, "错误的调用参数。"),//
    BAD_REQUEST(902, "错误的请求。"),//
    BAD_REQUEST_PARAMS(903, "错误数据查询参数。"),//
    BAD_RESOURCE_DELETE(904, "资源已经被删除，不能执行该操作。"),//
    BAD_UNKNOWN(999, "未知类型异常: %s");
    // ------------------------------------------------------------------------
    //
    //
    private MessageTemplate temp = null;
    ErrorCodes(int errorCode, String message) {
        this.temp = new MessageTemplateString(errorCode, message);
    }
    //
    public Message getMsg(Object... params) {
        return new Message(this.temp, params);
    }
}