package com.ai.ecs.ecm.mall.wap.modules.member.address;
import com.ai.ecs.integral.entity.Address85;
import com.ai.ecs.member.api.address.IAddressQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

/**
 * 地址查询 superAddress 为空查询第一级城市，不为空查询superAddress下的二级城市
 * Created by xtf on 2016/9/3.
 */
@Controller
public class AddressQueryController{
    @Autowired
    IAddressQueryService addressQueryService;
    @RequestMapping("addressQuery")
    @ResponseBody
    public Object queryAddress(Address85 address85){
       List<Address85> address = addressQueryService.queryAddressBySuper(address85);
        return address;
    }
}
