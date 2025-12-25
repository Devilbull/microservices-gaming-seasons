package com.vuckoapp.gamingservice.controllers;

import com.vuckoapp.gamingservice.feigncalls.NotificationserviceCalls;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TestController {
    private final NotificationserviceCalls callsToNotificationserviceService;


    @GetMapping("/test-mail")
    public void test() {
        callsToNotificationserviceService.test();
    }


}
