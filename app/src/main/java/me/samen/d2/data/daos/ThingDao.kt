package me.samen.d2.data.daos

import androidx.lifecycle.LiveData
import androidx.room.*
import me.samen.d2.data.entities.Thing

@Dao
abstract class ThingDao {
    @Query("SELECT * FROM things ORDER BY lastOpened DESC")
    abstract fun all(): androidx.paging.DataSource.Factory<Int, Thing>

    @Query("SELECT * FROM things WHERE `desc` like :q OR tags like :q OR people like :q OR type like :q ORDER BY lastOpened DESC")
    abstract fun search(q: String): androidx.paging.DataSource.Factory<Int, Thing>

    @Insert
    abstract suspend fun ins(vararg thing: Thing): List<Long>

    @Insert
    abstract suspend fun ins1(thing: Thing): Long

    @Query("SELECT * FROM things")
    abstract fun _all(): List<Thing>

    @Query("SELECT * FROM things WHERE id = :id")
    abstract suspend fun lookup(id: Long): Thing?

    @Query("SELECT * FROM things WHERE id = :id")
    abstract fun lookupLive(id: Long): LiveData<Thing?>

    @Update
    abstract suspend fun upd(thing: Thing)

    @Delete
    abstract suspend fun del(theThing: Thing)
}