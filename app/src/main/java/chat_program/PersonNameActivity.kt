package chat_program

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.gettogether.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_person_name.*
import postPakage.postActivity

class PersonNameActivity : AppCompatActivity() {

    lateinit var userList: ArrayList<User>
    lateinit var adapter: UserAdapter
    lateinit var mAuth: FirebaseAuth
    lateinit var mDB_Ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_person_name)

        mAuth = FirebaseAuth.getInstance()
        mDB_Ref = FirebaseDatabase.getInstance().getReference()

        userList = ArrayList()
        adapter = UserAdapter(this, userList)


        recyclerView_personName.layoutManager = LinearLayoutManager(this)
        recyclerView_personName.adapter = adapter

        back_to_postActivity.setOnClickListener() {
            val intent = Intent(this, postActivity::class.java)
            startActivity(intent)
        }

        mDB_Ref.child("users").addValueEventListener(object : ValueEventListener {
            @SuppressLint("NotifyDataSetChanged")
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                for (postSnapshot in snapshot.children) {
                    val currentUser = postSnapshot.getValue(User::class.java)


                    if (mAuth.currentUser?.uid != currentUser?.uid) {
                        userList.add(currentUser!!)
                    }


                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {

            }

        })

    }



}