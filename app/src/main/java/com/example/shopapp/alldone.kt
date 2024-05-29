package com.example.shopapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class alldone : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alldone)
        val buttonGo:ConstraintLayout = findViewById(R.id.buttonGo)
        buttonGo.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }
    override fun onBackPressed() {}
}