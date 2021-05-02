package me.samen.d2.data.daos

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import me.samen.d2.data.entities.BULLET_TYPE_EVT
import me.samen.d2.data.entities.Bullet

@Dao
abstract class BulletDao {
    @Query("SELECT * FROM bullets")
    abstract suspend fun _all(): List<Bullet>

    @Query("SELECT * FROM bullets WHERE `desc` LIKE :q AND thought_id = :thoughtId AND type LIKE :type AND status LIKE :status ORDER BY id DESC")
    abstract fun all(
        thoughtId: Long,
        q: String,
        type: String,
        status: String
    ): DataSource.Factory<Int, Bullet>

    @Insert
    abstract suspend fun ins(bullet: Bullet): Long

    @Delete
    abstract suspend fun del(bullet: Bullet)

    @Query("UPDATE bullets SET status = CASE WHEN status = 'todo' then '' else 'todo' end WHERE id = :id")
    abstract suspend fun doneToggle(id: Long)

    suspend fun insHpnd(thoughtId: Long, desc: String): Long {
        return ins(Bullet(thoughtId, BULLET_TYPE_EVT, "Hpnd: $desc", ""))
    }

    @Query("select * from bullets where id = :bulletId")
    abstract fun lookup(bulletId: Long): LiveData<Bullet?>

    @Update
    abstract fun update(bullet: Bullet)
}