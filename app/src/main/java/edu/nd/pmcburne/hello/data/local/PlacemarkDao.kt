package edu.nd.pmcburne.hello.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface PlacemarkDao {
    @Upsert
    suspend fun upsertAll(items: List<PlacemarkEntity>)

    @Query("SELECT * FROM placemarks")
    fun getAllPlacemarks(): Flow<List<PlacemarkEntity>>
}