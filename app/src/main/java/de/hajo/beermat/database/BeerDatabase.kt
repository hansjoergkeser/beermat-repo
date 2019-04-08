package de.hajo.beermat.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import de.hajo.beermat.model.Beermat
import de.hajo.beermat.model.User
import timber.log.Timber

const val DB_NAME = "beermat-database.db"

/**
 * @author hansjoerg.keser
 * @since 23.11.18
 */
@Database(entities = [User::class, Beermat::class], version = 3)
abstract class BeerDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
    abstract fun beerDao(): BeermatDao

    // https://medium.com/@BladeCoder/kotlin-singletons-with-argument-194ef06edd9e
    companion object : SingletonHolder<BeerDatabase, Context>({
        Timber.i("Creating BeerDatabase.")
        Room.databaseBuilder(it.applicationContext, BeerDatabase::class.java, DB_NAME)
            .addMigrations(MIGRATION_1_2)
            .addMigrations(MIGRATION_2_3)
            // https://developer.android.com/training/data-storage/room/migrating-db-versions
            .fallbackToDestructiveMigration()
            .build()
    }) {
        object MIGRATION_1_2 : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Timber.d("Executing migration from version 1 to 2 in beerDatabase: ALTER TABLE User ADD COLUMN has_premium_status INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE User ADD COLUMN has_premium_status INTEGER NOT NULL DEFAULT 0")
            }
        }

        object MIGRATION_2_3 : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                Timber.d("Executing migration from version 1 to 2 in beerDatabase: ALTER TABLE User ADD COLUMN has_premium_status INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE Beermat ADD COLUMN price INTEGER NOT NULL DEFAULT 0")
                database.execSQL("ALTER TABLE Beermat ADD COLUMN total_price INTEGER NOT NULL DEFAULT 0")
            }
        }
    }

}
