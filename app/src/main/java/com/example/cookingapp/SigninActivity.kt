package com.example.cookingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_signin.*

class SigninActivity : AppCompatActivity() {

    var mAuth:FirebaseAuth = FirebaseAuth.getInstance()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
    }

    override fun onStart() {
        super.onStart()
        tv_signin_login.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_signin.setOnClickListener {
            val email:String = et_signin_email.text.toString().trim()
            val password:String = et_signin_password.text.toString()
            
            if (email.isEmpty() || password.isEmpty()){
                et_signin_email.setError(resources.getString(R.string.signin_compile_error))
                et_signin_password.setError(resources.getString(R.string.signin_compile_error))
                return@setOnClickListener
            }
            if(password.length < 8){
                et_signin_password.setError(resources.getString(R.string.signin_password_length_error))
                return@setOnClickListener
            }

            progress_signin.visibility = View.VISIBLE


            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "${resources.getText(R.string.welcome_message)}${resources.getText(R.string.app_name)}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this, "${resources.getText(R.string.signin_error_message)}", Toast.LENGTH_SHORT).show()

                }
            }
        }
    }
}