<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="utf-8">
<meta name="viewport"
	content="width=device-width, initial-scale=1, maximum-scale=1">
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
				<c:if test="${ sessionScope.admin.rid =='2'}">
				<li class="layui-nav-item layui-nav-itemed "><a
						class="javascript:;" href="javascript:;">图书管理</a>
						<dl class="layui-nav-child">
							<dd>
								<a href="index.jsp">编辑图书</a>
							</dd>
							<dd  class="layui-this">
								<a href="addbook.jsp">新添图书</a>
							</dd>
						</dl></li>
				
				</c:if>
				<c:if test="${ sessionScope.admin.rid =='3'}">
					<li class="layui-nav-item layui-nav-itemed "><a
						class="javascript:;" href="javascript:;">图书管理</a>
						<dl class="layui-nav-child">
							<dd>
								<a href="index.jsp">编辑图书</a>
							</dd>
							<dd class="layui-this">
								<a href="addbook.jsp">新添图书</a>
							</dd>
						</dl></li>
					<li class="layui-nav-item"><a href="javascript:;">管理员管理</a>
						<dl class="layui-nav-child">
							<dd>
								<a href="admin.jsp">编辑管理员</a>
							</dd>
							<dd>
								<a href="addadmin.jsp">新添管理员</a>
							</dd>
						</dl></li>
					<li class="layui-nav-item"><a href="countMoney.jsp">财务管理</a></li>
					</c:if>
				</ul>
			</div>
		</div>

		<div class="layui-body">
			<!-- 内容主体区域 -->
			<div style="padding: 15px;">
			<div class="layui-form-item">
						<label class="layui-form-label"> 上传图片</label>
						<button type="button" class="layui-btn" id="upimg">选择图片</button>
						<div id="container"></div>
					</div>
				<form class="layui-form"
					action="${pageContext.request.contextPath}/AdminServlet?method=addBook"
					method="POST">
					<input type="hidden" name="img" id="img">
					<input type="hidden" name="btoken" value="${sessionScope.addbook}">
					<div class="layui-form-item">
						<label class="layui-form-label">图书名</label>
						<div class="layui-input-block">
							<input type="text" name="bookname" required lay-verify="required"
								autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">作者</label>
						<div class="layui-input-block">
							<input type="text" name="author" required lay-verify="required"
								autocomplete="off" class="layui-input">
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">价格</label>
						<div class="layui-input-block">
							<input type="text" name="price" required lay-verify="required"
								autocomplete="off" class="layui-input"
								onkeyup='clearNoNum(this)'>
						</div>
					</div>
					<div class="layui-form-item">
						<label class="layui-form-label">分类</label>
						<div class="layui-input-block">
							<input type="checkbox" name="classify" value="1" lay-skin="primary" title="男生">
							<input type="checkbox" name="classify" value="2" lay-skin="primary" title="女生">
							<input type="checkbox" name="classify" value="3" lay-skin="primary" title="完结">
							<input type="checkbox" name="classify" value="4" lay-skin="primary" title="付费">
							<input type="checkbox" name="classify" value="5" lay-skin="primary" title="连载">
							<input type="checkbox" name="classify" value="6" lay-skin="primary" title="免费">
						</div>
					</div>
					<div class="layui-form-item layui-form-text">
						<label class="layui-form-label">简介</label>
						<div class="layui-input-block">
							<textarea class="layui-textarea" name="text" id="content"  rows="6" style="resize:none;"></textarea>
						</div>
					</div>
					<div class="layui-form-item">
						<div class="layui-input-block">
							<button class="layui-btn" lay-submit lay-filter="formDemo">立即提交</button>
							<button type="reset" class="layui-btn layui-btn-primary">重置</button>
						</div>
					</div>
				</form>
			</div>
		</div>

		<div class="layui-footer"
			style="height: 50px; line-height: 50px; text-align: center;">
			<!-- 底部固定区域 -->
			<h3>Copyright © 2018&nbsp;&nbsp;纯袖科技&nbsp;&nbsp;渝ICP备17004144号</h3>
		</div>
	</div>
	<script src="../layui/layui.js"></script>
	<script>
		var img = false;
		var number = 0;
		layui.use([ 'form', 'upload', 'element' ],function() {
							var form = layui.form, $ = layui.jquery, upload = layui.upload, element = layui.element;
							//监听提交
							form.on('submit(formDemo)', function(data) {
								//layer.msg(JSON.stringify(data.field));
								var text = $("#content").val();
								if (img == false) {
									layer.msg('请选择图片');
									return false;
								}else if (number == 0||number >3) {
									layer.msg('分类只能选择 1-3种');
									return false;
								}else if(isNull(text)){
									layer.msg('请填写简介');
									return false;
								}
							});
							//图片
							upload.render({
										elem : '#upimg',
										url : '',
										accept : 'images',
										acceptMime : 'image/*',
										auto : false,
										bindAction : '#forDemo',
										multiple : false,
										choose : function(obj) {
											//预读本地文件，如果是多文件，则会遍历。(不支持ie8/9)
											obj.preview(function(index,file, result) {
												//console.log(result); //得到文件对象
														var container = document.getElementById("container");
														container.innerHTML = "";
														var img = document.createElement('img');
														img.src = result;
														img.style = "width:50px;height:auto;margin-left:220px;margin-top:-50px"
														container.appendChild(img);
														$("#img").val(result);
														//console.log(file); //得到文件对象
													});
											img = true;
										}
									});
							//类别
							form.on('checkbox',function(data){
								checknb(data.elem.checked);
							});
						});
		//jquery
		function clearNoNum(obj) {
			obj.value = obj.value.replace(/[^\d.]/g, ""); //清除"数字"和"."以外的字符
			obj.value = obj.value.replace(/^\./g, ""); //验证第一个字符是数字而不是
			obj.value = obj.value.replace(/\.{2,}/g, "."); //只保留第一个. 清除多余的
			obj.value = obj.value.replace(".", "$#$").replace(/\./g, "")
					.replace("$#$", ".");
			obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/, '$1$2.$3'); //只能输入两个小数

		}
		
		function checknb(flag){
			if(flag){
				number++;
			}else{
				number--;
			}
		};
		function isNull( str ){
			if ( str == "" ) return true;
			var regu = "^[ ]+$";
			var re = new RegExp(regu);
			return re.test(str);
			}
	</script>
	${ sessionScope.msg }
	<% request.getSession().removeAttribute("msg"); %>
</body>

</html>