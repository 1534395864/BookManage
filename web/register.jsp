<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户-注册</title>
		<link rel="icon" href="img/icon/favicon.ico" type="image/x-icon">
		<meta name="renderer" content="webkit">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<link rel="stylesheet" href="layui/css/layui.css" media="all">
		<link rel="stylesheet" href="css/register.css" />
	</head>

	<body>
		<!-- 背景 -->
		<div id="bg">
			<canvas></canvas>
			<canvas></canvas>
			<canvas></canvas>
		</div>
		<!-- 头部 -->
		<div class="layui-container">
			<div class="layui-row">
				<div class="layui-col-xs7 layui-col-lg4">
					<a href="index.jsp" target="_blank"><img src="img/logo.png" style="width: 100%;" /></a>
				</div>
				<div class="layui-col-xs4 layui-col-lg8 logo">
					<p>
						<span>|</span>用户注册
					</p>
				</div>
			</div>
		</div>
		<hr class="layui-bg-gray">
		<!--  注册表单-->
		<form class="layui-container layui-form" id="form">
			<input type="hidden" name="token" value="${sessionScope.token}">
			<div class="layui-row form-item">
				<p class="layui-col-lg1 layui-col-lg-offset3 layui-col-xs2 layui-col-xs-offset1">用户名</p>
				<i class=" layui-icon layui-col-lg1 layui-col-xs2">&#xe66f;</i> <input type="text" name="username" lay-verify="username" autocomplete="off" placeholder="字母和数字组合,4位字符以上" class=" layui-col-lg4 layui-col-xs6">
			</div>
			<div class="form-sex">
				<label class="layui-form-label">性别</label>
				<div class="layui-input-block" lay-verify="sex">
					<input type="radio" name="sex" value="1" title="男" lay-filter="sex">
					<input type="radio" name="sex" value="0" title="女" lay-filter="sex">
				</div>
			</div>
			<div class="layui-row form-item">
				<p class="layui-col-lg1 layui-col-lg-offset3 layui-col-xs2 layui-col-xs-offset1">密码</p>
				<i class=" layui-icon layui-col-lg1 layui-col-xs2">&#xe673;</i> <input type="text" name="password" lay-verify="password" autocomplete="off" placeholder="长度5位以上，不能有空格和中文" class=" layui-col-lg4 layui-col-xs6" id="pass">
			</div>
			<div class="layui-row form-item">
				<p class="layui-col-lg1 layui-col-lg-offset3 layui-col-xs2 layui-col-xs-offset1">确认密码</p>
				<i class=" layui-icon layui-col-lg1 layui-col-xs2">&#xe673;</i> <input type="text" name="repass" lay-verify="repass" autocomplete="off" placeholder="确认密码" class=" layui-col-lg4 layui-col-xs6" id="repass">
			</div>
			<div class="layui-row form-item">
				<p class="layui-col-lg1 layui-col-lg-offset3 layui-col-xs2 layui-col-xs-offset1">手机号</p>
				<i class=" layui-icon layui-col-lg1 layui-col-xs2">&#xe678;</i> <input type="text" name="phone" lay-verify="phone" autocomplete="off" placeholder="填写后可使用手机号登录" class=" layui-col-lg4 layui-col-xs6">
			</div>
			<div class="layui-row form-item">
				<p class="layui-col-lg1 layui-col-lg-offset3 layui-col-xs2 layui-col-xs-offset1">邮箱</p>
				<i class=" layui-icon layui-col-lg1 layui-col-xs2">&#x2709;</i> <input type="text" name="email" lay-verify="title" autocomplete="off" placeholder="常用邮箱,没有可不填写" class=" layui-col-lg4 layui-col-xs6">
			</div>
			<div class="form-rules layui-col-lg-offset4 layui-col-xs-offset2">
				<label class="agree-label" onselectstart="return false;" style="-moz-user-select: none;" lay-verify="rules"> <i
				class=""></i>我已阅读并同意&nbsp;<a href="rules.html" target="_blank">《服务协议》</a>
			</label>
			</div>
			<div class="layui-row">
				<button class="layui-btn layui-btn-normal  layui-btn-fluid layui-btn-radius" style="margin-bottom: 3%;" lay-submit lay-filter="btn">立即注册</button>
			</div>
		</form>
		<div class="layui-fluid" style="height: 40px; line-height: 50px; text-align: center; color: #F8F8F8;">
			<h3>Copyright © 2018&nbsp;&nbsp;纯袖科技&nbsp;&nbsp;渝ICP备17004144号</h3>
		</div>
		<!-- canva -->
		<script type="text/javascript" src="jQuery/jquery3.3.1.js"></script>
		<script type="text/javascript" src="js/canva.js"></script>
		<!--layui-->
		<script type="text/javascript" src="layui/layui.js"></script>
		<script>
			var isrules = false;
			var sex = false;
			layui.use('form', function() {
				var form = layui.form;
				var $ = layui.jquery;
				$('.agree-label').click(function() {
					if($('.agree-label i').hasClass('cus-checked')) {
						$('.agree-label i').removeClass("cus-checked");
						isrules = false;
					} else {
						$('.agree-label i').addClass("cus-checked");
						isrules = true;
					}
				});
				//验证
				form.verify({
					username: function(value) {
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
						if(value.length < 5) {
							return '密码长度不够';
						}
					},
					sex: function() {
						if(!sex)
							return '请选择性别';
					},
					repass: function(value, password) {
						if($("#pass").val() != $("#repass").val())
							return '两次密码输入不一致';
					},
					rules: function() {
						if(!isrules)
							return '请同意服务协议';
					}
				});
				form.on('submit(btn)', function() {
					//发送
					$.ajax({
						type: "post",
						url: "./UserServlet?method=register",
						data: $("#form").serialize(), //要提交的表单
						async: true,
						success: function(data) {
							var code = JSON.parse(data);
							if(code.code == 0) {
								alert("注册成功");
								window.location.href = "./login.jsp";
							} else if(code.code == 1) {
								alert("该用户已注册");
							} else if(code.code == 400) {
								alert("请勿重复提交");
							} else if(code.code == 408) {
								alert("与服务器连接超时,请刷新页面");
							} else if(code.code == 500) {
								alert("服务器错误,请联系网站管理员");
							}
						}
					})
					return false;
				});
				form.on('radio(sex)', function(data) {
					sex = true;
				});
			});
		</script>
	</body>

</html>