package com.cinestream.app.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        WatchlistEntity::class,
        ContinueWatchingEntity::class,
        SearchHistoryEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class CineStreamDatabase : RoomDatabase() {
    abstract fun watchlistDao(): WatchlistDao
    abstract fun continueWatchingDao(): ContinueWatchingDao
    abstract fun searchHistoryDao(): SearchHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: CineStreamDatabase? = null

        fun getInstance(context: Context): CineStreamDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    CineStreamDatabase::class.java,
                    "cinestream_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
