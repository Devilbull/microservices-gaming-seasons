package com.vuckoapp.gamingservice.services;

import com.vuckoapp.gamingservice.dto.SessionEligibilityDto;
import com.vuckoapp.gamingservice.dto.UserDto;
import com.vuckoapp.gamingservice.exceptions.DownstreamServiceException;
import com.vuckoapp.gamingservice.feigncalls.UserserviceCalls;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceRetry {
    private final UserserviceCalls userserviceCalls;

    @Retryable(
            value = {feign.FeignException.class, java.net.ConnectException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public SessionEligibilityDto canCreateSession() {
        return userserviceCalls.canCreateSession();
    }

    @Recover
    public SessionEligibilityDto recoverCanCreateSession(Exception ex) {
        throw new DownstreamServiceException(
                "UserService unavailable while checking session eligibility",
                ex
        );
    }

    @Retryable(
            value = {feign.FeignException.class, java.net.ConnectException.class},
            maxAttempts = 3,
            backoff = @Backoff(delay = 1000)
    )
    public UserDto getUserInfo() {
        return userserviceCalls.getUserInfo();
    }

    @Recover
    public UserDto recoverGetUserInfo(Exception ex) {
        throw new DownstreamServiceException(
                "UserService unavailable while fetching user info",
                ex
        );
    }
}

