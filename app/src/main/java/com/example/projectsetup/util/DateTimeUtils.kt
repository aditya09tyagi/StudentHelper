package com.example.projectsetup.util

import org.threeten.bp.*
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

class DateTimeUtils {

    companion object {
        private val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.ENGLISH)
        private val formatter12HourOnlyTime: DateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm a", Locale.ENGLISH)

        fun getTimeElapsedInMilliSeconds(date: String): Long {
            val localString = LocalDateTime.parse(date, formatter)
                    .atOffset(ZoneOffset.UTC)
                    .atZoneSameInstant(ZoneId.systemDefault())
                    .format(formatter)

            return Math.abs(Duration.between(LocalDateTime.now(), ZonedDateTime.parse(localString)).toMillis())
        }

        fun getTimeMilliSeconds(date: String): Long {
            val tz = TimeZone.getDefault()
            val cal = GregorianCalendar.getInstance(tz)
            val offsetInMillis = tz.getOffset(cal.timeInMillis)

            val otherString = LocalDateTime.parse(date, formatter)
                    .atOffset(ZoneOffset.UTC)
                    .atZoneSameInstant(ZoneId.systemDefault())
                    .format(formatter)

            return Duration.ofMillis(
                    ZonedDateTime.parse(otherString).toInstant().toEpochMilli().minus(offsetInMillis)
            ).toMillis()
        }

        fun getLocalDateFromString(date: String): ZonedDateTime {
            val localString = LocalDateTime.parse(date, formatter)
                    .atOffset(ZoneOffset.UTC)
                    .atZoneSameInstant(ZoneId.systemDefault())
                    .format(formatter)
            return ZonedDateTime.parse(localString)
        }

        fun getCurrentLocalDateInUtc(): String {
            return LocalDateTime.parse(ZonedDateTime.now().toInstant().toString(), formatter)
                    .atOffset(ZoneOffset.UTC)
                    .atZoneSameInstant(ZoneId.systemDefault())
                    .format(formatter)
        }

        fun getTimeLeftFromCurrentDateTime(zonedDateTime: ZonedDateTime): Long {
            return zonedDateTime.toInstant().toEpochMilli() - ZonedDateTime.now().toInstant().toEpochMilli()
        }

        fun convertLocalDateToUtcDate(localDateTime: LocalDateTime): String {
            return localDateTime
                    .atZone(ZoneId.systemDefault())
                    .withZoneSameInstant(ZoneOffset.UTC)
                    .format(formatter)
        }

        fun getDateFromMillis(millis: Long): String {
            return Instant.ofEpochMilli(millis)
                    .atOffset(ZoneOffset.UTC)
                    .atZoneSameInstant(ZoneId.systemDefault())
                    .format(formatter12HourOnlyTime)
        }

        fun getDateInTimeOnlyFormatFromUtcDateTime(startDate: String): String {
            val date = LocalDateTime.parse(startDate, formatter)
            val millis = date.atOffset(ZoneOffset.UTC).atZoneSameInstant(ZoneId.systemDefault()).toInstant().toEpochMilli()
            return Instant.ofEpochMilli(millis).atZone(ZoneId.systemDefault()).format(formatter12HourOnlyTime)
        }

    }
}