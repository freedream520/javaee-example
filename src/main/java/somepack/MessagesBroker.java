package somepack;

import javax.ejb.Singleton;
import javax.websocket.Session;
import java.util.HashSet;
import java.util.function.Consumer;

@Singleton
public class MessagesBroker {
    transient HashSet<Session> sessions = new HashSet<>();

    public void add(Session session) {
        sessions.add(session);
    }

    public void remove(Session session) {
        sessions.remove(session);
    }

    public void forEach(Consumer<Session> action) {
        sessions.forEach(action);
    }
}
