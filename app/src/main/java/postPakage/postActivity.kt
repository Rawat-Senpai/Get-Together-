package postPakage

import VideoCallingfeature.videoCallingActivity
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.GravityCompat
import androidx.recyclerview.widget.LinearLayoutManager
import chat_program.PersonNameActivity
import com.bumptech.glide.Glide
import com.example.gettogether.R
import com.example.gettogether.infoActivity
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_post.*

class postActivity : AppCompatActivity(),  IpostAdapter{


    lateinit var adapter: PostAdapter
    lateinit var postDao: postDao
    lateinit var auth: FirebaseAuth
    private  val CHANNEL_ID = "Your_Channel_ID"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_post)
        postDao =postDao()

        auth = Firebase.auth

        loadDataOfUser()


        videoCallingFeature.setOnClickListener(){
            setUpVideoCall()
        }


        about_developer.setOnClickListener(){
            aboutDeveloperFunction()
        }


        createPost.setOnClickListener(){
            createPostFunction()
        }

        messageActivity.setOnClickListener(){
            sendMessageFunction()
        }

        info_section.setOnClickListener(){
//            Toast.makeText(this,"info section is to be added ",Toast.LENGTH_SHORT).show()
            val intent= Intent(this, infoActivity::class.java)
            startActivity(intent)
        }

        val postCollection =postDao.postcollection
        val query= postCollection.orderBy("createdAt", Query.Direction.DESCENDING)
        val recyclerViewOption = FirestoreRecyclerOptions.Builder<Post>().setQuery(query,Post::class.java).build()



        adapter = PostAdapter(recyclerViewOption,this)

        post_recyclerView.adapter= adapter
        post_recyclerView.layoutManager= LinearLayoutManager(this)
        post_recyclerView.scrollToPosition(adapter.itemCount.minus(-1))
        post_recyclerView.itemAnimator= null

    }


    private fun loadDataOfUser() {
        var count = false
        if (!count){
            val firebaseUser = auth.currentUser

            val db = FirebaseFirestore.getInstance()
            val ref: DocumentReference = db.collection("users").document(firebaseUser!!.uid)

            ref.get().addOnSuccessListener {
                val userProfileName = it.get("displayName")
                val userProfileImage = it.get("displayPicture")

                val hView = nav_view.getHeaderView(0)
                val nameOfUser = hView.findViewById<TextView>(R.id.UserPersonName)
                nameOfUser.text = userProfileName.toString()

                val userImagePic = hView.findViewById<ImageView>(R.id.userProfilePic)
                Glide.with(userImagePic).load(userProfileImage).fitCenter().into(userImagePic)

                count= true
            }

        }

    }

    private fun setUpVideoCall() {
        val intent = Intent(this, videoCallingActivity::class.java)
        Toast.makeText(this,"video calling activity", Toast.LENGTH_SHORT).show()
        startActivity(intent)
    }


    private fun aboutDeveloperFunction() {
        if (mainDrawer.isDrawerOpen(GravityCompat.START))
        {
            mainDrawer.closeDrawer(GravityCompat.START)
        }
        else{
            mainDrawer.openDrawer(GravityCompat.START)
        }
    }

    private fun sendMessageFunction() {
        Toast.makeText(this,"Wait a Moment", Toast.LENGTH_SHORT).show()
        val intent = Intent(this, PersonNameActivity::class.java)
        startActivity(intent)
    }

    private fun createPostFunction() {
        val intent= Intent(this,creatPostActivity::class.java)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun onLikeClicked(postId: String) {
        postDao.updateLikes(postId)
    }


}