package sebastian.osorios.udea.currencies

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.Gravity
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
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
    //todo falta implemetar la barra de busqueda y cuando se le da aceptar en la alerta que se devuelva de activity y borde a los botones y la lista del spinner
    //todo hacer un refresh de la pagina
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_value)
        var from: String
        var dst: String
        var nameCoins: NameCoins = NameCoins()
        var currencies: Array< String> = nameCoins.getNamesCurrencies()
        currencies = orderArray(currencies)
        spinnerFrom = findViewById(R.id.spinnerOrigen)
        spinnerDst = findViewById(R.id.spinnerDestino)

        var arrayAdapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, R.layout.spinner_item_currencies, currencies)
        spinnerFrom.setAdapter(arrayAdapter)
        spinnerDst.setAdapter(arrayAdapter)

        Calculate.setOnClickListener {
            val tableLayout = findViewById<TableLayout>(R.id.linearLayoutResult)
            tableLayout.removeAllViews()
            dst = nameCoins.selectCurrencie(spinnerDst.getSelectedItem().toString())
            from = nameCoins.selectCurrencie(spinnerFrom.getSelectedItem().toString())
            getValueChange(from, dst, this)

        }
    }

    private fun getValueChange(from: String, dst: String, context: Context) {
        val alert = AlertDialog.Builder(context)
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.devises.zone/v1/quotes/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val service = retrofit.create(ApiService::class.java)

        service.getChangeValues(from,dst).enqueue(object : Callback<jsonResponse> {
            override fun onResponse(call: Call<jsonResponse>, response: Response<jsonResponse>) {
                if (response.code() >= 400) {
                    alert.setTitle("Error")
                    alert.setMessage("Se presentó un error con el servicio. \n Codigo de Error = " + response.code())
                    alert.setPositiveButton(
                        "Confirmar", DialogInterface.OnClickListener { dialogo1, id -> null })
                    alert.show()
                }else{
                    var respons = response.body()
                    viewResult(context, respons)
                }
            }
            override fun onFailure(call: Call<jsonResponse>, t: Throwable) {
                alert.setTitle("Error")
                alert.setMessage("Se presentó un error al realizar la consulta, revise su conexión a internet")
                alert.setPositiveButton(
                        "Confirmar", DialogInterface.OnClickListener { dialogo1, id -> backActivity() })
                alert.show()
            }
        })

    }
    private fun backActivity() {
        val intento = Intent(this, MainActivity ::class.java)
        startActivity(intento)
    }

    private fun viewResult(context: Context, respons: jsonResponse? ){
        val nameCoins : NameCoins = NameCoins()
        val tableLayout : TableLayout = findViewById(R.id.linearLayoutResult)
        var textViewTitleSource = TextView(context)
        var textViewTitleTarget = TextView(context)
        var textViewSource = TextView(context)
        var textViewTarget = TextView(context)
        var textViewValue = TextView(context)
        var textViewQuantity = TextView(context)
        val tableRow0 = TableRow(context)
        val tableRow1 = TableRow(context)
        val tableRow2 = TableRow(context)
        tableRow0.setHorizontalGravity(Gravity.CENTER)
        tableRow1.setHorizontalGravity(Gravity.CENTER)
        tableRow2.setHorizontalGravity(Gravity.CENTER)
        textViewTitleSource.gravity
        textViewTitleSource.text = "A"
        textViewTitleTarget.text = "De"
        textViewQuantity.setTextSize(20f)
        textViewValue.setTextSize(20f)
        textViewTarget.setTextSize(20f)
        textViewSource.setTextSize(20f)
        textViewTitleSource.setTextSize(25f)
        textViewTitleTarget.setTextSize(25f)
        textViewQuantity.setPadding(5,5,5,5)
        textViewValue.setPadding(5,5,5,5)
        textViewTarget.setPadding(5,5,5,5)
        textViewSource.setPadding(5,5,5,5)
        textViewTitleSource.setPadding(8,8,8,8)
        textViewTitleTarget.setPadding(8,8,8,8)
        var result  = respons?.result
        if (result != null) {
            textViewTarget.text = nameCoins.selectName(result.source)
        }
        if (result != null) {
            textViewSource.text = nameCoins.selectName(result.target)
        }
        textViewValue.text = result?.value.toString()
        textViewQuantity.text = result?.quantity.toString().substring(0,1)
        tableLayout.setPadding(10,10,10,10)
        tableRow0.setPadding(10,10,10,10)
        tableRow1.setPadding(10,10,10,10)
        tableRow2.setPadding(10,10,10,10)
        tableRow0.addView(textViewTitleTarget)
        tableRow0.addView(textViewTitleSource)
        tableRow1.addView(textViewTarget)
        tableRow1.addView(textViewSource)
        tableRow2.addView(textViewQuantity)
        tableRow2.addView(textViewValue)
        tableLayout.addView(tableRow0)
        tableLayout.addView(tableRow1)
        tableLayout.addView(tableRow2)
    }

    private fun orderArray(array : Array<String>):Array<String>{
        for (i in 0 until array.size - 1) {
            for (j in i + 1 until array.size) {
                if (array[i] > array[j]) { //Intercambiamos valores
                    val variableauxiliar: String = array[i]
                    array[i] = array[j]
                    array[j] = variableauxiliar
                }
            }
        }
        return array
    }
}
