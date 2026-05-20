package ap.mobile.composablemap.entity

data class ParcelEntity(
    val id: Int,
    val lat: Double,
    val lng: Double,
    val type: String,
    val recipientName: String,
    val address: String,
)
