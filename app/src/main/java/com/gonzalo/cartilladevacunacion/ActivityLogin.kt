package com.gonzalo.cartilladevacunacion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import com.bumptech.glide.Glide
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

class ActivityLogin : AppCompatActivity() {

    private val bd = FirebaseFirestore.getInstance()
    private val GOOGLE_SING_IN = 100

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
        login_boton_google.setOnClickListener { GoogleButton()}
    }

    private fun GoogleButton(){
        val googleConf = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        val googleClient = GoogleSignIn.getClient(this, googleConf)
        googleClient.signOut()
        startActivityForResult(googleClient.signInIntent, GOOGLE_SING_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GOOGLE_SING_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)
                if (account != null) {
                    val credential = GoogleAuthProvider.getCredential(account.idToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(credential).addOnCompleteListener {
                        if (it.isSuccessful) {
                            ValidateEmail(account.email.toString())
                        }
                    }

                }
            } catch (e: ApiException) {}

        }
    }

    private fun ValidateEmail(email:String){
        bd.collection("users").document(email).get().addOnSuccessListener {
            if (!it.get("email").toString().equals("null")){
                startActivity(Intent(this, ActivityHome::class.java)
                    .putExtra("email",email)
                    .putExtra("name",it.get("name").toString()))
            }else{
                Toast.makeText(this, "Correo no registrado", Toast.LENGTH_SHORT).show()
            }
        }
    }



    private fun ValidateEmailAndPAsss(email:String,pass:String){
        bd.collection("users").document(email).get().addOnSuccessListener {
            if (!it.get("email").toString().equals("null")){
                if (pass.equals(it.get("pass").toString())){
                    startActivity(Intent(this, ActivityHome::class.java)
                        .putExtra("email",email)
                        .putExtra("name",it.get("name").toString()))
                }else{
                    Toast.makeText(this, "Contraseña incorrecta", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText(this, "Correo no encontrado", Toast.LENGTH_SHORT).show()
            }
        }
    }



}