package com.ai.ecs.ecm.mall.wap.modules.member;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ai.ecs.ecm.mall.wap.platform.annotation.RefreshCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.annotation.VerifyCSRFToken;
import com.ai.ecs.ecm.mall.wap.platform.utils.TriDes;
import com.ai.ecs.ecm.mall.wap.platform.utils.UtilString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.ecm.mall.wap.modules.BaseController;
import com.ai.ecs.ecm.mall.wap.modules.member.vo.MemberAddressResult;
import com.ai.ecs.ecm.mall.wap.platform.utils.ArrayUtils;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.member.api.IMemberAddressService;
import com.ai.ecs.member.entity.MemberAddress;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.member.entity.ThirdLevelAddress;

@Controller
@RequestMapping("memberAddress")
public class MemberAddressController extends BaseController
{
    @Autowired
    IMemberAddressService memberAddressService;

    private static String keyStr = "ABCDEFGHIJKLMNOP" + "QRSTUVWXYZabcdef"
            + "ghijklmnopqrstuv" + "wxyz0123456789+/" + "=";
    
    @RequestMapping(value = "/toMemberAddressList", method = RequestMethod.GET)
    public String toMemberAddressList(HttpServletRequest request,
            HttpServletResponse response, Model model)
    {
        MemberVo member = UserUtils.getLoginUser(request);
        if (member != null)
        {
//        	String random_form = request.getParameter("random_form");
//			String random_session = (String) request.getSession().getAttribute(
//					"random_session");
//			if (random_form != null && random_session != null
//					&& random_form.equalsIgnoreCase(random_session)) {
            List<MemberAddress> addressList = memberAddressService
                    .getAddrs(member.getMemberLogin().getMemberId());
            if (!CollectionUtils.isEmpty(addressList))
            {
                LinkedList<MemberAddress> addressListNew=new LinkedList<MemberAddress>();
                for(MemberAddress addr:addressList){
                    addr.setMemberRecipientProvince(ArrayUtils.lastNotNull(addr.getMemberRecipientProvince().split(":")));
                    addr.setMemberRecipientCity(ArrayUtils.lastNotNull(addr.getMemberRecipientCity().split(":")));
                    addr.setMemberRecipientCounty(ArrayUtils.lastNotNull(addr.getMemberRecipientCounty().split(":")));
                    if(addr.getMemberRecipientPhone().length() == 11){
                        addr.setMemberRecipientPhone(addr.getMemberRecipientPhone().replace(addr.getMemberRecipientPhone().substring(3, 7), "****"));
                    }
                    if ("Y".equals(addr.getMemberIsDefault())) {
                        addressListNew.addFirst(addr);
                    } else {
                        addressListNew.add(addr);
                    }
                }
                
                request.setAttribute("addressList", addressListNew);
            }
//			} else {
//				return "警告提醒：非法访问限制！";
//			}
        }
        return "web/member/memberAddressList";
    }


    @RefreshCSRFToken
    @RequestMapping(value = "/toUpdateMemberAddress", method = RequestMethod.GET)
    public String toUpdateMemberAddress(HttpServletRequest request,
            HttpServletResponse response, Long addrId) throws Exception
    {
        MemberAddress memberAddress = memberAddressService.getAddr(addrId);
        MemberVo member = UserUtils.getLoginUser(request);
        if (!member.getMemberLogin().getMemberId().equals(memberAddress
				.getMemberId())) {
        	throw new Exception("非法访问限制！");
		}
        memberAddress.setMemberRecipientProvince(ArrayUtils.getFirst(memberAddress.getMemberRecipientProvince().split(":")));
        memberAddress.setMemberRecipientCity(ArrayUtils.getFirst(memberAddress.getMemberRecipientCity().split(":")));
        memberAddress.setMemberRecipientCounty(ArrayUtils.getFirst(memberAddress.getMemberRecipientCounty().split(":")));
        List<ThirdLevelAddress> addrParent = memberAddressService.getParents();
        List<ThirdLevelAddress> citys=  memberAddressService.getChildrensByPId(memberAddress.getMemberRecipientProvince());
        List<ThirdLevelAddress> countys=  memberAddressService.getChildrensByPId(memberAddress.getMemberRecipientCity());
        if(memberAddress.getMemberRecipientPhone().length() == 11){
            memberAddress.setMemberRecipientPhone(memberAddress.getMemberRecipientPhone().replace(memberAddress.getMemberRecipientPhone().substring(3, 7), "****"));
        }
        request.setAttribute("citys", citys);
        request.setAttribute("countys", countys);
        request.setAttribute("memberAddress", memberAddress);
        request.setAttribute("addrParent", addrParent);
        return "web/member/updateMemberAddress";
    }

    @VerifyCSRFToken
    @RequestMapping(value = "/updateMemberAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String updateMemberAddress(HttpServletRequest request,
            HttpServletResponse response, MemberAddress addr)
    {
        MemberVo member = UserUtils.getLoginUser(request);
        if (member != null)
        {
             addr.setMemberRecipientName(UtilString.clearXss(TriDes.getInstance()
                    .strDec(addr.getMemberRecipientName(), keyStr, null, null)));
             String phone = UtilString.clearXss(TriDes.getInstance()
                             .strDec(addr.getMemberRecipientPhone(), keyStr, null, null));
             if(phone.contains("*")){
                 addr.setMemberRecipientPhone(null);
             }else{
                 addr.setMemberRecipientPhone(phone);
             }
            addr.setMemberRecipientAddress(UtilString.clearXss(TriDes.getInstance()
                    .strDec(addr.getMemberRecipientAddress(), keyStr, null, null)));

            
            ThirdLevelAddress province = memberAddressService.getById(Integer
                    .parseInt(addr.getMemberRecipientProvince()));
            ThirdLevelAddress city = memberAddressService.getById(Integer
                    .parseInt(addr.getMemberRecipientCity()));
            ThirdLevelAddress region = memberAddressService.getById(Integer
                    .parseInt(addr.getMemberRecipientCounty()));
            addr.setMemberRecipientProvince(province.getOrgId() + ":"
                    + province.getOrgName());
            addr.setMemberRecipientCity(city.getOrgId() + ":"
                    + city.getOrgName());
            addr.setMemberRecipientCounty(region.getOrgId() + ":"
                    + region.getOrgName());
            addr.setMemberId(member.getMemberLogin().getMemberId());
            MemberAddress address = memberAddressService.updateAddr(addr);
            if (address == null)
            {
                return "修改失败";
            }
        }
        else
        {
            return "请先登录！";
        }
        return "success";
    }

    @RequestMapping(value = "/deleteMemberAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String deleteMemberAddress(HttpServletRequest request,
            HttpServletResponse response, Long addrId) throws Exception
    {
    	MemberVo member = UserUtils.getLoginUser(request);
		MemberAddress memberAddress = memberAddressService.getAddr(addrId);
		 if (!member.getMemberLogin().getMemberId().equals(memberAddress
					.getMemberId())) {
			 throw new Exception("非法访问限制！");
			}
        memberAddressService.delAddr(addrId);
        return "success";
    }

    @RequestMapping(value = "/setDefAddr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String setDefAddr(HttpServletRequest request,
            HttpServletResponse response, Long addrId)
    {
        MemberVo member = UserUtils.getLoginUser(request);
        if (member != null)
        {
            List<MemberAddress>  addressList=memberAddressService.getAddrs(member.getMemberLogin().getMemberId());
            if(!CollectionUtils.isEmpty(addressList)){
                for(MemberAddress memberAddress:addressList) {
                    memberAddressService.setDefAddr(member.getMemberLogin()
                            .getMemberId(), memberAddress.getMemberAddressId(), false);
                }
            }
            memberAddressService.setDefAddr(member.getMemberLogin()
                    .getMemberId(), addrId,true);
        }
        return "success";
    }
    
    @RequestMapping(value = "/cancelDefAddr", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String cancelDefAddr(HttpServletRequest request,
            HttpServletResponse response, Long addrId)
    {
        MemberVo member = UserUtils.getLoginUser(request);
        if (member != null)
        {
            memberAddressService.setDefAddr(member.getMemberLogin()
                    .getMemberId(), addrId,false);
        }
        return "success";
    }

    @RefreshCSRFToken
    @RequestMapping(value = "/toAddMemberAddress", method = RequestMethod.GET)
    public String toAddMemberAddress(HttpServletRequest request,
            HttpServletResponse response)
    {
        List<ThirdLevelAddress> addrParent = memberAddressService.getParents();
        request.setAttribute("addrParent", addrParent);
        return "web/member/addMemberAddress";
    }

    @VerifyCSRFToken
    @RequestMapping(value = "/addMemberAddress", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public String addMemberAddress(HttpServletRequest request,
            HttpServletResponse response, MemberAddress addr) throws Exception
    {
        MemberVo member = UserUtils.getLoginUser(request);
        if (member != null)
        {
            addr.setMemberId(member.getMemberLogin().getMemberId());
            addr.setMemberIsDefault("N");
            ThirdLevelAddress province = memberAddressService.getById(Integer
                    .parseInt(addr.getMemberRecipientProvince()));
            ThirdLevelAddress city = memberAddressService.getById(Integer
                    .parseInt(addr.getMemberRecipientCity()));
            ThirdLevelAddress region = memberAddressService.getById(Integer
                    .parseInt(addr.getMemberRecipientCounty()));
            addr.setMemberRecipientProvince(province.getOrgId() + ":"
                    + province.getOrgName());
            addr.setMemberRecipientCity(city.getOrgId() + ":"
                    + city.getOrgName());
            addr.setMemberRecipientCounty(region.getOrgId() + ":"
                    + region.getOrgName());
            List<MemberAddress> addrlist=memberAddressService.getAddrs(member.getMemberLogin().getMemberId());
            if(addrlist.size()>=10){
            	throw new Exception("每个用户最多录入十个收货地址！");
            }
            MemberAddress address = memberAddressService.saveAddr(addr);
            if (address == null)
            {
                return "添加失败";
            }
        }
        else
        {
            return "请先登录！";
        }
        return "success";
    }

    @RequestMapping(value = "/getChildrenByPid", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Map<String, Object> getChildrenByPid(HttpServletRequest request,
            HttpServletResponse response, String pId)
    {
        Map<String, Object> resultmap = new HashMap<String, Object>();
        List<ThirdLevelAddress> addrChildren = memberAddressService
                .getChildrensByPId(pId);
        List<MemberAddressResult> addrList = new ArrayList<MemberAddressResult>();
        if (!CollectionUtils.isEmpty(addrChildren))
        {
            for (ThirdLevelAddress addr : addrChildren)
            {
                MemberAddressResult res = new MemberAddressResult();
                res.setOrgId(addr.getOrgId());
                res.setOrgName(addr.getOrgName());
                addrList.add(res);
            }
            resultmap.put("flag", "Y");
            resultmap.put("orgList", addrList);
        }
        else
        {
            resultmap.put("flag", "N");
        }
        return resultmap;
    }
}
