package edu.nd.pmcburne.hello

import org.json.JSONArray
import java.net.HttpURLConnection
import java.net.URL

private const val API_URL = "https://www.cs.virginia.edu/~wxt4gm/placemarks.json"

data class ApiPlacemark(
    val id: Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val tags: List<String>
)

object PlacemarkApi {
    fun fetchPlacemarks(): List<ApiPlacemark> {
        val connection = URL(API_URL).openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 10000
        connection.readTimeout = 10000

        return try {
            val text = connection.inputStream.bufferedReader().use { it.readText() }
            parseJson(text)
        } finally {
            connection.disconnect()
        }
    }

    private fun parseJson(json: String): List<ApiPlacemark> {
        val array = JSONArray(json)
        val results = mutableListOf<ApiPlacemark>()

        for (i in 0 until array.length()) {
            val obj = array.getJSONObject(i)

            val id = obj.getInt("id")
            val name = obj.getString("name")
            val description = obj.optString("description", "")

            val center = obj.getJSONObject("visual_center")
            val latitude = center.getDouble("latitude")
            val longitude = center.getDouble("longitude")

            val tagArray = obj.getJSONArray("tag_list")
            val tags = mutableListOf<String>()
            for (j in 0 until tagArray.length()) {
                tags.add(tagArray.getString(j))
            }

            results.add(
                ApiPlacemark(
                    id = id,
                    name = name,
                    description = description,
                    latitude = latitude,
                    longitude = longitude,
                    tags = tags
                )
            )
        }

        return results
    }
}