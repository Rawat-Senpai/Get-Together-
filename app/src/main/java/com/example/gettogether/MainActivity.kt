package com.example.gettogether

import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import chat_program.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import details_person.Person
import details_person.parsonDao
import kotlinx.android.synthetic.main.activity_main.*
import postPakage.postActivity
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var mAuth:FirebaseAuth
    lateinit var postUrl: Uri
    lateinit var mDB_ref:DatabaseReference
    lateinit var parsonDao: parsonDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this,"Uploding photo is mandatory ",Toast.LENGTH_SHORT).show()

        mAuth= Firebase.auth
        parsonDao= parsonDao()

        button_logIn.setOnClickListener(){
            createUser()
        }

        select_photo_liveData.setOnClickListener(){
            selectPoto()
        }

        if(mAuth.currentUser != null)
        {
            Toast.makeText(this,"User Is Already Registered ",Toast.LENGTH_SHORT).show()
            redirectFunction("MAIN")
        }

        log_to_sign.setOnClickListener(){

            val intent= Intent(this,old_user_activity::class.java)
            startActivity(intent)

        }

    }

    private fun selectPoto() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 15)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 15 && resultCode == AppCompatActivity.RESULT_OK) {
            postUrl = data?.data!!
            select_photo_liveData.setImageURI(postUrl)

        }
    }



    // function jis se pata chale ki kis activity me rakhna he user ko
    private fun redirectFunction(name: String) {
        val intent= when(name)
        {

            "MAIN" -> Intent(this,postActivity::class.java)

            else ->Toast.makeText(this,"You have to log in first ",Toast.LENGTH_SHORT).show()


        }

        startActivity(intent as Intent?)
        finish()
    }

    private fun createUser() {

        autenticationFunction()


    }


    private fun autenticationFunction() {
        val NAME=personName_logIn.text.toString()
        val EMAIL= email_logIn.text.toString()
        val PASSWORD=emailPassowrd_logIn.text.toString()
        val PASSWORD2=password_confirm_logIn.text.toString()

        if(EMAIL.equals("")||PASSWORD==""||PASSWORD2==""|| NAME.equals("")){
            Toast.makeText(this," something is blank ", Toast.LENGTH_LONG).show()
            return
        }
        if(PASSWORD!= PASSWORD2){
            Toast.makeText(this,"passowrd do not match ", Toast.LENGTH_LONG).show()
            return
        }

        if(PASSWORD.length<=6)
        {
            Toast.makeText(this,"Password should be of 8 or more than 8 degit ",Toast.LENGTH_SHORT).show()
            return
        }

        mAuth.createUserWithEmailAndPassword(EMAIL,PASSWORD).addOnCompleteListener(this){ task->
            if (task.isSuccessful){
                addUserToDataBase(NAME,EMAIL,mAuth.currentUser?.uid!!)
            }
        }

    }


    private fun addUserToDataBase(name: String, email: String, uid: String?) {

        mDB_ref=FirebaseDatabase.getInstance().reference

        val loading= loading_bar(this)
        loading.startLoading()

        val formatter = SimpleDateFormat("dd_MM_yyyy_ss", Locale.getDefault())
        val now = Date()
        val fileName = formatter.format(now)
        val storageReference = FirebaseStorage.getInstance().getReference("profilePhoto/$fileName")

        val resolver = this.contentResolver
        val bitmap = MediaStore.Images.Media.getBitmap(resolver, postUrl)
        val byteArrayOutputStrem = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStrem)
        val reduceImage: ByteArray = byteArrayOutputStrem.toByteArray()

        storageReference.putBytes(reduceImage).addOnSuccessListener {

            Toast.makeText(this, "Photo is uploaded successfully ", Toast.LENGTH_LONG).show()

            storageReference.downloadUrl.addOnSuccessListener {

                val profileImageUrl = it.toString()

                val user=Person(uid!!,name,profileImageUrl)

                parsonDao.addUser(user)

                Toast.makeText(this,"Uploaded successfully",Toast.LENGTH_SHORT).show()

                //    loading.isDismiss()

                if (uid != null) {
                    mDB_ref.child("users").child(uid).setValue(User(name,email, uid,profileImageUrl))



                    loading.isDismiss()

                    val intent=Intent(this,postActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

}