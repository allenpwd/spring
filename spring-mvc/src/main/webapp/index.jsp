<%@ page import="pwd.allen.entity.Fruit" %>
<%
    Fruit fruit = new Fruit();
    fruit.say("fuck you");
    fruit.setName("i am a fruit!");
%>
<html>
<body>
<h2>Hello World!<%=fruit.getName()%></h2>
</body>
</html>
