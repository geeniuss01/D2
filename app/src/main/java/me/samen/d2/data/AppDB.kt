package me.samen.d2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import me.samen.d2.data.daos.ThingDao
import me.samen.d2.data.entities.Bullet
import me.samen.d2.data.entities.Thing

@Database(entities = [Thing::class, Bullet::class], version = 3)
abstract class AppDB : RoomDatabase() {
    abstract fun thingDao(): ThingDao

    companion object {
        private lateinit var INSTANCE: AppDB

        @JvmStatic
        fun instance(context: Context): AppDB {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(context, AppDB::class.java, "appdb")
                    .addMigrations(M_2_3)
                    .build()
            }
            return INSTANCE
        }

        private val M_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `bullets` (`thought_id` INTEGER NOT NULL, `type` TEXT NOT NULL, `desc` TEXT NOT NULL, `status` TEXT NOT NULL, `id` INTEGER NOT NULL, `ts` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`thought_id`) REFERENCES `things`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            }
        }
    }
}
