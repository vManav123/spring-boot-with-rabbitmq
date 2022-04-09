# Spring Boot Application with RabbitMQ Messaging Service 📧

Messaging is a technique for communicating between applications. It relies on asynchronous message-passing instead of synchronous request response-based architecture. Producers and consumers of messages are decoupled by an intermediate messaging layer known as a message broker. A message broker provides features like persistent storage of messages, message filtering, and message transformation.

## Introduction

Briefly, AMQP is made up of Exchanges, Queues, and Bindings:

1) Exchanges are like post offices or mailboxes and clients publish a message to an AMQP exchange. There are four built-in exchange types
   * Direct Exchange – Routes messages to a queue by matching a complete routing key
   * Fanout Exchange – Routes messages to all the queues bound to it
   * Topic Exchange – Routes messages to multiple queues by matching a routing key to a pattern
   * Headers Exchange – Routes messages based on message headers
2) Queues are bound to an exchange using a routing key
3) Messages are sent to an exchange with a routing key. The exchange then distributes copies of messages to queues

## Maven Dependencies
In order to add the spring-amqp and spring-rabbit modules to our project, we add the spring-boot-starter-amqp dependency to our pom.xml:
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
        <version>2.2.2.RELEASE</version>
    </dependency>
</dependencies>
```

## Configuration

We'll use Spring Boot's auto-configuration to create our ConnectionFactory, RabbitTemplate, and RabbitAdmin beans. As a result, we get a connection to our RabbitMQ broker on port 5672 using the default username and password of “guest”. 

```java
@EnableRabbit
@Configuration
public class RabbitMQConfig {

    @Value("${queue.name}")
    private String queueName;

    @Value("${exchange.name}")
    private String exchangeName;

    @Value("${routing.key}")
    private String routingKey;


    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("localhost");
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");
        connectionFactory.setPort(5672);
        return connectionFactory;
    }

    @Bean
    public Queue queue() {
        return new Queue(queueName, true);
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName);
    }

    @Bean
    public Binding binding(Queue queue, DirectExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(routingKey);
    }

}
```

## Properties

Add Properties for RabbitMQ and configure them.

```properties
spring.rabbitmq.host=localhost
spring.rabbitmq.port=5672
spring.rabbitmq.username=guest
spring.rabbitmq.password=guest

queue.name=testing
exchange.name=test
routing.key=testing-key
```

## Message Sender

```java
@Component
public class QueueSender {

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private Queue queue;

    @Autowired
    private Binding binding;

    public String sendToQueue(String order) {
        try {
            rabbitTemplate.convertAndSend(this.queue.getName(), order);
        } catch (Exception e) {
            return "Failed";
        }
        return "Success";
    }

    public String sendToExchange(String textMessage) {
        MessageProperties messageProperties = new MessageProperties();
        messageProperties.setHeader("ENV", "LOCAL");
        Message message = new Message(textMessage.getBytes(), messageProperties);
        try {
            rabbitTemplate.convertAndSend(binding.getExchange(), binding.getRoutingKey(), message);
        } catch (Exception e) {
            return "Failed";
        }
        return "Success";
    }
}
```

## Message Receiver

```java
@Component
@Slf4j
public class QueueReceiver {

    @RabbitListener(queues = {"${queue.name}"})
    public void receive(@Payload String fileBody) {
        log.info("Message Received : {}",fileBody);
    }

}
```

## Conclusion

Now you can add controller api for calling sender , review in rabbitmq and console logs
Run Application and Test it 😊😊👍