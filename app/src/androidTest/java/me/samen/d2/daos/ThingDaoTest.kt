package me.samen.d2.daos

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import me.samen.d2.data.AppDB
import me.samen.d2.data.entities.Thing
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ThingDaoTest {
    lateinit var db: AppDB
    lateinit var thingDao: ThingDao
    val thing = Thing(0, "ty", "desc", "", "", "")
    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(ApplicationProvider.getApplicationContext(), AppDB::class.java).allowMainThreadQueries().build()
        thingDao = db.thingDao()
    }

    @Test
    fun testIns_autogen_works() {
        runBlocking {
            val ids = thingDao.ins(thing, thing, thing)
            assertThat(ids).containsExactly(1L, 2L, 3L).inOrder()
            assertThat(thingDao._all().map { it.id }).containsExactly(1L, 2L, 3L)
        }
    }

    @Test
    fun testLookup() {
        runBlocking {
            val id = thingDao.ins1(thing)
            val id1 = thingDao.ins1(thing)
            assertThat(thingDao.lookup(id)).isEqualTo(thing.copy(id = id))
            assertThat(thingDao.lookup(id1)).isEqualTo(thing.copy(id = id1))
        }
    }
}