package com.example.cookingapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Patterns
import android.util.Patterns.*
import android.view.View
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_signin.*
import kotlinx.android.synthetic.main.activity_signin.et_signin_email
import kotlinx.android.synthetic.main.activity_signin.et_signin_password
import java.util.regex.Pattern
import android.util.Patterns.EMAIL_ADDRESS

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
            val passwordPattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#&()–[{}]:;',?/*~\$^+=<>]).{8,20}\$")
            val email:String = et_signin_email.text.toString().trim()
            val password:String = et_signin_password.text.toString()

            var retErr = false
            if (!EMAIL_ADDRESS.matcher(email).matches()){
                et_signin_email.error = resources.getString(R.string.signin_invalid_email)
                retErr = true
            }
            if(!passwordPattern.containsMatchIn(password)){
                et_signin_password.error = resources.getString(R.string.signin_password_error)
                retErr = true
            }
            if(retErr){
                return@setOnClickListener
            }


            progress_signin.visibility = View.VISIBLE


            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
                if (it.isSuccessful){
                    Toast.makeText(this, "${resources.getText(R.string.welcome_message)} ${resources.getText(R.string.app_name)}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    Toast.makeText(this, "${resources.getText(R.string.signin_error_message)}", Toast.LENGTH_SHORT).show()
                    progress_signin.visibility = View.GONE
                }
            }
        }
    }
}

