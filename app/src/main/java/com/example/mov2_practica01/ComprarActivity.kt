package com.example.mov2_practica01

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class ComprarActivity : AppCompatActivity() {

    private lateinit var objCompra: Compra

    private lateinit var nombre: EditText
    private lateinit var domicilio: EditText
    private lateinit var telefono: EditText
    private lateinit var productos: Spinner
    private lateinit var precio: EditText

    private lateinit var nacional: RadioButton
    private lateinit var internacional: RadioButton

    private lateinit var agregar: Button

    companion object {
        val compras = Array<Compra?>(10) { null }
    }
    private val CHANNEL_ID = "Canal_notificacion"
    private val titleAPP = "SHELL SHOCK"
    private val notificationId = 1
    private val mensaje = "Compra exitosa"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_comprar)

        setSupportActionBar(findViewById(R.id.barraCompras))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        objCompra = Compra()

        nombre = findViewById(R.id.edtNombre)
        domicilio = findViewById(R.id.edtDomicilio)
        telefono = findViewById(R.id.edtTelefono)
        productos = findViewById(R.id.spnProducto)
        precio = findViewById(R.id.edtPrecio)

        nacional = findViewById(R.id.radioNacional)
        internacional = findViewById(R.id.radioInternacional)

        agregar = findViewById(R.id.btnAgregar)

        createNotificationChannel()

        var productosArray = arrayOf("Camisa NIKE","Bicicleta","Iphone 15","Sudadera PUMA","Lentes de sol","Mochila")
        productos.adapter = ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,productosArray)
        val preciosMap = mapOf(
            "Camisa NIKE" to 29.99,
            "Bicicleta" to 199.99,
            "Iphone 15" to 999.99,
            "Sudadera PUMA" to 49.99,
            "Lentes de sol" to 19.99,
            "Mochila" to 39.99
        )
        productos.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val selectedProduct = productosArray[position]
                val productPrice = preciosMap[selectedProduct]
                precio.setText(productPrice.toString())
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // No hacer nada si no se selecciona ningún ítem
            }
        }
        agregar.setOnClickListener {
            val nombreComprador = nombre.text.toString()
            val domicilioComprador = domicilio.text.toString()
            val telefonoComprador = telefono.text.toString().toIntOrNull()
            val productoComprado = productos.selectedItem.toString()
            var procedenciaProducto = "No especificado"
            val precioTotal = precio.text.toString().toDoubleOrNull()

            if(nacional.isChecked){
                procedenciaProducto = "Nacional"
            }
            if(internacional.isChecked){
                procedenciaProducto = "Internacional"
            }


            if(nombreComprador.isNotEmpty() && telefonoComprador != null
                && domicilioComprador.isNotEmpty() && precioTotal != null){
                var posicion = compras.indexOfFirst { it == null }

                if(posicion != -1){
                    compras[posicion] = Compra(nombreComprador,domicilioComprador,telefonoComprador,productoComprado,precioTotal,procedenciaProducto)
                    notificacionCompra()
                    nombre.setText("")
                    telefono.setText("")
                    domicilio.setText("")
                    nacional.isChecked = false
                    internacional.isChecked = false

                }
                else{
                    Toast.makeText(this, "No hay espacio disponible", Toast.LENGTH_SHORT).show()
                }
            }
            else{
                Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

    }
    private fun createNotificationChannel(){
        val name = "My Channel"
        val descriptionText = "Descripcion del canal"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }
        val notificationManager : NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
    @SuppressLint("MissingPermission")
    private fun notificacionCompra(){
        val intent = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        val builder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_catching_pokemon_24)
            .setContentTitle(titleAPP)
            .setContentText(mensaje)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("Puedes consultar tus compras"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
        with(NotificationManagerCompat.from(this)) {
            notify(notificationId, builder.build())
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