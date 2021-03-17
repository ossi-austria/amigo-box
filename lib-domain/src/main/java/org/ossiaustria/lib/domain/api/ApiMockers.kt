package org.ossiaustria.lib.domain.api

import java.util.*

/**
 * Useful to mock data for ApiTests
 */
class JsonMocker {
    companion object {

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
                "items": [${multimediaMock.joinToString(", ")}]
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