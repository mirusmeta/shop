package com.example.shopapp


import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class CustomAdapter(private val context: Context, private val dataList: List<CustomModel>) : RecyclerView.Adapter<CustomAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.list_item_layout, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = dataList[position]
        holder.textViewName.text = currentItem.name
        holder.textViewDescription.text = currentItem.description
        holder.iddoc.text = currentItem.id
        holder.price.text = currentItem.price
        /*holder.itemView.setOnClickListener {
            val intent = Intent(context, RaitingAdmin::class.java)
            intent.putExtra("id", currentItem.iddoc)
            context.startActivity(intent)
        }*/
        val storage = FirebaseStorage.getInstance()
        val storageRef = storage.reference.child(currentItem.image)

        storageRef.downloadUrl.addOnSuccessListener { uri ->
            Picasso.get().load(uri).into(holder.imagev)
        }.addOnFailureListener { exception ->
            Log.e("FirestoreImageLoadError", "Failed to load image: ${exception.message}")
        }
    }


    override fun getItemCount(): Int = dataList.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.name)
        val textViewDescription: TextView = itemView.findViewById(R.id.desk)
        val iddoc: TextView = itemView.findViewById(R.id.pid)
        val imagev: ImageView = itemView.findViewById(R.id.image)
        val price: TextView = itemView.findViewById(R.id.price)
    }
}
