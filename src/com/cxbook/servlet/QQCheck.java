package com.cxbook.servlet;

import com.cxbook.listener.SessionMap;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

/**
 * Servlet implementation class QQCheck
 */
@WebServlet("/QQCheck")
public class QQCheck extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private SessionMap map = SessionMap.getinstance();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public QQCheck() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            // 编码
            response.setContentType("text/html;charset=UTF-8");

            String code = request.getParameter("code");
            String state = request.getParameter("state");
            HttpSession session = map.getSession(state);
            if (session == null) {
                response.getWriter().write("小老弟,你想干啥?");
                return;
            }
            // 创建HttpClient 对象
            CloseableHttpClient httpclient = HttpClients.createDefault();
            // 创建URI并添加参数
            URIBuilder builder = new URIBuilder("https://graph.qq.com/oauth2.0/token");
            builder.addParameter("grant_type", "authorization_code");
            builder.addParameter("client_id", "101530672");
            builder.addParameter("client_secret", "62ccb27d3a7a4b7bb987d0c1b06dfb77");
            builder.addParameter("code", code);
            builder.addParameter("redirect_uri", "https://zxg.cqchunxiu.com/QQCheck");
            // 转换
            URI uri = builder.build();
            // 创建get
            HttpGet get = new HttpGet(uri);
            // 发送
            CloseableHttpResponse execute = httpclient.execute(get);
            // 处理结果
            if (execute.getStatusLine().getStatusCode() == 200) {
                String result = EntityUtils.toString(execute.getEntity(), "UTF-8");
                String[] params = result.split("&");
                String access_token = params[0].split("=")[1];
                //String expires_in = params[1].split("=")[1];  token过期时间
                //String refresh_token = params[2].split("=")[1]; 授权自动续期
                // 创建URI并添加参数
                URIBuilder builder2 = new URIBuilder("https://graph.qq.com/oauth2.0/me");
                builder2.addParameter("access_token", access_token);
                // 转换
                URI uri2 = builder2.build();
                // 创建get
                HttpGet get2 = new HttpGet(uri2);
                // 发送
                CloseableHttpResponse execute2 = httpclient.execute(get2);
                if (execute2.getStatusLine().getStatusCode() == 200) {
                    String result2 = EntityUtils.toString(execute2.getEntity(), "UTF-8");
                    String openid = result2.substring(result2.substring(0,result2.lastIndexOf("\"")).lastIndexOf("\"")+1,result2.lastIndexOf("\""));
                    request.getRequestDispatcher("/UserServlet?method=QQ&access_token="+access_token+"&openid="+openid+"&state="+state).forward(request, response);

                }else {
                    response.sendRedirect(this.getServletContext().getContextPath()+"/login.jsp");
                }

            } else {
                response.sendRedirect(this.getServletContext().getContextPath()+"/login.jsp");
            }

        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        doGet(request, response);
    }

}
