package postPakage

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import com.example.gettogether.R
import com.example.gettogether.loading_bar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_creat_post.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import notificationPackage.NotificationData
import notificationPackage.PushNotification
import notificationPackage.RetrofitInstance
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

const val TOPIC="/topics/myTopicGetTogether"

class creatPostActivity : AppCompatActivity() {

    val TAG = "MainActivity"

    lateinit var postDao: postDao
    lateinit var postUrl: Uri
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_creat_post)


        FirebaseMessaging.getInstance().subscribeToTopic(TOPIC)




        auth = Firebase.auth
        postDao = postDao()

        post_Image.setOnClickListener() {
            selectPhotoFromDevice()
        }

        createpost_to_post.setOnClickListener(){
            val intent= Intent(this,postActivity::class.java)
            startActivity(intent)
        }
        uploadPost.setOnClickListener() {
            upload_post_to_database()
        }

    }

    private fun selectPhotoFromDevice() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(intent, 15)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 15 && resultCode == AppCompatActivity.RESULT_OK) {
            postUrl = data?.data!!
            post_Image.setImageURI(postUrl)

        }
    }

    private fun upload_post_to_database() {
        val loading= loading_bar(this)
        loading.startLoading()
        val firebaseUser = auth.currentUser

        val db= FirebaseFirestore.getInstance()
        val ref= db.collection("users").document(firebaseUser!!.uid)


        if (firebaseUser != null) {

            val formatter = SimpleDateFormat("dd_MM_yyyy_ss", Locale.getDefault())
            val now = Date()
            val fileName = formatter.format(now)
            val storageReference = FirebaseStorage.getInstance().getReference("UserPost/$fileName")

            val resolver = this.contentResolver
            val bitmap = MediaStore.Images.Media.getBitmap(resolver, postUrl)
            val byteArrayOutputStrem = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStrem)
            val reduceImage: ByteArray = byteArrayOutputStrem.toByteArray()

            storageReference.putBytes(reduceImage).addOnSuccessListener {

                Toast.makeText(this, "Photo is uploaded successfully ", Toast.LENGTH_LONG).show()


                ref.get().addOnSuccessListener {
                    val name =it.get("displayName")
                    val title = "HellO"
                    val message = "$name has uploaded a post "
                    if (title.isNotEmpty() && message.isNotEmpty()){
                        PushNotification(
                            NotificationData(title,message),
                            TOPIC
                        ).also {itt->
                            sendNotification(itt)
                        }
                    }
                }


                storageReference.downloadUrl.addOnSuccessListener {

                    val postImageUrl = it.toString()
                    val caption = postCaption.text.toString()

                    if(postImageUrl.isNotEmpty()){

                        postDao.addPost(caption,postImageUrl)

                        val intent = Intent(this, postActivity::class.java)
                        startActivity(intent)
                        Toast.makeText(this,"Uploaded successfully", Toast.LENGTH_SHORT).show()


                    }
                    else{
                        Toast.makeText(this,"Error occurred ", Toast.LENGTH_SHORT).show()
                        val intent= Intent(this,postActivity::class.java)
                        startActivity(intent)
                    }

                    loading.isDismiss()

                }

            }

        }
    }



    @SuppressLint("LogNotTimber")
    private fun sendNotification(notification: PushNotification) = CoroutineScope(Dispatchers.IO).launch {
        try {
            val response= RetrofitInstance.api.postNotification(notification)
            if (response.isSuccessful){
                Log.d(TAG,"Response: ${Gson().toJson(response)}")
            }

        }catch (e:Exception){
            Log.e(TAG,e.toString())
        }
    }

}
