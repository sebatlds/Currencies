package sebastian.osorios.udea.currencies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

       buttonSeeValue.setOnClickListener{
           val intento1 = Intent(this, CurrenciesValue ::class.java)
           startActivity(intento1)
       }

        buttonCalculValue.setOnClickListener {
            val intento2 = Intent(this, CalculateValue ::class.java)
            startActivity(intento2)
        }
    }
}
