package com.vuckoapp.userservice.controllers;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @PutMapping("/users/{id}/block")
    public void blockUser(@PathVariable UUID id) {
        userService.blockUser(id);
    }

    @PutMapping("/users/{id}/unblock")
    public void unblockUser(@PathVariable UUID id) {
        userService.unblockUser(id);
    }
}

