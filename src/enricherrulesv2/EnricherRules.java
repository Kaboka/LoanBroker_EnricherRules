package enricherrulesv2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
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

public class EnricherRules {

    private static final String EXCHANGE_NAME = "translator_exchange_topic";
    private static final String IN_QUEUE_NAME = "enricher_rules";
    
    public static void main(String[] args) throws IOException {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("student");
        factory.setPassword("cph");
        factory.setHost("datdb.cphbusiness.dk");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //mangler exchange og bind
        channel.queueDeclare(IN_QUEUE_NAME, false, false, false, null);
        channel.exchangeDeclare(EXCHANGE_NAME, "topic");
        
        QueueingConsumer consumer = new QueueingConsumer(channel);
        channel.basicConsume(IN_QUEUE_NAME, consumer);
//        System.out.println("Loanbroker.CreditScore: " + creditScore(ssn));

        while (true) {
            QueueingConsumer.Delivery delivery = null;
            try {
                delivery = consumer.nextDelivery();
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
                String message = new String(delivery.getBody());
                System.out.println("Message: " + message);
                String severity = getRules(message);
                channel.basicPublish(EXCHANGE_NAME, severity, null, message.getBytes());
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            } catch (ShutdownSignalException ex) {
                ex.printStackTrace();
            } catch (ConsumerCancelledException ex) {
                ex.printStackTrace();
            }
        }
    }

    private static String getRules(String xmlMessage) {
        String rules = "";
        IRuleBaseGateway gateway = new RuleBaseGateway();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(xmlMessage.getBytes()));
            XPath xPath = XPathFactory.newInstance().newXPath();
            int creditScore = Integer.parseInt(xPath.compile("/LoanRequest/creditScore").evaluate(doc));
            int loanDuration = Integer.parseInt(xPath.compile("/LoanRequest/loanDuration").evaluate(doc));
            double loanAmount = Double.parseDouble(xPath.compile("/LoanRequest/loanAmount").evaluate(doc));
            rules = gateway.getRules(creditScore, loanDuration, loanAmount);
        } catch (ParserConfigurationException ex) {
            Logger.getLogger(EnricherRules.class.getName()).log(Level.SEVERE, null, ex);
        } catch (SAXException ex) {
            Logger.getLogger(EnricherRules.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(EnricherRules.class.getName()).log(Level.SEVERE, null, ex);
        } catch (XPathExpressionException ex) {
            Logger.getLogger(EnricherRules.class.getName()).log(Level.SEVERE, null, ex);
        }
        return rules;
    }

}
