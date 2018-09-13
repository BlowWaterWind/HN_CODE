package com.ai.ecs.ecm.mall.wap.modules.member.vo;

public class WangTingInfoResult
{
    /**
     * 通话时长
     */
    private Long voiceResv=0L;
    /**
     * 流量
     */
    private Long gprsTotal=0L;
    /**
     * 话费
     */
    private Long realFee=0L;
    
    /**
     * 剩余通话时长
     */
    private Long voiceBalance=0L;
    /**
     *  剩余 流量
     */
    private Long gprsBalance=0L;
    /**
     * 剩余话费
     */
    private Long feeBalance=0L;
    
    /**
     * 剩余通话时长百分比
     */
    private Long voiceBalanceWeight=0L;
    /**
     *  剩余 流量百分比
     */
    private Long gprsBalanceWeight=0L;
    /**
     * 剩余话费百分比
     */
    private Long feeBalanceWeight=0L;
    
    public Long getVoiceBalanceWeight()
    {
        if(voiceResv==0L){
            return 0L;
        }
        return (voiceBalance/voiceResv)*100;
    }
    public void setVoiceBalanceWeight(Long voiceBalanceWeight)
    {
        this.voiceBalanceWeight = voiceBalanceWeight;
    }
    public Long getGprsBalanceWeight()
    {
        if(gprsTotal==0L){
            return 0L;
        }
        return (gprsBalance/gprsTotal)*100;
    }
    public void setGprsBalanceWeight(Long gprsBalanceWeight)
    {
        this.gprsBalanceWeight = gprsBalanceWeight;
    }
    public Long getFeeBalanceWeight()
    {
        if(realFee==0L){
            return 0L;
        }
        return (feeBalance/realFee)*100;
    }
    public void setFeeBalanceWeight(Long feeBalanceWeight)
    {
        this.feeBalanceWeight = feeBalanceWeight;
    }
    public Long getVoiceResv()
    {
        return voiceResv;
    }
    public void setVoiceResv(Long voiceResv)
    {
        this.voiceResv = voiceResv;
    }
    public Long getGprsTotal()
    {
        return gprsTotal;
    }
    public void setGprsTotal(Long gprsTotal)
    {
        this.gprsTotal = gprsTotal;
    }
    public Long getRealFee()
    {
        return realFee;
    }
    public void setRealFee(Long realFee)
    {
        this.realFee = realFee;
    }
    public Long getVoiceBalance()
    {
        return voiceBalance;
    }
    public void setVoiceBalance(Long voiceBalance)
    {
        this.voiceBalance = voiceBalance;
    }
    public Long getGprsBalance()
    {
        return gprsBalance;
    }
    public void setGprsBalance(Long gprsBalance)
    {
        this.gprsBalance = gprsBalance;
    }
    public Long getFeeBalance()
    {
        return feeBalance;
    }
    public void setFeeBalance(Long feeBalance)
    {
        this.feeBalance = feeBalance;
    }
    
}
