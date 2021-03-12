package org.ossiaustria.lib.domain.database.converters

import androidx.room.TypeConverter
import org.ossiaustria.lib.domain.models.enums.CallType
import org.ossiaustria.lib.domain.models.enums.MembershipType
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import org.ossiaustria.lib.domain.models.enums.NFCTagType


open class EnumConverter<T>(val converter: (String) -> T) {
    @TypeConverter
    fun fromUUID(value: T?): String? = value.toString()

    @TypeConverter
    fun toUUID(value: String?): T? = if (value == null) null else converter(value)
}

internal class MembershipTypeConverter :
    EnumConverter<MembershipType>({ MembershipType.valueOf(it) })

internal class CallTypeConverter : EnumConverter<CallType>({ CallType.valueOf(it) })
internal class MultimediaTypeConverter :
    EnumConverter<MultimediaType>({ MultimediaType.valueOf(it) })

internal class NFCTagTypeConverter : EnumConverter<NFCTagType>({ NFCTagType.valueOf(it) })