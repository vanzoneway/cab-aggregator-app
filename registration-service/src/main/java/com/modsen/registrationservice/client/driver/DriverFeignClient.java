package com.modsen.registrationservice.client.driver;

import com.modsen.registrationservice.dto.SignUpDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface DriverFeignClient {

    @PostMapping("/api/v1/drivers")
    DriverResponseDto createDriver(@RequestBody SignUpDto signUpDto,
                                   @RequestHeader("Accept-Language") String acceptLanguage,
                                   @RequestHeader("Authorization") String authorization);

}
