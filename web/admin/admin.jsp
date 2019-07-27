<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
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
<style>
input[type=radio]{
	cursor: pointer;
	margin: 20px;
}
input[type=radio]:after  {
    content: "";
    display:block;
    width: 20px;
    height: 20px;
    border-radius: 50%;
    text-align: center;
    line-height: 14px;
    font-size: 16px;
    color: #fff;
    border: 2px solid #ddd;
    background-color: #fff;
    box-sizing:border-box;
    position: relative;
    left: -2px;
}
input[type=radio]:checked:after  {
    content: "";
    transform:matrix(-0.766044,-0.642788,-0.642788,0.766044,0,0);
    -webkit-transform:matrix(-0.766044,-0.642788,-0.642788,0.766044,0,0);
    border: 4px solid #5774FF;
}
</style>

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
					<li class="layui-nav-item"><a
						class="javascript:;" href="javascript:;">图书管理</a>
						<dl class="layui-nav-child">
							<dd>
								<a href="index.jsp">编辑图书</a>
							</dd>
							<dd>
								<a href="addbook.jsp">新添图书</a>
							</dd>
						</dl></li>
					<li class="layui-nav-item layui-nav-itemed">
							<a href="javascript:;">管理员管理</a>
							<dl class="layui-nav-child">
								<dd class="layui-this">
									<a href="admin.jsp">编辑管理员</a>
								</dd>
								<dd>
									<a href="addadmin.jsp">新添管理员</a>
								</dd>
							</dl>
						</li>
					<li class="layui-nav-item"><a href="countMoney.jsp">财务管理</a></li>
				</ul>
			</div>
		</div>

		<div class="layui-body">
			<!-- 内容主体区域 -->
			<div style="padding: 15px;">
				<div class="layui-row">
					<div class="demoTable">
						搜索管理员：
						<div class="layui-inline">
							<input class="layui-input" id="uname"
								autocomplete="off">
						</div>
						<div class="layui-btn" id="sf" >搜索</div>
						<div class="layui-btn" id="index">返回第一页</div>
						<a class="layui-btn layui-btn-warm" href="addadmin.jsp" style="margin-left: 50%">添加管理员</a>
					</div>
				</div>
				<table id="admins" lay-filter="info"></table>
				<div id="page"></div>
			</div>
		</div>

		<div class="layui-footer"
			style="height: 50px; line-height: 50px; text-align: center;">
			<!-- 底部固定区域 -->
			<h3>Copyright © 2018&nbsp;&nbsp;纯袖科技&nbsp;&nbsp;渝ICP备17004144号</h3>
		</div>
	</div>
	<script src="../layui/layui.js"></script>
	<script type="text/html" id="operaction">
			<a class="layui-btn layui-btn-xs" lay-event="edit">编辑</a>
			<a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">删除</a>
	</script>
	<script>
		//JavaScript代码区域
		layui.use('element', function() {
			var element = layui.element;

		});
		layui.use([ 'table', 'laypage'],function() {
			var table = layui.table, $ = layui.jquery, laypage = layui.laypage;
			//第一个实例
			var  action = table.render({
										elem : "#admins",
										url : '${pageContext.request.contextPath}/AdminServlet?method=allAdmin',
										cols : [ [ {
											field : 'uid',
											title : 'ID',
										}, {
											field : 'username',
											title : '用户名',
										}, {
											field : 'password',
											title : '密码',
										},{
											field : 'status',
											title : '状态',
										},{
											field : 'operation',
											title : '操作',
											toolbar : '#operaction',
										}, ] ],
										page : {
											limit : 5,
											layout : [ 'prev', 'page', 'next',
													'skip', 'count' ]
										},
										defaultToolbar : [ 'filter', 'print',
												'exports' ],
										initSort : {
											//排序
											field : 'uid',
											type : 'asc',
										},
										id : 'retable',
									});
							//监听图书操作
							table.on('tool(info)', function(obj) {
								var data = obj.data; //获得当前行数据
								var layEvent = obj.event; //获得 lay-event 对应的值
								var tr = obj.tr; //获得当前行 tr 的DOM对象
								if(layEvent === 'edit'){
									if(data.status ==='正常'){
										layer.open({
											type:1,
											title:'修改管理员信息',
											content:"   <div class=\"layui-form-item\">"+
											"    <label class=\"layui-form-label\">用户名：</label>"+
											"    <div class=\"layui-input-block\">"+
											"      <input type=\"text\" id=\"username\"  autocomplete=\"off\" value="+data.username+" class=\"layui-input\">"+
											"    </div>"+
											"  </div>"+
											"<div class=\"layui-form-item\">"+
											"    <label class=\"layui-form-label\">密码：</label>"+
											"    <div class=\"layui-input-block\">"+
											"      <input type=\"text\" id=\"password\"  autocomplete=\"off\"  value="+data.password+" class=\"layui-input\">"+
											"    </div>"+
											"  </div>"+
											" <div class=\"layui-form-item\">"+
											"    <label class=\"layui-form-label\">状态：</label>"+
											"    <div class=\"layui-input-block\">"+
											"      <input type=\"radio\" name=\"status\" value=\"0\" >冻结"+
											"      <input type=\"radio\" name=\"status\" value=\"1\" checked>正常"+
											"    </div>"+
											"  </div>"
											,
											offset: ['100px', '300px'],
											btn:['提交修改'],
											yes: function(index, dom){
												  //按钮【修改】的回调
												var name = dom.find("#username").val();
												var password = dom.find("#password").val();
												var status = dom.find("input[name='status']:checked").val();
												if(data.username === name&&data.password === password&&status == '1' ){
													layer.msg('未做修改');
												}else if(checkNull(name)||checkNull(password)){
													layer.msg('请检查空值');
												}else{
													$.ajax({
														type:'POST',
														data:{'method':'editAdmin','uid':data.uid,'username':name,'password':password,'status':status},
														url:'${pageContext.request.contextPath}/AdminServlet',
														success:function(data){
															var c = JSON.parse(data).code;
															if(c == 200){
																layer.msg("修改成功");
																action.reload();//重载表格
																layer.close(index); //关闭修改层
															}else{
																layer.msg("系统异常,请重新修改");
															}
														}
													});
												}
											  },
											  closeBtn:2,
											  resize:false,
											  scrollbar:false,
											  area: ['800px', '400px']
										});
									}else{
										layer.open({
											type:1,
											title:'修改管理员信息',
											content:"   <div class=\"layui-form-item\">"+
											"    <label class=\"layui-form-label\">用户名：</label>"+
											"    <div class=\"layui-input-block\">"+
											"      <input type=\"text\" id=\"username\"  autocomplete=\"off\" value="+data.username+" class=\"layui-input\">"+
											"    </div>"+
											"  </div>"+
											"<div class=\"layui-form-item\">"+
											"    <label class=\"layui-form-label\">密码：</label>"+
											"    <div class=\"layui-input-block\">"+
											"      <input type=\"text\" id=\"password\"  autocomplete=\"off\"  value="+data.password+" class=\"layui-input\">"+
											"    </div>"+
											"  </div>"+
											" <div class=\"layui-form-item\">"+
											"    <label class=\"layui-form-label\">状态：</label>"+
											"    <div class=\"layui-input-block\">"+
											"      <input type=\"radio\" name=\"status\" value=\"0\" checked>冻结"+
											"      <input type=\"radio\" name=\"status\" value=\"1\">正常"+
											"    </div>"+
											"  </div>"
											,
											offset: ['100px', '300px'],
											btn:['提交修改'],
											yes: function(index, dom){
												  //按钮【修改】的回调
												var name = dom.find("#username").val();
												var password = dom.find("#password").val();
												var status = dom.find("input[name='status']:checked").val();
												if(data.username === name&&data.password === password&&status == '0' ){
													layer.msg('未做修改');
												}else if(checkNull(name)||checkNull(password)){
													layer.msg('请检查空值');
												}else{
													$.ajax({
														type:'POST',
														data:{'method':'editAdmin','uid':data.uid,'username':name,'password':password,'status':status},
														url:'${pageContext.request.contextPath}/AdminServlet',
														success:function(data){
															var c = JSON.parse(data).code;
															if(c == 200){
																layer.msg("修改成功");
																action.reload();//重载表格
																layer.close(index); //关闭修改层
															}else{
																layer.msg("系统异常,请重新修改");
															}
														}
													});
												}
											  },
											  closeBtn:2,
											  resize:false,
											  scrollbar:false,
											  area: ['800px', '400px']
										});
									}
								}else{
									layer.confirm('此操作不可恢复!', {icon: 3, title:'确认删除吗?'}, function(index){
										$.ajax({
											type:'POST',
											url:'${pageContext.request.contextPath}/AdminServlet',
											data:{'method':'deleteAdmin','uid':data.uid},
											success:function(data){
												var data = JSON.parse(data).code;
												if(data == 'NOTF'){
													layer.msg('权限不足');
												}else if(data == '0'){
													layer.msg('删除成功');
													action.reload();
												}else{
													layer.msg('未知异常,请联系超管');
												}
											}
										})
										  layer.close(index);
									});
								}
							});
						$("#sf").on('click',function() {
								if ($("#uname").val() == '') {
								alert("请输入管理员名");
								return false;
								}
								table.reload('retable',{
								url : '${pageContext.request.contextPath}/AdminServlet?method=getAdmin',//设置异步接口
								where : {//设定异步数据接口的额外参数
									name : $("#uname").val(),
									},
								method : 'post'
									}); 	
							 });
						$("#index").on('click',function() {
							action.reload();
							$("#uname").val("");
							});
						});
		function clearNoNum(obj) {
			obj.value = obj.value.replace(/[^\d.]/g,""); //清除"数字"和"."以外的字符
		        obj.value = obj.value.replace(/^\./g,""); //验证第一个字符是数字而不是
		        obj.value = obj.value.replace(/\.{2,}/g,"."); //只保留第一个. 清除多余的
		        obj.value = obj.value.replace(".","$#$").replace(/\./g,"").replace("$#$",".");
		        obj.value = obj.value.replace(/^(\-)*(\d+)\.(\d\d).*$/,'$1$2.$3'); //只能输入两个小数
			
		}
		
		function checkNull(string){
			if(string == ""|string == ' '){
				return true
			}
			return false
		}

	</script>
	</body>

</html>