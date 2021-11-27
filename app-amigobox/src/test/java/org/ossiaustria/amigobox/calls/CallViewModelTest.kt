package org.ossiaustria.amigobox.calls

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.jraska.livedata.test
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.ossiaustria.lib.commons.testing.TestCoroutineRule
import org.ossiaustria.lib.domain.common.Resource
import org.ossiaustria.lib.domain.models.Call
import org.ossiaustria.lib.domain.models.Group
import org.ossiaustria.lib.domain.models.Person
import org.ossiaustria.lib.domain.models.enums.CallState
import org.ossiaustria.lib.domain.models.enums.CallType
import org.ossiaustria.lib.domain.models.enums.MemberType
import org.ossiaustria.lib.domain.modules.UserContext
import org.ossiaustria.lib.domain.repositories.GroupRepository
import org.ossiaustria.lib.domain.services.CallService
import java.util.*
import java.util.UUID.randomUUID

@FlowPreview
internal class CallViewModelTest {

    private lateinit var subject: CallViewModel

    @get:Rule
    val coroutineRule = TestCoroutineRule()

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @MockK
    lateinit var callService: CallService

    @MockK
    lateinit var userContext: UserContext

    @MockK
    lateinit var groupRepository: GroupRepository

    val groupId = randomUUID()
    val analoguePerson = Person(
        id = randomUUID(),
        name = "Analgue",
        groupId = groupId,
        memberType = MemberType.ANALOGUE,
        avatarUrl = "https://thispersondoesnotexist.com/image"
    )
    val digitalPerson = Person(
        id = randomUUID(),
        name = "digitalPerson",
        groupId = groupId,
        memberType = MemberType.MEMBER,
        avatarUrl = "https://thispersondoesnotexist.com/image"
    )
    val group = Group(id = groupId, name = "Group", members = listOf(analoguePerson, digitalPerson))

    @Before
    fun setupBeforeEach() {
        MockKAnnotations.init(this, relaxUnitFun = true)
        subject = CallViewModel(
            coroutineRule.dispatcher,
            userContext,
            callService,
            groupRepository
        )

        every { groupRepository.getGroup(eq(groupId)) } returns flowOf(Resource.success(group))
        every { userContext.person() } returns analoguePerson
        every { userContext.personId() } returns analoguePerson.id
        coEvery { callService.accept(any()) } answers {
            val call = it.invocation.args.first() as Call
            Resource.success(call.copy(callState = CallState.ACCEPTED))
        }
    }

    @Test
    fun `acceptIncomingCall should set Outgoing call state`() = runBlockingTest {
        val call = mockIncomingCall()

        subject.prepareIncomingCall(call)

        subject.state
            .test()
            .assertValue { it is CallViewState.Calling }
            .assertValue { !it.outgoing }
    }

    @Test
    fun `createNewOutgoingCall should set outgoing call state`() = runBlockingTest {
        val resultingCall = mockOutgoingCall()
        coEvery { callService.createCall(eq(digitalPerson), any()) } returns
            Resource.Success(resultingCall)

        subject.createNewOutgoingCall(digitalPerson)

        subject.state
            .test()
            .assertValue { it is CallViewState.Calling }
            .assertValue { it.outgoing }
    }

    @Test
    fun `accept should accept Incoming Call`() = runBlockingTest {
        val call = mockIncomingCall()

        subject.prepareIncomingCall(call)

        val resultingCall = call.copy(startedAt = Date(), callState = CallState.ACCEPTED)
        coEvery { callService.accept(eq(call)) } returns
            Resource.Success(resultingCall)

        subject.accept()

        subject.state.test()
            .assertHasValue()
            .assertValue(CallViewState.Accepted(resultingCall, false))
    }

    @Test
    fun `deny should deny Incoming Call`() = runBlockingTest {
        val call = mockIncomingCall()
        subject.prepareIncomingCall(call)

        val resultingCall = call.copy(startedAt = Date(), callState = CallState.CANCELLED)
        coEvery { callService.deny(eq(call)) } returns
            Resource.Success(resultingCall)

        subject.deny()

        subject.state.test()
            .assertHasValue()
            .assertValue(CallViewState.Cancelled(resultingCall, false))
    }

    @Test
    fun `cancel should cancel Outgoing Call`() = runBlockingTest {
        val call = mockOutgoingCall()
        coEvery { callService.createCall(eq(digitalPerson), any()) } returns
            Resource.Success(call)
        subject.createNewOutgoingCall(digitalPerson)

        val resultingCall = call.copy(startedAt = null, callState = CallState.CANCELLED)
        coEvery { callService.cancel(eq(call)) } returns
            Resource.Success(resultingCall)

        subject.cancel()

        subject.state.test()
            .assertHasValue()
            .assertValue(CallViewState.Cancelled(resultingCall, true))
    }

    private fun mockOutgoingCall() = mockCall(
        senderId = analoguePerson.id,
        receiverId = digitalPerson.id,
    )

    private fun mockIncomingCall() = mockCall(
        senderId = digitalPerson.id,
        receiverId = analoguePerson.id,
    )

    private fun mockCall(
        senderId: UUID = analoguePerson.id,
        receiverId: UUID = digitalPerson.id,
    ) = Call(
        id = randomUUID(),
        senderId = senderId,
        receiverId = receiverId,
        callType = CallType.AUDIO,
        callState = CallState.CALLING,
        startedAt = null,
        finishedAt = null
    )

}