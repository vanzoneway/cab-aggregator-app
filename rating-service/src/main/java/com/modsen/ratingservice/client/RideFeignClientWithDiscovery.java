package com.modsen.ratingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

@FeignClient(value = "rides-service")
@Profile("dev")
public interface RideFeignClientWithDiscovery extends RideFeignClient {
}
