package com.ai.ecs.ecm.mall.wap.modules.member.vo;

public class OrderCountResult
{
    private Long memberId;
    private Long waitPayCount;
    private Long waitSendCount;
    private Long waitReceiveCount;
    private Long waitCommentCount;
    
    public Long getMemberId()
    {
        return memberId;
    }
    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }
    public Long getWaitPayCount()
    {
        return waitPayCount;
    }
    public void setWaitPayCount(Long waitPayCount)
    {
        this.waitPayCount = waitPayCount;
    }
    public Long getWaitSendCount()
    {
        return waitSendCount;
    }
    public void setWaitSendCount(Long waitSendCount)
    {
        this.waitSendCount = waitSendCount;
    }
    public Long getWaitReceiveCount()
    {
        return waitReceiveCount;
    }
    public void setWaitReceiveCount(Long waitReceiveCount)
    {
        this.waitReceiveCount = waitReceiveCount;
    }
    public Long getWaitCommentCount()
    {
        return waitCommentCount;
    }
    public void setWaitCommentCount(Long waitCommentCount)
    {
        this.waitCommentCount = waitCommentCount;
    }
}
