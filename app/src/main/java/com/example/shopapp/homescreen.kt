package com.example.shopapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class homescreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homescreen)
        var sharedPreferences = getSharedPreferences("IN", MODE_PRIVATE)
        if(sharedPreferences.getString("vhod", "0") == "0"){
            startActivity(Intent(this@homescreen, Login::class.java ))
        }
        else{
            //Проверка на валидность
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
}