<%--
  Created by IntelliJ IDEA.
  User: luo3300612
  Date: 18-1-31
  Time: 下午6:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.sql.ResultSet" %>
<jsp:useBean id="conn" scope="page" class="com.tools.ConnDB"></jsp:useBean>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    request.setCharacterEncoding("UTF-8");
    String account = request.getParameter("account");
    String checkCode = request.getParameter("checkCode");
    String rd = session.getAttribute("randCheckCode").toString();
    if (checkCode.equals(session.getAttribute("randCheckCode").toString())) {
        try {
            ResultSet rs = conn.executeQuery("select * from tb_member where account='"
                    + account + "'");
            if (rs.next()) {
                String PWD = request.getParameter("password");
                int activate = rs.getInt("activate");
                if (PWD.equals(rs.getString("psw"))) {
                    session.setAttribute("account", account);
                    session.setAttribute("activate", activate);
                    response.sendRedirect("index.jsp");
                } else {
                    out.println(
                            "<script language='javascript'>alert('密码错误！');"
                                    + "window.location.href='login.jsp';</script>"
                    );
                }
            } else {
                out.println(
                        "<script language='javascript'>alert('密码错误！');"
                                + "window.location.href='login.jsp';</script>"
                );
            }
        } catch (Exception e) {
            out.println(
                    "<script language='javascript'>alert('您的操作有误！');"
                            + "window.location.href='login.jsp';</script>"
            );
        }
        conn.close();
    } else {
        out.println("<script language='javascript'>alert('您输入的验证码有错误！');history.back();</script>");
    }
%>
</body>
</html>
