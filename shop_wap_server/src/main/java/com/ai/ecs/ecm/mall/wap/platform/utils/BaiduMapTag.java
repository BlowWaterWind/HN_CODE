package com.ai.ecs.ecm.mall.wap.platform.utils;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.SimpleTagSupport;
import java.io.IOException;

/**
 * 百度地图自定义标签
 * Created by think on 2018/2/26.
 */
public class BaiduMapTag extends SimpleTagSupport {
    private String bmapClass;
    private String bmapStyle;
    private String coordinate;
    private String callback;

    @Override
    public void doTag() throws JspException, IOException {
        JspWriter out = getJspContext().getOut();
        StringBuilder html = new StringBuilder();
        html.append("<input type=\"text\" placeholder=\"请输入地址\" style=\""+bmapStyle+"\" id=\"bmapAddress\" class=\""+bmapClass+"\" data-coord=\""+coordinate+"\" callback=\""+callback+"\">");
        html.append("<div class=\"bmap-div\" style=\"display: none;\">");
        html.append("<div id=\"r-result\">");
        html.append("<input type=\"text\" id=\"suggestId\" size=\"20\" value=\"\" callback=\"callBackD\"/>");
        html.append("<a href=\"#\" class=\"icon-search\"></a>");
        html.append("</div>");
        html.append("<div id=\"searchResultPanel\" style=\"border:1px solid #C0C0C0;width:150px;height:auto; display:none;\"></div>");
        html.append(" <div id=\"allmap\"></div>");
        html.append("<div id=\"BmapButton\" class=\"bmap-button\">");
        html.append("<a href=\"javascript:void(0)\" class=\"bmap-cancel\" id=\"bmap-cancel\">取消</a>");
        html.append("<a href=\"javascript:void(0)\" class=\"bmap-comfirm\" id=\"bmap-comfirm\">确定</a>");
        html.append("</div>");
        html.append("</div>");
        out.println(html);
    }

    public String getBmapClass() {
        return bmapClass;
    }

    public void setBmapClass(String bmapClass) {
        this.bmapClass = bmapClass;
    }

    public String getBmapStyle() {
        return bmapStyle;
    }

    public void setBmapStyle(String bmapStyle) {
        this.bmapStyle = bmapStyle;
    }

    public String getCoordinate() {
        return coordinate;
    }

    public void setCoordinate(String coordinate) {
        this.coordinate = coordinate;
    }

    public String getCallback() {
        return callback;
    }

    public void setCallback(String callback) {
        this.callback = callback;
    }
}
