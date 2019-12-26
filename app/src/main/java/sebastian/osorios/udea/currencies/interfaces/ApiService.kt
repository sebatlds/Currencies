package sebastian.osorios.udea.currencies.interfaces

import retrofit2.http.GET
import retrofit2.Call
import sebastian.osorios.udea.currencies.models.*

interface ApiService {

    @GET("json?key=2787|*f918Jxw6qeCZAJv^rfGK*h3DCiA8C0v")
    fun getAllCurrencies(): Call<jsonResponse>
}