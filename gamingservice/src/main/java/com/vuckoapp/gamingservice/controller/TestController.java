package com.vuckoapp.gamingservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequiredArgsConstructor
public class TestController {



    @GetMapping("/test-mail")
    public void test() {
        System.out.println("Aca prica da posaljem primio zahtev");
    }


}
