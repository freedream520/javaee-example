package somepack;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.websocket.Session;
import java.io.IOException;
import java.util.HashSet;

@Singleton
public class MessagesBroker {
    @Inject
    transient Logger logger;

    transient HashSet<Session> sessions = new HashSet<>();

    public void add(Session session) {
        sessions.add(session);
        logger.info("connected client id: " + session.getId());
    }

    public void remove(Session session) {
        sessions.remove(session);
        logger.info("disconnected client id: " + session.getId());
    }

    public void forEach(String message) {
        sessions.forEach(s -> {
            try {
                s.getBasicRemote().sendText(message);
            } catch (IOException e) {
                logger.error("cant't send message to session " + s.getId(), e);
            }
        });
    }

    @PostConstruct
    void postConstruct() {
        logger.info("websocket msg broker constructed");
    }

    public boolean isEmpty() {
        return sessions.isEmpty();
    }
}
