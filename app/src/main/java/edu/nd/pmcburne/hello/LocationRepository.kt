package edu.nd.pmcburne.hello

class LocationRepository(
    private val dao: LocationDao
) {
    suspend fun syncFromApi() {
        val apiLocations = PlacemarkApi.fetchPlacemarks()

        val locationEntities = apiLocations.map {
            LocationEntity(
                id = it.id,
                name = it.name,
                description = it.description,
                latitude = it.latitude,
                longitude = it.longitude
            )
        }

        val tagEntities = apiLocations.flatMap { placemark ->
            placemark.tags.map { tag ->
                LocationTagEntity(
                    locationId = placemark.id,
                    tag = tag
                )
            }
        }

        dao.upsertLocations(locationEntities)
        dao.upsertLocationTags(tagEntities)
    }

    suspend fun getAllTags(): List<String> {
        return dao.getAllTags()
    }

    suspend fun getLocationsForTag(tag: String): List<LocationWithTags> {
        val rows = dao.getLocationsForTagRaw(tag)
        return rows.map { row ->
            LocationWithTags(
                id = row.id,
                name = row.name,
                description = row.description,
                latitude = row.latitude,
                longitude = row.longitude,
                tags = dao.getTagsForLocation(row.id)
            )
        }
    }
}