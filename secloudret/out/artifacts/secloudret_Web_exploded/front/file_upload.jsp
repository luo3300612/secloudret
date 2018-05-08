<%--
  Created by IntelliJ IDEA.
  User: luo3300612
  Date: 18-3-17
  Time: 下午4:25
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>公钥上传</title>
    <!--Import Google Icon Font-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Import materialize.css-->
    <link type="text/css" rel="stylesheet" href="css/materialize.min.css" media="screen,projection"/>

    <!--Let browser know website is optimized for mobile-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
</head>
<style type="text/css">
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
        text-decoration: none;
    }
</style>
<body>
<div class="container">
    <!-- 主体内容 -->
    <div class="center">
        <div class="login-wrap" style="margin-bottom: 60px; margin-top: 140px">
            <div style="max-width: 540px; margin: 0 auto;">
                <a href="index.jsp" title="点击返回首页"><h3>SeCloudret</h3></a>
            </div>
            <div class="page-header"><h5>文件上传</h5></div>
            <!-- 会员登录表单 -->
            <div class="row">
                <form action="/FileUpload" method="post" enctype="multipart/form-data">
                    <div class="row">
                        <div class="col s6 offset-s3">
                            <div class="file-field input-field">
                                <div class="btn black">
                                    <span>文件</span>
                                    <input type="file" required="required" name="file">
                                </div>
                                <div class="file-path-wrapper">
                                    <input class="file-path validate blue-grey-text" type="text">
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row">
                        <div>
                            <button type="submit" class="btn black">上传</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<!-- //主体内容 -->
</div>
</form>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>
