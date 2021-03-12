package org.ossiaustria.lib.domain.database.converters

import androidx.room.TypeConverter
import java.util.*


class UUIDConverter {
    @TypeConverter
    fun fromUUID(value: UUID?): String? = value?.toString()

    @TypeConverter
    fun toUUID(value: String?): UUID? = if (value == null) null else UUID.fromString(value)
}