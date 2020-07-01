package me.samen.d2.data.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import me.samen.d2.data.entities.Thing
import javax.sql.DataSource

@Dao
abstract class ThingDao  {
    @Query("SELECT * FROM things")
    abstract fun all(): androidx.paging.DataSource.Factory<Int, Thing>

    @Insert
    abstract suspend fun ins(vararg thing: Thing)
}