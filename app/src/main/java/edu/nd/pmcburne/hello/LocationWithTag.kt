package edu.nd.pmcburne.hello
data class LocationWithTags(
    val id: Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val tags: List<String>
)