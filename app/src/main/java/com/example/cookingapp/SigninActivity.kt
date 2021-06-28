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
import android.util.Patterns.EMAIL_ADDRESS
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.database.ktx.database
import java.util.HashMap

class SigninActivity : AppCompatActivity() {

    private val mAuth:FirebaseAuth = FirebaseAuth.getInstance()
    private val mDb:DatabaseReference = FirebaseDatabase.getInstance("https://cookingapp-97c73-default-rtdb.europe-west1.firebasedatabase.app").getReference("Users")

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
            if(et_signin_name.text.isEmpty()){
                et_signin_name.error = resources.getString(R.string.signin_compile_error)
                retErr = true;
            }
            if(et_signin_surname.text.isEmpty()){
                et_signin_surname.error = resources.getString(R.string.signin_compile_error)
                retErr = true;
            }
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

                    val toSaveUser: HashMap<String, String> = HashMap()
                    toSaveUser.put("name", et_signin_name.text.toString())
                    toSaveUser.put("surname", et_signin_surname.text.toString())
                    toSaveUser.put("email", email)
                    mDb!!.push().setValue(toSaveUser)

                    Toast.makeText(this, "${et_signin_name.text}, ${resources.getText(R.string.welcome_message)} ${resources.getText(R.string.app_name)}", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
                else{
                    et_signin_email.error = resources.getString(R.string.signin_error_message)
                    progress_signin.visibility = View.GONE
                }
            }
        }
    }
}

