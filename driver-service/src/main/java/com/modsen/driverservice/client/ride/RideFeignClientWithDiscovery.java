package com.modsen.driverservice.client.ride;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

@FeignClient(value = "rides-service", fallbackFactory = RideFeignClientFallbackFactory.class)
@Profile("dev")
public interface RideFeignClientWithDiscovery extends RideFeignClient {
}
