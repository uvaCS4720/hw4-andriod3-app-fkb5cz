package edu.nd.pmcburne.hello.data.repository
import edu.nd.pmcburne.hello.data.local.PlacemarkDao
import edu.nd.pmcburne.hello.data.local.PlacemarkEntity
import edu.nd.pmcburne.hello.data.remote.ApiClient
import kotlinx.coroutines.flow.Flow

class PlacemarkRepository (private val dao: PlacemarkDao) {
    val allPlacemarks: Flow<List<PlacemarkEntity>> = dao.getAllPlacemarks()

    suspend fun syncFromApi() {
        val remote= ApiClient.service.getPlacemarks()
        val entities = remote.map {
            PlacemarkEntity(
                id = it.id,
                name = it.name,
                description = it.description,
                latitude = it.visual_center.latitude,
                longitude = it.visual_center.longitude,
                tags = it.tag_list.sorted().joinToString("|")
            )
        }
        dao.upsertAll(entities)
    }
}