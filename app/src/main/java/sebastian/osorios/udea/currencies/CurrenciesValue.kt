package sebastian.osorios.udea.currencies

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.widget.GridLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import sebastian.osorios.udea.currencies.interfaces.ApiService
import sebastian.osorios.udea.currencies.models.Conversion
import sebastian.osorios.udea.currencies.models.jsonResponse
import sebastian.osorios.udea.currencies.util.NameCoins


class CurrenciesValue : AppCompatActivity() {

    var grid : GridLayout? = null
    var linear : LinearLayout? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_currencies_value)
        getCurrencies(this)
        grid = findViewById(R.id.gridValue)
        linear = findViewById(R.id.linearLayout)

    }

    private fun printValues() {


    }


    fun getCurrencies(currenciesValue: CurrenciesValue) {
        val alert = AlertDialog.Builder(currenciesValue)
        var context : Context = this
        var nameCoins = NameCoins()
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.cambio.today/v1/full/USD/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service = retrofit.create(ApiService::class.java)

        service.getAllCurrencies().enqueue(object: Callback<jsonResponse>{
            override fun onResponse(call: Call<jsonResponse>, response: Response<jsonResponse>) {
                if(response.code() >= 400){
                    alert.setTitle("Error")
                    alert.setMessage("Se presento un error con el servicio. \n Codigo de Error = "+response.code())
                    alert.setPositiveButton(
                        "Confirmar", DialogInterface.OnClickListener { dialogo1, id -> aceptar() })
                    alert.show()
                }else {
                    var rpta: jsonResponse? = response.body()
                    var listConversion: List<Conversion>? = rpta?.result?.conversion
                    if (listConversion != null) {
                        grid?.setPadding(5,5,5,5)
                        var textViewTitleSiglas = TextView(context)
                        var textViewTitleCurrencie = TextView(context)
                        var textViewTitleValue = TextView(context)
                        textViewTitleSiglas.text = "Siglas"
                        textViewTitleCurrencie.text = "Nombre de la moneda"
                        textViewTitleValue.text = "Valor Dolar"
                        textViewTitleSiglas.setTextSize(15f)
                        textViewTitleCurrencie.setTextSize(15f)
                        textViewTitleValue.setTextSize(15f)
                        grid?.addView(textViewTitleSiglas)
                        grid?.addView(textViewTitleCurrencie)
                        grid?.addView(textViewTitleValue)
                        for (item in listConversion) {
                            var textviewTo  = TextView(context)
                            var textviewRate  = TextView(context)
                            var textviewNameCurrencies  = TextView(context)
                            textviewNameCurrencies.text = nameCoins
                                .selectName(listConversion[listConversion
                                .indexOf(item)].to)
                            textviewTo.text = listConversion[listConversion.indexOf(item)].to
                            textviewRate.text = listConversion[listConversion.indexOf(item)].rate.toString()
                            grid?.addView(textviewTo)
                            grid?.addView(textviewNameCurrencies)
                            grid?.addView(textviewRate)
                        }
                    }
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






