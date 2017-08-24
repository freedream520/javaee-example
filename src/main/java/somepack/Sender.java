package somepack;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.*;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedHashMap;
import java.util.Map;

@Singleton
public class Sender {
    @Resource(lookup = "java:/ConnectionFactory")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:/jms/queue/ExampleQueue")
    private Queue queue;

    @Inject
    private Logger logger;

    public Map<String, String> getStatus() {
        LinkedHashMap<String, String> map = new LinkedHashMap<String, String>();

        map.put("logger", String.valueOf(logger == null));
        map.put("connectionFactory", String.valueOf(connectionFactory == null));
        map.put("queue", String.valueOf(queue == null));

        return map;
    }

    public String sendMessage(String text) {
        Throwable toReturn = null;
        try {
            logger.info("Try to send message <" + text + ">");
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            MessageProducer messageProducer = session.createProducer(queue);
            messageProducer.send(session.createTextMessage(text));
            session.close();
            connection.close();
        } catch (JMSException | NullPointerException e) {
            logger.error("err " + text, e);
            toReturn = e;
        }
        if (toReturn == null) {
            return "no errors";
        }
        StringWriter sw = new StringWriter();
        toReturn.printStackTrace(new PrintWriter(sw));
        return sw.toString();
    }

    @Schedule(second = "*/2", minute="*", hour="*", persistent = false)
    void scheduled() {
        sendMessage("main time is " + String.valueOf(System.currentTimeMillis()));
    }

    @PostConstruct
    public void postConstruct() {
        logger.info("sender constructed");
    }
}
