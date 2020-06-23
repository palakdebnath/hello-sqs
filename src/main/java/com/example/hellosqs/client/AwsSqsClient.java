package com.example.hellosqs.client;

import com.example.hellosqs.dto.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.aws.messaging.config.annotation.NotificationMessage;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class AwsSqsClient {

  @Value("${standard.queue.name}")
  private String standardQueueName;

  @Value("${fifo.queue.name}")
  private String fifoQueueName;

  @Autowired
  private QueueMessagingTemplate sqsMsgTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  private final String messageGroupId = "testMsgGroupId-1";

  private static final Logger logger = LoggerFactory.getLogger(AwsSqsClient.class);

  public void sendSQSMessage(Customer customer) {
    sqsMsgTemplate.convertAndSend(standardQueueName, customer);
    logger.info("Customer info sent : {}", customer);
  }

  public void sendFifoSQSMessage(Customer customer) {
    Map<String, Object> headers = new HashMap<>();
    headers.put("message-group-id", messageGroupId);
    headers.put("message-deduplication-id", UUID.randomUUID().toString());
    sqsMsgTemplate.convertAndSend(fifoQueueName, customer, headers);
    logger.info("Customer info sent to FIFO : {}", customer);
  }

  @SqsListener("${standard.queue.name}")
  public void receiveSQSMessage(Customer customer) {
    logger.info("------ Msg Received From STANDARD Queue ------");
    logger.info("Customer Id : {}  name : {}", customer.getId(), customer.getName());
    logger.info("------------------ END -----------------------");
  }

  @SqsListener(value = "${fifo.queue.name}", deletionPolicy = SqsMessageDeletionPolicy.ON_SUCCESS)
  public void receiveFIFOSQSMessage(Customer customer) {
    logger.info("-------- Msg Received From FIFO Queue --------");
    logger.info("Customer Id : {}  name : {}", customer.getId(), customer.getName());
    logger.info("------------------ END -----------------------");
  }

  @SqsListener("${standard.queue.for.sns.name}")
  public void receiveSQSMessageFromSns(@NotificationMessage String msg) {
    logger.info("------------ Msg Received From SNS -----------");
    logger.info("\n{}", msg);

    try {
      Customer customer = objectMapper.readValue(msg, Customer.class);
      logger.info("Customer Id : {}  name : {}", customer.getId(), customer.getName());
    } catch (JsonProcessingException e) {
      logger.error("Exception msg : {} ", e.getMessage());
    }
    logger.info("------------------ END -----------------------");
  }
}


