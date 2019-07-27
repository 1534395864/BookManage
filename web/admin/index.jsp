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
							<dd class="layui-this">
								<a href="index.jsp">编辑图书</a>
							</dd>
							<dd>
								<a href="addbook.jsp">新添图书</a>
							</dd>
						</dl></li>
				
				</c:if>
				<c:if test="${ sessionScope.admin.rid =='3'}">
					<li class="layui-nav-item layui-nav-itemed "><a
						class="javascript:;" href="javascript:;">图书管理</a>
						<dl class="layui-nav-child">
							<dd class="layui-this">
								<a href="index.jsp">编辑图书</a>
							</dd>
							<dd>
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
				<div class="layui-row">
					<div class="demoTable">
						搜索图书：
						<div class="layui-inline">
							<input class="layui-input" id="bookname"
								autocomplete="off">
						</div>
						<div class="layui-btn" id="sf" >搜索</div>
						<div class="layui-btn" id="index">返回第一页</div>
						<a class="layui-btn layui-btn-warm" href="addbook.jsp" style="margin-left: 50%">新添图书</a>
					</div>
				</div>
				<table id="books" lay-filter="info"></table>
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
			
{{#  if(d.status == 1){ }}
    <a class="layui-btn layui-btn-danger layui-btn-xs" lay-event="del">下架</a>
  {{#  } }}
{{#  if(d.status == 0){ }}
    <a class="layui-btn layui-btn-normal layui-btn-xs" lay-event="del">上架</a>
  {{#  } }}
		</script>
	<script>
		//JavaScript代码区域
		layui.use('element', function() {
			var element = layui.element;

		});
		layui.use([ 'table', 'laypage' ],function() {
			var table = layui.table, $ = layui.jquery, laypage = layui.laypage;
			//第一个实例
			var  action = table.render({
										elem : "#books",
										url : '${pageContext.request.contextPath}/AdminServlet?method=getAll',
										cols : [ [ {
											field : 'bid',
											title : 'ID',
											width :'5%',
										}, {
											field : 'name',
											title : '图书名',
											width :'17%',
										}, {
											field : 'author',
											title : '作者',
											width :'10%',
										}, {
											field : 'price',
											title : '价格',
											width :'10%',
										}, {
											field : 'content',
											title : '简介',
											width :'30%',
										}, {
											field : 'counts',
											title : '下载量',
											width :'7%',
										}, {
											field : 'created',
											title : '更新时间',
										}, {
											field : 'operation',
											title : '操作',
											toolbar : '#operaction',
										}, ] ],
										page : {
											limit : 10,
											layout : [ 'prev', 'page', 'next',
													'skip', 'count' ]
										},
										defaultToolbar : [ 'filter', 'print',
												'exports' ],
										initSort : {
											//排序
											field : 'bid',
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
									layer.open({
										type:1,
										title:'修改图书',
										content:
										"<div class=\'layui-form-item\'>"+
										" <img src="+data.img+" style=\'width:110px;height:110px;margin-left:400px;\'/>"+
										"    </div>"+
										"<div class=\'layui-form-item\'>"+
										"    <label class=\'layui-form-label\'>图书名：</label>"+
										"    <div class=\'layui-input-block\'>"+
										"      <input type=\'text\' class=\'layui-input\' id =\'bname\' value="+data.name +">"+
										"    </div>"+
										"  </div>"+
										"<div class=\'layui-form-item\'>"+
										"    <label class=\'layui-form-label\'>作者：</label>"+
										"    <div class=\'layui-input-block\'>"+
										"      <input type=\'text\' class=\'layui-input\'  id=\'bauthor\' value="+data.author +">"+
										"    </div>"+
										"  </div>"+
										"<div class=\'layui-form-item\'>"+
										"    <label class=\'layui-form-label\'>价格：</label>"+
										"    <div class=\'layui-input-block\'>"+
										"      <input type=\'text\' class=\'layui-input\' onkeyup='clearNoNum(this)' id=\'bprice\' value="+data.price+">"+
										"    </div>"+
										"  </div>"+
										"<div class=\'layui-form-item layui-form-text\'>"+
										"    <label class=\'layui-form-label\'>简介：</label>"+
										"    <div class=\'layui-input-block\'>"+
										"      <textarea name=\'desc\' id=\'bcontent\' class=\'layui-textarea\'>"+data.content+"</textarea>"+
										"    </div>"+
										"  </div>"
										,
										offset: ['50px', '200px'],
										btn:['提交修改'],
										yes: function(index, dom){
											  //按钮【修改】的回调
											var name = dom.find("#bname").val();
											var author = dom.find("#bauthor").val();
											var price = dom.find("#bprice").val();
											var content = dom.find("#bcontent").val();
											if(data.name === name&&data.author === author&&data.price ===price && data.content ===content){
												layer.msg('未做修改');
											}else if(checkNull(name)||checkNull(author)||checkNull(price)||checkNull(content)){
												layer.msg('请检查空值');
											}else{
												$.ajax({
													type:'POST',
													data:{'method':'editBook','bid':data.bid,'name':name,'author':author,'price':price,'content':content},
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
										  area: ['1000px', '500px']
									});
								}else{
								$.ajax({
									type:'POST',
									url:'${pageContext.request.contextPath}/AdminServlet',
									data:{'method':'deleteBook','bid':data.bid,'status':data.status},
									success:function(data){
										var data = JSON.parse(data).code;
										if(data == 'NOTF'){
											layer.msg('权限不足');
										}else if(data == '0'){
											layer.msg('操作成功');
											action.reload();
										}else{
											layer.msg('未知异常,请联系超管');
										}
									}
								});
								}
							});
						$("#sf").on('click',function() {
								if ($("#bookname").val() == '') {
								alert("请输入书名");
								return false;
								}
								table.reload('retable',{
								url : '${pageContext.request.contextPath}/AdminServlet?method=getbook',//设置异步接口
								where : {//设定异步数据接口的额外参数
									bookname : $("#bookname").val(),
									},
								method : 'post'
									}); 	
							 });
						$("#index").on('click',function() {
							action.reload();
							$("#bookname").val("");
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