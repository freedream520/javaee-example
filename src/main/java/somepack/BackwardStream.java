package somepack;

import org.slf4j.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import javax.xml.bind.JAXB;
import java.io.StringReader;
import java.io.StringWriter;

public class BackwardStream implements MessageListener {

    @Inject
    private Logger logger;

    @Inject
    MessagesBroker broker;

    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String text = ((TextMessage) message).getText();
                somepack.Message msg = JAXB.unmarshal(new StringReader(text), somepack.Message.class);

                StringWriter xml = new StringWriter();
                JAXB.marshal(msg, xml);

                logger.info("MESSAGE BEAN: Message received: " + xml.toString());
                broker.forEach(xml.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        else {
            logger.warn("is not a text message");
        }
    }

    @PostConstruct
    void postConstruct() {
        logger.info("im created");
    }
}
