package com.testhuamou.vltava.domain.coverage;

/**
 * @author Rob
 */
public class RateMsgVO {
    private Integer total;
    private Integer passedCount;
    private Integer failedCount;
    private Integer rate;
    private Boolean finish;
    private Throwable failCause;

    public RateMsgVO(Integer total, Integer passedCount, Integer failedCount, Integer rate, Boolean finish, Throwable failCause) {
        this.total = total;
        this.passedCount = passedCount;
        this.failedCount = failedCount;
        this.rate = rate;
        this.finish = finish;
        this.failCause = failCause;
    }

    public RateMsgVO(Integer total, Integer passedCount, Integer rate){
        this.total = total;
        this.passedCount = passedCount;
        this.rate = rate;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public Integer getPassedCount() {
        return passedCount;
    }

    public void setPassedCount(Integer passedCount) {
        this.passedCount = passedCount;
    }

    public Integer getFailedCount() {
        return failedCount;
    }

    public void setFailedCount(Integer failedCount) {
        this.failedCount = failedCount;
    }

    public Throwable getFailCause() {
        return failCause;
    }

    public void setFailCause(Throwable failCause) {
        this.failCause = failCause;
    }

    public Integer getRate() {
        return rate;
    }

    public void setRate(Integer rate) {
        this.rate = rate;
    }

    public Boolean getFinish() {
        return finish;
    }

    public void setFinish(Boolean finish) {
        this.finish = finish;
    }

}

