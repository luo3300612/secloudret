<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>登录</title>
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
            <div class="login">
                <div class="page-header"><h5>登录</h5></div>
                <!-- 会员登录表单 -->
                    <div class="row">
                        <form action="login_check.jsp" method="post" class="form-horizontal">
                            <div class="row">
                                <div class="input-field col s4 offset-s4">
                                    <i class="material-icons prefix">account_circle</i>
                                <input id="account" name="account" type="text" class="validate" required="required">
                                    <label for="account">账号</label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="input-field col s4 offset-s4">
                                    <i class="material-icons prefix">vpn_key</i>
                                    <input id="password" name="password" type="password" class="validate" required="required">
                                    <label for="password">密码</label>
                                </div>
                            </div>
                            <div class="row">
                                <div class="input-field col s2 offset-s4">
                                    <i class="material-icons prefix">verified_user</i>
                                    <input id="checkCode" name="checkCode" type="text" class="validate" required="required">
                                    <label for="checkCode">验证码</label>
                                </div>
                                <div class="col s2">
                                    <!-- 显示验证码 -->
                                    <img src="../CheckCode" name="img_checkCode" width="116" height="43"
                                         class="img_checkcode" id="img_checkCode" onClick="myReload()"/>
                                </div>
                            </div>
                            <div class="row">
                                <div>
                                    <button type="submit" class="btn black">登录</button>
                                </div>
                            </div>
                            <div class="row s4 offset-s4">
                                <label
                                        style="color: #858585;font-size: 14px;">没有账户？<a
                                        href="register.jsp">立即注册</a></label>
                            </div>
                        </form>
                    </div>
            </div>
        </div>
    </div>
    <!-- //主体内容 -->
</div>
<script language="javascript">
    //刷新验证码
    function myReload() {
        document.getElementById("img_checkCode").src = document
            .getElementById("img_checkCode").src + "?nocache=" + new Date().getTime();
    }
</script>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="js/materialize.min.js"></script>
</body>
</html>