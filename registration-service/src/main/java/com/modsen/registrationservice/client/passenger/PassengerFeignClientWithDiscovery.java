package com.modsen.registrationservice.client.passenger;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

@FeignClient(value = "passenger-service", fallbackFactory = PassengerFeignClientFallbackFactory.class)
@Profile("dev")
public interface PassengerFeignClientWithDiscovery extends PassengerFeignClient {
}
