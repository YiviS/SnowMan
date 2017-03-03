<%--
  Created by IntelliJ IDEA.
  User: XUGANG
  Date: 2017/2/20
  Time: 10:27
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ include file="/WEB-INF/jsp/common/meta.jsp" %>
<html lang="en" class="no-js">
<head>
    <title>登录</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content="">
    <meta name="author" content="">
    <!-- CSS -->
    <link rel='stylesheet' href='${basePath}/plugins/googleapis/fonts/fonts.css'>
    <link rel='stylesheet' href='${basePath}/plugins/layer/skin/default/layer.css'>
    <link rel="stylesheet" href="${basePath}/static/css/login/reset.css">
    <link rel="stylesheet" href="${basePath}/static/css/login/supersized.css">
    <link rel="stylesheet" href="${basePath}/static/css/login/style.css">
    <style type="text/css">

    </style>
</head>
<body>
<div class="page-container">
    <h1>Login</h1>
    <form id="_form" action="" method="post">
        <div class="contain">
            <input type="text" name="username" class="username" placeholder="Username">
            <input type="password" name="password" class="password" placeholder="Password">
        </div>
        <%-- 滑动验证 --%>
        <div class="box">
            <span id="wait" class="show">正在加载验证码......</span>
            <div class="box-captcha" id="float-captcha">
            </div>
        </div>
        <%-- 错误提示 --%>
        <div id="messageBox" class="alert-error ${empty message ? 'hide' : ''}">
            <label class="loginError">${message}</label>
        </div>

        <button type="button" id="login">Sign me in</button>
        <div class="remember-me">
            <input type="checkbox" id="rememberMe" name="rememberMe" value="true">
            <label for="rememberMe">记住我</label>
            <a href="">忘记密码？</a>
        </div>
        <div class="error"><span>+</span></div>
    </form>
    <div class="connect">
        <p>Or connect with:</p>
        <p>
            <a class="qq" href=""></a>
            <a class="wechat" href=""></a>
            <a class="sina" href=""></a>
            <a class="register" href=""></a>
        </p>
    </div>
    <div align="center">Copyright © 2017 <a href="https://github.com/Yivis" target="_blank"
                                            style="text-decoration: none" title="Yivis">github.com/Yivis</a></div>
</div>

<!-- Javascript -->
<script src="${basePath}/plugins/jquery/jquery-1.8.2.min.js"></script>
<script src="${basePath}/plugins/supersized/supersized.3.2.7.min.js"></script>
<script src="${basePath}/plugins/supersized/supersized-init.js"></script>
<script src="${basePath}/plugins/layer/layer.js"></script>
<%--<script src="${basePath}/static/js/login/scripts.js"></script>--%>
<script src="${basePath}/static/js/login/gt.js"></script>
<script>

    jQuery(document).ready(function () {
        var geetest_challenge;
        var geetest_validate;
        var geetest_seccode;
        try {
            var _href = window.location.href + "";
            if (_href && _href.indexOf('?kickout') != -1) {
                layer.msg('您已经被踢出，请重新登录！');
            }
        } catch (e) {

        }
        //回车事件绑定
        document.onkeydown = function (event) {
            var e = event || window.event || arguments.callee.caller.arguments[0];
            if (e && e.keyCode == 13) {
                $('#login').click();
            }
        };

        //登录操作
        $('#login').click(function () {
            var username = $('.username').val();
            var password = $('.password').val();
            if (username == '') {
                $('.error').fadeOut('fast', function () {
                    $('.error').css('top', '27px').show();
                });
                $('.error').fadeIn('fast', function () {
                    $('.username').focus();
                });
                return false;
            }
            if (password == '') {
                $('.error').fadeOut('fast', function () {
                    $('.error').css('top', '96px').show();
                });
                $(this).find('.error').fadeIn('fast', function () {
                    $('.password').focus();
                });
                return false;
            }
            var data = {
                password: password,
                username: username,
                rememberMe: $("#rememberMe").is(':checked'),
                "geetest_challenge": geetest_challenge,
                "geetest_validate": geetest_validate,
                "geetest_seccode": geetest_seccode
            };
            var load = layer.load();
            $.post("${basePath}/submitLogin", data, function (result) {
                layer.close(load);
                if (result && result.status != 200) {
                    layer.msg(result.message, function () {
                    });
                    $('.password').val('');
                    return;
                } else {
                    layer.msg('登录成功！');
                    setTimeout(function () {
                        //登录返回
                        window.location.href = result.back_url || "${basePath}/";
                    }, 1000)
                }
            }, "json");

        });
        $('.page-container form .username, .page-container form .password').keyup(function () {
            $(this).parent().find('.error').fadeOut('fast');
        });

        var handlerEmbed = function (captchaObj) {
            // 成功的回调
            captchaObj.onSuccess(function () {
                var validate = captchaObj.getValidate();
                geetest_challenge = validate.geetest_challenge;
                geetest_validate = validate.geetest_validate;
                geetest_seccode = validate.geetest_seccode;
            })
            // 将验证码加到id为captcha的元素里，同时会有三个input的值：geetest_challenge, geetest_validate, geetest_seccode
            captchaObj.appendTo("#float-captcha"); // 包括：float-captcha，embed-captcha，popup-captcha
            captchaObj.onReady(function () {        //captchaObj 初始化时隐藏 wait
                $("#wait")[0].className = "hide";
            });
            // 更多接口参考：http://www.geetest.com/install/sections/idx-client-sdk.html
        };
        $.ajax({
            // 获取id，challenge，success（是否启用failback）
            url: "${basePath}/pc/geetest/register?t=" + (new Date()).getTime(), // 加随机数防止缓存
            type: "get",
            dataType: "json",
            success: function (data) {
                // 使用initGeetest接口
                // 参数1：配置参数
                // 参数2：回调，回调的第一个参数验证码对象，之后可以使用它做appendTo之类的事件
                initGeetest({
                    gt: data.gt,
                    challenge: data.challenge,
                    product: "float", // 产品形式，包括：float，embed，popup。注意只对PC版验证码有效
                    offline: !data.success // 表示用户后台检测极验服务器是否宕机，一般不需要关注
                    // 更多配置参数请参见：http://www.geetest.com/install/sections/idx-client-sdk.html#config
                }, handlerEmbed);
            }
        });
    });


</script>
</body>
</html>
