package com.example.firebasestore

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasestore.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item.view.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var db : FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("items")
    private var adapter: RecyclerViewAdapter? = null
    private val TAG : String =  "Update firestore"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        RecyclerView.adapter = RecyclerViewAdapter()
        RecyclerView.layoutManager = LinearLayoutManager(this)
        update()
    }

    private fun update() {
        val cart = intent.getBooleanExtra("cart", false)
        val name = intent.getStringExtra("name").toString()

       // Toast.makeText(this,"${name}", Toast.LENGTH_SHORT).show()
        itemsCollectionRef.document(name).update("cart", cart)
                .addOnSuccessListener { Log.d(TAG, "update successfully") }
    }


    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
        private var items : ArrayList<Item> = arrayListOf()

        init {
            db?.collection("items")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                items.clear()

                for (snapshot in querySnapshot!!.documents) {
                    var item = snapshot.toObject(Item::class.java)
                    items.add(item!!)
                }
                notifyDataSetChanged()
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
            return ViewHolder(view)
        }

        inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewHolder = (holder as ViewHolder).itemView

            viewHolder.name.text = items[position].name.toString()
            viewHolder.cart.text = items[position].cart.toString()
            holder.itemView.setOnClickListener {
                val intent = Intent(this@MainActivity, SecondActivity::class.java)
                intent.putExtra("cart", items[position].cart)
                intent.putExtra("name", items[position].name)
                intent.putExtra("price", items[position].price)
                startActivity(intent)
            }
        }


        override fun getItemCount(): Int {
            return items.size
        }
    }
}


