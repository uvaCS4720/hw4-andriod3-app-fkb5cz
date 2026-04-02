package edu.nd.pmcburne.hello.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "placemarks")
data class PlacemarkEntity (
    // unique id to avoid duplicates
    @PrimaryKey val id:Int,
    val name: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val tags: String
) {

    // convert stored string into list of tags
    fun tagList(): List<String> =
        tags.split("|").map {it.trim()}.filter{it.isNotBlank()}
}
