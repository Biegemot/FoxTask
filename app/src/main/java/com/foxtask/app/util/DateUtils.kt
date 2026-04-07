package com.foxtask.app.util

import java.time.LocalDate
import java.time.ZoneId
import java.util.*

object DateUtils {
    fun getStartOfDay(date: Date = Date()): Long {
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault())
            .atStartOfDay(ZoneId.systemDefault())
            .toInstant()
            .toEpochMilli()
    }

    fun getEpochDay(date: Date = Date()): Long {
        return LocalDate.ofInstant(date.toInstant(), ZoneId.systemDefault())
            .toEpochDay()
    }

    fun isSameDay(date1: Long, date2: Long): Boolean {
        val epochDay1 = date1 / (1000 * 60 * 60 * 24)
        val epochDay2 = date2 / (1000 * 60 * 60 * 24)
        return epochDay1 == epochDay2
    }
}
