package com.example.mov2_practica01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import kotlin.random.Random

class CapturarActivity : AppCompatActivity() {

    private lateinit var descuento: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_capturar)

        setSupportActionBar(findViewById(R.id.barraCapturar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        descuento = findViewById(R.id.txtDescuento)

        var porcentajeDescuento = Random.nextInt(5,80)

        descuento.text = "TU DESCUENTO ES DEL $porcentajeDescuento%"

    }
    //Regresar al menu
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val intent = Intent(this, MainActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}