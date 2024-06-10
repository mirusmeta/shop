package com.example.shopapp

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class CardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card)
        val ID = intent.getStringExtra("id").toString()

        val imagev:ImageView = findViewById(R.id.imagev)
        val price:TextView = findViewById(R.id.price)
        val nameofitem:TextView = findViewById(R.id.nameofitem)
        val nal:TextView = findViewById(R.id.nal)
        val desc:TextView = findViewById(R.id.desc)
        val buyButton:ConstraintLayout = findViewById(R.id.buyButton)
        buyButton.isEnabled = false
        val db = Firebase.firestore

        db.collection("items").document(ID).get().addOnSuccessListener {
            buyButton.isEnabled = true
            price.text = it.getString("price").toString()
            nameofitem.text = it.getString("name").toString()
            nal.text = it.getString("nal").toString()
            desc.text = it.getString("desc").toString()
            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference.child("images/${it.get("image").toString()}")
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                Picasso.get().load(uri).into(imagev)
            }.addOnFailureListener { exception ->
                Log.e("FirestoreImageLoadError", "Failed to load image: ${exception.message}")
            }

        }.addOnFailureListener {
            Toast.makeText(this, "проблема с бд!", Toast.LENGTH_LONG).show()
        }
        buyButton.setOnClickListener {
            val db = Firebase.firestore
            val sharedPreferences = getSharedPreferences("IN", MODE_PRIVATE)

            val order = hashMapOf(
                "iduser" to sharedPreferences.getString("ID", "n"),
                "iditem" to ID,
                "state" to "Создано",
                "adress" to sharedPreferences.getString("adress", "n"),
                "price" to price.text.toString()
            )
            db.collection("orders").add(order).addOnSuccessListener {
                changeinUser(it.id)
            }.addOnFailureListener {
                Toast.makeText(this, "Не заказано!", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun changeinUser(str: String) {
        val db = Firebase.firestore
        val db2 = Firebase.firestore
        val sharedPreferences = getSharedPreferences("IN", MODE_PRIVATE)
        db.collection("users").document(sharedPreferences.getString("ID", "n").toString()).get().addOnSuccessListener {
            val z2 = it.getString("zakazi").toString()
            val user = hashMapOf(
                "zakazi" to "$z2$str|"
            )
            db2.collection("users").document(sharedPreferences.getString("ID", "n").toString()).update(
                user as Map<String, Any>
            ).addOnSuccessListener {
                finish()
            }
        }
    }
}