package somepack;

import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint("/messages")
public class MessagesSocket {
    @Inject
    MessagesBroker broker;

    @OnOpen
    public void onOpen(Session session) {
        broker.add(session);
    }

    @OnClose
    public void onClose(Session session) {
        broker.remove(session);
    }

}
