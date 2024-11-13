package com.modsen.ratingservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.context.annotation.Profile;

@FeignClient(name = "rides-service", url = "${rides-service.wire-mock.url}")
@Profile("test")
public interface RideFeignClientIT extends  RideFeignClient {
}
