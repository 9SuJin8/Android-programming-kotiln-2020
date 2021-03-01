package com.example.firebasestorage

import android.content.Intent
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.example.firebasestorage.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage

class MainActivity : AppCompatActivity() {
    lateinit var storage: FirebaseStorage
    lateinit var auth: FirebaseAuth

    companion object {
        const val REQUEST_CODE = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        if (auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java)
            )
            finish()
        }

        val remoteCongig = Firebase.remoteConfig
        val configsSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 1
        }
        remoteCongig.setConfigSettingsAsync(configsSettings)
        remoteCongig.setDefaultsAsync(R.xml.remote_config_defaults)

        storage = Firebase.storage
        var imageRef1 = storage.getReferenceFromUrl("gs://fir-storage-5aef4.appspot.com/spring.jpg")
        displayImageRef(imageRef1, binding.imageView)

        binding.refresh.setOnClickListener {
            remoteCongig.fetchAndActivate()
                .addOnCompleteListener(this) {
                    val season = remoteCongig.getString("season")
                    imageRef1 = storage.getReferenceFromUrl("gs://fir-storage-5aef4.appspot.com/${season}.jpg")
                    displayImageRef(imageRef1, binding.imageView)
                }
        }
    }

    private fun displayImageRef(imageRef: StorageReference?, view: ImageView){
        imageRef?.getBytes(Long.MAX_VALUE)?.addOnSuccessListener {
            val bmp = BitmapFactory.decodeByteArray(it,0,it.size)
            view.setImageBitmap(bmp)
        }?.addOnFailureListener {
            // Failed to download the image
        }
    }
}