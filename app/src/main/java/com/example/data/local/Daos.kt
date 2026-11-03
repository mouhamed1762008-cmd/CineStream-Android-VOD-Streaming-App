package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface WatchlistDao {
    @Query("SELECT * FROM watchlist ORDER BY addedAt DESC")
    fun getAllWatchlist(): Flow<List<WatchlistEntity>>

    @Query("SELECT EXISTS(SELECT 1 FROM watchlist WHERE id = :contentId)")
    fun isWatchlisted(contentId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(item: WatchlistEntity)

    @Query("DELETE FROM watchlist WHERE id = :contentId")
    suspend fun deleteById(contentId: String)
}

@Dao
interface ContinueWatchingDao {
    @Query("SELECT * FROM continue_watching ORDER BY lastWatchedAt DESC")
    fun getAllContinueWatching(): Flow<List<ContinueWatchingEntity>>

    @Query("SELECT * FROM continue_watching WHERE id = :id LIMIT 1")
    suspend fun getById(id: String): ContinueWatchingEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(item: ContinueWatchingEntity)

    @Query("DELETE FROM continue_watching WHERE id = :id")
    suspend fun deleteById(id: String)

    @Query("DELETE FROM continue_watching")
    suspend fun clearAll()
}

@Dao
interface SearchHistoryDao {
    @Query("SELECT DISTINCT query FROM search_history ORDER BY searchedAt DESC LIMIT 10")
    fun getRecentSearches(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearch(item: SearchHistoryEntity)

    @Query("DELETE FROM search_history WHERE query = :query")
    suspend fun deleteSearch(query: String)

    @Query("DELETE FROM search_history")
    suspend fun clearHistory()
}
