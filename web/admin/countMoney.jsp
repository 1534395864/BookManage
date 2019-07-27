<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta charset="utf-8">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<title>后台</title>
		<link rel="icon" href="../img/icon/favicon.ico" type="image/x-icon">
		<link rel="stylesheet" href="../layui/css/layui.css">
	</head>

	<body class="layui-layout-body">
		<div class="layui-layout layui-layout-admin">
			<div class="layui-header">
			<div class="layui-logo">图书管理后台</div>
			<!-- 头部区域（可配合layui已有的水平导航） -->
			<ul class="layui-nav layui-layout-right">
				<li class="layui-nav-item"><a href="javascript:;"> 欢迎您：${ sessionScope.admin.username}
				</a>
					<dl class="layui-nav-child">
						<dd>
							<a
								href="${pageContext.request.contextPath}/AdminServlet?method=exit">退出登录<span
								class="layui-badge">99+</span></a>
						</dd>
					</dl></li>
			</ul>
		</div>

			<div class="layui-side layui-bg-black">
				<div class="layui-side-scroll">
					<!-- 左侧导航区域（可配合layui已有的垂直导航） -->
					<ul class="layui-nav layui-nav-tree" lay-filter="test">
						<li class="layui-nav-item">
							<a class="" href="javascript:;">图书管理</a>
							<dl class="layui-nav-child">
								<dd>
									<a href="index.jsp">编辑图书</a>
								</dd>
								<dd>
									<a href="addbook.jsp">新添图书</a>
								</dd>
							</dl>
						</li>
						<li class="layui-nav-item ">
							<a href="javascript:;">管理员管理</a>
							<dl class="layui-nav-child">
								<dd>
									<a href="admin.jsp">编辑管理员</a>
								</dd>
								<dd>
									<a href="addadmin.jsp">新添管理员</a>
								</dd>
							</dl>
						</li>
						<li class="layui-nav-item layui-this">
							<a href="countMoney.jsp">财务管理</a>
						</li>
					</ul>
				</div>
			</div>

			<div class="layui-body">
				<!-- 内容主体区域 -->
				<div style="padding: 15px;" class="err">
					<div style="margin-top: 5%;">
						<div class="layui-row">
							<div class="layui-col-lg-offset2">
								<!--<input type="text" class="layui-input" id="date">-->
								<i class="layui-icon" style="font-size: 30px;color: red;cursor:pointer;float: left;" id="i">&#xe637;</i>
								<h3 id="date" style="font-size: 13px;line-height: 30px; cursor: pointer;width: 11%;"></h3>
							</div>
							<div style="margin-left: 60%;margin-top: -2%;">
								本月总计:<span id="count"></span>
							</div>
						</div>
						<div class="layui-row" style="width: 60%;margin-left: 15%;margin-top: 1%;">
							<div id="data"></div>
							<div class="layui-row" style="margin-left:40%;">
								<div id="laypage"></div>
							</div>
						</div>
					</div>
				</div>
				<div class="layui-footer" style="height: 50px;line-height: 50px;text-align: center;">
					<!-- 底部固定区域 -->
					<h3>Copyright © 2018&nbsp;&nbsp;纯袖科技&nbsp;&nbsp;渝ICP备17004144号</h3>
				</div>
			</div>
			<script src="../layui/layui.js"></script>
			<script type="text/javascript" src="../js/jquery-3.3.1.min.js"></script>
			<script>
				layui.use(['element', 'laydate','laypage'], function() {
					var element = layui.element,
						$ = layui.jquery,
						laydate = layui.laydate,
						laypage = layui.laypage;
					
					var definedate = timestampToTime();
					var definecurr = 1;
					var definecount = 0;
					//请求
					function getdata(date,curr){
						$.ajax({
							type:'POST',
							url:'${pageContext.request.contextPath}/AdminServlet',
							data:{"method":"allMoney","date":date.replace(" / ","-"),"curr":curr},
							async:false,
							success:function(data){
								if(data.search("NOTF") != -1){
									$(".err").html("");
									$(".err").html("<div style='margin-top:150px;margin-left:400px;'><h1>你没有权限查看</h1></div>");
									return;
								} 
								var data  = JSON.parse(data);
								//清空
								document.getElementById("data").innerHTML ="";
								if(data.count == 0){
								//没有记录
								document.getElementById("data").innerHTML = "<div class='record'><div class='layui-col-lg3 layui-col-xs9 layui-col-lg-offset5'><h2>本月暂无记录</h2></div></div>";
								$("#laypage").hide();
								$("#count").text(data.count);
								return;
								}
								//有记录
								definecount = data.count;
								$("#laypage").show();
								for(var i in data.data) {
								var html = document.getElementById("data").innerHTML;
								document.getElementById("data").innerHTML = html +
									"<div class='record'>" +
									"<p>时间:" +
									data.data[i].created +
									"</p><div class='layui-col-lg11 layui-col-xs9'>用户:<span style='color:#1E9FFF;'>"+data.data[i].username+"</span>"+data.data[i].msg +
									"</div><div class='layui-col-lg1 layui-col-xs1'>￥" +
									data.data[i].money +
									"</div><br /><br /></div>"
								}
								$("#count").text(new Number(data.countmoney));
							}
						});
					};
					$(function(){
						getdata(definedate,definecurr);
						page(definecount);
					});
					//日期
					laydate.render({
						elem: '#date',
						type: 'month',
						format: 'yyyy / MM',
						value: timestampToTime(),
						btns: ['confirm'],
						theme: 'grid',
						done: function(value, date, endDate) {
							definedate = value;
							getdata(definedate,definecurr);
							page(definecount);
						},
						eventElem: '#i',
						trigger: 'click',
					});
					//执行一个laypage实例
					function page(count) {
						laypage.render({
							elem: 'laypage',
							count: count,
							limit: 5,
							prev: '<i class="layui-icon">&#xe603;</i>',
							next: '<i class="layui-icon">&#xe602;</i>',
							first: '首页',
							last: '尾页',
							jump: function(obj,first) {
							    //首次不执行
							    if(!first){
							    	getdata(definedate,obj.curr);
							    }
							}
						});
					};
				});

				function timestampToTime() {
					var time = new Date().getFullYear();
					var c = new Date().getMonth() + 1;
					return time + ' / ' + c
				};
			</script>
	</body>

</html>