package com.ai.ecs.ecm.mall.wap.modules.member.vo;

public class MemberBaseVo {
	private Long memberId;
    private Long memberPhone;
	private boolean loginFromHeApp;  // 是否是和包app登陆
	private boolean bindCard; // 是否绑卡
	private String eparchCode;//地市编码

	public String getEparchCode() {
		return eparchCode;
	}
	public void setEparchCode(String eparchCode) {
		this.eparchCode = eparchCode;
	}
	public Long getMemberId() {
		return memberId;
	}
	public void setMemberId(Long memberId) {
		this.memberId = memberId;
	}
	public Long getMemberPhone() {
		return memberPhone;
	}
	public void setMemberPhone(Long memberPhone) {
		this.memberPhone = memberPhone;
	}
	public boolean isLoginFromHeApp() {
		return loginFromHeApp;
	}

	public void setLoginFromHeApp(boolean loginFromHeApp) {
		this.loginFromHeApp = loginFromHeApp;
	}
	public boolean isBindCard() {
		return bindCard;
	}
	public void setBindCard(boolean bindCard) {
		this.bindCard = bindCard;
	}
}
