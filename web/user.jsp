<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>个人中心</title>
<link rel="icon" href="img/icon/favicon.ico" type="image/x-icon">
<meta name="renderer" content="webkit">
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
<link rel="stylesheet" href="layui/css/layui.css" media="all">
<link rel="stylesheet" href="css/user.css" />
</head>

<body>
	<!-- 头部 -->
	<div class="layui-container">
		<div class="layui-row">
			<div class="layui-col-xs7 layui-col-lg4">
				<a href="index.jsp" target="_blank"><img src="img/logo.png"
					style="width: 100%;" /></a>
			</div>
			<div class="layui-col-xs4 layui-col-lg8 logo">
				<p>个人中心</p>
			</div>
		</div>
	</div>
	<hr class="layui-bg-blue">
	<div class="layui-container">
		<!--页面导航-->
		<div class="bread layui-row">
			<span class="layui-breadcrumb" lay-separator=">"> <a
				href="${pageContext.request.contextPath}/index.jsp">主页</a> <a id="a">个人中心</a>
			</span>
		</div>
		<!--个人页-->
		<div class="layui-tab" lay-filter="tab">
			<ul class="layui-tab-title">
				<li class="layui-this">个人信息</li>
				<li>消费记录</li>
			</ul>
			<div class="layui-tab-content">
				<div class="layui-tab-item layui-show">
					<div class="layui-row info">
						<div class="layui-col-lg1 layui-col-xs2 ">用户名：</div>
						<div class="layui-col-lg2 layui-col-xs4">${user.username}
							<c:if test="${user.vip == 1 }">
								<img src="./img/VIP.png">
							</c:if>
						</div>
						<div class="layui-col-lg1 layui-col-xs2"></div>
						<div class="layui-col-lg2 layui-col-xs3">
							<c:if test="${user.vip == 2 }">
								<a data-method="offset" data-type="auto" id="vip">[点击成为VIP]</a>
							</c:if>
						</div>
					</div>
					<div class="layui-row info">
						<div class="layui-col-lg1 layui-col-xs2">性别：</div>
						<div class="layui-col-lg2 layui-col-xs2">
							<c:if test="${user.sex == 1}">男</c:if>
							<c:if test="${user.sex == 0}">女</c:if>
						</div>
					</div>
					<div class="layui-row info">
						<div class="layui-col-lg1 layui-col-xs2">邮箱：</div>
						<div class="layui-col-lg2 layui-col-xs6">${user.email}</div>
					</div>
					<div class="layui-row info">
						<div class="layui-col-lg1 layui-col-xs3">联系电话：</div>
						<div class="layui-col-lg2 layui-col-xs6">${user.telephone}</div>
					</div>
					<div class="layui-row info">
						<div class="layui-col-lg1 layui-col-xs2">余额：</div>
						<div class="layui-col-lg2 layui-col-xs3">
							<fmt:formatNumber value="${user.money}" pattern="0.00" />
							元
						</div>
					</div>
				</div>
				<div class="layui-tab-item">
					<div id="date"></div>
					<div class="layui-row">
						<div id="laypage"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="layui-fluid layui-bg-cyan"
		style="height: 40px; line-height: 50px; text-align: center;">
		<h3>Copyright © 2018&nbsp;&nbsp;纯袖科技&nbsp;&nbsp;渝ICP备17004144号</h3>
	</div>
	<!--
			layJS
        -->
	<script type="text/javascript" src="layui/layui.js"></script>
	<script type="text/javascript" src="jQuery/jquery3.3.1.js"></script>
	<script>
	var index;
		layui.use([ 'element', 'laypage' ], function() {
			var element = layui.element, laypage = layui.laypage;
			element.on('tab(tab)', function(data) {
				//console.log(data.index); //得到当前Tab的所在下标
				if (data.index == 1) {
					document.getElementById("a").innerHTML = '消费记录';
					laypage.render({
						elem : 'laypage',
						count :count(),
						limit : 5,
						prev : '<i class="layui-icon">&#xe603;</i>',
						next : '<i class="layui-icon">&#xe602;</i>',
						jump : function(obj,first) {
							if(!first){
								$.ajax({
									type : "POST",
									data : {
										"method" : "layuiPage",
										"curr" : obj.curr
									},
									url : "./UserServlet",
									async:false,
									success : function(data) {
									 json = JSON.parse(data);
									 if(json.count == 0){
											return;
										}
									 document.getElementById("date").innerHTML ="";
										for(var i in json.data) {
											var html = document
												.getElementById("date").innerHTML;
											document.getElementById("date").innerHTML = html +
												"<div class='record'>" +
												"<p>时间:" +
												json.data[i].time +
												"</p><div class='layui-col-lg11 layui-col-xs9'>" +
												"购买了" +
												json.data[i].name +
												"</div><div class='layui-col-lg1 layui-col-xs1'>￥" +
												json.data[i].price +
												"</div><br /><hr class='layui-bg-black'></div>"
										} 
									}
								})
							}else{
								if(index == 0){
						document.getElementById("date").innerHTML ="<div class='record'>" +
									"<div class='layui-col-lg2 layui-col-xs9'><h2>暂无记录</h2></div>";
									$("#laypage").hide();
									return;
								}
								 document.getElementById("date").innerHTML ="";
									for(var i in index.data) {
										var html = document.getElementById("date").innerHTML;
										document.getElementById("date").innerHTML = html +
											"<div class='record'>" +
											"<p>时间:" +
											index.data[i].time +
											"</p><div class='layui-col-lg11 layui-col-xs9'>" +
											"购买了" +
											index.data[i].name +
											"</div><div class='layui-col-lg1 layui-col-xs1'>￥" +
											index.data[i].price +
											"</div><br /><hr class='layui-bg-black'></div>"
									} 
							}
						},
					});

				} else {
					document.getElementById("a").innerHTML = '个人中心';
				}
			});
		});
		layui.use('layer', function() {
			var layer = layui.layer, $ = layui.jquery;

			var open = function() {
				layer.open({
					type : 2,
					title : '成为vip',
					content : './vip.html',
					skin : 'layui-layer-molv',
					offset : 'auto',
					area : [ '90%', '55%' ],
					anim : 5
				});
			};
			var pc = function() {
				layer.open({
					type : 2,
					title : '成为vip',
					content : './vip.html',
					skin : 'layui-layer-molv',
					offset : 'auto',
					area : [ '40%', '50%' ],
					anim : 5,
					cancel : function() {
						var flag = localStorage.getItem('flag');
						if (flag || flag == null) {
							layer.msg('用户取消支付');
							localStorage.removeItem('flag');
						}
					}
				});
			}
			$('#vip').on('click', function() {
				var height = $(window).width();
				if (height > 600) {
					pc();
				} else {
					open();
				}
			});
		});
	</script>
	<script type="text/javascript">
		function count() {
			var json;
			$.ajax({
				type : "POST",
				data : {
					"method" : "layuiPage",
					"curr" : 1
				},
				url : "./UserServlet",
				async:false,
				success : function(data) {
				 json = JSON.parse(data);
				if(json.count == 0){
					$("#data").text("暂无记录");
					index = 0;
					return;
				}
				 index = json;
					for(var i in json.data) {
						var html = document.getElementById("date").innerHTML;
						document.getElementById("date").innerHTML = html +
							"<div class='record'>" +
							"<p>时间:" +
							json.data[i].time +
							"</p><div class='layui-col-lg11 layui-col-xs9'>" +
							"购买了" +
							json.data[i].name +
							"</div><div class='layui-col-lg1 layui-col-xs1'>￥" +
							json.data[i].price +
							"</div><br /><hr class='layui-bg-black'></div>"
					} 
				}
			})
			return json.count
		}
	</script>
</body>

</html>