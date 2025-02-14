package com.modsen.notificationservice.service;

import com.modsen.notificationservice.dto.statistic.GeneralUserStatisticDto;

public interface NotificationService {

    void sendUserStatisticOnEmail(GeneralUserStatisticDto generalUserStatisticDto);

}
