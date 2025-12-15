package com.vuckoapp.notificationservice.types;

public enum NotificationType {
    // user
    ACTIVATION_EMAIL,
    PASSWORD_RESET,
    // game
    SESSION_INVITATION,
    SESSION_CONFIRMATION,
    SESSION_CANCELLATION,
    SESSION_REMINDER_60_MIN,
    SESSION_REJECTED_ATTENDANCE
}
