package com.dldmswo1209.carrotmarket

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.dldmswo1209.carrotmarket.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class LoginActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    var mBinding : ActivityLoginBinding? = null
    val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()


        binding.register.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
        binding.btnLogin.setOnClickListener {
            val email = binding.inputId.text.toString()
            val pw = binding.inputPw.text.toString()
            auth.signInWithEmailAndPassword(email,pw)
                .addOnFailureListener {
                    Toast.makeText(this, "로그인 실패", Toast.LENGTH_SHORT).show()
                }
                .addOnSuccessListener {
                    Toast.makeText(this, "로그인 성공", Toast.LENGTH_SHORT).show()
                    var nickname = ""
                    db.collection("users")
                        .document(auth.currentUser!!.uid)
                        .get()
                        .addOnSuccessListener {
                            nickname = it["nickname"].toString()
                        }
                    val sharedPreferences = getSharedPreferences("user_info", Context.MODE_PRIVATE)
                    val editor = sharedPreferences.edit()
                    editor.putString("user_uid", auth.currentUser!!.uid)
                    editor.putString("user_name", nickname)
                    editor.commit()
                    startActivity(Intent(this, MainActivity::class.java))
                }
        }

    }
}