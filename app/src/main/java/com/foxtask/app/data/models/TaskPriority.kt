package com.foxtask.app.data.models

enum class TaskPriority(val value: Int, val stringRes: Int) {
    MINIMAL(1, android.R.string.ok),
    LOW(2, android.R.string.ok),
    MEDIUM(3, android.R.string.ok),
    HIGH(4, android.R.string.ok),
    CRITICAL(5, android.R.string.ok);

    companion object {
        fun fromValue(value: Int) = entries.find { it.value == value } ?: MEDIUM
    }
}
