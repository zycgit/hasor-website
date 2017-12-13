package net.hasor.website.domain.futures;
import java.util.Date;
/**
 *
 * @version : 2016年10月07日
 * @author 赵永春 (zyc@hasor.net)
 */
public class ProjectVersionFutures {
    private String downloadURL;      // 下载地址
    private String apiURL;           // 在线API地址
    private String sourceURL;        // 源码地址
    private Date   recoveryStartTime;// 计算回收的起始时间点
    private Date   recoveryEndTime;  // 计算回收的终止时间点
    private String recoveryStatus;   // 回收时的所处状态
    //
    public String getDownloadURL() {
        return downloadURL;
    }
    public void setDownloadURL(String downloadURL) {
        this.downloadURL = downloadURL;
    }
    public String getApiURL() {
        return apiURL;
    }
    public void setApiURL(String apiURL) {
        this.apiURL = apiURL;
    }
    public String getSourceURL() {
        return sourceURL;
    }
    public void setSourceURL(String sourceURL) {
        this.sourceURL = sourceURL;
    }
    public Date getRecoveryStartTime() {
        return recoveryStartTime;
    }
    public void setRecoveryStartTime(Date recoveryStartTime) {
        this.recoveryStartTime = recoveryStartTime;
    }
    public Date getRecoveryEndTime() {
        return recoveryEndTime;
    }
    public void setRecoveryEndTime(Date recoveryEndTime) {
        this.recoveryEndTime = recoveryEndTime;
    }
    public String getRecoveryStatus() {
        return recoveryStatus;
    }
    public void setRecoveryStatus(String recoveryStatus) {
        this.recoveryStatus = recoveryStatus;
    }
}