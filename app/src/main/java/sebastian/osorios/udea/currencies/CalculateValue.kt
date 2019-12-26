package sebastian.osorios.udea.currencies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_calculate_value.*

class CalculateValue : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculate_value)

        backCalculate.setOnClickListener{
            val intento = Intent(this, MainActivity ::class.java)
            startActivity(intento)
        }
    }
}
