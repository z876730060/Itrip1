<%--
  Created by IntelliJ IDEA.
  User: zth
  Date: 2021/1/13
  Time: 13:52
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Title</title>
    <script type="text/javascript" src="/auth/js/jQ1.8.3.js"></script>
    <script>
        $(function () {
            $.ajax({
                url:'/auth/Login',
                type:'get',
                data:{name:7,password:8},
                success:function (data) {
                    $("body").val(data);
                }

            })
        })
    </script>
</head>
<body>

</body>
</html>
