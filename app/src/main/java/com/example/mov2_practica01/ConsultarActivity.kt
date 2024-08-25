package com.example.mov2_practica01

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.ListView

class ConsultarActivity : AppCompatActivity() {

    private lateinit var listView: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_consultar)

        setSupportActionBar(findViewById(R.id.barraConsulta))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        listView = findViewById(R.id.listViewCompras)

        // Recupera los datos pasados desde ComprarActivity
        val comprasList = intent.getStringArrayListExtra("comprasList")

        // Si no es nulo, setea el adaptador para mostrar los datos
        comprasList?.let {
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, it)
            listView.adapter = adapter
        }?: run {
            // Manejo de caso en que no hay datos
            val adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, listOf("No hay compras registradas"))
            listView.adapter = adapter
        }

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