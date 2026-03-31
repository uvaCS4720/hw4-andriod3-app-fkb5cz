package edu.nd.pmcburne.hello.data.remote

data class PlacemarkDto (
    val id:Int,
    val name:String,
    val tag_list:List<String>,
    val description: String,
    val visual_center:VisualCenterDto
)

data class VisualCenterDto(
    val latitude:Double,
    val longitude:Double
)