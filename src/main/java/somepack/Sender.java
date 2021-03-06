package somepack;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.inject.Inject;
import javax.jms.*;
import javax.xml.bind.JAXB;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Singleton
public class Sender {
    @Resource(lookup = "java:jboss/jms/ivt/IVTCF")
    private ConnectionFactory connectionFactory;

    @Resource(lookup = "java:jboss/jms/ivt/IVTQueue")
    private Queue queue;

    @Inject
    private Logger logger;

    public Map<String, Object> getStatus() {
        LinkedHashMap<String, Object> map = new LinkedHashMap<String, Object>();

        map.put("logger", logger);
        map.put("connectionFactory", connectionFactory);
        map.put("queue", queue);

        return map;
    }

    public String sendMessage(String text) {
        Throwable toReturn = null;
        try {
            logger.info("Try to send message");
            Connection connection = connectionFactory.createConnection();
            Session session = connection.createSession(false, Session.SESSION_TRANSACTED);
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

    @Schedule(second = "*/3", minute="*", hour="*", persistent = false)
    void scheduled() {
        Message msg = new Message();
        msg.setTimestamp(new Date(System.currentTimeMillis()));
        msg.setText("main time is " + String.valueOf(System.currentTimeMillis()));
        msg.setClientId("internal");
        StringWriter xml = new StringWriter();
        JAXB.marshal(msg, xml);
        sendMessage(xml.toString());
    }

    @PostConstruct
    public void postConstruct() {
        logger.info("sender constructed");
        try {
            logger.info("with queue name: " + queue.getQueueName());
        } catch (JMSException e) {
            logger.error("errr", e);
        }
    }
}
