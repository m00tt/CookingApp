package com.example.cookingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.et_signin_email
import kotlinx.android.synthetic.main.activity_login.et_signin_password
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
            val email:String = et_signin_email.text.toString().trim()
            val password:String = et_signin_password.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                et_signin_email.setError(resources.getString(R.string.signin_compile_error))
                et_signin_password.setError(resources.getString(R.string.signin_compile_error))
                return@setOnClickListener
            }

            progress_login.visibility = View.VISIBLE

            mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener {
                if(it.isSuccessful){
                    Toast.makeText(this, "${resources.getText(R.string.welcome_message)}${resources.getText(R.string.app_name)}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this, "${resources.getText(R.string.login_cred_error)}", Toast.LENGTH_SHORT).show()
                    progress_login.visibility = View.INVISIBLE
                }
            }

        }

    }

    override fun onStart() {
        super.onStart()
        tv_login_signin.setOnClickListener {
            val intent = Intent(this, SigninActivity::class.java)
            startActivity(intent)
        }

    }
}