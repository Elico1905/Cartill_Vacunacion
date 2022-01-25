package com.gonzalo.cartilladevacunacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.activity_recover.*

class ActivityRecover : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recover)

        val bundle = intent.extras
        var EMAIL = bundle?.getString("email").toString()
        recover_email.setText(EMAIL)

        recover_button_cancel.setOnClickListener {
            finish()
        }
        recover_button_recover.setOnClickListener {
         if (!recover_email.text.isNullOrEmpty()){

             val emailAddress = recover_email.text.toString()

             auth.setLanguageCode("es")
             auth.sendPasswordResetEmail(emailAddress).addOnCompleteListener { task ->
                     if (task.isSuccessful) {
                         Toast.makeText(this, "hemos mandado un correo de recuperacion", Toast.LENGTH_LONG).show()
                         finish()
                     }else{
                         Toast.makeText(this, "algo salio mal", Toast.LENGTH_SHORT).show()
                     }
                 }


         }else{
             Toast.makeText(this, "ingresa el correo", Toast.LENGTH_SHORT).show()
         }
        }
    }
}