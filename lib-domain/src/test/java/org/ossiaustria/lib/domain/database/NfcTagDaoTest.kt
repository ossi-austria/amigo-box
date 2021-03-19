@file:Suppress("IllegalIdentifier")

package org.ossiaustria.lib.domain.database

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.junit.runner.RunWith
import org.ossiaustria.lib.domain.database.entities.NfcTagEntity
import org.ossiaustria.lib.domain.models.enums.NfcTagType
import org.robolectric.RobolectricTestRunner
import java.util.*


@RunWith(RobolectricTestRunner::class)
internal class NfcTagDaoTest : DoubleEntityDaoTest<NfcTagEntity, NfcTagEntity, NfcTagDao>() {

    override fun init() {
        dao = db.nfcTagDao()
    }

    override fun createEntity(id: UUID): NfcTagEntity {
        return NfcTagEntity(
            nfcTagId = UUID.randomUUID(),
            type = NfcTagType.COLLECTION,
            creatorId = UUID.randomUUID()
        )
    }

    override fun permuteEntity(entity: NfcTagEntity): NfcTagEntity {
        return entity.copy(
            linkedAlbumId = UUID.randomUUID(),
            linkedPersonId = UUID.randomUUID()
        )
    }

    override fun findById(entity: NfcTagEntity): NfcTagEntity {
        return runBlocking { dao.findById(entity.nfcTagId).take(1).first() }
    }

    override fun checkEqual(wrapper: NfcTagEntity, entity: NfcTagEntity) {
        MatcherAssert.assertThat(wrapper, CoreMatchers.equalTo(entity))
    }

    override fun checkSameId(wrapper: NfcTagEntity, entity: NfcTagEntity) {
        MatcherAssert.assertThat(wrapper.nfcTagId, CoreMatchers.equalTo(entity.nfcTagId))
    }

    override fun deleteById(entity: NfcTagEntity) {
        runBlocking { dao.deleteById(entity.nfcTagId) }
    }
}