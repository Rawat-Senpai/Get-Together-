package postPakage

import com.google.android.gms.tasks.Task
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import details_person.Person
import details_person.parsonDao
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class postDao {

    val db = FirebaseFirestore.getInstance()
    val postcollection= db.collection("post")
    val auth= Firebase.auth


    fun addPost(text:String,ImageUrl:String)
    {
        GlobalScope.launch {
            val currentUserId= auth.currentUser!!.uid
            val parsonDao= parsonDao()
            val person= parsonDao.getUserById(currentUserId).await().toObject(Person::class.java)!!

            val currentTime=System.currentTimeMillis()
            val post=Post(text,ImageUrl,person,currentTime)
            postcollection.document().set(post)

        }

    }
    fun getPost(postId:String): Task<DocumentSnapshot> {
        return  postcollection.document(postId).get()
    }

    fun updateLikes(postId:String){
        GlobalScope.launch {
            val currentUser=auth.currentUser!!.uid
            val post = getPost(postId).await().toObject(Post::class.java)!!
            val isLiked = post.likedBy.contains(currentUser)
            if (isLiked) {
                post.likedBy.remove(currentUser)
            }

            else{
                post.likedBy.add(currentUser)
            }
            postcollection.document(postId ).set(post)
        }
    }

}