package com.ai.ecs.ecm.mall.wap.modules.member.vo;

import java.util.Date;

public class UserRatingResult
{
    private String memberName;
    private Long memberId;
    private Integer score;
    /**
     * 评价ID 
     */
    private Long userRatingId;


    /**
     * 子订单ID 
     */
    private Long orderSubId;

    /**
     * 评价内容 
     */
    private String ratingContain;


    /**
     * 评价时间 
     */
    private Date ratingTime;
    
    /**
     * 产品ID 
     */
    private Long goodsId;

    public String getMemberName()
    {
        return memberName;
    }

    public void setMemberName(String memberName)
    {
        this.memberName = memberName;
    }

    public Long getMemberId()
    {
        return memberId;
    }

    public void setMemberId(Long memberId)
    {
        this.memberId = memberId;
    }

    public Integer getScore()
    {
        return score;
    }

    public void setScore(Integer score)
    {
        this.score = score;
    }

    public Long getUserRatingId()
    {
        return userRatingId;
    }

    public void setUserRatingId(Long userRatingId)
    {
        this.userRatingId = userRatingId;
    }

    public Long getOrderSubId()
    {
        return orderSubId;
    }

    public void setOrderSubId(Long orderSubId)
    {
        this.orderSubId = orderSubId;
    }

    public String getRatingContain()
    {
        return ratingContain;
    }

    public void setRatingContain(String ratingContain)
    {
        this.ratingContain = ratingContain;
    }

    public Date getRatingTime()
    {
        return ratingTime;
    }

    public void setRatingTime(Date ratingTime)
    {
        this.ratingTime = ratingTime;
    }

    public Long getGoodsId()
    {
        return goodsId;
    }

    public void setGoodsId(Long goodsId)
    {
        this.goodsId = goodsId;
    }
    
}
