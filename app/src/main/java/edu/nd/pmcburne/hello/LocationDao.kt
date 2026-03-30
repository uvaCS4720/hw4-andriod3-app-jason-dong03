package edu.nd.pmcburne.hello

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert

@Dao
interface LocationDao {

    @Upsert
    suspend fun upsertLocations(locations: List<LocationEntity>)

    @Upsert
    suspend fun upsertLocationTags(tags: List<LocationTagEntity>)

    @Query("SELECT DISTINCT tag FROM location_tags ORDER BY tag ASC")
    suspend fun getAllTags(): List<String>

    @Transaction
    @Query("""
        SELECT l.id, l.name, l.description, l.latitude, l.longitude
        FROM locations l
        INNER JOIN location_tags t ON l.id = t.locationId
        WHERE t.tag = :selectedTag
        ORDER BY l.name ASC
    """)
    suspend fun getLocationsForTagRaw(selectedTag: String): List<LocationRow>

    @Query("SELECT tag FROM location_tags WHERE locationId = :locationId ORDER BY tag ASC")
    suspend fun getTagsForLocation(locationId: Int): List<String>
}

data class LocationRow(
    val id: Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double
)