package edu.nd.pmcburne.hello.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "placemarks")
data class PlacemarkEntity (
    @PrimaryKey val id:Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val tags: String
) {
    fun tagList(): List<String> =
        tags.split("|").map {it.trim()}.filter{it.isNotBlank()}
}
