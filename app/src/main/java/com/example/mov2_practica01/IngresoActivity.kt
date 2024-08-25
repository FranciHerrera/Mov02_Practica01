package com.example.mov2_practica01

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class IngresoActivity : AppCompatActivity() {

    private lateinit var usuario : EditText
    private lateinit var contrasena : EditText

    private lateinit var acceder : Button
    private lateinit var salir : Button

    private val CHANNEL_ID = "Canal_notificacion"
    private val titleAPP = "SHELL SHOCK"
    private val notificationId = 1

    private var cuentas = listOf<Cuenta>(
        Cuenta("Francisco","1234"),
        Cuenta("Angel","123"),
        Cuenta("Lalo","abc"),
        Cuenta("Alex","1234")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ingreso)

        usuario = findViewById(R.id.edtUsuario)
        contrasena = findViewById(R.id.edtContrasena)

        acceder = findViewById(R.id.btnAcceder)
        salir = findViewById(R.id.btnSalir)

        createNotificationChannel()

        acceder.setOnClickListener {
            val user = usuario.text.toString()
            val passwd = contrasena.text.toString()

            if (user.isBlank() || passwd.isBlank()){
                Toast.makeText(this,"Completa todos los campos",Toast.LENGTH_SHORT).show()
            }
            else{
                var cuenta = cuentas.find { it.usuario == user && it.contrasena == passwd }
                if(cuenta != null){
                    notificacionLogin()
                    val intent = Intent(this,MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    Toast.makeText(this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show()
                }
            }
        }
        salir.setOnClickListener {
            finish()
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
    private fun notificacionLogin(){
        var username = usuario.text.toString()
        val builder = NotificationCompat.Builder(this,CHANNEL_ID)
            .setSmallIcon(R.drawable.baseline_catching_pokemon_24)
            .setContentTitle(titleAPP)
            .setStyle(
                NotificationCompat.BigTextStyle()
                    .bigText("!!! Bienvenido $username !!!"))
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(this)){
            notify(notificationId,builder.build())
        }
    }

}