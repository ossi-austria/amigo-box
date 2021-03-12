package org.ossiaustria.lib.domain.database.converters

import androidx.room.TypeConverter
import org.ossiaustria.lib.domain.database.entities.MembershipType


open class EnumConverter<T>(val converter: (String) -> T) {
    @TypeConverter
    fun fromUUID(value: T?): String? = value.toString()

    @TypeConverter
    fun toUUID(value: String?): T? = if (value == null) null else converter(value)
}

class MembershipTypeConverter : EnumConverter<MembershipType>({ MembershipType.valueOf(it) })