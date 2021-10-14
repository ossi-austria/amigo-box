package org.ossiaustria.amigobox.ui.contacts

import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.MemberType
import java.util.UUID.randomUUID

object ContactsSourceMockData {

    private val groupId = randomUUID()

    val lukas = createPersonMock("Lukas", "lukas@tum.de")

    fun listOfPeopleWithImages(): MutableList<Person> {
        return mutableListOf(
            lukas,
            createPersonMock("Michl", "michl@tum.de"),
            createPersonMock("Moni", "moni@tum.de"),
            createPersonMock("Peter", "peter@tum.de"),
            createPersonMock("Flo", "flo@tum.de"),
            createPersonMock("1 Michl", "michl@tum.de"),
            createPersonMock("1 Moni", "moni@tum.de"),
            createPersonMock("1 Peter", "peter@tum.de"),
            createPersonMock("1 Flo", "flo@tum.de"),
        )
    }

    fun createPersonMock(name: String, email: String) =
        Person(
            randomUUID(),
            name,
            groupId,
            MemberType.MEMBER,
            email,
            avatarUrl = "https://thispersondoesnotexist.com/image"
        )

}



