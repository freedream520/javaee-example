<%@ taglib uri = "http://java.sun.com/jsp/jstl/core" prefix = "c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title></title>
  </head>
  <body>
  <table>
    <thead>
    <tr>
      <th>variable</th>
      <th>is null</th>
    </tr>
    </thead>
    <tbody>
    <c:forEach items="${status.keySet()}" var="key">
      <tr>
        <td><c:out value="${key}"/></td>
        <td><c:out value="${status[key]}"/></td>
      </tr>
    </c:forEach>
    </tbody>
  </table>
  <pre><c:out value='${message}'/></pre>
  <div id="timer_lines"></div>
  </body>
<script type="application/javascript">
    var loc = window.location, new_uri;
    if (loc.protocol === "https:") {
        new_uri = "wss:";
    } else {
        new_uri = "ws:";
    }
    new_uri += "//" + loc.host;
    new_uri += loc.pathname + "/../messages";
    var socket = new WebSocket(new_uri);
    socket.onmessage = function (event) {
        var p = document.createElement("p");
        p.textContent = event.data;
        window.timer_lines.appendChild(p);
    }
</script>
</html>