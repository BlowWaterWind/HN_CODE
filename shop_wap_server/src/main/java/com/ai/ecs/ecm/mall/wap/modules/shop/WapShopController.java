package com.ai.ecs.ecm.mall.wap.modules.shop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.ai.ecs.common.utils.PropertiesLoader;
import com.ai.ecs.common.web.BaseController;
import com.ai.ecs.ecm.mall.wap.platform.utils.DictUtil;
import com.ai.ecs.ecm.mall.wap.platform.utils.UserUtils;
import com.ai.ecs.ecop.sys.service.SystemService;
import com.ai.ecs.ecsite.modules.sys.entity.User;
import com.ai.ecs.entity.base.Page;
import com.ai.ecs.goods.api.IGoodsQueryService;
import com.ai.ecs.goods.entity.goods.TfGoodsInfo;
import com.ai.ecs.goods.entity.goods.TfGoodsStatic;
import com.ai.ecs.integral.service.UserRatingService;
import com.ai.ecs.member.api.IMemberFavoriteService;
import com.ai.ecs.member.entity.MemberFollow;
import com.ai.ecs.member.entity.MemberLogin;
import com.ai.ecs.member.entity.MemberVo;
import com.ai.ecs.merchant.entity.TfShopOnlineService;
import com.ai.ecs.merchant.shop.ShopManage;
import com.ai.ecs.merchant.shop.pojo.Shop;

/**
 * 店铺controller
 * simple introduction
 *
 * <p>detailed comment
 * @author MaYunLong 2016-3-24
 * @see
 * @since 1.0
 */
@Controller
@RequestMapping("shop")
public class WapShopController extends BaseController {
    @Autowired
    private ShopManage shopManage;
    @Autowired
    private IMemberFavoriteService memberService;
    @Autowired
    private UserRatingService userRatingService;
    @Autowired
    private IGoodsQueryService goodsQueryService;

    private int pageSize = 10;
    private Logger log = Logger.getLogger(WapShopController.class);
    //private static String IMAGE_SERVER_PATH  = new PropertiesLoader("tfs-config.properties").getProperty("tfs.req.url");
    //private static String IMAGE_SERVER_PATH = DictUtil.getDictValue("STATIC_IMAGE_SERVER", "STATIC_SERVER_PATH", "");

    /**
     * 查询店铺列表
     * @param request
     * @param response
     * @param model
     * @return
     */
   @RequestMapping(value = "/shopList")
   public String shopList(HttpServletRequest request, HttpServletResponse response,Model model) {
       String pageNo = request.getParameter("pageNo");
       String sortType = request.getParameter("sortType");
       String sortClounm =request.getParameter("sortClounm");
       String shopName= request.getParameter("shopName");
       if(sortType==null||"".equals(sortType)){
           sortType="ASC";
       }
       if(sortClounm==null||"".equals(sortClounm)){
           sortClounm = "shopid";
       }
       if(pageNo==null||"".equals(pageNo)){
           pageNo = "1";
       }
       int pageNum = Integer.parseInt(pageNo);
       List<Shop> shopList = null;
       Shop shop = new Shop();
       shop.setShopStatusId(Shop.STATUS_NOMAL);
       shop.setSortClounm(sortClounm);
       shop.setSortType(sortType);
       shop.setShopName(shopName);
       Page<Shop> page = shopManage.queryShopForFront(shop, pageNum, pageSize);
       model.addAttribute("page", page);
       model.addAttribute("sortType",sortType);
       model.addAttribute("sortClounm",sortClounm);
       model.addAttribute("shopName",shopName);
       //model.addAttribute("imageServerPath", IMAGE_SERVER_PATH);
       return "web/shop/shopListNew";
   }
   /**
    * 异步查询店铺列表
    * @param request
    * @param response
    * @return
    */
   @RequestMapping(value = "/ajaxShopList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
   @ResponseBody
   public Page<Shop> ajaxShopList(HttpServletRequest request, HttpServletResponse response)
   {
       
       String pageNo = request.getParameter("pageNo");
       if(pageNo==null||"".equals(pageNo)){
           pageNo = "1";
       }
       String sortType = request.getParameter("sortType");
       String sortClounm =request.getParameter("sortClounm");
       String shopName= request.getParameter("shopName");
       if(sortType==null||"".equals(sortType)){
           sortType="ASC";
       }
       if(sortClounm==null||"".equals(sortClounm)){
           sortClounm = "shopid";
       }
       int pageNum = Integer.parseInt(pageNo);
       List<Shop> shopList = null;
       Shop shop = new Shop();
       shop.setShopStatusId(Shop.STATUS_NOMAL);
       shop.setSortClounm(sortClounm);
       shop.setSortType(sortType);
       shop.setShopName(shopName);
       Page<Shop> page = shopManage.queryShopForFront(shop, pageNum, pageSize);
       
       return page;
   }
   /**
    * 查询店铺信息
    * @param request
    * @param response
    * @param model
    * @return
    */
  @RequestMapping(value = "/shopIntroduction")
  public String shopIntroduction(HttpServletRequest request, HttpServletResponse response,Model model) {
      String shopId = request.getParameter("shopId");
      
      List<Shop> shopList = null;
      Shop shop = new Shop();
      shop.setShopId(Long.parseLong(shopId));
      shop.setShopStatusId(Shop.STATUS_NOMAL);
      
      
      Page<Shop> page = shopManage.queryShopForFront(shop, 1, pageSize);
      shopList = page.getList();
      if(shopList!=null&&shopList.size()>0){
          shop = shopList.get(0);
      }
      //查询店铺好评率
      String goodRate = userRatingService.findScore("shop", shopId, "1");
      
      // 查询店铺商品数量
      TfGoodsInfo goodsInfo = new TfGoodsInfo();
      Long shopIdLong  = Long.parseLong(shopId);
      goodsInfo.setShopId(shopIdLong);
      Page<TfGoodsInfo> goodsInfoPage =  goodsQueryService.queryGoodsListByPage(goodsInfo);
      
      model.addAttribute("shopGoodsCount", goodsInfoPage.getCount());
      model.addAttribute("shop", shop);
      //model.addAttribute("imageServerPath", IMAGE_SERVER_PATH);
      model.addAttribute("goodRate", goodRate);
      return "web/shop/shopIntroduction";
  }
  /**
   * 收藏店铺
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/saveMemberFlow", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
  @ResponseBody
  public Map saveMemberFlow(HttpServletRequest request, HttpServletResponse response){
      Map<String,String> map = new HashMap<String,String>();
      map.put("flag", "y");
      map.put("info", "收藏成功！");
      /*User currentUser = systemService.;
      String userId = currentUser.getId();*/
      Long userId = null;
      try
      {
        userId = UserUtils.getLoginUser(request).getMemberLogin().getMemberId();
      }
      catch (Exception e1)
      {
        log.error("获取用户登录信息出错："+e1.toString());
        map.put("flag", "noLogin");
        map.put("info", "请先登录！");
        return map;
        
      }
      String shopName = request.getParameter("shopName");
      String shopUrl = request.getParameter("shopUrl"); 
      String shopId = request.getParameter("shopId");
      String shopLogo = request.getParameter("shopLogo");
     // Long userId = 123l;
      MemberFollow flow = new MemberFollow();
      flow.setMemberId(userId);
      flow.setMemberFollowBusiId(Long.parseLong(shopId));
      flow.setMemberFollowBusiName(shopName);
      flow.setMemberFollowBusiUrl(shopUrl);
      flow.setMemberFollowBusiType("2");
      flow.setMemberFollowBusiImgUrl(shopLogo);
      flow.setMemberFollowBusiShortdes("店铺收藏");
  
      
      
      
      try
      {
        memberService.saveMemberFollow(flow);
      }
      catch (Exception e)
      {
        log.error("收藏店铺失败:"+e.toString());
        map.put("flag", "n");
        map.put("info", "收藏失败！");
        return map;
      }
      return map;
  }
  
  /**
   * 查询店铺 联系号码  当前QQ号码
   * @param request
   * @param response
   * @param model
   * @return
   */
  @RequestMapping(value = "/getShopOnlineServiceList")
  @ResponseBody
  public List<TfShopOnlineService> getShopOnlineServiceList(Shop shop){
     
      TfShopOnlineService tfShopOnlineService= new TfShopOnlineService();
      Page<Shop> page = new Page<Shop>();
     
      List<Shop> shopList = new ArrayList<Shop>();
      shopList.add(shop);
      page.setList(shopList);
      tfShopOnlineService.setShopList(page.getList());
      Page<TfShopOnlineService> pp= shopManage.queryTfShopOnlineServiceList(tfShopOnlineService, 1, 1000);
      
      return pp.getList();
  }
  
  
  /**
   * 查询店铺内商品列表
   * @param request
   * @param response
   * @param model
   * @return
   */
 @RequestMapping(value = "/shopGoodsList")
 public String shopGoodsList(HttpServletRequest request, HttpServletResponse response,Model model) {
     String pageNo = request.getParameter("pageNo");
     String sortType = request.getParameter("sortType");
     String sortClounm =request.getParameter("sortClounm");
     String goodsName= request.getParameter("goodsName");
     String shopId = request.getParameter("shopId");
     if(sortType==null||"".equals(sortType)){
         sortType="ASC";
     }
     if(sortClounm==null||"".equals(sortClounm)){
         sortClounm = "goodsSaleNumOrderBy";
     }
     if(pageNo==null||"".equals(pageNo)){
         pageNo = "1";
     }
     int pageNum = Integer.parseInt(pageNo);
     Page<TfGoodsInfo> p = new Page<TfGoodsInfo>();
     p.setPageNo(pageNum);
     p.setPageSize(pageSize);
     TfGoodsInfo goodsInfo = new TfGoodsInfo();
     goodsInfo.setPage(p);
     goodsInfo.setGoodsName(goodsName);
     goodsInfo.setChnlCode("E007");
     goodsInfo.setKeyWord(goodsName);
     goodsInfo.setShopId(Long.parseLong(shopId));
     if("goodsSaleNumOrderBy".equals(sortClounm)){
         goodsInfo.setGoodsSaleNumOrderBy(sortType);
         goodsInfo.setGoodsSalePriceOrderBy("ASC");
     }else{
         goodsInfo.setGoodsSalePriceOrderBy(sortType);
         goodsInfo.setGoodsSaleNumOrderBy("ASC");
     }
     Page<TfGoodsInfo> page = goodsQueryService.queryGoodsListByPage(goodsInfo);
     staticUrl(page);
     model.addAttribute("page", page);
     model.addAttribute("sortType",sortType);
     model.addAttribute("sortClounm",sortClounm);
     model.addAttribute("goodsName",goodsName);
     model.addAttribute("shopId",shopId);
     //model.addAttribute("imageServerPath", IMAGE_SERVER_PATH);
     return "web/shop/shopGoodsList";
 }
 /**
  * 异步查询店铺商品列表
  * @param request
  * @param response
  * @return
  */
 @RequestMapping(value = "/ajaxShopGoodsList", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
 @ResponseBody
 public Page<TfGoodsInfo> ajaxShopGoodsList(HttpServletRequest request, HttpServletResponse response)
 {
     
     String pageNo = request.getParameter("pageNo");
     if(pageNo==null||"".equals(pageNo)){
         pageNo = "1";
     }
     String sortType = request.getParameter("sortType");
     String sortClounm =request.getParameter("sortClounm");
     String shopName= request.getParameter("shopName");
     String goodsName= request.getParameter("goodsName");
     String shopId = request.getParameter("shopId");
     if(sortType==null||"".equals(sortType)){
         sortType="DESC";
     }
     if(sortClounm==null||"".equals(sortClounm)){
         sortClounm = "goodsSaleNumOrderBy";
     }
     int pageNum = Integer.parseInt(pageNo);
     Page<TfGoodsInfo> p = new Page<TfGoodsInfo>();
     p.setPageNo(pageNum);
     p.setPageSize(pageSize);
     TfGoodsInfo goodsInfo = new TfGoodsInfo();
     goodsInfo.setPage(p);
     goodsInfo.setGoodsName(goodsName);
     goodsInfo.setChnlCode("E007");
     goodsInfo.setKeyWord(goodsName);
     goodsInfo.setShopId(Long.parseLong(shopId));
     if("goodsSaleNumOrderBy".equals(sortClounm)){
         goodsInfo.setGoodsSaleNumOrderBy(sortType);
         goodsInfo.setGoodsSalePriceOrderBy("ASC");
     }else{
         goodsInfo.setGoodsSalePriceOrderBy(sortType);
         goodsInfo.setGoodsSaleNumOrderBy("ASC");
     }
     Page<TfGoodsInfo> page = goodsQueryService.queryGoodsListByPage(goodsInfo);
     staticUrl(page);
     return page;
 }
 private void staticUrl(Page<TfGoodsInfo> p){
     List<TfGoodsInfo> list = p.getList();
    
     for(TfGoodsInfo g:list){
         if(g.getGoodsStaticList()!=null&&g.getGoodsStaticList().size()>0){
             for(TfGoodsStatic s:g.getGoodsStaticList()){
                 if("0".equals(s.getGoodsStaticDefault())){
                     g.setGoodsStaticUrl(s.getGoodsStaticUrl());
                 }
             }
         }
     }
 }
}
