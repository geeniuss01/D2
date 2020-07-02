package me.samen.d2.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import me.samen.d2.data.daos.BulletDao
import me.samen.d2.data.daos.ThingDao
import me.samen.d2.data.entities.Bullet
import me.samen.d2.data.entities.Thing

@Database(entities = [Thing::class, Bullet::class], version = 4)
abstract class AppDB : RoomDatabase() {
    abstract fun thingDao(): ThingDao
    abstract fun bulletDao(): BulletDao

    companion object {
        private lateinit var INSTANCE: AppDB

        @JvmStatic
        fun instance(context: Context): AppDB {
            if (!::INSTANCE.isInitialized) {
                INSTANCE = Room.databaseBuilder(context, AppDB::class.java, "appdb")
                    .addMigrations(M_2_3, M_3_4)
                    .build()
            }
            return INSTANCE
        }

        private val M_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("CREATE TABLE IF NOT EXISTS `bullets` (`thought_id` INTEGER NOT NULL, `type` TEXT NOT NULL, `desc` TEXT NOT NULL, `status` TEXT NOT NULL, `id` INTEGER NOT NULL, `ts` TEXT NOT NULL, PRIMARY KEY(`id`), FOREIGN KEY(`thought_id`) REFERENCES `things`(`id`) ON UPDATE NO ACTION ON DELETE CASCADE )")
            }
        }

        private val M_3_4 = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
    }
}
