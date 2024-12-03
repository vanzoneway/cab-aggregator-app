package com.modsen.ridesservice.client.driver;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

@FeignClient(value = "driver-service", fallbackFactory = DriverFeignClientFallbackFactory.class)
@Profile("dev")
public interface DriverFeignClientWithDiscovery extends DriverFeignClient {
}
