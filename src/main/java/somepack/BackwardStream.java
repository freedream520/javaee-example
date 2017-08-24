package somepack;

import org.slf4j.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.inject.Inject;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

@MessageDriven(
        name = "BackwardStreamMDB",
        activationConfig = {
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:/jms/queue/ExampleQueue"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class BackwardStream implements MessageListener {

    @Inject
    private Logger logger;

    @Inject
    MessagesBroker broker;

    public void onMessage(Message message) {
        if (message instanceof TextMessage) {
            try {
                String text = ((TextMessage) message).getText();
                logger.info("MESSAGE BEAN: Message received: " + text);
                broker.forEach(text);
            } catch (JMSException e) {
                e.printStackTrace();
            }
        }
        else {
            logger.warn("is not a text message");
        }
    }


}
