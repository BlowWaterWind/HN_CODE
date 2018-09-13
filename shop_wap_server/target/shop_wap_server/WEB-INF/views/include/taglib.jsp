<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fns" uri="/WEB-INF/tlds/fns.tld" %>
<%@ taglib prefix="sys" tagdir="/WEB-INF/tags/sys" %>
<c:set var="contextPath" value="${pageContext.request.contextPath}"/>
<c:set var="ctx" value="${pageContext.request.contextPath}${fns:getAdminPath()}"/>
<c:set var="ctxStatic" value="${pageContext.request.contextPath}/static"/>
<%--<c:set var="tfsUrl" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH' , '')}"/>--%>
<c:set var="tfsUrl" value="/res/img/"/>
<c:set var="tfsUrlSql" value="${fns:getDictValue('STATIC_IMAGE_SERVER','STATIC_SERVER_PATH','')}"/>
<c:set var="gUrl" value="${pageContext.request.contextPath}/goods/"/> <%--商品路径--%>
<c:set var="sUrl" value="${pageContext.request.contextPath}/shops/"/> <%--店铺路径--%>