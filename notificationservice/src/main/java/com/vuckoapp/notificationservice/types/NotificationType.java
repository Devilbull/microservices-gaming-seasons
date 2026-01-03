package com.vuckoapp.notificationservice.types;

public enum NotificationType {
    // user
    ACTIVATION_EMAIL,
    PASSWORD_RESET,
    RANK_CHANGED,
    // game
    SESSION_INVITATION,
    SESSION_JOINED,
    SESSION_CANCELLATION,
    SESSION_REMINDER_60_MIN,
    SESSION_CREATION_REJECTED
}
