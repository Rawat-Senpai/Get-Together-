package details_person

import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class parsonDao {
    val db= FirebaseFirestore.getInstance()
    val userCollection= db.collection("users")

    fun addUser(Person:Person?){
        Person?.let {
            GlobalScope.launch(Dispatchers.IO){
                userCollection.document(Person.uid).set(it)
            }
        }
    }

    fun getUserById(uid:String): Task<DocumentSnapshot> {
        return  userCollection.document(uid).get()
    }

}