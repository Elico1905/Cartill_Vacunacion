package com.gonzalo.cartilladevacunacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {

    private val bd = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        login_register.setOnClickListener {
            startActivity(Intent(this, ActivityRegister::class.java))
        }
        login_recover.setOnClickListener {
            startActivity(Intent(this, ActivityRecover::class.java).putExtra("email",login_user.text.toString()))
        }
        login_button_singIn.setOnClickListener {
            if (!login_user.text.toString().isEmpty() && !login_password.text.toString().isEmpty()){
                ValidateEmailAndPAsss(login_user.text.toString(),login_password.text.toString())
            }else{
                Toast.makeText(this, "llena todos los campos", Toast.LENGTH_SHORT).show()
            }
        }

    }


    private fun ValidateEmailAndPAsss(email:String,pass:String){
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass)
            .addOnCompleteListener {
                if (it.isSuccessful) {

                    bd.collection("users").document(email).get().addOnSuccessListener {
                        if (!it.get("email").toString().equals("null")){
                            startActivity(Intent(this, ActivityHome::class.java)
                                .putExtra("email",email)
                                .putExtra("name",it.get("name").toString()))
                        }else{
                            Toast.makeText(this, "algo salio mal", Toast.LENGTH_SHORT).show()
                        }
                    }

                } else {
                    Toast.makeText(this, "correo o contraseña incorrecto", Toast.LENGTH_SHORT).show()
                }
            }
    }


}