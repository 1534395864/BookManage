<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>用户-登录</title>
		<link rel="icon" href="img/icon/favicon.ico" type="image/x-icon">
		<meta name="renderer" content="webkit">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<link rel="stylesheet" href="layui/css/layui.css" media="all">
		<link rel="stylesheet" href="css/login.css" />
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
					<p><span>|</span>用户登录</p>
				</div>
			</div>
		</div>
		<hr class="layui-bg-gray">
		<!-- 登录验证-->
		<div class="layui-container">
			<div class="layui-tab layui-tab-card" style="margin: 2%;">
				<ul class="layui-tab-title layui-row">
					<li class="layui-this layui-col-lg5  layui-col-xs6" style="font-size: 20px;">密码登录</li>
					<li class="layui-col-lg5 layui-col-xs6" style="font-size: 20px;">短信码登录</li>
				</ul>
				<div class="layui-tab-content" style="height: 330px;">
					<!-- 账密 -->
					<div class="layui-tab-item layui-show ">
						<div class="layui-form" id="passlogin">
							<div class="user-form">
								<span><i class="layui-icon i" >&#xe770;</i></span>
								<input type="text" name="username" autocomplete="off" placeholder="请输入用户名" lay-verify="name" />
							</div>
							<div class="user-form">
								<span><i class="layui-icon i" >&#xe673;</i></span>
								<input type="password" name="password" autocomplete="off" placeholder="请输入密码" lay-verify="pass" />
							</div>
							<div class="user-form" style="background: none;height: 10px;color: #F8F8F8;">
								<input type="checkbox" name="autologin" lay-skin="primary" lay-filter="auto">今天内自动登录 &nbsp;&nbsp;&nbsp;
							</div>
							<div class="login-btn" lay-verify="btn">
								<button class="layui-btn" lay-submit lay-filter="passbtn">立即登录</button>
							</div>
							<div class="login-qq">
								<p>其他方式登录:</p>
								<a href="./QQLogin" title="QQ登录" style="cursor: pointer;"><i class="layui-icon">&#xe676;</i></a>
							</div>
						</div>
					</div>
					<!--短信 -->
					<div class="layui-tab-item">
						<form class="layui-form" id="codelogin">
							<div class="user-form">
								<span><i class="layui-icon i" >&#xe678;</i></span>
								<input type="text" name="phone" autocomplete="off" placeholder="账户所绑定手机号" lay-verify="phone" id="phone" maxlength="11" />
								<span id="msg"></span>
							</div>
							<div class="login-code">
								<span><i class="layui-icon i" >&#xe679;</i></span>
								<input type="number" id="code" autocomplete="off" placeholder="验证码" lay-verify="code" oninput="if(value.length>4)value=value.slice(0,4)" />
								<button class="layui-btn" id="getcode">获取验证码</button>
							</div>
							<div class="login-btn" lay-verify="btn">
								<button class="layui-btn" lay-submit lay-filter="codebtn">立即登录</button>
							</div>
						</form>
					</div>
				</div>
			</div>
		</div>
		<div class="layui-fluid" style="height: 40px;line-height: 50px;text-align: center;color: #F8F8F8;">
			<h3>Copyright © 2018&nbsp;&nbsp;纯袖科技&nbsp;&nbsp;渝ICP备17004144号</h3>
		</div>
		<!-- canva -->
		<script type="text/javascript" src="jQuery/jquery3.3.1.js"></script>
		<script type="text/javascript" src="jQuery/jquery.md5.js"></script>
		<script type="text/javascript" src="js/canva.js"></script>
		<!--layui-->
		<script type="text/javascript" src="layui/layui.js"></script>
		<script>
		//confim
		var Phone;
			//tab
			layui.use('element', function() {
				var $ = layui.jquery,
					element = layui.element; //Tab的切换功能

			});
			//提交
			layui.use('form', function() {
				var form = layui.form;
				var layer = layui.layer,
					$ = layui.jquery;
				//自定义验证
				form.verify({
					name: function(value) {
						if(value == '') {
							return '用户名不能为空';
						}
					},
					pass: function(value) {
						if(value == '') {
							return '密码不能为空';
						}
					},
					code: function(value) {
						if(value == '') {
							return '验证码不能为空 ';
						} else if(value.length != 4) {
							return '验证码为4位数 ';
						}
					}
				});
				$("#phone").bind('input propertychange', function() {
					 removemsg();
					var re = new RegExp(/^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\d{8}$/);
					if(re.test(jQuery.trim($(this).val())) == true) {
						removecss();
						Phone = jQuery.trim($(this).val());
					} else {
						addcss();
					}
				});
				$().ready(function() {
					addcss();
				})
				//监听密码提交
				form.on('submit(passbtn)', function(data) {
					var btn = data.elem;
					removebtn(btn);
					//发送
					$.ajax({
						type: "post",
						url: "./UserServlet?method=passwordlogin",
						data: {
							"username": $("input[name='username']").val(),
							"password": $.md5($("input[name='password']").val()),
							"autologin": $("input[name='autologin']").is(':checked')
						}, //要提交的表单
						async: true,
						success: function(data) {
							var code = JSON.parse(data);
							if(code.result == 0) {
								layer.msg('登录成功，即将跳转');
								setTimeout(function() {
									window.location.href = "./index.jsp";
								}, 1000);
							}else if(code.result == 999){
								layer.msg('登录成功，即将跳转');
								setTimeout(function() {
									window.location.href = "./admin/index.jsp";
								}, 1000);
							} else {
								layer.msg('用户名或密码错误');
								addbtn(btn);
							}
						}
					})

					return false;
				});
				//监听验证码提交
				form.on('submit(codebtn)', function(data) {
					var btn = data.elem;
					removebtn(btn);
					$.ajax({
						type:'POST',
						url:'./UserServlet',
						data:{'method':'phoneLogin','phone':Phone,'code':$("#code").val()},
						success:function(data){
							var result = JSON.parse(data).code;
							if(result == 'YSE'){
								layer.msg('登录成功，即将跳转');
								setTimeout(function() {
									window.location.href = "./index.jsp";
								}, 1000);
							}else if(result =='ERROR'){
									layer.msg('验证码错误', {icon: 5});
									addbtn(btn);
							}else if(result == 'OVERTIME'){
									layer.msg('验证码已过期', {icon: 5});
									addbtn(btn);
							}
						}
					});
					
					return false;
				});
				//获取验证码
				$("#getcode").on('click', function() {
					$.ajax({
						type:'POST',
						url:'./UserServlet',
						data:{'method':'getCode','phone':Phone},
						success:function(data){
							var result = JSON.parse(data);
							if(result.code == 'NULLU'){
								addmsg();
							}else if(result.code == 'TIME'){
								layer.msg('距离上次发送未超过一分钟');
								showtime($("#getcode"),result.time);
							}else if(result.code == '0'){
								layer.msg('短信发送成功');
								showtime($("#getcode"),59);
							}else{
								layer.msg(result.code);
							}
						}
					});
					return false;
				});

				//复选框
				form.on('checkbox(auto)', function(data) {
					console.log(data.elem.checked);
				});
			});

			function removebtn(btn) {
				$(btn).attr("disabled", true);
				$(btn).text("登陆中....");
			}

			function addbtn(btn) {
				$(btn).attr("disabled", false);
				$(btn).text("立即登录");
			}

			function addcss() {
				$("#getcode").attr("disabled", "disabled");
				$("#getcode").css("background-color", "#FBFBFB");
				$("#getcode").css("color", "#C9C9C9");
				$("#getcode").css("cursor", "not-allowed");
			};

			function removecss() {
				$("#getcode").removeAttr("disabled");
				$("#getcode").css("color", "#fff");
				$("#getcode").css("background-color", "#009688");
				$("#getcode").css("cursor", "pointer");
			}
			function addmsg(){
				$("#msg").css("color","red");
				$("#msg").text("此手机号未注册");
			}
			function removemsg(){
				$("#msg").text("");
			}
			// 倒计时
			function showtime(a,time) {
				// 设置颜色和不可点击
				setTimeout(function() {
					a.css("pointer-events", "none");
					a.css("opacity", "0.8");
				}, 1000);
				//var time = 59;
				// 倒计时
				var code = setInterval(function() {
					a.text("重新发送" + time--);
				}, 1000);
				// 重置
				var re = setTimeout(function() {
					a.css("pointer-events", "auto");
					a.css("opacity", "1");
					a.text("重新获取");
					clearInterval(code);
				}, (time+1)*1000);
			};
		</script>
		<!--myjs  -->
		<script type="text/javascript">
	/* 	function QQLogin(width, height)
			{
		    width = width || 600;
		    height = height || 400;
		    var left = (window.screen.width - width) / 2;
		    var top = (window.screen.height - height) / 2;
		    window.open("./QQLogin", "_blank", "toolbar=yes, location=yes, directories=no, status=no, menubar=yes, scrollbars=yes, resizable=no, copyhistory=yes, left="+left+", top="+top+", width="+width+", height="+height);
			} */
		</script>
	</body>

</html>