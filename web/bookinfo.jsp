<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>图书页</title>
		<link rel="icon" href="img/icon/favicon.ico" type="image/x-icon">
		<meta name="renderer" content="webkit">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<link rel="stylesheet" href="layui/css/layui.css" media="all">
		<link rel="stylesheet" href="css/bookinfo.css" />
	</head>
	<c:if test="${empty book}">
		<c:redirect url="${pageContext.request.contentType}/404.html" />
	</c:if>

	<body>
		<!-- 头部 -->
		<div class="layui-container">
			<div class="layui-row">
				<div class="layui-col-xs7 layui-col-lg4">
					<a href="index.jsp" target="_blank"><img src="img/logo.png" style="width: 100%;" /></a>
				</div>
				<div class="layui-col-xs4 layui-col-lg8 logo">
					<p>图书详情</p>
				</div>
			</div>
		</div>
		<hr class="layui-bg-blue">
		<div class="img">
			<img src="${book.image}" />
		</div>
		<div class="info">
			<div class="layui-row">
				<div class="layui-col-lg3 layui-col-xs4">书名:</div>
				<div class="layui-col-lg4 layui-col-xs4" id="text">${book.name}</div>
			</div>
			<div class="layui-row">
				<div class="layui-col-lg3 layui-col-xs4">作者:</div>
				<div class="layui-col-lg3 layui-col-xs4" id="text">${book.author}</div>
			</div>
			<div class="layui-row">
				<div class="layui-col-lg3 layui-col-xs4">价格:</div>
				<div class="layui-col-lg3 layui-col-xs3" style="text-decoration: line-through;">${book.price}</div>
				<div class="layui-col-lg3 layui-col-xs4" style="color: red;margin-left: -2%;">VIP:<c:if test="${book.price !=0.0 }"><fmt:formatNumber value="${book.price*0.8}" pattern="#.00" /></c:if><c:if test="${book.price ==0.0 }"><fmt:formatNumber value="${book.price*1.0}" pattern="#" /></c:if>
				</div>
			</div>
			<div class="layui-row">
				<div class="layui-col-lg3 layui-col-xs5">下载量:</div>
				<div class="layui-col-lg2 layui-col-xs4">${book.counts}</div>
			</div>
			<div class="layui-row">
				<div class="layui-col-lg4">详细介绍:</div>
			</div>
			<div class="layui-row">
				<div id="text">${book.content}</div>
			</div>
			<div class="layui-row">
				<button class="layui-btn layui-btn-fluid  layui-btn-normal" id="pay">立即下载</button>
				<p id="tips">*免费图书普通用户仅可下载5次,VIP不限次数,开通VIP还有付费图书专享折扣哦</p>
			</div>
		</div>
		<br />
		<br />
		<div class="layui-fluid layui-bg-black " id="footer">
			<h3>Copyright © 2018&nbsp;&nbsp;纯袖科技&nbsp;&nbsp;渝ICP备17004144号</h3>
		</div>
		<script type="text/javascript" src="layui/layui.js"></script>
		<script>
			layui.use(['jquery', 'layer'], function() {
				var $ = layui.jquery,
					layer = layui.layer;
				$("#pay").on('click', function() {
					$.ajax({
						type: 'POST',
						url: '${pageContext.request.contextPath}/UserServlet',
						data: {
							'method': 'consumption',
							'bid':'${book.bid}',
						},
						success: function(data) {
							var json = JSON.parse(data);
							var result = json.result;
							if(result == 'NOTLOGIN') {
								layer.msg('您未登录');
								setTimeout(function() {
									window.open("login.jsp");
									//window.location.href = 'login.jsp';
								}, 1500)
							} else if(result == 'YES') {
								window.open("./BookServlet?method=downloadBook&bid=${book.bid}");
							} else if(result == 'NOTCOUNT') {
								layer.msg('您的免费次数用完了,快去个人中心开通VIP吧');
							}else if(result == 'NOTMONEY') {
								layer.msg('余额不足,请充值');
							}
						}
					});
				});
			});
		</script>
	</body>

</html>