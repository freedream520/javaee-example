<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
    <style>
        #timer_lines {
            overflow-y: scroll;
            height: 400px;
        }
    </style>
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
<pre id="message"><c:out value='${message}'/></pre>
<div id="timer_lines"></div>
<div>
    <textarea id="message_text" name="message_text" title="message"></textarea>
    <button id="send_message_mtn" onclick="sendMessage()">Send</button>
</div>
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
        addText(event.data);
    };

    function addText(text) {
        var p = document.createElement("p");
        p.textContent = text;
        window.timer_lines.appendChild(p);
        window.timer_lines.scrollTop = p.offsetTop;
    }

    function showMessage(message) {
        var p = document.createTextNode(message);
        for(var i = 0; i < window.message.childNodes.length; i++) {
            window.message.removeChild(window.message.childNodes.item(i));
        }
        window.message.appendChild(document.createTextNode(message));
    }

    function sendMessage() {
        var text = window.message_text.value;
        var xhr = new XMLHttpRequest();
        xhr.open("POST", window.location, true);
        xhr.onreadystatechange = function () {
            if (this.readyState !== 4) return;
            showMessage(this.responseText);
        };
        var data = new FormData();
        data.append("q", text);
        xhr.send(data);
    }
</script>
</html>