package com.ai.ecs.ecm.mall.wap.modules.member.vo;

public class LogisticResult
{
    private String time; // 时间点

    private String remark; // 节点说明（EMS） memo类似 三人行

    private String opcode; // 操作状态（EMS）

    private String acceptaddress; // 到达城市（EMS）

    private String scantype; // 快件状态

    public String getTime()
    {
        return time;
    }

    public void setTime(String time)
    {
        this.time = time;
    }

    public String getRemark()
    {
        return remark;
    }

    public void setRemark(String remark)
    {
        this.remark = remark;
    }

    public String getOpcode()
    {
        return opcode;
    }

    public void setOpcode(String opcode)
    {
        this.opcode = opcode;
    }

    public String getAcceptaddress()
    {
        return acceptaddress;
    }

    public void setAcceptaddress(String acceptaddress)
    {
        this.acceptaddress = acceptaddress;
    }

    public String getScantype()
    {
        return scantype;
    }

    public void setScantype(String scantype)
    {
        this.scantype = scantype;
    }

}
