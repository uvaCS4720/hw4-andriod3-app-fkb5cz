package edu.nd.pmcburne.hello.data.remote
import retrofit2.http.GET

interface PlacemarkApiService {
    @GET("~wxt4gm/placemarks.json")
    suspend fun getPlacemarks(): List<PlacemarkDto>

}