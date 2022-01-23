package postPakage

import Utils.Utils
import Utils.Utils.Companion.getTimeAgo
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.gettogether.R
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase


class PostAdapter(options: FirestoreRecyclerOptions<Post>, val listener:IpostAdapter):
    FirestoreRecyclerAdapter<Post, PostAdapter.postViewHolder>(options) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): postViewHolder {

        val viewHolder = postViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.post_layout,parent,false))
        viewHolder.likeButton.setOnClickListener(){
            listener.onLikeClicked(snapshots.getSnapshot(viewHolder.absoluteAdapterPosition).id)
        }
        return viewHolder
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: postViewHolder, position: Int, model: Post) {

        val auth= Firebase.auth
        val currentUser=auth.currentUser!!.uid
        val isLiked= model.likedBy.contains(currentUser)

        // data for comment image pic

        val db = FirebaseFirestore.getInstance()
        val ref: DocumentReference = db.collection("users").document(currentUser)
        ref.get().addOnSuccessListener {
            val userProfileImage = it.get("displayPicture")
            Glide.with(holder.commentImage).load(userProfileImage).centerCrop().circleCrop().into(holder.commentImage)
        }

        holder.userPost.text= model.text
        holder.caption.text=model.createdBy.displayName+" -"
        holder.user_name.text=model.createdBy.displayName

        Glide.with(holder.user_image.context).load(model.createdBy.displayPicture).circleCrop().into(holder.user_image)

        Glide.with(holder.post_image.context).load(model.imageUrl).into(holder.post_image)
        holder.post_time.text= Utils.getTimeAgo(model.createdAt)
        holder.totLike.text=model.likedBy.size .toString() + " people liked"


        if(isLiked){
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context,R.drawable.liked_))
        }
        else{
            holder.likeButton.setImageDrawable(ContextCompat.getDrawable(holder.likeButton.context,R.drawable.fav_unlike))
        }

    }

    inner class postViewHolder(itemView: View): RecyclerView.ViewHolder(itemView)
    {
        val user_name: TextView = itemView.findViewById(R.id.user_name)
        val post_time: TextView = itemView.findViewById(R.id.post_time)
        val userPost: TextView = itemView.findViewById(R.id.user_text)
        val user_image: ImageView = itemView.findViewById(R.id.user_image)
        val post_image: ImageView =itemView.findViewById(R.id.user_image_post)
        val likeButton: ImageView = itemView.findViewById(R.id.likeButton)
        val totLike: TextView =itemView.findViewById(R.id.total_like)
        val caption: TextView = itemView.findViewById(R.id.captionName)

        val commentImage: ImageView = itemView.findViewById(R.id.image_for_comment)

    }

}

interface  IpostAdapter{
    fun onLikeClicked(postId:String)
}