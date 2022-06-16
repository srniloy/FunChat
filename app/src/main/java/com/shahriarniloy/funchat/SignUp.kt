package com.shahriarniloy.funchat

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class SignUp: AppCompatActivity() {

    private lateinit var name_input : EditText
    private lateinit var email_input : EditText
    private lateinit var password_input : EditText
    private lateinit var signUpBtn : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var mDbRef : DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.signup_layout)
        mAuth = FirebaseAuth.getInstance()

        name_input = findViewById(R.id.nameInputS)
        email_input = findViewById(R.id.emailInputS)
        password_input = findViewById(R.id.passwordInputS)
        signUpBtn = findViewById(R.id.signupBtn)

        signUpBtn.setOnClickListener{
            var name = name_input.text.toString()
            var email = email_input.text.toString()
            var password = password_input.text.toString()

            signup(name, email, password)
        }
    }
    private fun signup(name: String, email: String, password: String){
        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserDataToDatabase(name,email,mAuth.currentUser?.uid!!)
                    var intent = Intent(this@SignUp, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SignUp,"Some Error Occurred", Toast.LENGTH_SHORT).show()
                }
            }
    }
    private fun addUserDataToDatabase(name: String, email: String, uid: String){
        mDbRef = FirebaseDatabase.getInstance().getReference("user_data")
        mDbRef.child(uid).setValue(User(name, email, uid))
    }
}