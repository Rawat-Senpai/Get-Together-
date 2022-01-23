package com.example.gettogether

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_old_user.*
import postPakage.postActivity

class old_user_activity : AppCompatActivity() {

    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_old_user)


        firebaseAuth= FirebaseAuth.getInstance()

        log_in_user.setOnClickListener(){
            logInFunction()

            val intent= Intent(this, postActivity::class.java)
            startActivity(intent)

            back_To_logIn.setOnClickListener(){
                val intent = Intent(this,MainActivity::class.java)
                startActivity(intent)
            }

        }
    }

    private fun logInFunction() {
        val gmail= log_in_email.text.toString()
        val password= log_in_passowrd.text.toString()

        if (gmail == "" || password == ""){
            Toast.makeText(this,"enter data carefully  ", Toast.LENGTH_LONG).show()
            return
        }

        firebaseAuth.signInWithEmailAndPassword(gmail,password).addOnCompleteListener(this){
            if(it.isSuccessful){
                Toast.makeText(this,"Log in successfully ", Toast.LENGTH_LONG).show()
            }
            else
            {
                Toast.makeText(this,"error aa gaya bhai", Toast.LENGTH_LONG).show()
            }
        }

    }
}