package com.ai.ecs.ecm.mall.wap.modules.member.vo;

import java.io.Serializable;
/**
 * 会员信息复合实体
 * @author dyy 2016年2月26日
 * @see
 * @since 1.0
 */
public class MemberSsoVo  implements Serializable {
    
    private static final long serialVersionUID = -3903889015045947429L;

    private String memberId;

    private String memberLogingName;

    private String memberEmail;

    private String memberPhone;

    private String memberTypeId;

    private String memberStatusId;

    private String memberRealname;

    private String communityId;
//    @JsonProperty("CUST_NAME")
    private String memberNickname;
//    @JsonProperty("ACCT_ID")
    private String acctId;

    private String acctTag;

    private String assureCustId;

    private String assureDate;

    private String assureName;

    private String assureType;

    private String assureTypeCode;

    private String bank;

    private String bankAcctNo;

    private String bankCode;
    
    private String basicCreditValue;
    
    private String birthday;
    
    private String brand;
    
    private String brandCode;
    
    private String characterType;
    
    private String characterTypeCode;
    
    private String cityCode;
    
    private String cityName;
    
    private String classId;
    
    private String classId2;
    
    private String className;
    
    private String clientInfo1;
    
    private String clientInfo2;
    
    private String clientInfo5;
    
    private String contact;
    
    private String contactType;
    
    private String contactTypeCode;
    
    private String contractNo;
    
    private String creditClass;//用户星级评分
    
    private String creditValue;
    
    private String custId;
    
    private String custManagerId;
    
    private String custName;
    
    private String custPasswd;
    
    private String custState;
    
    private String custType;
    
    private String debutyCode;
    
    private String debutyUserId;
    
    private String depositPriorRuleId;
    
    private String destroyTime;
    
    private String developCityCode;
    
    private String developDate;
    
    private String developDepartId;
    
    private String developEparchyCode;
    
    private String developNo;
    
    private String developStaffId;
    
    private String educateDegree;
    
    private String educateDegreeCode;
    
    private String email;
    
    private String eparchyCode;
    
    private String faxNbr;
    
    private String firstCallTime;
    
    private String folk;
    
    private String folkCode;
    
    private String groupBrand;
    
    private String homeAddress;
    
    private String inDate;
    
    private String inDepartId;
    
    private String inStaffId;
    
    private String itemPriorRuleId;
    
    private String job;
    
    private String jobType;
    
    
    private String jobTypeCode;
    private String languageCode;
    private String languageName;
    private String lastStopTime;
    private String linkPhone;
    private String localNativeCode;
    private String localNativeName;
    private String marriage;
    private String mputeDate;
    private String mputeMonthFee;
    private String nationalityCode;
    private String nationalityName;
    private String openDate;
    private String openLimit;
    private String openMode;
    private String payMode;
    private String payModeCode;
    private String payName;
    private String phone;
    private String population;
    private String postAddress;
    private String postCode;
    private String prepayTag;
    private String productId;
    private String productName;
    private String psptAddr;
    private String psptEndDate;
    private String psptId;
    private String psptType;
    private String pspt_type_code;
    private String puk;
    private String religionCode;
    private String religionName;
    private String remark;
    private String removeCityCode;
    private String removeDepartId;
    private String removeEparchyCode;//地市编码
    private String removeReason;
    private String removeReasonCode;
    private String removeTag;
    private String revenueLevel;
    private String revenueLevelCode;
    private String rsrvStr1;
    private String rsrvStr10;
    private String rsrvStr2;
    private String rsrvStr3;
    private String rsrvStr4;
    private String rsrvStr5;
    private String rsrvStr6;
    private String rsrvStr7;
    private String rsrvStr8;
    private String rsrvStr9;
    private String scoreValue;
    private String serialNumber;
    private String serialNumberB;
    private String sex;
    private String simCardNo;
    private String updateTime;
    private String usecustId;
    private String userId;
    private String userPasswd;
    private String userStateCodeset;
    private String userType;
    private String userTypeCode;
    private String vipManagerName;
    private String vipManagerTag;
    private String vipNo;
    private String vipTag;
    private String vipTypeCode;
    private String vpmnGroupId;
    private String vpmnGroupName;
    private String webuserId;
    private String webPasswd;
    private String workDepart;
    private String workName;
    private String acctTagName;
    private String xCustState;
    private String xCustType;
    private String developCityName;
    private String developDepartName;
    private String developEparchyName;
    private String developStaffName;
    private String eparchyName;//地市名称
    private String inDepartName;
    private String inStaffName;
    private String xMarriage;
    private String openModeName;
    private String pagincount;
    private String pagincurrent;
    private String paginsize;
    private String prepayTagName;
    private String removeCityName;
    private String removeDepartName;
    private String removeEparchyName;
    private String removeTagName;
    private String resultCode;
    private String resultCount;
    private String resultinfo;
    private String resultsize;
    private String rspcode;
    private String rspdesc;
    private String rsptype;
    private String xSex;
    private String svcstateExplain;
    private String preDestroyTime;
    private String productExplain;

    /**
     * 会员ID
     */
    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    /**
     * 会员登录名
     */
    public String getMemberLogingName() {
        return memberLogingName;
    }

    public void setMemberLogingName(String memberLogingName) {
        this.memberLogingName = memberLogingName;
    }

    /**
     * 会员绑定邮箱
     */
    public String getMemberEmail() {
        return memberEmail;
    }

    public void setMemberEmail(String memberEmail) {
        this.memberEmail = memberEmail;
    }

    /**
     * 会员绑定手机号
     */
    public String getMemberPhone() {
        return memberPhone;
    }

    public void setMemberPhone(String memberPhone) {
        this.memberPhone = memberPhone;
    }

    /**
     * 会员类型ID
     */
    public String getMemberTypeId() {
        return memberTypeId;
    }

    public void setMemberTypeId(String memberTypeId) {
        this.memberTypeId = memberTypeId;
    }

    /**
     * 会员状态ID
     */
    public String getMemberStatusId() {
        return memberStatusId;
    }

    public void setMemberStatusId(String memberStatusId) {
        this.memberStatusId = memberStatusId;
    }

    /**
     * 会员真实姓名
     */
    public String getMemberRealname() {
        return memberRealname;
    }

    public void setMemberRealname(String memberRealname) {
        this.memberRealname = memberRealname;
    }

    /**
     * 会员昵称
     */
    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public String getAcctId() {
        return acctId;
    }

    public void setAcctId(String acctId) {
        this.acctId = acctId;
    }

    public String getAcctTag() {
        return acctTag;
    }

    public void setAcctTag(String acctTag) {
        this.acctTag = acctTag;
    }

    public String getAssureCustId() {
        return assureCustId;
    }

    public void setAssureCustId(String assureCustId) {
        this.assureCustId = assureCustId;
    }

    public String getAssureDate() {
        return assureDate;
    }

    public void setAssureDate(String assureDate) {
        this.assureDate = assureDate;
    }

    public String getAssureName() {
        return assureName;
    }

    public void setAssureName(String assureName) {
        this.assureName = assureName;
    }

    public String getAssureType() {
        return assureType;
    }

    public void setAssureType(String assureType) {
        this.assureType = assureType;
    }

    public String getAssureTypeCode() {
        return assureTypeCode;
    }

    public void setAssureTypeCode(String assureTypeCode) {
        this.assureTypeCode = assureTypeCode;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getBankAcctNo() {
        return bankAcctNo;
    }

    public void setBankAcctNo(String bankAcctNo) {
        this.bankAcctNo = bankAcctNo;
    }

    public String getBankCode() {
        return bankCode;
    }

    public void setBankCode(String bankCode) {
        this.bankCode = bankCode;
    }

    public String getBasicCreditValue() {
        return basicCreditValue;
    }

    public void setBasicCreditValue(String basicCreditValue) {
        this.basicCreditValue = basicCreditValue;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getBrandCode() {
        return brandCode;
    }

    public void setBrandCode(String brandCode) {
        this.brandCode = brandCode;
    }

    public String getCharacterType() {
        return characterType;
    }

    public void setCharacterType(String characterType) {
        this.characterType = characterType;
    }

    public String getCharacterTypeCode() {
        return characterTypeCode;
    }

    public void setCharacterTypeCode(String characterTypeCode) {
        this.characterTypeCode = characterTypeCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public String getClassId2() {
        return classId2;
    }

    public void setClassId2(String classId2) {
        this.classId2 = classId2;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getClientInfo1() {
        return clientInfo1;
    }

    public void setClientInfo1(String clientInfo1) {
        this.clientInfo1 = clientInfo1;
    }

    public String getClientInfo2() {
        return clientInfo2;
    }

    public void setClientInfo2(String clientInfo2) {
        this.clientInfo2 = clientInfo2;
    }

    public String getClientInfo5() {
        return clientInfo5;
    }

    public void setClientInfo5(String clientInfo5) {
        this.clientInfo5 = clientInfo5;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getContactTypeCode() {
        return contactTypeCode;
    }

    public void setContactTypeCode(String contactTypeCode) {
        this.contactTypeCode = contactTypeCode;
    }

    public String getContractNo() {
        return contractNo;
    }

    public void setContractNo(String contractNo) {
        this.contractNo = contractNo;
    }

    public String getCreditClass() {
        return creditClass;
    }

    public void setCreditClass(String creditClass) {
        this.creditClass = creditClass;
    }

    public String getCreditValue() {
        return creditValue;
    }

    public void setCreditValue(String creditValue) {
        this.creditValue = creditValue;
    }

    public String getCustId() {
        return custId;
    }

    public void setCustId(String custId) {
        this.custId = custId;
    }

    public String getCustManagerId() {
        return custManagerId;
    }

    public void setCustManagerId(String custManagerId) {
        this.custManagerId = custManagerId;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getCustPasswd() {
        return custPasswd;
    }

    public void setCustPasswd(String custPasswd) {
        this.custPasswd = custPasswd;
    }

    public String getCustState() {
        return custState;
    }

    public void setCustState(String custState) {
        this.custState = custState;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getDebutyCode() {
        return debutyCode;
    }

    public void setDebutyCode(String debutyCode) {
        this.debutyCode = debutyCode;
    }

    public String getDebutyUserId() {
        return debutyUserId;
    }

    public void setDebutyUserId(String debutyUserId) {
        this.debutyUserId = debutyUserId;
    }

    public String getDepositPriorRuleId() {
        return depositPriorRuleId;
    }

    public void setDepositPriorRuleId(String depositPriorRuleId) {
        this.depositPriorRuleId = depositPriorRuleId;
    }

    public String getDestroyTime() {
        return destroyTime;
    }

    public void setDestroyTime(String destroyTime) {
        this.destroyTime = destroyTime;
    }

    public String getDevelopCityCode() {
        return developCityCode;
    }

    public void setDevelopCityCode(String developCityCode) {
        this.developCityCode = developCityCode;
    }

    public String getDevelopDate() {
        return developDate;
    }

    public void setDevelopDate(String developDate) {
        this.developDate = developDate;
    }

    public String getDevelopDepartId() {
        return developDepartId;
    }

    public void setDevelopDepartId(String developDepartId) {
        this.developDepartId = developDepartId;
    }

    public String getDevelopEparchyCode() {
        return developEparchyCode;
    }

    public void setDevelopEparchyCode(String developEparchyCode) {
        this.developEparchyCode = developEparchyCode;
    }

    public String getDevelopNo() {
        return developNo;
    }

    public void setDevelopNo(String developNo) {
        this.developNo = developNo;
    }

    public String getDevelopStaffId() {
        return developStaffId;
    }

    public void setDevelopStaffId(String developStaffId) {
        this.developStaffId = developStaffId;
    }

    public String getEducateDegree() {
        return educateDegree;
    }

    public void setEducateDegree(String educateDegree) {
        this.educateDegree = educateDegree;
    }

    public String getEducateDegreeCode() {
        return educateDegreeCode;
    }

    public void setEducateDegreeCode(String educateDegreeCode) {
        this.educateDegreeCode = educateDegreeCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEparchyCode() {
        return eparchyCode;
    }

    public void setEparchyCode(String eparchyCode) {
        this.eparchyCode = eparchyCode;
    }

    public String getFaxNbr() {
        return faxNbr;
    }

    public void setFaxNbr(String faxNbr) {
        this.faxNbr = faxNbr;
    }

    public String getFirstCallTime() {
        return firstCallTime;
    }

    public void setFirstCallTime(String firstCallTime) {
        this.firstCallTime = firstCallTime;
    }

    public String getFolk() {
        return folk;
    }

    public void setFolk(String folk) {
        this.folk = folk;
    }

    public String getFolkCode() {
        return folkCode;
    }

    public void setFolkCode(String folkCode) {
        this.folkCode = folkCode;
    }

    public String getGroupBrand() {
        return groupBrand;
    }

    public void setGroupBrand(String groupBrand) {
        this.groupBrand = groupBrand;
    }

    public String getHomeAddress() {
        return homeAddress;
    }

    public void setHomeAddress(String homeAddress) {
        this.homeAddress = homeAddress;
    }

    public String getInDate() {
        return inDate;
    }

    public void setInDate(String inDate) {
        this.inDate = inDate;
    }

    public String getInDepartId() {
        return inDepartId;
    }

    public void setInDepartId(String inDepartId) {
        this.inDepartId = inDepartId;
    }

    public String getInStaffId() {
        return inStaffId;
    }

    public void setInStaffId(String inStaffId) {
        this.inStaffId = inStaffId;
    }

    public String getItemPriorRuleId() {
        return itemPriorRuleId;
    }

    public void setItemPriorRuleId(String itemPriorRuleId) {
        this.itemPriorRuleId = itemPriorRuleId;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getJobTypeCode() {
        return jobTypeCode;
    }

    public void setJobTypeCode(String jobTypeCode) {
        this.jobTypeCode = jobTypeCode;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public String getLastStopTime() {
        return lastStopTime;
    }

    public void setLastStopTime(String lastStopTime) {
        this.lastStopTime = lastStopTime;
    }

    public String getLinkPhone() {
        return linkPhone;
    }

    public void setLinkPhone(String linkPhone) {
        this.linkPhone = linkPhone;
    }

    public String getLocalNativeCode() {
        return localNativeCode;
    }

    public void setLocalNativeCode(String localNativeCode) {
        this.localNativeCode = localNativeCode;
    }

    public String getLocalNativeName() {
        return localNativeName;
    }

    public void setLocalNativeName(String localNativeName) {
        this.localNativeName = localNativeName;
    }

    public String getMarriage() {
        return marriage;
    }

    public void setMarriage(String marriage) {
        this.marriage = marriage;
    }

    public String getMputeDate() {
        return mputeDate;
    }

    public void setMputeDate(String mputeDate) {
        this.mputeDate = mputeDate;
    }

    public String getMputeMonthFee() {
        return mputeMonthFee;
    }

    public void setMputeMonthFee(String mputeMonthFee) {
        this.mputeMonthFee = mputeMonthFee;
    }

    public String getNationalityCode() {
        return nationalityCode;
    }

    public void setNationalityCode(String nationalityCode) {
        this.nationalityCode = nationalityCode;
    }

    public String getNationalityName() {
        return nationalityName;
    }

    public void setNationalityName(String nationalityName) {
        this.nationalityName = nationalityName;
    }

    public String getOpenDate() {
        return openDate;
    }

    public void setOpenDate(String openDate) {
        this.openDate = openDate;
    }

    public String getOpenLimit() {
        return openLimit;
    }

    public void setOpenLimit(String openLimit) {
        this.openLimit = openLimit;
    }

    public String getOpenMode() {
        return openMode;
    }

    public void setOpenMode(String openMode) {
        this.openMode = openMode;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getPayModeCode() {
        return payModeCode;
    }

    public void setPayModeCode(String payModeCode) {
        this.payModeCode = payModeCode;
    }

    public String getPayName() {
        return payName;
    }

    public void setPayName(String payName) {
        this.payName = payName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPopulation() {
        return population;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public String getPostAddress() {
        return postAddress;
    }

    public void setPostAddress(String postAddress) {
        this.postAddress = postAddress;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getPrepayTag() {
        return prepayTag;
    }

    public void setPrepayTag(String prepayTag) {
        this.prepayTag = prepayTag;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getPsptAddr() {
        return psptAddr;
    }

    public void setPsptAddr(String psptAddr) {
        this.psptAddr = psptAddr;
    }

    public String getPsptEndDate() {
        return psptEndDate;
    }

    public void setPsptEndDate(String psptEndDate) {
        this.psptEndDate = psptEndDate;
    }

    public String getPsptId() {
        return psptId;
    }

    public void setPsptId(String psptId) {
        this.psptId = psptId;
    }

    public String getPsptType() {
        return psptType;
    }

    public void setPsptType(String psptType) {
        this.psptType = psptType;
    }

    public String getPspt_type_code() {
        return pspt_type_code;
    }

    public void setPspt_type_code(String pspt_type_code) {
        this.pspt_type_code = pspt_type_code;
    }

    public String getPuk() {
        return puk;
    }

    public void setPuk(String puk) {
        this.puk = puk;
    }

    public String getReligionCode() {
        return religionCode;
    }

    public void setReligionCode(String religionCode) {
        this.religionCode = religionCode;
    }

    public String getReligionName() {
        return religionName;
    }

    public void setReligionName(String religionName) {
        this.religionName = religionName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getRemoveCityCode() {
        return removeCityCode;
    }

    public void setRemoveCityCode(String removeCityCode) {
        this.removeCityCode = removeCityCode;
    }

    public String getRemoveDepartId() {
        return removeDepartId;
    }

    public void setRemoveDepartId(String removeDepartId) {
        this.removeDepartId = removeDepartId;
    }

    public String getRemoveEparchyCode() {
        return removeEparchyCode;
    }

    public void setRemoveEparchyCode(String removeEparchyCode) {
        this.removeEparchyCode = removeEparchyCode;
    }

    public String getRemoveReason() {
        return removeReason;
    }

    public void setRemoveReason(String removeReason) {
        this.removeReason = removeReason;
    }

    public String getRemoveReasonCode() {
        return removeReasonCode;
    }

    public void setRemoveReasonCode(String removeReasonCode) {
        this.removeReasonCode = removeReasonCode;
    }

    public String getRemoveTag() {
        return removeTag;
    }

    public void setRemoveTag(String removeTag) {
        this.removeTag = removeTag;
    }

    public String getRevenueLevel() {
        return revenueLevel;
    }

    public void setRevenueLevel(String revenueLevel) {
        this.revenueLevel = revenueLevel;
    }

    public String getRevenueLevelCode() {
        return revenueLevelCode;
    }

    public void setRevenueLevelCode(String revenueLevelCode) {
        this.revenueLevelCode = revenueLevelCode;
    }

    public String getRsrvStr1() {
        return rsrvStr1;
    }

    public void setRsrvStr1(String rsrvStr1) {
        this.rsrvStr1 = rsrvStr1;
    }

    public String getRsrvStr10() {
        return rsrvStr10;
    }

    public void setRsrvStr10(String rsrvStr10) {
        this.rsrvStr10 = rsrvStr10;
    }

    public String getRsrvStr2() {
        return rsrvStr2;
    }

    public void setRsrvStr2(String rsrvStr2) {
        this.rsrvStr2 = rsrvStr2;
    }

    public String getRsrvStr3() {
        return rsrvStr3;
    }

    public void setRsrvStr3(String rsrvStr3) {
        this.rsrvStr3 = rsrvStr3;
    }

    public String getRsrvStr4() {
        return rsrvStr4;
    }

    public void setRsrvStr4(String rsrvStr4) {
        this.rsrvStr4 = rsrvStr4;
    }

    public String getRsrvStr5() {
        return rsrvStr5;
    }

    public void setRsrvStr5(String rsrvStr5) {
        this.rsrvStr5 = rsrvStr5;
    }

    public String getRsrvStr6() {
        return rsrvStr6;
    }

    public void setRsrvStr6(String rsrvStr6) {
        this.rsrvStr6 = rsrvStr6;
    }

    public String getRsrvStr7() {
        return rsrvStr7;
    }

    public void setRsrvStr7(String rsrvStr7) {
        this.rsrvStr7 = rsrvStr7;
    }

    public String getRsrvStr8() {
        return rsrvStr8;
    }

    public void setRsrvStr8(String rsrvStr8) {
        this.rsrvStr8 = rsrvStr8;
    }

    public String getRsrvStr9() {
        return rsrvStr9;
    }

    public void setRsrvStr9(String rsrvStr9) {
        this.rsrvStr9 = rsrvStr9;
    }

    public String getScoreValue() {
        return scoreValue;
    }

    public void setScoreValue(String scoreValue) {
        this.scoreValue = scoreValue;
    }

    public String getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getSerialNumberB() {
        return serialNumberB;
    }

    public void setSerialNumberB(String serialNumberB) {
        this.serialNumberB = serialNumberB;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getSimCardNo() {
        return simCardNo;
    }

    public void setSimCardNo(String simCardNo) {
        this.simCardNo = simCardNo;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getUsecustId() {
        return usecustId;
    }

    public void setUsecustId(String usecustId) {
        this.usecustId = usecustId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserPasswd() {
        return userPasswd;
    }

    public void setUserPasswd(String userPasswd) {
        this.userPasswd = userPasswd;
    }

    public String getUserStateCodeset() {
        return userStateCodeset;
    }

    public void setUserStateCodeset(String userStateCodeset) {
        this.userStateCodeset = userStateCodeset;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getUserTypeCode() {
        return userTypeCode;
    }

    public void setUserTypeCode(String userTypeCode) {
        this.userTypeCode = userTypeCode;
    }

    public String getVipManagerName() {
        return vipManagerName;
    }

    public void setVipManagerName(String vipManagerName) {
        this.vipManagerName = vipManagerName;
    }

    public String getVipManagerTag() {
        return vipManagerTag;
    }

    public void setVipManagerTag(String vipManagerTag) {
        this.vipManagerTag = vipManagerTag;
    }

    public String getVipNo() {
        return vipNo;
    }

    public void setVipNo(String vipNo) {
        this.vipNo = vipNo;
    }

    public String getVipTag() {
        return vipTag;
    }

    public void setVipTag(String vipTag) {
        this.vipTag = vipTag;
    }

    public String getVipTypeCode() {
        return vipTypeCode;
    }

    public void setVipTypeCode(String vipTypeCode) {
        this.vipTypeCode = vipTypeCode;
    }

    public String getVpmnGroupId() {
        return vpmnGroupId;
    }

    public void setVpmnGroupId(String vpmnGroupId) {
        this.vpmnGroupId = vpmnGroupId;
    }

    public String getVpmnGroupName() {
        return vpmnGroupName;
    }

    public void setVpmnGroupName(String vpmnGroupName) {
        this.vpmnGroupName = vpmnGroupName;
    }

    public String getWebuserId() {
        return webuserId;
    }

    public void setWebuserId(String webuserId) {
        this.webuserId = webuserId;
    }

    public String getWebPasswd() {
        return webPasswd;
    }

    public void setWebPasswd(String webPasswd) {
        this.webPasswd = webPasswd;
    }

    public String getWorkDepart() {
        return workDepart;
    }

    public void setWorkDepart(String workDepart) {
        this.workDepart = workDepart;
    }

    public String getWorkName() {
        return workName;
    }

    public void setWorkName(String workName) {
        this.workName = workName;
    }

    public String getAcctTagName() {
        return acctTagName;
    }

    public void setAcctTagName(String acctTagName) {
        this.acctTagName = acctTagName;
    }

    public String getxCustState() {
        return xCustState;
    }

    public void setxCustState(String xCustState) {
        this.xCustState = xCustState;
    }

    public String getxCustType() {
        return xCustType;
    }

    public void setxCustType(String xCustType) {
        this.xCustType = xCustType;
    }

    public String getDevelopCityName() {
        return developCityName;
    }

    public void setDevelopCityName(String developCityName) {
        this.developCityName = developCityName;
    }

    public String getDevelopDepartName() {
        return developDepartName;
    }

    public void setDevelopDepartName(String developDepartName) {
        this.developDepartName = developDepartName;
    }

    public String getDevelopEparchyName() {
        return developEparchyName;
    }

    public void setDevelopEparchyName(String developEparchyName) {
        this.developEparchyName = developEparchyName;
    }

    public String getDevelopStaffName() {
        return developStaffName;
    }

    public void setDevelopStaffName(String developStaffName) {
        this.developStaffName = developStaffName;
    }

    public String getEparchyName() {
        return eparchyName;
    }

    public void setEparchyName(String eparchyName) {
        this.eparchyName = eparchyName;
    }

    public String getInDepartName() {
        return inDepartName;
    }

    public void setInDepartName(String inDepartName) {
        this.inDepartName = inDepartName;
    }

    public String getInStaffName() {
        return inStaffName;
    }

    public void setInStaffName(String inStaffName) {
        this.inStaffName = inStaffName;
    }

    public String getxMarriage() {
        return xMarriage;
    }

    public void setxMarriage(String xMarriage) {
        this.xMarriage = xMarriage;
    }

    public String getOpenModeName() {
        return openModeName;
    }

    public void setOpenModeName(String openModeName) {
        this.openModeName = openModeName;
    }

    public String getPagincount() {
        return pagincount;
    }

    public void setPagincount(String pagincount) {
        this.pagincount = pagincount;
    }

    public String getPagincurrent() {
        return pagincurrent;
    }

    public void setPagincurrent(String pagincurrent) {
        this.pagincurrent = pagincurrent;
    }

    public String getPaginsize() {
        return paginsize;
    }

    public void setPaginsize(String paginsize) {
        this.paginsize = paginsize;
    }

    public String getPrepayTagName() {
        return prepayTagName;
    }

    public void setPrepayTagName(String prepayTagName) {
        this.prepayTagName = prepayTagName;
    }

    public String getRemoveCityName() {
        return removeCityName;
    }

    public void setRemoveCityName(String removeCityName) {
        this.removeCityName = removeCityName;
    }

    public String getRemoveDepartName() {
        return removeDepartName;
    }

    public void setRemoveDepartName(String removeDepartName) {
        this.removeDepartName = removeDepartName;
    }

    public String getRemoveEparchyName() {
        return removeEparchyName;
    }

    public void setRemoveEparchyName(String removeEparchyName) {
        this.removeEparchyName = removeEparchyName;
    }

    public String getRemoveTagName() {
        return removeTagName;
    }

    public void setRemoveTagName(String removeTagName) {
        this.removeTagName = removeTagName;
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultCount() {
        return resultCount;
    }

    public void setResultCount(String resultCount) {
        this.resultCount = resultCount;
    }

    public String getResultinfo() {
        return resultinfo;
    }

    public void setResultinfo(String resultinfo) {
        this.resultinfo = resultinfo;
    }

    public String getResultsize() {
        return resultsize;
    }

    public void setResultsize(String resultsize) {
        this.resultsize = resultsize;
    }

    public String getRspcode() {
        return rspcode;
    }

    public void setRspcode(String rspcode) {
        this.rspcode = rspcode;
    }

    public String getRspdesc() {
        return rspdesc;
    }

    public void setRspdesc(String rspdesc) {
        this.rspdesc = rspdesc;
    }

    public String getRsptype() {
        return rsptype;
    }

    public void setRsptype(String rsptype) {
        this.rsptype = rsptype;
    }

    public String getxSex() {
        return xSex;
    }

    public void setxSex(String xSex) {
        this.xSex = xSex;
    }

    public String getSvcstateExplain() {
        return svcstateExplain;
    }

    public void setSvcstateExplain(String svcstateExplain) {
        this.svcstateExplain = svcstateExplain;
    }

    public String getCommunityId() {
        return communityId;
    }

    public void setCommunityId(String communityId) {
        this.communityId = communityId;
    }

    public String getPreDestroyTime() {
        return preDestroyTime;
    }

    public void setPreDestroyTime(String preDestroyTime) {
        this.preDestroyTime = preDestroyTime;
    }

    public String getProductExplain() {
        return productExplain;
    }

    public void setProductExplain(String productExplain) {
        this.productExplain = productExplain;
    }
}
