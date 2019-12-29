package sebastian.osorios.udea.currencies.interfaces

import retrofit2.http.GET
import retrofit2.Call
import retrofit2.http.Path
import sebastian.osorios.udea.currencies.models.*

interface ApiService {

    @GET("json?key=2787|*f918Jxw6qeCZAJv^rfGK*h3DCiA8C0v")
    fun getAllCurrencies(): Call<jsonResponse>

    @GET("{from}/{dst}/json?quantity=1&key=2787|*f918Jxw6qeCZAJv^rfGK*h3DCiA8C0v")
    fun getChangeValues(@Path("from") from :String,  @Path("dst") dst :String): Call<jsonResponse>

}