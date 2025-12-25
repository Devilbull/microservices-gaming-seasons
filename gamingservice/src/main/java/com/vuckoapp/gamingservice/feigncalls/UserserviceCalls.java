package com.vuckoapp.gamingservice.feigncalls;

import com.vuckoapp.gamingservice.config.FeignCookieConfig;
import com.vuckoapp.gamingservice.dto.SessionEligibilityDto;
import com.vuckoapp.gamingservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

@FeignClient(
        name = "userservice",
        configuration = FeignCookieConfig.class
)
public interface UserserviceCalls {

    @GetMapping("/api/userservice/users/session-eligibility")
    SessionEligibilityDto canCreateSession();


    @GetMapping("/api/userservice/users/me")
    UserDto getUserInfo();
}

