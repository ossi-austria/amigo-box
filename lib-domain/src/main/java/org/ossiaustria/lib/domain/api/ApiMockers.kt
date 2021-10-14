package org.ossiaustria.lib.domain.api

import org.ossiaustria.lib.domain.models.enums.CallType
import org.ossiaustria.lib.domain.models.enums.MultimediaType
import org.ossiaustria.lib.domain.models.enums.NfcTagType
import java.util.*
import java.util.UUID.randomUUID

/**
 * Useful to mock data for ApiTests
 */
class JsonMocker {
    companion object {

        fun createList(mocks: List<String>) = """[${mocks.joinToString(", ")}]"""

        fun group(
            id: UUID = randomUUID(),
            name: String = "group",
            personsMock: List<String> = emptyList(),
        ) = """
            {
                "id":"$id",
                "name":"$name",
                "members": [${personsMock.joinToString(", ")}]
            }""".trim()

        fun multimedia(
            id: UUID = randomUUID(),
            ownerId: UUID = randomUUID(),
            albumId: UUID = randomUUID(),
            type: String = "IMAGE"
        ) = """
            {
                "id":"$id",
                "albumId":"$albumId",
                "contentType": "string",
                "createdAt":"2021-10-07T06:57:44.191Z",
                "ownerId":"$ownerId",
                "filename":"filename",
                "type":"$type",
                "size":3
            }""".trim()

        fun album(
            id: UUID = randomUUID(),
            ownerId: UUID = randomUUID(),
            multimediaMock: List<String> = emptyList()
        ) = """
            {
                "id":"$id",
                "createdAt":"2021-10-07T06:57:44.191Z",
                "updatedAt":"2021-10-07T06:57:44.191Z",
                "name":"name",
                "ownerId":"$ownerId",
                "items": ${createList(multimediaMock)}
            }""".trim()

        fun person(
            id: UUID = randomUUID(),
            groupId: UUID? = randomUUID(),
            name: String = "",
            memberType: String = "MEMBER"
        ) = """
            {
                "id":"$id",
                "name":"$name",
                "avatarUrl":"avatarUrl",
                "memberType":"$memberType",
                "groupId":"$groupId"
            }""".trim()

        fun nfc(
            id: UUID = randomUUID(),
            name: String = "name",
            creatorId: UUID = randomUUID(),
            createdAt: String = "2021-10-07T06:57:44.191Z",
            updatedAt: String? = null,
            ownerId: UUID? = randomUUID(),
            type: NfcTagType = NfcTagType.OPEN_ALBUM,
            linkedPersonId: UUID? = randomUUID(),
            linkedAlbumId: UUID? = randomUUID(),
        ) = """
            {
                "id":"$id",
                "creatorId":"$creatorId",
                "createdAt":"$createdAt",
                "updatedAt":${optDate(updatedAt)},
                "ownerId":${ownerId.json()},
                "type":"$type",
                "name": "$name",
                "linkedPersonId":${linkedPersonId.json()},
                "linkedAlbumId":${linkedAlbumId.json()}
            }""".trim()

        fun share(
            id: UUID = randomUUID(),
            createdAt: String = "2021-10-07T06:57:44.191Z",
            sendAt: String = "2021-10-07T06:57:44.191Z",
            retrievedAt: String = "2021-10-07T06:57:44.191Z",
            senderId: UUID? = randomUUID(),
            receiverId: UUID? = randomUUID(),
            albumMock: String = album()
        ) = """
            {
                "id":"$id",
               "createdAt":${optDate(createdAt)},
                   "sendAt":${optDate(sendAt)},
                "retrievedAt":${optDate(retrievedAt)},
                "senderId":${senderId.json()},
                "receiverId":${receiverId.json()},
                "album":$albumMock
            }""".trim()

        fun call(
            id: UUID = randomUUID(),
            createdAt: String = "2021-10-07T06:57:44.191Z",
            sendAt: String = "2021-10-07T06:57:44.191Z",
            retrievedAt: String = "2021-10-07T06:57:44.191Z",
            senderId: UUID? = randomUUID(),
            receiverId: UUID? = randomUUID(),
            callType: CallType = CallType.VIDEO,
            startedAt: String? = null,
            finishedAt: String? = null,
        ) = """
            {
               "id":"$id",
               "createdAt":${optDate(createdAt)},
                "sendAt":${optDate(sendAt)},
                "retrievedAt":${optDate(retrievedAt)},
                "senderId":${senderId.json()},
                "receiverId":${receiverId.json()},
                "callType":"$callType",
                "startedAt":$startedAt,
                "finishedAt":$finishedAt
            }""".trim()

        fun message(
            id: UUID = randomUUID(),
            createdAt: String = "2021-10-07T06:57:44.191Z",
            sendAt: String = "2021-10-07T06:57:44.191Z",
            retrievedAt: String = "2021-10-07T06:57:44.191Z",
            senderId: UUID? = randomUUID(),
            receiverId: UUID? = randomUUID(),
            text: String = "",
        ) = """
            {
               "id":"$id",
               "createdAt":${optDate(createdAt)},
               "sendAt":${optDate(sendAt)},
               "retrievedAt":${optDate(retrievedAt)},
               "senderId":${senderId.json()},
               "receiverId":${receiverId.json()},
               "text":"$text"
            }""".trim()

        fun multimedia(
            id: UUID = randomUUID(),
            createdAt: String = "2021-10-07T06:57:44.191Z",
            sendAt: String = "2021-10-07T06:57:44.191Z",
            retrievedAt: String = "2021-10-07T06:57:44.191Z",
            senderId: UUID? = randomUUID(),
            receiverId: UUID? = randomUUID(),
            ownerId: UUID? = randomUUID(),
            remoteUrl: String = "remoteUrl",
            localUrl: String = "localUrl",
            type: MultimediaType = MultimediaType.VIDEO,
            size: Long = 100,
            albumId: UUID? = null,
        ) = """
            {
                "id":"$id",
                "createdAt":${optDate(createdAt)},
                "sendAt":${optDate(sendAt)},
                "retrievedAt":${optDate(retrievedAt)},
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

        private fun optDate(nullableDate: String?) =
            if (nullableDate != null) "\"$nullableDate\"" else "null"

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

    private fun mockAlbum(uri: String): MockResponse {
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