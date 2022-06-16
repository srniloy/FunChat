package com.shahriarniloy.funchat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Login: AppCompatActivity() {

    private lateinit var email_input : EditText
    private lateinit var password_input : EditText
    private lateinit var signUpBtn : Button
    private lateinit var logInBtn : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_layout)
        mAuth = FirebaseAuth.getInstance()

        email_input = findViewById(R.id.emailInput)
        password_input = findViewById(R.id.passwordInput)
        signUpBtn = findViewById(R.id.signupBtnL)
        logInBtn = findViewById(R.id.loginBtn)

        signUpBtn.setOnClickListener{
            var intent = Intent(this@Login, SignUp::class.java)
            startActivity(intent)
        }
        logInBtn.setOnClickListener{
            var email = email_input.text.toString()
            var password = password_input.text.toString()

            var db = FirebaseDatabase.getInstance().getReference("Users")
            db.setValue(User("shahrir","shahriar@gmail.com","uid23fasdf"))
                .isSuccessful.let {
                    Toast.makeText(this,"Added", Toast.LENGTH_SHORT).show()
                }
//            login(email, password)
        }
    }
    private fun login(email: String, password: String){
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    var intent = Intent(this@Login, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@Login,"User doesn't exist", Toast.LENGTH_SHORT).show()
                }
            }
    }
}