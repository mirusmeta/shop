package com.example.shopapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.tasks.Tasks
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.firestore
import kotlinx.coroutines.tasks.await

class orders : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_orders)
        val profile2:ImageView = findViewById(R.id.profile)
        val main:ImageView = findViewById(R.id.cart)

        profile2.setOnClickListener {
            startActivity(Intent(this@orders, profile::class.java))
        }
        main.setOnClickListener {
            startActivity(Intent(this@orders, MainActivity::class.java))
        }
        firebaseUpdate()
    }

    private fun firebaseUpdate() {
        val sharedPreferences = getSharedPreferences("IN", MODE_PRIVATE)
        val db = Firebase.firestore
        val userId = sharedPreferences.getString("ID", "n").toString()

        db.collection("users").document(userId).get().addOnSuccessListener { documentSnapshot ->
            val zakazi = documentSnapshot.getString("zakazi")
            if (!zakazi.isNullOrEmpty()) {
                val sz = zakazi.split("|")
                updateWithString(sz)
            }
        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

    private fun updateWithString(ls: List<String>) {
        val db = Firebase.firestore
        val recycleview: RecyclerView = findViewById(R.id.recycleview)
        val itemList = mutableListOf<CustomModel2>()

        val tasks = ls.filter { it.length >= 10 }.map { orderId ->
            db.collection("orders").document(orderId).get()
        }

        Tasks.whenAllSuccess<DocumentSnapshot>(tasks).addOnSuccessListener { documents ->
            for (document in documents) {
                val item = CustomModel2(
                    document.id,
                    document.getString("name").toString(),
                    document.getString("price").toString(),
                    document.getString("state").toString()
                )
                itemList.add(item)
            }

            itemList.sortBy { it.id }
            val itemAdapter = CustomAdapter2(this@orders, itemList)
            recycleview.layoutManager = LinearLayoutManager(this)
            recycleview.adapter = itemAdapter
        }.addOnFailureListener { exception ->
            Toast.makeText(this, exception.message.toString(), Toast.LENGTH_LONG).show()
        }
    }

}