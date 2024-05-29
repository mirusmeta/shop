package com.example.shopapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class register : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        val fio: TextView = findViewById(R.id.fio)
        val phonetext: TextView = findViewById(R.id.phonetext)
        val passwordtext: TextView = findViewById(R.id.passwordtext)
        val buttonGo: ConstraintLayout = findViewById(R.id.buttonGo)

        buttonGo.setOnClickListener {
            buttonGo.isEnabled = false
            if(phonetext.text.length >= 6 && fio.text.length >= 6 && passwordtext.text.length >= 6){
                var eh = EncryptionHelper("G8BnK9Qfuntk9vl6")
                val db = Firebase.firestore
                var textfio = fio.text.split(" ")
                val user = hashMapOf(
                    "name" to textfio[1],
                    "password" to eh.encrypt(phonetext.text.toString()),
                    "phone" to eh.encrypt(passwordtext.text.toString()),
                    "surname" to textfio[0],
                    "thirdname" to textfio[2],
                    "zakazi" to ""
                )
                db.collection("users")
                    .add(user)
                    .addOnSuccessListener { documentReference ->
                        var sharedPreferences = getSharedPreferences("IN", MODE_PRIVATE).edit()
                        sharedPreferences.putString("name", user["name"].toString())
                        sharedPreferences.putString("vhod", "1")
                        sharedPreferences.putString("surname", user["surname"].toString())
                        sharedPreferences.putString("thirdname", user["thirdname"].toString())
                        sharedPreferences.putString("ID", documentReference.id)
                        sharedPreferences.putString("phone", phonetext.text.toString())
                        sharedPreferences.putString("password", passwordtext.text.toString())
                        sharedPreferences.putString("zakazi", "").apply()
                        startActivity(Intent(this@register, alldone::class.java))
                    }
            }
            else{
                buttonGo.isEnabled = true
                Toast.makeText(this@register, "Проверьте поля!", Toast.LENGTH_LONG).show()
            }
        }
    }
}