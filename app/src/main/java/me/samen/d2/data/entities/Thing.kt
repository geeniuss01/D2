package me.samen.d2.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "things")
data class Thing(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val type: String,
    val desc: String,
    val tags: String,
    val links: String,
    val people: String,
    val ts: String = me.samen.d2.util.ts(),
    val lastOpened: Long = 0,
    val lastTypeSetting: String = BULLET_TYPE_TODO
)


data class ThingWithBullets(
    @Embedded
    var thing: Thing? = null,
    @Relation(
        parentColumn = "id",
        entity = Bullet::class,
        entityColumn = "thought_id",
        projection = ["desc"]
    )
    var bullets: List<String> = listOf()
)