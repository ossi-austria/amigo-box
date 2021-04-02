package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.enums.CallType
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import org.ossiaustria.lib.domain.models.enums.NfcTagType
import java.util.*

/**
 * Useful to mock data for ApiTests
 */
class JsonMocker {
    companion object {

        fun createList(mocks: List<String>) = """[${mocks.joinToString(", ")}]"""

        fun group(
            id: UUID = UUID.randomUUID(),
            name: String = "group",
            personsMock: List<String> = emptyList(),
        ) = """
            {
                "id":"$id",
                "name":"$name",
                "members": [${personsMock.joinToString(", ")}]
            }""".trim()

        fun multimedia(
            id: UUID = UUID.randomUUID(),
            senderId: UUID = UUID.randomUUID(),
            receiverId: UUID = UUID.randomUUID(),
            ownerId: UUID = UUID.randomUUID(),
            albumId: UUID = UUID.randomUUID(),
            type: String = "IMAGE"
        ) = """
            {
                "id":"$id",
                "createdAt":100,
                "sendAt":100,
                "retrievedAt":100,
                "senderId":"$senderId",
                "receiverId":"$receiverId",
                "ownerId":"$ownerId",
                "remoteUrl":"remoteUrl",
                "localUrl":"localUrl",
                "type":"$type",
                "size":3,
                "albumId":"$albumId"
            }""".trim()

        fun album(
            id: UUID = UUID.randomUUID(),
            ownerId: UUID = UUID.randomUUID(),
            multimediaMock: List<String> = emptyList()
        ) = """
            {
                "id":"$id",
                "createdAt":100,
                "updatedAt":100,
                "name":"name",
                "ownerId":"$ownerId",
                "items": ${createList(multimediaMock)}
            }""".trim()

        fun person(
            id: UUID = UUID.randomUUID(),
            groupId: UUID? = UUID.randomUUID(),
            name: String = "",
            memberType: String = "MEMBER"
        ) = """
            {
                "id":"$id",
                "name":"$name",
                "email":"email",
                "memberType":"$memberType",
                "groupId":"$groupId"
            }""".trim()

        fun nfc(
            id: UUID = UUID.randomUUID(),
            creatorId: UUID = UUID.randomUUID(),
            createdAt: Long = 0,
            ownerId: UUID? = UUID.randomUUID(),
            type: NfcTagType = NfcTagType.COLLECTION,
            linkedPersonId: UUID? = UUID.randomUUID(),
            linkedMediaId: UUID? = UUID.randomUUID(),
            linkedAlbumId: UUID? = UUID.randomUUID(),
        ) = """
            {
                "id":"$id",
                "creatorId":"$creatorId",
                "createdAt":$createdAt,
                "ownerId":${ownerId.json()},
                "type":"$type",
                "linkedPersonId":${linkedPersonId.json()},
                "linkedMediaId":${linkedMediaId.json()},
                "linkedAlbumId":${linkedAlbumId.json()}
            }""".trim()

        fun share(
            id: UUID = UUID.randomUUID(),
            createdAt: Long = 0,
            sendAt: Long = 0,
            retrievedAt: Long = 0,
            senderId: UUID? = UUID.randomUUID(),
            receiverId: UUID? = UUID.randomUUID(),
            albumMock: String = album()
        ) = """
            {
                "id":"$id",
                "createdAt":$createdAt,
                "sendAt":$sendAt,
                "retrievedAt":$retrievedAt,
                "senderId":${senderId.json()},
                "receiverId":${receiverId.json()},
                "album":$albumMock
            }""".trim()

        fun call(
            id: UUID = UUID.randomUUID(),
            createdAt: Long = 0,
            sendAt: Long = 0,
            retrievedAt: Long = 0,
            senderId: UUID? = UUID.randomUUID(),
            receiverId: UUID? = UUID.randomUUID(),
            callType: CallType = CallType.VIDEO,
            startedAt: Long = 0,
            finishedAt: Long = 0,
        ) = """
            {
                "id":"$id",
                "createdAt":$createdAt,
                "sendAt":$sendAt,
                "retrievedAt":$retrievedAt,
                "senderId":${senderId.json()},
                "receiverId":${receiverId.json()},
                "callType":"$callType",
                "startedAt":$startedAt,
                "finishedAt":$finishedAt
            }""".trim()

        fun message(
            id: UUID = UUID.randomUUID(),
            createdAt: Long = 0,
            sendAt: Long = 0,
            retrievedAt: Long = 0,
            senderId: UUID? = UUID.randomUUID(),
            receiverId: UUID? = UUID.randomUUID(),
            text: String = "",
        ) = """
            {
                "id":"$id",
                "createdAt":$createdAt,
                "sendAt":$sendAt,
                "retrievedAt":$retrievedAt,
                "senderId":${senderId.json()},
                "receiverId":${receiverId.json()},
                "text":"$text"
            }""".trim()

        fun multimedia(
            id: UUID = UUID.randomUUID(),
            createdAt: Long = 0,
            sendAt: Long = 0,
            retrievedAt: Long = 0,
            senderId: UUID? = UUID.randomUUID(),
            receiverId: UUID? = UUID.randomUUID(),
            ownerId: UUID? = UUID.randomUUID(),
            remoteUrl: String = "remoteUrl",
            localUrl: String = "localUrl",
            type: MultimediaType = MultimediaType.VIDEO,
            size: Long = 100,
            albumId: UUID? = null,
        ) = """
            {
                "id":"$id",
                "createdAt":$createdAt,
                "sendAt":$sendAt,
                "retrievedAt":$retrievedAt,
                "senderId":${senderId.json()},
                "receiverId":${receiverId.json()},
                "ownerId":${ownerId.json()},
                "remoteUrl":"$remoteUrl",
                "localUrl":"$localUrl",
                "type":"$type",
                "size":$size,
                "albumId":${albumId.json()}
            }""".trim()

        fun UUID?.json(): String = if (this != null) {
            "\"$this\""
        } else "null"
    }

}

class DebugMockInterceptorAdapter {
    fun requestUrl(uri: String): MockResponse? {
        return when {
            uri.endsWith("albums") -> mockAlbums()
            uri.contains("albums/") -> mockAlbum(uri)
            else -> null
        }
    }

    private fun mockAlbum(uri: String): MockResponse? {
        val pathId = uri.substringAfterLast("/")
        val uuid = UUID.fromString(pathId)
        return MockResponse(JsonMocker.album(id = uuid))
    }

    private fun mockAlbums(): MockResponse? {
        val albums = listOf(JsonMocker.album())
        return MockResponse(
            content = "[${albums.joinToString(", ")}]"
        )
    }

}