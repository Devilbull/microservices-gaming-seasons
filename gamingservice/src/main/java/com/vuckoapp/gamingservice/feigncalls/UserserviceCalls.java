package com.vuckoapp.gamingservice.feigncalls;

import com.vuckoapp.gamingservice.config.FeignCookieConfig;
import com.vuckoapp.gamingservice.dto.SessionEligibilityDto;
import com.vuckoapp.gamingservice.dto.UserDto;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.UUID;

@FeignClient(
        name = "userservice",
        configuration = FeignCookieConfig.class
)
public interface UserserviceCalls {

    @GetMapping("/api/userservice/users/session-eligibility")
    SessionEligibilityDto getEligibilityStats();


    @GetMapping("/api/userservice/users/me")
    UserDto getUserInfo();

    @PostMapping("/api/userservice/gameservice/{userId}/update-total-sessions")
    void increaseNumberOfSeasonsJoined(@PathVariable UUID userId);
}

