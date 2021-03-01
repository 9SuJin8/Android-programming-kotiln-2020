package com.example.firebaseauth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.firebaseauth.databinding.ActivityLoginBinding
import com.example.firebaseauth.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        if (auth.currentUser == null) {
            startActivity(
                Intent(this, LoginActivity::class.java))
            finish()
        }

        binding.textView.text = auth.currentUser?.uid ?: "No User"

        binding.buttonSignout.setOnClickListener {
            startActivity(
                Intent(this, LoginActivity::class.java))
            auth.signOut()
            finish()
        }
    }

}
        //signInWithEmailAndPassword 아래에 넣으면 안된다 (비동기적 실행이기 때문에 당연히 null값이 출력
        //이걸로 아이디 생성
       // auth.createUserWithEmailAndPassword()
        //if(auth.currentUser == null) // 현재 유저 로그인 되었는지 알아보기 위함
        //else auth.currentUser?.uid //uid 확인