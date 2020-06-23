package com.example.hellosqs.controller;

import com.example.hellosqs.client.AwsSqsClient;
import com.example.hellosqs.dto.Customer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/customers")
public class AwsSqsController {

  @Autowired
  AwsSqsClient awsSqsClient;

  @PostMapping
  public String sendToStandardQueue(@RequestBody Customer customer) {
    awsSqsClient.sendSQSMessage(customer);
    return "Customer payload sent to STANDARD Queue!";
  }

  @PostMapping("/fifo")
  public String sendToFIFOQueue(@RequestBody Customer customer) {
    awsSqsClient.sendFifoSQSMessage(customer);
    return "Customer payload sent to FIFO Queue!";
  }
}
