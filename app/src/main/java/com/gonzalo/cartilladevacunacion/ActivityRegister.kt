package com.gonzalo.cartilladevacunacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

class ActivityRegister : AppCompatActivity() {

    private val bd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_cancel.setOnClickListener {

        }
        register_button_register.setOnClickListener {
            ValidateData()
        }
    }

    private fun ValidateData() {
        RegisterUser()
    }

    private fun RegisterUser(){
        bd.collection("users").document(register_email.text.toString()).set(
            hashMapOf(
                "email" to register_email.text.toString(),
                "pass" to register_pass.text.toString(),
                "name" to register_name.text.toString(),
                "last_name_1" to register_last_name_1.text.toString(),
                "last_name_2" to register_last_name_2.text.toString(),
                "years" to register_years.text.toString()))
    }
}