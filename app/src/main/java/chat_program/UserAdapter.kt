package chat_program

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gettogether.R

class UserAdapter(private val context: Context, private val userList:ArrayList<User>): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.user_list_layout,parent,false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser= userList[position]

        holder.app_User_Name.text= currentUser.name
        Glide.with(holder.app_user_profile.context).load(currentUser.photoProfile).circleCrop().into(holder.app_user_profile)

        holder.itemView.setOnClickListener(){
            val intent = Intent(context, Chat_layout::class.java)
            intent.putExtra("name",currentUser.name)
            intent.putExtra("uid",currentUser.uid)
            intent.putExtra("userDP",currentUser.photoProfile)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return  userList.size
    }


    class UserViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val app_User_Name= itemView.findViewById<TextView>(R.id.personName_layout)
        val app_user_profile= itemView.findViewById<ImageView>(R.id.personImage_layout)

    }
}