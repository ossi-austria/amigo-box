package org.ossiaustria.lib.domain.example.daos

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.internal.runner.junit4.AndroidJUnit4ClassRunner
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import java.io.IOException


@RunWith(AndroidJUnit4ClassRunner::class)
abstract class AbstractDaoTest {
    protected lateinit var db: AppDatabase

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, AppDatabase::class.java).build()
        init()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    abstract fun init()
}