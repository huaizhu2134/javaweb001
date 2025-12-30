package org.example.oracle01.model;

import java.util.Date;

public class Evaluation {
    private Long evalId;
    private Long orderId;
    private Long customerId;
    private Long staffId;
    private Integer score;
    private String content;
    private String tags;
    private String isAnonymous;
    private String staffReply;
    private Date replyTime;
    private Date createTime;
    private String isDeleted;

    // Getters and Setters
    public Long getEvalId() {
        return evalId;
    }

    public void setEvalId(Long evalId) {
        this.evalId = evalId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Long customerId) {
        this.customerId = customerId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getIsAnonymous() {
        return isAnonymous;
    }

    public void setIsAnonymous(String isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public String getStaffReply() {
        return staffReply;
    }

    public void setStaffReply(String staffReply) {
        this.staffReply = staffReply;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }
}