<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>

	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<title>首页</title>
		<link rel="icon" href="img/icon/favicon.ico" type="image/x-icon">
		<meta name="renderer" content="webkit">
		<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
		<link rel="stylesheet" href="layui/css/layui.css">
		<link rel="stylesheet" href="css/index.css" />
	</head>

	<body>
		<!-- 顶部  -->
		<div class="layui-container">
			<div class="layui-row">
				<div class="layui-col-xs5 layui-col-lg3">
					<a href="index.jsp"><img src="img/logo.png" style="width: 100%; margin-top: 5%;" /></a>
				</div>
			</div>
		</div>

		<!-- 导航-->
		<div class="layui-fluid">
			<ul class="layui-nav  nav" lay-filter="list">
				<c:forEach items="${category}" varStatus="i" var="category">
					<li class="layui-nav-item">
						<a href="${pageContext.request.contextPath}/BookServlet?method=category&cid=${category.cid}&currentpage=1">${category.cname}</a>
					</li>
				</c:forEach>
				<c:choose>
					<c:when test="${not empty sessionScope.user}">
						<li class="layui-nav-item user">
							<a>
								<img src='${sessionScope.user.img}' class="layui-nav-img">${sessionScope.user.username}</a>
							<dl class="layui-nav-child">
								<dd>
									<a href="${pageContext.request.contextPath}/UserServlet?method=logout">退出登录</a>
								</dd>
							</dl>
						</li>
						<li class="layui-nav-item user">
							<a href="user.jsp">个人中心<span class="layui-badge-dot layui-bg-dot"></span></a>
						</li>
						<li class="layui-nav-item user">
							<a href="usepay.jsp">充值</a>
						</li>
					</c:when>
					<c:otherwise>
						<li class="layui-nav-item user">
							<a href="login.jsp">登录</a>
						</li>
						<li class="layui-nav-item user">
							<a href="register.jsp">注册</a>
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
		<!-- 主体-->
		<div class="my-body">
			<div class="layui-carousel" id="carousel">
				<div carousel-item>
					<div>
						<img src="http://img62.ddimg.cn/upload_img/00087/geq/750x315-1542961155.jpg">
					</div>
					<div>
						<img src="http://img60.ddimg.cn/upload_img/00782/liuxin/750x315-1542713344.jpg">
					</div>
					<div>
						<img src="http://img62.ddimg.cn/upload_img/00087/hw/750x315_dl_1122-1542966714.jpg">
					</div>
					<div>
						<img src="http://img60.ddimg.cn/cuxiao/xxgt-1114-675240.jpg">
					</div>
				</div>
			</div>
			<!-- 下载榜 -->
			<div class="download">
				<table lay-filter="countBooks" id="countBook">
				</table>
			</div>
			<!-- 图书简介 -->
			<div class="bookinfo">
				<ul>
					<c:forEach items="${indexpage.books}" varStatus="i" var="book">
						<li>
							<a href="<%=request.getServletContext().getContextPath()%>/BookServlet?method=getbook&bid=${book.bid}"><img src="${book.image}" style="cursor: pointer;"> <br />
								<p>${book.name}</p>
							</a> <span>${book.author} </span></li>
					</c:forEach>
				</ul>
			</div>
		</div>
		<!-- 新书榜 -->
		<div class="newbook">
			<table lay-filter="newBooks" id="newBook">
			</table>
		</div>
		<!--描述：分页 -->
		<div class="indexpage">
			<ul class="pagination">
				<%--首页 --%>
				<c:if test="${indexpage.currentpage+1 == 1}">
					<li>
						<a href="#" class="no">首页</a>
					</li>
					<li>
						<a href="#" class="no">«</a>
					</li>
				</c:if>
				<c:if test="${indexpage.currentpage+1 != 1}">
					<li>
						<a href="<%=request.getServletContext().getContextPath()%>/BookServlet?method=indexPage&currentpage=1">首页</a>
					</li>
					<li>
						<a href="<%=request.getServletContext().getContextPath()%>/BookServlet?method=indexPage&currentpage=${indexpage.currentpage}">«</a>
					</li>
				</c:if>
				<%--中间页--%>
				<%--显示6页中间页[begin=起始页,end=最大页]--%>
				<c:choose>
					<%--总页数没有5页--%>
					<c:when test="${indexpage.countpage <=5 }">
						<c:set var="begin" value="1" />
						<c:set var="end" value="${indexpage.countpage}" />
					</c:when>
					<%--页数超过了5页--%>
					<c:otherwise>
						<c:set var="begin" value="${indexpage.currentpage }" />
						<c:set var="end" value="${indexpage.currentpage +4 }" />
						<%--如果begin减1后为0,设置起始页为1,最大页为5--%>
						<c:if test="${begin == 0}">
							<c:set var="begin" value="1" />
							<c:set var="end" value="5" />
						</c:if>
						<%--如果end超过最大页,设置起始页=最大页-5--%>
						<c:if test="${end > indexpage.countpage}">
							<c:set var="begin" value="${indexpage.countpage - 5}" />
							<c:set var="end" value="${indexpage.countpage}" />
						</c:if>
					</c:otherwise>
				</c:choose>
				<%--遍历--%>
				<c:forEach var="i" begin="${begin}" end="${end}">
					<%--当前页,选中--%>
					<c:choose>
						<c:when test="${i == indexpage.currentpage+1}">
							<li>
								<a href="#" class="active">${i}</a>
							</li>
						</c:when>
						<%--不是当前页--%>
						<c:otherwise>
							<li>
								<a href="<%=request.getServletContext().getContextPath()%>/BookServlet?method=indexPage&currentpage=${i}">${i}</a>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
				<%--尾页 --%>
				<c:if test="${indexpage.currentpage+1 == indexpage.countpage}">
					<li>
						<a href="#" class="no">»</a>
					</li>
					<li>
						<a href="#" class="no">尾页</a>
					</li>
				</c:if>
				<c:if test="${indexpage.currentpage+1 != indexpage.countpage}">
					<li>
						<a href="<%=request.getServletContext().getContextPath()%>/BookServlet?method=indexPage&currentpage=${indexpage.currentpage+2}">»</a>
					</li>
					<li>
						<a href="<%=request.getServletContext().getContextPath()%>/BookServlet?method=indexPage&currentpage=${indexpage.countpage}">尾页</a>
					</li>
				</c:if>
			</ul>
		</div>
		<div class=" layui-bg-black footer">
			<h3>Copyright © 2018&nbsp;&nbsp;纯袖科技&nbsp;&nbsp;渝ICP备17004144号</h3>
		</div>
		<!--layui js-->
		<script type="text/javascript" src="layui/layui.js"></script>
		<script>
			//导航栏
			layui.use('element', function() {
				var element = layui.element;
				//导航点击
				element.on('nav(list)', function(list) {})
			});

			//轮播
			layui.use('carousel', function() {
				var carousel = layui.carousel;
				//实例
				carousel.render({
					elem: '#carousel',
					width: '100%',
					arrow: 'always',
					anim: 'default',
					interval: '2500'
				});
			});

			//排行榜初始化
			layui.use('table', function() {
				var table = layui.table;
				//实例化
				table.render({
					elem: '#newBook',
					height: 430,
					url: './BookServlet?method=newBook', //数据接口
					page: false, //开启分页
					cols: [
						[ //表头
							{
								field: 'username',
								title: '<h2>新书榜</h2>',
								align: 'left',
								templet: function(d) {
									return "<a href ='./BookServlet?method=getbook&bid=" +
										d.bid + "' title='点击查看'>" + d.username +
										"</a>"
								}
							}
						]
					],
					skin: 'nob',
				});
				table.render({
					elem: '#countBook',
					height: 430,
					url: './BookServlet?method=getDownloadList', //数据接口
					page: false, //开启分页
					cols: [
						[ //表头
							{
								field: 'username',
								title: '<h2>下载总榜</h2>',
								align: 'left',
								templet: function(d) {
									return "<a href ='./BookServlet?method=getbook&bid=" +
										d.bid + "' title='点击查看'>" + d.username +
										"</a>"
								}
							}
						]
					],
					skin: 'nob',
				});
			});
		</script>
	</body>

</html>