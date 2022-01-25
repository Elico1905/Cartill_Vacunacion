package com.gonzalo.cartilladevacunacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*

class ActivityHome : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val bundle = intent.extras
        var NAME = bundle?.getString("name").toString()
        if (NAME == ""){
            Toast.makeText(this, "algo salio mal", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ActivityLogin::class.java))
        }
        home_textview.text = "Hola, ${NAME}"

    }


}