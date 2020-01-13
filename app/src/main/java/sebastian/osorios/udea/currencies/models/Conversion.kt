package sebastian.osorios.udea.currencies.models


data class Conversion(
    val to : String,
    val date : String,
    val rate : Double
)