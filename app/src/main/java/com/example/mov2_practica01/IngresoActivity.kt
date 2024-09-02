package com.example.mov2_practica01

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessaging
import android.Manifest


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
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            // Permiso concedido
            Toast.makeText(this, "Permiso de notificaciones concedido", Toast.LENGTH_SHORT).show()
        } else {
            // Permiso denegado
            Toast.makeText(this, "Permiso de notificaciones denegado", Toast.LENGTH_SHORT).show()
        }
    }

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
        // Solicitar permiso de notificaciones en tiempo de ejecución
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            when {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED -> {
                    // Permiso ya concedido
                }
                shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS) -> {
                    // Mostrar una explicación al usuario
                    Toast.makeText(this, "Se necesita permiso para mostrar notificaciones", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    // Solicitar el permiso
                    requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                }
            }
        }

        // Obtener el token del dispositivo
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                return@addOnCompleteListener
            }

            // Obtener el token
            val token = task.result
            Log.d("FCM", "Token: $token")
            // Aquí puedes enviar el token a tu servidor si es necesario
        }
    }
}