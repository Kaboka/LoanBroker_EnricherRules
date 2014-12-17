package enricherrulesv2;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import dk.cphbusiness.connection.ConnectionCreator;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;
import utilities.xml.xmlMapper;

public class EnricherRules {

    private static final String EXCHANGE_NAME = "ex_translators_gr1";
    private static final String IN_QUEUE_NAME = "enricher_rules_gr1";
    private static String correlationId = "1";
    
    public static void main(String[] args) throws IOException {
        ConnectionCreator creator = ConnectionCreator.getInstance();
        Channel inChannel = creator.createChannel();
        Channel outChannel = creator.createChannel();
        inChannel.queueDeclare(IN_QUEUE_NAME, false, false, false, null);
        outChannel.exchangeDeclare(EXCHANGE_NAME, "topic");
        QueueingConsumer consumer = new QueueingConsumer(inChannel);
        inChannel.basicConsume(IN_QUEUE_NAME, consumer);

        while (true) {
            QueueingConsumer.Delivery delivery = null;
            try {
                delivery = consumer.nextDelivery();
                inChannel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                String message = new String(delivery.getBody());
                System.out.println("Message: " + message);
                String severity = getRules(message);
                AMQP.BasicProperties props = new AMQP.BasicProperties().builder().correlationId(correlationId).build();
                outChannel.basicPublish(EXCHANGE_NAME, severity, props, message.getBytes());
            } catch (InterruptedException | ShutdownSignalException | ConsumerCancelledException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static String getRules(String xmlMessage) {
        String rules = "";
        IRuleBaseGateway gateway = new RuleBaseGateway();
        try {
            Document doc = xmlMapper.getXMLDocument(xmlMessage);
            XPath xPath = XPathFactory.newInstance().newXPath();
            int creditScore = Integer.parseInt(xPath.compile("/LoanRequest/creditScore").evaluate(doc));
            int loanDuration = Integer.parseInt(xPath.compile("/LoanRequest/loanDuration").evaluate(doc));
            double loanAmount = Double.parseDouble(xPath.compile("/LoanRequest/loanAmount").evaluate(doc));
            rules = gateway.getRules(creditScore, loanDuration, loanAmount);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(EnricherRules.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rules;
    }

}
