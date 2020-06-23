package com.example.hellosqs.config;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.sqs.AmazonSQSAsync;
import com.amazonaws.services.sqs.AmazonSQSAsyncClientBuilder;
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AwsSqsConfig {

  @Bean
  public QueueMessagingTemplate queueMessagingTemplate() {
    return new QueueMessagingTemplate(amazonSQSClient());
  }

  private AmazonSQSAsync amazonSQSClient() {
    AmazonSQSAsync amazonSQS;
    amazonSQS = AmazonSQSAsyncClientBuilder
        .standard()
        .withRegion(Regions.US_EAST_1)
        .build();
    return amazonSQS;
  }
}
