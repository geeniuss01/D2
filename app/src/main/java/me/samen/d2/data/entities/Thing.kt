package me.samen.d2.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.SimpleDateFormat
import java.util.*

@Entity(tableName = "things")
data class Thing(
    @PrimaryKey(autoGenerate = true) val id : Long = 0L,
    val type : String,
    val desc: String,
    val tags: String,
    val links: String,
    val people : String,
    val ts: String = SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(Date())
)
