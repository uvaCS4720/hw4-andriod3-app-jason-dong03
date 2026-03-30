package edu.nd.pmcburne.hello

import androidx.room.Entity

@Entity(
    tableName = "location_tags",
    primaryKeys = ["locationId", "tag"]
)
data class LocationTagEntity(
    val locationId: Int,
    val tag: String
)