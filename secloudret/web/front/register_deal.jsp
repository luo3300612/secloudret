<%@ page import="java.sql.ResultSet" %><%--
  Created by IntelliJ IDEA.
  User: luo3300612
  Date: 18-2-1
  Time: 上午11:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%-- 创建ConnDB类对象 --%>
<jsp:useBean id="conn" scope="page" class="com.tools.ConnDB"/>
<%-- 创建MemberDaImpl类的对象 --%>
<jsp:useBean id="ins_member" scope="page" class="com.dao.MemberDaoImpl"/>
<%-- 创建Member类的对象，并对Member类的所有属性进行赋值 --%>
<jsp:useBean id="member" scope="request" class="com.model.Member">
    <jsp:setProperty name="member" property="*"/>
</jsp:useBean>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    request.setCharacterEncoding("UTF-8");
    String account = member.getAccount();
    ResultSet rs = conn.executeQuery("select * from tb_member where account = '" + account + "'");
    member.setActivate(1);
    String input = request.getParameter("verification");
    String code = session.getAttribute("code").toString();
    if (input.equals(code)){
        try{
            if (rs.next()) {
                out.println("<script language='javascript'>alert('该账号已经存在，请重新注册！');" +
                        "window.location.href='register.jsp';</script>");
            } else {
                int ret = 0;
                ret = ins_member.insert(member);
                if (ret != 0) {
                    session.setAttribute("account", account);
                    out.println("<script language='javascript'>alert('会员注册成功！');"
                            + "window.location.href='index.jsp';</script>");
                } else {
                    out.println("<script language='javascript'>alert('会员注册失败！');"
                            + "window.location.href='register.jsp';</script>");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    else{
        out.println("<script language='javascript'>alert('验证码错误！');"
                + "window.location.href='register.jsp';</script>");
    }


%>
</body>
</html>
