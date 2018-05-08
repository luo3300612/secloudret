<%--
  Created by IntelliJ IDEA.
  User: luo3300612
  Date: 18-3-19
  Time: 下午5:08
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<body>

</body>
</html>
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
    <script src="jquery-3.3.1.js"></script>
    <script>
        function add() {
            $("#addto").append("<p><input type='checkbox' name='users' checked='checked' id='" + $("#icon_prefix").val() + "' value='" + $("#icon_prefix").val() + "'/><label for='" + $("#icon_prefix").val() + "'>" + $("#icon_prefix").val() + "</label></p>");
        }
    </script>
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
            <div class="page-header"><h5>公钥搜索</h5></div>
            <div class="row">
                <div class="col s12">
                    <div class="row">
                        <div class="input-field col s3 offset-s4">
                            <i class="material-icons prefix">account_circle</i>
                            <input id="icon_prefix" type="text" class="validate">
                            <label for="icon_prefix">First Name</label>
                        </div>
                        <br>
                        <div class="col s2">
                            <button class="btn black" id="btnSendCode" value="验证码">搜索</button>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row">
                <div class="col s4 offset-s4">
                    <form action="/pubsdownload" method="post">
                        <div class="row">
                            <div class="col s2 offset-s4">
                                <div id="addto">
                                </div>
                            </div>
                        </div>
                        <br>
                        <div class="row">
                            <div>
                                <button type="submit" class="btn black">获取</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<!-- //主体内容 -->
</div>
</form>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="js/materialize.min.js"></script>
<script type="text/javascript">
    $("#btnSendCode").click(function () {
        var ac = $("#icon_prefix").val();
        $.ajax({
            type: "POST",
            url: "/UserSearch",
            data: {account: ac},
            dataType: "text",
            success: function (data) {
                data = parseInt(data, 10);
                if (data == 1) {
                    alert("用户不存在")
                }
                else if (data == 0) {
                    var v = $("#icon_prefix").val();
                    var exist = $("input[value=" + v + "]");
                    if (exist.length >= 1) {
                    }
                    else {
                        add()
                    }
                }
                else {
                    alert("账号未上传公钥")
                }
            }
        });
    });
</script>
</body>
</html>
