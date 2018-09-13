<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script id="uploadImgTmpl" type="text/html">
	<input type="file" id="up{{index}}" name="up{{index}}" style="display:none" />
	<img id="ImgPr{{index}}" width="40" height="40" onclick="deleteImg('{{index}}')"/>
	</div>
</script>