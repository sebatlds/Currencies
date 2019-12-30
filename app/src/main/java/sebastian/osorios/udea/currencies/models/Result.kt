package sebastian.osorios.udea.currencies.models




data class Result (

    val from : String,
    val conversion : List<Conversion>,
    val updated : String,
    val source : String,
    val target : String,
    val value : Double,
    val quantity : Double,
    val amount : Double
)
