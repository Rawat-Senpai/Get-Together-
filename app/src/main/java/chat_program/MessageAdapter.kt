package chat_program

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.gettogether.R
import com.google.firebase.auth.FirebaseAuth

class messageAdapter(private val context: Context, private val messageList:ArrayList<Message>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val recieved_item=1
    val send_item=2

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType==1){
            val view: View = LayoutInflater.from(context).inflate(R.layout.recieve_message,parent,false)
            RecieveViewHolder(view)
        } else{
            val view: View = LayoutInflater.from(context).inflate(R.layout.send_layout,parent,false)
            SentViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        val currentMessage = messageList[position]

        if (holder.javaClass == SentViewHolder::class.java){
            // for send data
            val viewHolder = holder as SentViewHolder
            holder.sentMessage.text= currentMessage.message

        }
        else{
            //for recieve data
            val viewHolder= holder as RecieveViewHolder
            holder.recieveMessage.text= currentMessage.message

        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]
        return if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            send_item
        } else {
            recieved_item
        }
    }

    override fun getItemCount(): Int {
        return  messageList.size
    }


    class SentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val sentMessage = itemView.findViewById<TextView>(R.id.senderMessage)
    }

    class RecieveViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val recieveMessage = itemView.findViewById<TextView>(R.id.recieveMessage)
    }

}