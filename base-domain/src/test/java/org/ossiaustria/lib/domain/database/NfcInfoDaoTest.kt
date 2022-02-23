@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.NfcInfoEntity
import org.ossiaustria.lib.domain.models.enums.NfcTagType
import org.robolectric.RobolectricTestRunner
import java.util.*


@RunWith(RobolectricTestRunner::class)
internal class NfcInfoDaoTest : DoubleEntityDaoTest<NfcInfoEntity, NfcInfoEntity, NfcInfoDao>() {

    override fun init() {
        dao = db.nfcInfoDao()
    }

    override fun createEntity(id: UUID): NfcInfoEntity {
        return NfcInfoEntity(
            nfcTagId = UUID.randomUUID(),
            type = NfcTagType.OPEN_ALBUM,
            creatorId = UUID.randomUUID(),
            name="name", nfcRef = "ref"
        )
    }

    override fun permuteEntity(entity: NfcInfoEntity): NfcInfoEntity {
        return entity.copy(
            linkedAlbumId = UUID.randomUUID(),
            linkedPersonId = UUID.randomUUID()
        )
    }

    override fun findById(entity: NfcInfoEntity): NfcInfoEntity {
        return runBlocking { dao.findById(entity.nfcTagId).take(1).first() }
    }

    override fun deleteById(entity: NfcInfoEntity) {
        runBlocking { dao.deleteById(entity.nfcTagId) }
    }

    override fun checkEqual(wrapper: NfcInfoEntity, entity: NfcInfoEntity) {
        MatcherAssert.assertThat(wrapper, CoreMatchers.equalTo(entity))
    }

    override fun checkSameId(wrapper: NfcInfoEntity, entity: NfcInfoEntity) {
        MatcherAssert.assertThat(wrapper.nfcTagId, CoreMatchers.equalTo(entity.nfcTagId))
    }
}