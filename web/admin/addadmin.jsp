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
						<li class="layui-nav-item layui-nav-itemed">
							<a href="javascript:;">管理员管理</a>
							<dl class="layui-nav-child">
								<dd>
									<a href="admin.jsp">编辑管理员</a>
								</dd>
								<dd class="layui-this">
									<a href="addadmin.jsp">新添管理员</a>
								</dd>
							</dl>
						</li>
						<li class="layui-nav-item">
							<a href="countMoney.jsp">财务管理</a>
						</li>
					</ul>
				</div>
			</div>

			<div class="layui-body">
				<!-- 内容主体区域 -->
				<div style="padding: 15px;">
					<div class="layui-row" style="margin-top: 5%;">
						<div class="layui-col-lg-offset5">
							<h2>新建管理员</h2></div>
						<hr class="layui-bg-cyan" style="margin-bottom: 4%;">
					</div>
					<div class="layui-form">
						<div class="layui-row">
							<div class="layui-form-item layui-col-lg-offset3 layui-col-lg5">
								<label class="layui-form-label">用户名</label>
								<div class="layui-input-block">
									<input type="text" name="adminName" required lay-verify="name" autocomplete="off" class="layui-input" placeholder="字母和数字组合,4位字符以上">
								</div>
							</div>
						</div>
						<div class="layui-row">
							<div class="layui-form-item layui-col-lg-offset3 layui-col-lg5">
								<label class="layui-form-label">密码</label>
								<div class="layui-input-block">
									<input type="text" name="password" required lay-verify="password" autocomplete="off" class="layui-input" placeholder="长度4位以上，不能有空格和中文">
								</div>
							</div>
						</div>
						<div class="layui-row">
							<div class="layui-form-item layui-col-lg-offset4 layui-col-lg5">
								<div class="layui-input-block">
									<button class="layui-btn" lay-submit lay-filter="btn">立即创建</button>
								</div>
							</div>
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
		<script>
			layui.use(['form', 'element'], function() {
				var form = layui.form;
				var layer = layui.layer,
					$ = layui.jquery;
				//自定义验证
				form.verify({
					name: function(value) {
						if(value == '') {
							return '用户名不能为空';
						}
						if(!new RegExp("^[A-Za-z0-9]{4,15}$").test(value)) {
							return '用户名格式不正确';
						}
					},
					password: function(value) {
						if(value == '') {
							return '密码不能为空';
						}
						if(new RegExp("[\ ]+").test(value)) {
							return '密码不能有空格';
						}
						if(new RegExp("[\u4e00-\u9fa5]").test(value)) {
							return '密码不能有汉字';
						}
						if(value.length < 4) {
							return '密码长度不够';
						}
					}
				});
				form.on('submit(btn)', function() {
					$.ajax({
						type:'POST',
						data:{'method':'addAdmin','adminName':$("input[name='adminName']").val(),'password':$("input[name='password']").val()},
						url:'${pageContext.request.contextPath}/AdminServlet',
						success:function(data){
							var json = JSON.parse(data);
							if(json.result == 0){
								$("input[name='adminName']").val("");
								$("input[name='password']").val("");
								layer.msg('添加成功');
							}else if (json.code == 'NOTF'){
								layer.msg('没有权限');
							}else{
								layer.msg('未知异常');
							}
						}
					});
				});	
			});
		</script>
	</body>

</html>