package com.example.shopapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val sharedPreferences = getSharedPreferences("IN", MODE_PRIVATE)
        val editsharedPreferences = getSharedPreferences("IN", MODE_PRIVATE).edit()
        val mainscreen: ImageView = findViewById(R.id.mainscreen)
        val logout:TextView = findViewById(R.id.logout)
        val adressofuser:TextView = findViewById(R.id.adressofuser)
        val adresschange:TextView = findViewById(R.id.adresschange)
        val cart:ImageView = findViewById(R.id.cart)

        cart.setOnClickListener {
            startActivity(Intent(this, orders::class.java))
        }
        mainscreen.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        val infuser:TextView = findViewById(R.id.infuser)
        infuser.text = "Имя: ${sharedPreferences.getString("name", "null")}\n" +
                    "Фамилия: ${sharedPreferences.getString("surname", "null")}\n" +
                "Отчество: ${sharedPreferences.getString("thirdname", "null")}"

        adressofuser.text = sharedPreferences.getString("adress", "Назначьте адресс")
        logout.setOnClickListener {
            editsharedPreferences.clear().apply()
            startActivity(Intent(this, homescreen::class.java))
        }
        adresschange.setOnClickListener {
            val editTextText:TextView = findViewById(R.id.editTextText)
            if(editTextText.visibility == View.VISIBLE){
                editsharedPreferences.putString("adress", editTextText.text.toString()).apply()
                finish()
            }
            else{
                editTextText.visibility = View.VISIBLE
            }
        }

    }
}