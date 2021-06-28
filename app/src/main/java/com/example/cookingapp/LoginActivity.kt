package com.example.cookingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_login_email
import kotlinx.android.synthetic.main.activity_login.et_login_password
import kotlinx.android.synthetic.main.activity_signin.*

class LoginActivity : AppCompatActivity() {

    var mAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        if (mAuth.currentUser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        btn_login.setOnClickListener {
            val email:String = et_login_email.text.toString().trim()
            val password:String = et_login_password.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                et_login_email.error = resources.getString(R.string.signin_compile_error)
                et_login_password.error = resources.getString(R.string.signin_compile_error)
                return@setOnClickListener
            }

            progress_login.visibility = View.VISIBLE

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    et_login_email.error = resources.getString(R.string.login_cred_error)
                    et_login_password.error = resources.getString(R.string.login_cred_error)
                    progress_login.visibility = View.GONE
                }
            }

        }

    }

    override fun onStart() {
        super.onStart()
        tv_login_signin.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
            finish()
        }

    }
}