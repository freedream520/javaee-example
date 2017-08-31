package somepack;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.*;
import javax.inject.Inject;
import javax.jms.*;
import javax.jms.Message;
import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.io.StringWriter;

@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class BackwardStream implements MessageListener {

    @Inject
    private Logger logger;

    @Inject
    MessagesBroker broker;

    public void onMessage(Message message) {
        if (broker.isEmpty()) {
            try {
                message.acknowledge();
            } catch (JMSException e) {
                logger.error("err", e);
            }
        }
        if (message instanceof TextMessage) {
            try {
                String text = ((TextMessage) message).getText();
                somepack.Message msg = JAXB.unmarshal(new StringReader(text), somepack.Message.class);

                StringWriter xml = new StringWriter();
                JAXB.marshal(msg, xml);

                logger.info("MESSAGE BEAN: Message received: " + xml.toString() + " from " + message.getClass());
                broker.forEach(xml.toString() + " from " + message.getClass());
            } catch (Exception e) {
                logger.error("err", e);
                try {
                    broker.forEach(((TextMessage) message).getText());
                } catch (JMSException e1) {
                    logger.error("errr", e1);
                }
            }
        }
        else {
            logger.warn("is not a text message");
        }
    }

    @PostConstruct
    void postConstruct() {
        logger.info("im created " + this);
    }
}
