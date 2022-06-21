package com.dldmswo1209.carrotmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import com.dldmswo1209.carrotmarket.databinding.ActivityLoginBinding
import com.dldmswo1209.carrotmarket.databinding.ActivityRegisterBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    lateinit var db: FirebaseFirestore
    var mBinding : ActivityRegisterBinding? = null
    val binding get() = mBinding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        binding.btnRegister.setOnClickListener {
            val email = binding.registerEmail.text.toString()
            val pw = binding.registerPw.text.toString()
            val nickname = binding.registerNickname.text.toString()

            auth.createUserWithEmailAndPassword(email, pw)
                .addOnSuccessListener {
                    val newUser = User(email, pw, nickname, auth.currentUser!!.uid)
                    db.collection("users")
                        .document(auth.currentUser!!.uid)
                        .set(newUser)
                    Toast.makeText(this, "회원가입 성공", Toast.LENGTH_SHORT).show()
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "회원가입 실패", Toast.LENGTH_SHORT).show()
                }
        }
    }
}

class User(
    val email: String,
    val pw: String,
    val nickname: String,
    val uid: String
)