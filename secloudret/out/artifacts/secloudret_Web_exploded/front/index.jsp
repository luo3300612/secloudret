<%--
  Created by IntelliJ IDEA.
  User: luo3300612
  Date: 18-2-2
  Time: 上午2:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<jsp:useBean id="is_activate" scope="page" class="com.dao.MemberDaoImpl"/>
<%--jsp:useBean id="is_activate" scope="page" class="com.dao.MemberDaoImpl"/--%>
<html>
<head>
    <!--Import Google Icon Font-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Import materialize.css-->
    <link type="text/css" rel="stylesheet" href="css/materialize.min.css" media="screen,projection"/>

    <!--Let browser know website is optimized for mobile-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <title>SeCloudret</title>
    <style type="text/css">
        <!--
        a:link {
            font-size: 12px;
            color: #000000;
            text-decoration: none;
        }

        a:visited {
            font-size: 12px;
            color: #000000;
            text-decoration: none;
        }

        a:hover {
            font-size: 12px;
            color: #999999;
            text-decoration: underline;
        }

        .mid-icon {
            font-size: 120px;
        }

        .mid-icon-text {
            color: #5a5a5a;
        }

        .mid-icon-title {
            font-family: 文泉驿微米黑;
            font-size: 20px;
        }
    </style>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col s2">
            <a href="index.jsp"><h3>SeCloudret</h3></a>
            <h6>Secrete Your Cloud!</h6>
        </div>
        <%
            if (session.getAttribute("account") == null) {
        %>
        <div class="col s1 right">
            <br><br><a href="register.jsp"><h6>注册</h6></a>
        </div>
        <div class="col s1 right">
            <br><br><a href="login.jsp"><h6>登录</h6></a>
        </div>
        <%
        } else {
        %>
        <div class="col s1 right">
            <br><br><a href="logout.jsp"><h6>退出</h6></a>
        </div>
        <div class="col s1 right">
            <br><br><a href="logout.jsp"><h6><%=session.getAttribute("account")%>
        </h6></a>
        </div>
        <%
            }
        %>
    </div>
    <div class="divider"></div>
    <div class="container">
        <%
            int rows = 3;
            for (int i = 0; i <= rows; i++) {
        %>
        <div class="row">

        </div>
        <%
            }
        %>
        <div class="row">
            <div class="col s4">
                <div class="center">
                    <%
                        if (session.getAttribute("account") == null) {
                    %>
                    <a href="login.jsp"><i class="material-icons mid-icon">cloud_upload</i></a>
                    <%
                    } else {
                        boolean activate = is_activate.is_activate((session.getAttribute("account")).toString());
                        if(activate){
                    %>
                    <a href="file_upload.jsp"><i class="material-icons mid-icon">cloud_upload</i></a>
                    <%
                        }
                        else{
                    %>
                    <a href="key_upload.jsp"><i class="material-icons mid-icon">cloud_upload</i></a>
                    <%
                        }
                        }
                    %>
                    <p class="promo-caption mid-icon-title">文件上传</p>
                    <p class="light mid-icon-text">
                        通过这个按钮上传你的文件
                    </p>
                </div>
                <!-- Promo Content 1 goes here -->
            </div>
            <div class="col s4" align="center">
                <!-- Promo Content 2 goes here -->
                <div class="center">
                    <a href="file_download.jsp"><i class="material-icons mid-icon">cloud_download</i></a>
                    <p class="promo-caption mid-icon-title">文件下载</p>
                    <p class="light mid-icon-text">
                        通过这个按钮下载你的文件
                    </p>
                </div>
            </div>
            <div class="col s4" align="center">
                <!-- Promo Content 3 goes here -->
                <div class="center">
                    <a href="index.jsp"><i class="material-icons mid-icon">arrow_downward</i></a>
                    <p class="promo-caption mid-icon-title">工具下载</p>
                    <p class="light mid-icon-text">
                        通过这个按钮下载文件加密工具
                    </p>
                </div>
            </div>
        </div>
        <div class="row">
            <div class="col s4 offset-s2" align="center">
                <div class="center">
                    <a href="user_search.jsp"><i class="material-icons mid-icon">vpn_key</i></a>
                    <p class="promo-caption mid-icon-title">公钥查询</p>
                    <p class="light mid-icon-text">
                        通过这个按钮查询其他用户的公钥
                    </p>
                </div>
            </div>
            <div class="col s4" align="center">
                <div class="center">
                    <a href="/sharedownload"><i class="material-icons mid-icon">folder_shared</i></a>
                    <p class="promo-caption mid-icon-title">文件获取</p>
                    <p class="light mid-icon-text">
                        通过这个按钮获取被分享的文件
                    </p>
                </div>
            </div>
        </div>
    </div>
    <%
        for (int i = 0; i <= rows; i++) {
    %>
    <div class="row">

    </div>
    <%
        }
    %>
    <div class="divider"></div>
    <div class="row center">
        <p>
            五个步骤，实现您的云端安全！
        </p>
    </div>
</div>
<!--Import jQuery before materialize.js-->
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>
