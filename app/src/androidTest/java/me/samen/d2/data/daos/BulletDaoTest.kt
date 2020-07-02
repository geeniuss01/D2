package me.samen.d2.data.daos

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.runBlocking
import me.samen.d2.data.AppDB
import me.samen.d2.data.entities.BULLET_TYPE_EVT
import me.samen.d2.data.entities.BULLET_TYPE_TODO
import me.samen.d2.data.entities.Bullet
import me.samen.d2.data.entities.Thing
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BulletDaoTest {
    lateinit var db: AppDB
    lateinit var dao: BulletDao
    lateinit var thingDao: ThingDao
    val thing = Thing(0, "ty", "desc", "", "", "")

    @Before
    fun setUp() {
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDB::class.java
        ).allowMainThreadQueries().build()
        dao = db.bulletDao()
        thingDao = db.thingDao()

    }

    @Test
    fun testcrud() {
        runBlocking {
            val thingID = thingDao.ins1(thing)
            val bullet = Bullet(thingID, BULLET_TYPE_TODO, "test", "")

            assertThat(dao._all()).isEmpty()
            val bid = dao.ins(bullet)
            assertThat(dao._all()).containsExactly(bullet.copy(id = bid))

            dao.doneToggle(bid)
            assertThat(dao._all()).containsExactly(bullet.copy(id = bid, status = "todo"))

            dao.doneToggle(bid)
            assertThat(dao._all()).containsExactly(bullet.copy(id = bid, status = ""))

            dao.del(bullet.copy(id = bid))
            assertThat(dao._all()).isEmpty()
        }
    }

    @Test
    fun test_hpnd() {
        runBlocking {
            val thingID = thingDao.ins1(thing)
            val bid = dao.insHpnd(thingID, "10:30")
            assertThat(dao._all()).containsExactly(
                Bullet(thingID, BULLET_TYPE_EVT, "Hpnd: 10:30", "", bid)
            )
        }
    }
}