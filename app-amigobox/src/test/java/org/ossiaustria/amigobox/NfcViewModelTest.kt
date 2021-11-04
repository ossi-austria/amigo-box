package org.ossiaustria.amigobox

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.ossiaustria.amigobox.nfc.NfcViewModel
import org.ossiaustria.lib.commons.testing.TestCoroutineRule
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Album
import org.ossiaustria.lib.domain.models.NfcInfo
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.MemberType
import org.ossiaustria.lib.domain.models.enums.NfcTagType
import org.ossiaustria.lib.domain.repositories.AlbumRepository
import org.ossiaustria.lib.domain.repositories.PersonRepository
import org.ossiaustria.lib.domain.services.NfcInfoService
import org.ossiaustria.lib.nfc.NfcHandler
import java.util.UUID.randomUUID
import kotlin.test.assertEquals

@FlowPreview
internal class NfcViewModelTest {

    private lateinit var subject: NfcViewModel

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var nfcInfoService: NfcInfoService

    @MockK
    lateinit var albumRepository: AlbumRepository

    @MockK
    lateinit var personRepository: PersonRepository

    @MockK
    lateinit var navigator: Navigator

    @Before
    fun setupBeforeEach() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        subject = NfcViewModel(
            coroutineRule.dispatcher,
            nfcInfoService,
            albumRepository, personRepository
        )
    }

    @Test
    fun `processNfcTagData should search for nfcRef id`() =
        runBlockingTest {
            val data = NfcHandler.NfcTagData("nfcRef")
            // prepare test
            val nfcInfo = mockNfcInfo()
            every { nfcInfoService.findPerRef(eq("nfcRef")) } returns
                flowOf(Resource.success(nfcInfo))

            subject.processNfcTagData(data)

            verify { nfcInfoService.findPerRef(eq("nfcRef")) }
            assertEquals(nfcInfo, subject.nfcInfo.value?.valueOrNull())
        }

    @Test
    fun `handleNfcInfo should load Album for OPEN_ALBUM`() =
        runBlockingTest {
            // prepare test
            val nfcInfo = mockNfcInfo(tagType = NfcTagType.OPEN_ALBUM)
            every { albumRepository.getAlbum(eq(nfcInfo.linkedAlbumId!!)) } returns
                flowOf(Resource.success(EntityMocks.album(id = nfcInfo.linkedAlbumId!!)))

            subject.handleNfcInfo(nfcInfo)

            verify { albumRepository.getAlbum(eq(nfcInfo.linkedAlbumId!!)) }

            subject.nfcInfo.observeForever {
                assertEquals(nfcInfo, it?.valueOrNull())
            }
        }

    @Test
    fun `handleNfcInfo should load Person for CALL_PERSON`() = runBlockingTest {
        // prepare test
        val nfcInfo = mockNfcInfo(tagType = NfcTagType.CALL_PERSON)
        val person = EntityMocks.person(personId = nfcInfo.linkedPersonId!!, randomUUID())
        every { personRepository.getPerson(eq(nfcInfo.linkedPersonId!!)) } returns
            flowOf(Resource.success(person))

        subject.handleNfcInfo(nfcInfo)

        verify { personRepository.getPerson(eq(nfcInfo.linkedPersonId!!)) }
        subject.nfcInfo.observeForever {
            assertEquals(nfcInfo, it?.valueOrNull())
        }
    }

    private fun mockNfcInfo(
        tagType: NfcTagType = NfcTagType.OPEN_ALBUM,
        nfcRef: String = "nfcRef"
    ) = NfcInfo(
        randomUUID(), randomUUID(), randomUUID(),
        tagType,
        "name", nfcRef,
        randomUUID(), randomUUID()
    )

    @Test
    fun `openAlbum should navigate to Album `() = runBlockingTest {
        val mock = Album(randomUUID(), "Peter", randomUUID(), emptyList())
        subject.openAlbum(mock, navigator)
        every { navigator.toImageGallery(eq(mock)) }
    }

    @Test
    fun `callPerson should navigate to Person `() = runBlockingTest {
        val mock = Person(
            randomUUID(),
            "Peter",
            randomUUID(),
            MemberType.MEMBER,
            "abc@gmx.de"
        )

        subject.callPerson(mock, navigator)
        every { navigator.toCallFragment(eq(mock)) }
    }

}