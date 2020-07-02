package me.samen.d2.data.entities

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey

@Entity(
    tableName = "bullets",
    foreignKeys = [ForeignKey(
        entity = Thing::class,
        parentColumns = ["id"],
        childColumns = ["thought_id"],
        onDelete = CASCADE
    )]
)
data class Bullet(
    val thought_id: Long,
    val type: String, // BULLET_TYPE_*
    val desc: String,
    val status: String, // done or null
    @PrimaryKey val id: Long = 0,
    val ts: String = me.samen.d2.util.ts()
)

const val BULLET_TYPE_TODO = "todo"
const val BULLET_TYPE_EVT = "evt"
const val BULLET_TYPE_NOTE = "note"