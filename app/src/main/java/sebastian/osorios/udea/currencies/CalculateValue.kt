package sebastian.osorios.udea.currencies

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Spinner
import kotlinx.android.synthetic.main.activity_calculate_value.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sebastian.osorios.udea.currencies.interfaces.ApiService
import sebastian.osorios.udea.currencies.models.jsonResponse
import sebastian.osorios.udea.currencies.util.NameCoins

class CalculateValue : AppCompatActivity() {

    lateinit var spinnerFrom: Spinner
    lateinit var spinnerDst: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_value)
        var from: String
        var dst: String
        var nameCoins: NameCoins = NameCoins()
        var currencies: Array<String> = nameCoins.getNamesCurrencies()
        spinnerFrom = findViewById(R.id.spinnerOrigen)
        spinnerDst = findViewById(R.id.spinnerDestino)
        var arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.spinner_item_currencies, currencies)
        spinnerFrom.setAdapter(arrayAdapter)
        spinnerDst.setAdapter(arrayAdapter)

        Calculate.setOnClickListener {
            dst = nameCoins.selectCurrencie(spinnerDst.getSelectedItem().toString())
            from = nameCoins.selectCurrencie(spinnerFrom.getSelectedItem().toString())
            getValueChange(from, dst, this)
        }
    }

    private fun getValueChange(from: String, dst: String, context: Context) {
        val froma = from.trim()
        val dsta = dst.trim()
        val alert = AlertDialog.Builder(context)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.devises.zone/v1/quotes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiService::class.java)

        service.getChangeValues(froma,dsta).enqueue(object : Callback<jsonResponse> {
            override fun onResponse(call: Call<jsonResponse>, response: Response<jsonResponse>) {
                if (response.code() >= 400) {
                    alert.setTitle("Error")
                    alert.setMessage("Se presento un error con el servicio. \n Codigo de Error = " + response.code())
                    alert.setPositiveButton(
                        "Confirmar", DialogInterface.OnClickListener { dialogo1, id -> null })
                    alert.show()
                }else{
                    var respons = response.body()
                    print(respons)

                }
            }
            override fun onFailure(call: Call<jsonResponse>, t: Throwable) {
                    if (t != null) {
                        alert.setTitle("Error")
                        alert.setMessage(t.message.toString())
                        alert.setPositiveButton(
                            "Confirmar", DialogInterface.OnClickListener { dialogo1, id -> aceptar() })
                        alert.show()
                    }else{
                        alert.setTitle("Error")
                        alert.setMessage("No se pudo procesar la solicitud")
                        alert.setPositiveButton(
                            "Confirmar", DialogInterface.OnClickListener { dialogo1, id -> aceptar() })
                        alert.show()
                    }
            }
        })

    }
    private fun aceptar() {
        val intento = Intent(this, MainActivity ::class.java)
        startActivity(intento)
    }
}
