package com.gonzalo.cartilladevacunacion

import android.app.DatePickerDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.DatePicker
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_register.*
import java.text.SimpleDateFormat
import java.util.*

class ActivityRegister : AppCompatActivity() {

    private val bd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val myCalendar = Calendar.getInstance()

        val datePicker = DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
            myCalendar.set(Calendar.YEAR,year)
            myCalendar.set(Calendar.MONTH,month)
            myCalendar.set(Calendar.DAY_OF_MONTH,dayOfMonth)
            val myFormat = "dd/MM/yyyy"
            val sdf = SimpleDateFormat(myFormat,Locale.US)
            register_dateBirth.setText("${sdf.format(myCalendar.time).toString()}")
        }

        register_dateBirth.setOnClickListener {
            DatePickerDialog(this,datePicker,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show()
        }

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
            || register_city.text.isNullOrEmpty() || register_nationality.text.isNullOrEmpty()
            || register_dateBirth.text.isNullOrEmpty()){
                Toast.makeText(this, "llena todos los campos", Toast.LENGTH_SHORT).show()
        }else{

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                register_email.text.toString(),register_pass.text.toString()).addOnCompleteListener{

                if (it.isSuccessful) {
                    bd.collection("users").document(register_email.text.toString()).set(
                        hashMapOf(
                            "email" to register_email.text.toString(),
                            "name" to register_name.text.toString(),
                            "lastName1" to register_last_name_1.text.toString(),
                            "lastName2" to register_last_name_2.text.toString(),
                            "dateBirth" to register_dateBirth.text.toString(),
                            "nationality" to register_nationality.text.toString(),
                            "city" to register_city.text.toString()))
                    Toast.makeText(this, "Registrado", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this, "ocurrio un error", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}