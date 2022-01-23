package chat_program

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.gettogether.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_chat_layout.*

class Chat_layout : AppCompatActivity() {

    lateinit var  messageAdapter: messageAdapter
    lateinit var messageList:ArrayList<Message>
    lateinit var mDBref: DatabaseReference

    var recieverRoom:String?=null
    var senderRoom:String?=null
    val senderUid= FirebaseAuth.getInstance().currentUser?.uid


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_layout)


        val name = intent.getStringExtra("name")
        val RecieverUid= intent.getStringExtra("uid")
        val userDP=intent.getStringExtra("userDP")

        //photo lagao jo intent ke through aya he

        Glide.with(userDp).load(userDP).circleCrop().into(userDp)
        Person_chat_name.text = name

        mDBref= FirebaseDatabase.getInstance().getReference()

        senderRoom = RecieverUid + senderUid
        recieverRoom=senderUid+RecieverUid

        messageList= ArrayList()
        messageAdapter= messageAdapter(this,messageList)

        recyclerView_chat.layoutManager= LinearLayoutManager(this)
        recyclerView_chat.adapter= messageAdapter

        back_to_list.setOnClickListener(){
            val intent= Intent(this,PersonNameActivity::class.java)
            startActivity(intent)
        }


        mDBref.child("chats").child(senderRoom!!).child("Message")
            .addValueEventListener(object : ValueEventListener {
                @SuppressLint("NotifyDataSetChanged")
                override fun onDataChange(snapshot: DataSnapshot) {

                    messageList.clear()

                    for(postSnapshot in snapshot.children){
                        val message = postSnapshot.getValue(Message::class.java)
                        messageList.add(message!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }

                override fun onCancelled(error: DatabaseError) {

                }

            })

        //sending message to database
        button_sendMessage.setOnClickListener(){
            sendMessage_to_database()
        }

    }

    private fun sendMessage_to_database() {
        val message =sendMessageText.text.toString()
        val messageObject= Message(message,senderUid)
        mDBref.child("chats").child(senderRoom!!).child("Message").push()
            .setValue(messageObject).addOnSuccessListener {
                mDBref.child("chats").child(recieverRoom!!).child("Message").push()
                    .setValue(messageObject)
            }

        sendMessageText.text.clear()

    }
}