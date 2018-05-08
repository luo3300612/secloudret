<%--
  Created by IntelliJ IDEA.
  User: luo3300612
  Date: 18-3-17
  Time: 下午5:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="activate" scope="page" class="com.dao.MemberDaoImpl"/>
<html>
<head>
    <title>Title</title>
</head>
<body>
<%
    activate.activate((session.getAttribute("account")).toString());
    out.println("<script language='javascript'>alert('上传成功！');"
            + "window.location.href='index.jsp';</script>");
%>
</body>
</html>
