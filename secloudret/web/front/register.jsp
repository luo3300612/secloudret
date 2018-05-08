<%--
  Created by IntelliJ IDEA.
  User: luo3300612
  Date: 18-2-12
  Time: 上午12:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <!--Import Google Icon Font-->
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <!--Import materialize.css-->
    <link type="text/css" rel="stylesheet" href="css/materialize.min.css" media="screen,projection"/>

    <!--Let browser know website is optimized for mobile-->
    <meta name="viewport" content="width=device-width, initial-scale=1.0"/>
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet">
    <script src="jquery-3.3.1.js"></script>
    <title>注册</title>
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
        text-decoration: underline;
    }

</style>
<body>
<div class="container center" style="margin-bottom: 60px; margin-top: 25px">
    <div class="row"><a href="index.jsp"
    ><h3>
        SeCloudret</h3></a></div>
    <div class="row">
        <form method="post" action="register_deal.jsp" onsubmit="return regis();">
            <div class="row">
                <h5>注册</h5>
            </div>
            <div class="row">
                <div class="input-field col s4 offset-s4">
                    <i class="material-icons prefix" id="acTip">account_circle</i>
                    <input id="account" name="account" type="text" class="validate" required="required" focus="">
                    <label for="account" id="aclb">账号</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s4 offset-s4">
                    <i class="material-icons prefix" id="pswTip0">vpn_key</i>
                    <input id="password" name="password" type="password" class="validate" required="required">
                    <label for="password" id="pswlb0">密码</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s4 offset-s4">
                    <i class="material-icons prefix" id="pswTip">vpn_key</i>
                    <input id="password2" type="password" class="validate" required="required">
                    <label for="password2" id="pswlb">确认密码</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s4 offset-s4">
                    <i class="material-icons prefix" id="telTip">phone_android</i>
                    <input id="tel" name="tel" type="text" class="validate" required="required">
                    <label for="tel" id="tellb">手机号</label>
                </div>
            </div>
            <div class="row">
                <div class="input-field col s2 offset-s4">
                    <i class="material-icons prefix" id="verTip">verified_user</i>
                    <input id="verification" name="verification" type="text" class="validate" required="required">
                    <label for="tel" id="verlb">验证码</label>
                </div>
                <div class="col s2">
                    <button class="btn black" onclick="sendMessage()" id="btnSendCode" value="验证码">验证码</button>
                </div>
            </div>
            <div class="row">
                <button type="submit" class="btn black">注册</button>
            </div>
            <div class="row s4 offset-s4">
                <label
                        style="color: #858585;font-size: 14px;">已有账号<a
                        href="login.jsp">去登录</a></label>
            </div>
        </form>
    </div>
</div>
</body>
<script>
    function regis() {
        /* ----------- 验证输入的账户是否合法 ------------------------- */
        if (/[\u4e00-\u9fa5]/.test($('#account').val())) {
            alert("账户不能输入汉字！");
            return false;
        }
        if (document.getElementById("account").value.length > 20) {
            alert("账户不能超过20位！")
            return false;
        }
        /* ----------- 验证输入的联系电话是否合法 --------------------- */
        if (isNaN($('#tel').val())) {
            alert("联系电话请输入数字");
            return false;
        }
        /* ----------- 验证两次输入的密码是否一致 --------------------- */
        var pwd = document.getElementById("password").value;
        var pwd2 = document.getElementById("password2").value;
        if (pwd !== pwd2) {
            alert('密码前后不一致！');
            return false;
        }
        if (document.getElementById("password").value.length > 20) {
            alert("密码不能超过20位！")
            return false;
        }
        return true;
    }

    var InterValObj; //timer变量，控制时间
    var count = 60; //间隔函数
    var curCount; //当前剩余秒数
    var code = ""; //验证码
    var codeLength = 6; //验证码长度
    var TipWord = "重新发送";

    function sendMessage() {
        curCount = count;
        var tel = $("#tel").val();
        var telTip = $("#telTip").text();
        if (tel != "") {
            var reg = /^0?(13[0-9]|15[012356789]|18[012356789]|14[57]|17[0-9]|199)[0-9]{8}$/;
            if (!reg.test($("#tel").val())) {
                $("#telTip").css("color", "red")
                $("#tellb").css("color", "red")
                $("#telTip").attr("title", "请输入正确的11位手机号")
                return false;
            }
            // 产生验证码
            for (var i = 0; i < codeLength; i++) {
                code += parseInt(Math.random() * 9).toString();
            }
            $("#btnSendCode").attr("disabled", "true");
            $("#btnSendCode").html("重新发送" + curCount + "秒");
            InterValObj = window.setInterval(SetRemainTime, 1000); //计时器
            $.ajax({
                type: "POST",
                dataType: "text",
                url: "/Message",
                data: "tel=" + tel + "&code=" + code,
                error: function (XMLHttpRequest, textStatus, errorThrown) {

                },
                success: function (data) {
                    data = parseInt(data, 10);
                    if (data == 1) {
                        TipWord = "重新发送";
                    } else if (data == 0) {
                        TipWord = "发送失败";
                        return false;
                    } else if (data == 2) {
                        TipWord = "操作频繁";
                        return false;
                    } else if (data == 3) {
                        TipWord = "手机号已注册";
                        return false;
                    }
                }
            });
        } else {

        }
    }

    function SetRemainTime() {
        if (curCount == 0) {
            window.clearInterval(InterValObj);
            $("#btnSendCode").removeAttr("disabled");
            $("#btnSendCode").html("重新发送");
            code = "";
        } else {
            curCount--;
            $("#btnSendCode").html(TipWord + curCount + "秒");
        }
    }


    $("#verification").blur(function () {
        var SmsCheckCodeVal = $("#verification").val();
        $.ajax({
            url: "/Message",
            data: {input: SmsCheckCodeVal},
            dataType: "text",
            success: function (data) {
                data = parseInt(data, 10);
                if (data == 1) {
                    $("#verTip").css("color", "green")
                    $("#verlb").css("color", "green")
                    $("#verTip").attr("title", "验证码正确")
                }
                else {
                    $("#verTip").css("color", "red")
                    $("#verlb").css("color", "red")
                    $("#verTip").attr("title", "验证码错误")
                }

            }
        });
        return true;
    });
    $("#account").blur(function () {
        if (/[\u4e00-\u9fa5]/.test($('#account').val()) || $("#account").val() == "" || document.getElementById("account").value.length > 20 || document.getElementById("account").value.length < 6) {
            $("#acTip").css("color", "red")
            $("#aclb").css("color", "red")
            $("#acTip").attr("title", "账号不能输入汉字，长度在6~20位之间")
        }
        else {
            $("#acTip").css("color", "green")
            $("#aclb").css("color", "green")
        }
        var ac = $("#account").val();
        $.ajax({
            type:"POST",
            url:"/account",
            data:{account:ac},
            dataType:"text",
            success: function (data) {
                data = parseInt(data, 10);
                if (data == 1) {
                    $("#acTip").css("color", "green")
                    $("#aclb").css("color", "green")
                }
                else {
                    $("#acTip").css("color", "red")
                    $("#aclb").css("color", "red")
                    $("#acTip").attr("title", "账号已注册")
                }
            }
        });
        });
    $("#tel").blur(function () {
        var reg = /^0?(13[0-9]|15[012356789]|18[012356789]|14[57]|17[0-9]|199)[0-9]{8}$/;
        if (!reg.test($("#tel").val())) {
            $("#telTip").css("color", "red")
            $("#tellb").css("color", "red")
            $("#telTip").attr("title", "请输入正确的11位手机号")
        }
        else {
            $("#telTip").css("color", "green")
            $("#tellb").css("color", "green")
        }

    });
    $("#password2").blur(function () {
        if ($("#password").val() != $("#password2").val() || $("#password2").val() == "" || document.getElementById("password").value.length < 6 || document.getElementById("password").value.length > 20) {
            $("#pswTip").css("color", "red")
            $("#pswlb").css("color", "red")
            $("#pswTip").attr("title", "密码位数为6~20位")
            $("#pswTip0").css("color", "red")
            $("#pswlb0").css("color", "red")
            $("#pswTip0").attr("title", "密码位数为6~20位")
        }
        else {
            $("#pswTip").css("color", "green")
            $("#pswlb").css("color", "green")
            $("#pswTip0").css("color", "green")
            $("#pswlb0").css("color", "green")
        }
    });

</script>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.2.1.min.js"></script>
<script type="text/javascript" src="js/materialize.min.js"></script>
</html>
