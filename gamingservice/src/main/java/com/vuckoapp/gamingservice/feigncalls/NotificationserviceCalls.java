package com.vuckoapp.gamingservice.feigncalls;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(name = "notificationservice")
public interface NotificationserviceCalls {
    @GetMapping("/api/notificationservice/test-mail")
    public void test();
}
