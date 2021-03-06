package org.ossiaustria.lib.domain.database

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner


@RunWith(RobolectricTestRunner::class)
internal abstract class RobolectricDaoTest {
    protected lateinit var db: AppDatabaseImpl

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()

        db = Room.inMemoryDatabaseBuilder(context, AppDatabaseImpl::class.java).build()
        init()
    }

    @After
    fun closeDb() {
        db.close()
    }

    abstract fun init()
}