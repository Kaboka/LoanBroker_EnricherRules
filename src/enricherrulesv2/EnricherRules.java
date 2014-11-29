package enricherrulesv2;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.ConsumerCancelledException;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.ShutdownSignalException;
import java.io.IOException;

public class EnricherRules {
    
    private static final String EXCHANGE_NAME = "translator_exchange";
    private static final String IN_QUEUE_NAME = "enricher_rules";
    
    public static void main(String[] args) throws IOException {
        IRuleBaseGateway gateway = new DummyRuleBaseGateway();
        ConnectionFactory factory = new ConnectionFactory();
        factory.setUsername("student");
        factory.setPassword("cph");
        factory.setHost("datdb.cphbusiness.dk");
        
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();
        //mangler exchange og bind
        channel.queueDeclare(IN_QUEUE_NAME, false, false, false, null);
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
                String sevirity = gateway.getRules("123456", 300, 6000.0).get(0); // Dummy data, change it.
                channel.basicPublish(EXCHANGE_NAME,sevirity, null, message.getBytes());
            } catch (InterruptedException ex) {
               
            } catch (ShutdownSignalException ex) {
               
            } catch (ConsumerCancelledException ex) {
           
            }
        }
    }
}