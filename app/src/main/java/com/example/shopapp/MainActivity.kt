package com.example.shopapp

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val sharedPreferences = getSharedPreferences("IN", MODE_PRIVATE)
        val welcometext:TextView = findViewById(R.id.welcometext)
        val profilee:ImageView = findViewById(R.id.profile)
        val cart:ImageView = findViewById(R.id.cart)

        welcometext.text = "Welcome, ${sharedPreferences.getString("name", "null")}"
        profilee.setOnClickListener {
            startActivity(Intent(this, profile::class.java))
        }
        cart.setOnClickListener {
            startActivity(Intent(this@MainActivity, orders::class.java))
        }
        firebaseUpdate()

    }

    private fun firebaseUpdate() {
        val recycleview:RecyclerView = findViewById(R.id.recycleview)
        val db = Firebase.firestore
        db.collection("items").get().addOnSuccessListener {
            val itemList = mutableListOf<CustomModel>()
            for(res in it){
                val item = CustomModel(
                    res.id.toString(),
                    res.getString("name").toString(),
                    res.getString("nal").toString(),
                    "images/${res.get("image").toString()}",
                    res.getString("price").toString()
                )
                itemList.add(item)
            }
            itemList.sortBy { it.id }
            val itemAdapter = CustomAdapter(this@MainActivity, itemList)
            recycleview.layoutManager = LinearLayoutManager(this)
            recycleview.adapter = itemAdapter
        }.addOnFailureListener {
            Toast.makeText(this@MainActivity, "Не получилось", Toast.LENGTH_LONG).show()
        }

    }
}