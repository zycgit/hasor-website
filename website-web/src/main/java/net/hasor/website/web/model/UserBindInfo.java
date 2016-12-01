package net.hasor.website.web.model;
/**
 * 账号的第三方绑定信息
 * Created by yongchun.zyc on 2016/12/5.
 */
public class UserBindInfo {
    private boolean bind      = false;  // 是否已绑定
    private boolean allow     = true;   // 是否允许绑定
    private String  provider  = "";     // 名称
    //
    private String  html_id   = "";     // HTML元素ID
    private String  html_css  = "";     // HTML元素样式
    private String  html_href = "";     // HTML链接
    //
    //
    public boolean isBind() {
        return bind;
    }
    public void setBind(boolean bind) {
        this.bind = bind;
    }
    public boolean isAllow() {
        return allow;
    }
    public void setAllow(boolean allow) {
        this.allow = allow;
    }
    public String getProvider() {
        return provider;
    }
    public void setProvider(String provider) {
        this.provider = provider;
    }
    public String getHtml_id() {
        return html_id;
    }
    public void setHtml_id(String html_id) {
        this.html_id = html_id;
    }
    public String getHtml_css() {
        return html_css;
    }
    public void setHtml_css(String html_css) {
        this.html_css = html_css;
    }
    public String getHtml_href() {
        return html_href;
    }
    public void setHtml_href(String html_href) {
        this.html_href = html_href;
    }
}
