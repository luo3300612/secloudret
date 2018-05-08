<%--
  Created by IntelliJ IDEA.
  User: luo3300612
  Date: 18-3-19
  Time: 下午5:42
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
</head>
<script src="jquery-3.3.1.js"></script>
<script>
    $(document).ready(function(){
        $("#add").click(function(){
            $("#addto").append('  <input type="checkbox" name="animal">傻瓜');
        });
    });
</script>
</head>
<body>
<div id="addto">
    <input type="checkbox" name="animal" id="cat">大象
    <input type="checkbox" name="animal" id="dog">老虎
    <input type="checkbox" name="animal" id="pig">狮子
</div>
<br/>
<input type="button" name="add" id="add" value="添加">
</body>
</html>
