package com.example.mov2_practica01

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MainActivity : AppCompatActivity() {

    private lateinit var comprar: ImageButton
    private lateinit var consultar: ImageButton
    private lateinit var capturar: ImageButton
    private lateinit var alarma: ImageButton

    private val CHANNEL_ID = "Canal_notificacion"
    private val titleAPP = "SHELL SHOCK"
    private val notificationId = 1
    private val title = "Descuento"

    private lateinit var intent: Intent
    private lateinit var pendingIntent: PendingIntent
    private lateinit var alarMqr: AlarmManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Barra para regresar
        setSupportActionBar(findViewById(R.id.barraMenu))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        comprar = findViewById(R.id.btnComprar)
        consultar = findViewById(R.id.btnConsultar)
        capturar = findViewById(R.id.btnCapturar)
        alarma = findViewById(R.id.btnAlarma)

        createNotificationChannel()

        //Abrir alarma activity
        intent = Intent(this,AlarmaActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        pendingIntent = PendingIntent.getActivity(this,0, intent,PendingIntent.FLAG_IMMUTABLE)

        alarMqr = (applicationContext.getSystemService(Context.ALARM_SERVICE) as? AlarmManager)!!

        //FUNCIONAMIENTO DE BOTONES
        comprar.setOnClickListener {
            val intent = Intent(this,ComprarActivity::class.java)
            startActivity(intent)
        }

        consultar.setOnClickListener {
            val comprasList = ComprarActivity.compras.filterNotNull().map { compra ->
                "Nombre: ${compra.nombre}, Domicilio: ${compra.domicilio}, Tel: ${compra.telefono}, Producto: ${compra.producto}, Precio: ${compra.precio}, Origen: ${compra.origen}"
            }
            intent = Intent(applicationContext, ConsultarActivity::class.java)
            intent.putStringArrayListExtra("comprasList", ArrayList(comprasList))
            startActivity(intent)
        }

        capturar.setOnClickListener {
            descargarDescuento()
        }

        alarma.setOnClickListener {
            lanzarAlarma15seg()
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
    private fun descargarDescuento(){
        val builder = NotificationCompat.Builder(this, CHANNEL_ID).apply {
            setContentTitle(title)
            setContentText("CAPTURANDO DESCUENTO")
            setSmallIcon(R.drawable.baseline_catching_pokemon_24)
            setPriority(NotificationCompat.PRIORITY_LOW)
        }
        val PROGRESS_MAX = 100
        val PROGRESS_CURRENT = 0
        NotificationManagerCompat.from(this).apply {
            builder.setProgress(PROGRESS_MAX, PROGRESS_CURRENT, false)
            notify(notificationId, builder.build())

            builder.setContentText("CAPTURANDO DESCUENTO")
                .setProgress(PROGRESS_MAX, PROGRESS_CURRENT, true)
                .setTimeoutAfter(5000)
            notify(notificationId,builder.build())
        }
        val handler = android.os.Handler(mainLooper)
        handler.postDelayed({
            // Actualiza la notificación al completar la captura
            builder.setContentText("DESCUENTO CAPTURADO")
                .setProgress(0, 0, false)
            // Abre la CapturarActivity
            val intent = Intent(this, CapturarActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
        }, 5000)
    }
    private fun lanzarAlarma15seg(){
        alarMqr.set(
            AlarmManager.ELAPSED_REALTIME_WAKEUP,
            SystemClock.elapsedRealtime() + 15 * 1000,
            pendingIntent
        )
        Toast.makeText(applicationContext,"Recordatorio lanzado, 15 segundos restantes.", Toast.LENGTH_SHORT).show()
    }
    //Regresar al login
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            val intent = Intent(this, IngresoActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            startActivity(intent)
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}