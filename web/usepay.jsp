<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>充值</title>
		<link rel="icon" href="img/icon/favicon.ico" type="image/x-icon">
		<meta name="renderer" content="webkit">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<link rel="stylesheet" href="layui/css/layui.css" media="all">
		<link rel="stylesheet" href="css/userpay.css" />
	</head>

	<body>
		<!-- 头部 -->
		<div class="layui-container">
			<div class="layui-row">
				<div class="layui-col-xs7 layui-col-lg4">
					<a href="index.jsp" target="_blank"><img src="img/logo.png" style="width: 100%;" /></a>
				</div>
				<div class="layui-col-xs4 layui-col-lg8 logo">
					<h5>充值中心</h5>
				</div>
			</div>
		</div>
		<hr class="layui-bg-blue">
		<div class="layui-container paybox">
			<div class="layui-row name">
				<div class="layui-col-lg12 layui-col-xs12">
					<label>充值账号：${ user.username }</label>
				</div>
			</div>
			<div class="layui-row">
				<div class="layui-col-lg1 layui-col-xs4">
					<p>充值金币：</p>
				</div>
				<div class=" layui-col-lg1 choose layui-col-xs2">
					<strong>3</strong>
				</div>
				<div class=" layui-col-lg1 choose layui-col-xs2">
					<strong>10</strong>
				</div>
				<div class=" layui-col-lg1 choose layui-col-xs2">
					<strong>30</strong>
				</div>
			</div>
			<div class="layui-row br">
				<div class="layui-col-lg1  choose layui-col-xs2">
					<strong>50</strong>
				</div>
				<div class="layui-col-lg1 choose layui-col-xs2">
					<strong>100</strong>
				</div>
				<div class="layui-col-lg1 choose layui-col-xs2">
					<input type="number" placeholder="自定义" lay-ignore id="paynumber" onkeypress='return( /[\d]/.test(String.fromCharCode(event.keyCode)))' />
				</div>
			</div>
			<div class="layui-row">
				<div class="layui-col-lg1 layui-col-xs4">
					<p class="rechnum1">实付金额：</p>
				</div>
				<div class="layui-col-lg7">
					<span class="rechnum"></span>
				</div>
			</div>
			<br />
			<div class="layui-row">
				<div class="layui-col-lg1 layui-col-xs4">
					<p class="rechnum1">支付方式：</p>
				</div>
				<div class=" layui-col-lg1 pay layui-col-xs4">
					<img src="svg/zfb.svg" id="zfb" /> <span>支付宝</span>
				</div>
				<div class=" layui-col-lg1 pay layui-col-xs4">
					<img src="svg/wx.svg" id="wx" /> <span>微信</span>
				</div>
				<div class=" layui-col-lg1 pay layui-col-xs4">
					<img src="svg/bank.svg" id="bank" /> <span>银行卡</span>
				</div>
			</div>
			<div id="gopay">
				<p></p>
				<button class="layui-btn layui-btn-radius layui-btn-warm" id="pay">前往支付</button>
			</div>
		</div>
		<div class="layui-fluid layui-bg-cyan" style="height: 40px; line-height: 50px; text-align: center;">
			<h3>Copyright © 2018&nbsp;&nbsp;纯袖科技&nbsp;&nbsp;渝ICP备17004144号</h3>
		</div>
		<script type="text/javascript" src="layui/layui.js"></script>
		<script>
			var money;
			var pay;
			layui.use('jquery', function() {
				var $ = layui.jquery;

				$(".choose").on('click', function() {
					$(".choose").css("background",
						"none");
					$(".choose").css(
						"border-color",
						"#CCCCCC");
					$(this)
						.css("background",
							"transparent url(${pageContext.request.contextPath}/img/success1.png) no-repeat right bottom");
					$(this).css("border-color",
						"#27b0d6");
					money = $(this).children(
						"strong").text();
					$(".rechnum").text(money + "元");
				});
				$(".pay").on('click', function() {
					$(".pay").css("background",
						"none");
					$(".pay ").css("border-color",
						"#CCCCCC");
					$(this)
						.css("background",
							"transparent url(${pageContext.request.contextPath}/img/success1.png) no-repeat right bottom");
					$(this).css("border-color",
						"#27b0d6");
					pay = $(this).children("img").attr('id');
				});
				$("#pay").on('click', function() {
					if(money == 0 || money == null) {
						$("#gopay").children("p").text("请选择金额！");
					} else if(pay == null) {
						$("#gopay").children("p").text("请选择支付方式！");
					} else {
						$.ajax({
							type: 'POST',
							url: '${pageContext.request.contextPath}/UserServlet',
							async: false,
							data: {
								'method': 'recharge',
								'money': money,
								'paymentMethod': pay
							},
							success: function(data) {
								var json = JSON.parse(data);
								if(json.result == 0) {
									alert('充值成功!');
								}
							}
						})
					}
				});
				$("#paynumber").bind('input propertychange', function() {
					var diy = $(this).val();
					if(diy == 0) {
						$(this).val("");
					}
					if(diy.length > 7) {
						$(".rechnum").text(diy.slice(0, 7) + "元");
						$(this).val(diy.slice(0, 6));
					} else {
						$(".rechnum").text(diy + "元");
					}
					money = diy;
				});
			});
		</script>
	</body>

</html>