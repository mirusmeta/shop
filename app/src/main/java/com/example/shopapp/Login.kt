package com.example.shopapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        val phonetext:TextView = findViewById(R.id.phonetext)
        val passwordtext:TextView = findViewById(R.id.passwordtext)
        val reg:TextView = findViewById(R.id.reg)
        val buttonGo:ConstraintLayout = findViewById(R.id.buttonGo)

        reg.setOnClickListener {
            startActivity(Intent(this@Login, register::class.java))
        }

        buttonGo.setOnClickListener {
            buttonGo.isEnabled = false
            if(phonetext.text.isNotEmpty() && passwordtext.text.isNotEmpty()){
                val db = Firebase.firestore
                db.collection("users").get().addOnSuccessListener {
                    var bul = false
                    for(doc in it){
                        var eh = EncryptionHelper("G8BnK9Qfuntk9vl6")
                        val phone = eh.decrypt(doc.getString("phone").toString())
                        val password = eh.decrypt(doc.getString("password").toString())
                        if(phonetext.text == phone && passwordtext.text == password){
                            bul = true
                            var sharedPreferences = getSharedPreferences("IN", MODE_PRIVATE).edit()
                            sharedPreferences.putString("vhod", "1")
                            sharedPreferences.putString("name", doc.getString("name").toString())
                            sharedPreferences.putString("surname", doc.getString("surname").toString())
                            sharedPreferences.putString("thirdname", doc.getString("thirdname").toString())
                            sharedPreferences.putString("ID", doc.id)
                            sharedPreferences.putString("phone", phone)
                            sharedPreferences.putString("password", password)
                            sharedPreferences.putString("zakazi", password).apply()
                            startActivity(Intent(this@Login, alldone::class.java))
                        }
                    }
                    if(!bul){
                        buttonGo.isEnabled = true
                        Toast.makeText(this@Login, "Пользователь не найден", Toast.LENGTH_LONG).show()
                        startActivity(Intent(this@Login, reg::class.java))
                    }

                }.addOnFailureListener {
                    buttonGo.isEnabled = true
                    Toast.makeText(this@Login, "Ошибка с бд!", Toast.LENGTH_LONG).show()
                }
            }
            else{
                buttonGo.isEnabled = true
                Toast.makeText(this@Login, "Проверьте все поля!", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onBackPressed() {}
}