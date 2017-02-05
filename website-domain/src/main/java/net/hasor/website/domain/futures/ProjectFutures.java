package net.hasor.website.domain.futures;
import java.util.Date;
/**
 *
 * @version : 2016年10月07日
 * @author 赵永春(zyc@hasor.net)
 */
public class ProjectFutures {
    private Date   recoveryStartTime;// 计算回收的起始时间点
    private Date   recoveryEndTime;  // 计算回收的终止时间点
    private String recoveryStatus;   // 回收时的所处状态
    //
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