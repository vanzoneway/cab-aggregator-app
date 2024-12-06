package com.modsen.registrationservice.client.passenger;

import com.modsen.registrationservice.dto.SignUpDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

public interface PassengerFeignClient {

    @PostMapping("/api/v1/passengers")
    PassengerResponseDto createPassenger(@RequestBody SignUpDto signUpDto,
                                         @RequestHeader("Accept-Language") String acceptLanguage,
                                         @RequestHeader("Authorization") String authorization);

}
