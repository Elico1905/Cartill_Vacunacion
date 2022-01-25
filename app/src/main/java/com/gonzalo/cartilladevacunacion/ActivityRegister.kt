package com.gonzalo.cartilladevacunacion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*

class ActivityRegister : AppCompatActivity() {

    private val bd = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        register_button_cancel.setOnClickListener {
            finish()
        }
        register_button_register.setOnClickListener {
            ValidateData()
        }
    }

    private fun ValidateData() {
        if (!register_email.text.toString().isNullOrEmpty()){
            bd.collection("users").document(register_email.text.toString()).get().addOnSuccessListener {
                if (it.get("email").toString().equals("null")){
                    RegisterUser()
                }else{
                    Toast.makeText(this, "el correo ya esta ocupado", Toast.LENGTH_SHORT).show()
                }
            }
        }else{
            Toast.makeText(this, "llena todos los campos", Toast.LENGTH_SHORT).show()
        }

    }

    private fun RegisterUser(){
        if (register_pass.text.isNullOrEmpty() || register_name.text.isNullOrEmpty()
            || register_last_name_1.text.isNullOrEmpty() || register_last_name_2.text.isNullOrEmpty()
            || register_years.text.isNullOrEmpty()){
                Toast.makeText(this, "llena todos los campos", Toast.LENGTH_SHORT).show()

        }else{

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                register_email.text.toString(),register_pass.text.toString()).addOnCompleteListener{

                if (it.isSuccessful) {
                    bd.collection("users").document(register_email.text.toString()).set(
                        hashMapOf(
                            "email" to register_email.text.toString(),
                            "name" to register_name.text.toString(),
                            "last_name_1" to register_last_name_1.text.toString(),
                            "last_name_2" to register_last_name_2.text.toString(),
                            "years" to register_years.text.toString()))
                    Toast.makeText(this, "Registrado", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this, "ocurrio un error", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}