package com.oktydeniz.instagramklon.views

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.oktydeniz.instagramklon.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private lateinit var binding: ActivityAuthBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        actions()
    }

    private fun actions() {
        binding.singInButton.setOnClickListener {
            singIn()
        }
        binding.singUpButton.setOnClickListener {
            singUp()
        }
    }

    private fun singIn() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    private fun singUp() {
        val email = binding.emailEditText.text.toString()
        val password = binding.passwordEditText.text.toString()
        auth.createUserWithEmailAndPassword(email, password).addOnSuccessListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
            startActivity(intent)
        }.addOnFailureListener {
            Toast.makeText(applicationContext, it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }
}