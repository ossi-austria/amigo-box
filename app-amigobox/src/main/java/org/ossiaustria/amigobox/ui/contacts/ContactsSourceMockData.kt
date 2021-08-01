package org.ossiaustria.amigobox.ui.contacts

import org.ossiaustria.amigobox.R
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.MembershipType
import java.util.*


fun listOfPeopleWithImages() = mutableListOf(
    PersonImage(Person(UUID.randomUUID(), "Lukas", "lukas@tum.de", MembershipType.MEMBER,
        UUID.randomUUID()), R.drawable.image_lukas_amigo),
    PersonImage(Person(UUID.randomUUID(), "Michl", "michl@tum.de", MembershipType.MEMBER,
        UUID.randomUUID()), R.drawable.image_michl_amigo),
    PersonImage(Person(UUID.randomUUID(), "Moni", "moni@tum.de", MembershipType.MEMBER,
        UUID.randomUUID()), R.drawable.image_moni_amigo),
    PersonImage(Person(UUID.randomUUID(), "Peter", "peter@tum.de", MembershipType.MEMBER,
        UUID.randomUUID()), R.drawable.image_peter_amigo),
    PersonImage(Person(UUID.randomUUID(), "Flo", "flo@tum.de", MembershipType.MEMBER,
        UUID.randomUUID()), R.drawable.image_flo_amigo)
    )

