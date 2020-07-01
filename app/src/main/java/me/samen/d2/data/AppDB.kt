package me.samen.d2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import me.samen.d2.daos.ThingDao
import me.samen.d2.data.entities.Thing

@Database(entities = [Thing::class], version = 2)
abstract class AppDB : RoomDatabase() {
    abstract fun thingDao(): ThingDao

    companion object {
        private lateinit var INSTANCE: AppDB

        @JvmStatic
        fun instance(context: Context): AppDB {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(context, AppDB::class.java, "appdb")
                    .addMigrations(M_1_2)
                    .build()
            }
            return INSTANCE
        }

        // sample migration
        private val M_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                /*database.execSQL("ALTER TABLE u ADD COLUMN receivedTs INTEGER NOT NULL DEFAULT ${System.currentTimeMillis()}")*/
            }
        }
    }
}
