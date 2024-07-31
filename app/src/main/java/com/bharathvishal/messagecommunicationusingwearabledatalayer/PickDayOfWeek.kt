package com.bharathvishal.messagecommunicationusingwearabledatalayer

enum class PickDayOfWeek(val value: Int) {
    MONDAY(1),
    TUESDAY(2),
    WEDNESDAY(4),
    THURSDAY(8),
    FRIDAY(16),
    SATURDAY(32),
    SUNDAY(64);

    companion object {
        fun fromInt(value: Int) = PickDayOfWeek.values().first { it.value == value }
    }
}