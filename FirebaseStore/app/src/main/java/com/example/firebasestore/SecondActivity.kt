package com.example.firebasestore

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasestore.databinding.ActivityMainBinding
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(R.layout.activity_second)
        val name = findViewById<TextView>(R.id.name)
        val price = findViewById<TextView>(R.id.price)
        val btn = findViewById<Button>(R.id.AddToCart)

        name.text = intent.getStringExtra("name")
        price.text = intent.getIntExtra("price",0).toString()
        val incart = intent.getBooleanExtra("cart",false)


        if(incart){
            btn.setText("DeleteToCart")
        }
        else {
            btn.setText("AddToCart")
        }

        btn.setOnClickListener {
            if(btn.text == "AddToCart") {
                btn.setText("DeleteToCart")
                val intent2 = Intent(this, MainActivity::class.java)
                intent2.putExtra("cart",true)
                intent2.putExtra("name", name.text)
                startActivity(intent2)
            }
            else {
                btn.setText("AddtoCart")
                val intent2 = Intent(this, MainActivity::class.java)
                intent2.putExtra("cart",false)
                intent2.putExtra("name", name.text)
                startActivity(intent2)
            }

        }
    }
}

