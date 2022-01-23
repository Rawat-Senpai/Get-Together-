package VideoCallingfeature

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.gettogether.R
import kotlinx.android.synthetic.main.activity_video_calling.*
import org.jitsi.meet.sdk.JitsiMeetActivity
import org.jitsi.meet.sdk.JitsiMeetConferenceOptions
import postPakage.postActivity
import java.net.URL

class videoCallingActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_calling)

        videocalling_to_post.setOnClickListener(){
            val intent= Intent(this,postActivity::class.java)
            startActivity(intent)
        }

        try {
            var options = JitsiMeetConferenceOptions.Builder()
                .setServerURL(URL(""))
                .setWelcomePageEnabled(false)
                .build()
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this,e.message, Toast.LENGTH_LONG).show()
        }


    }

    fun onJoinButtonClick(view: View) {
        val editTextJoinMeeting = findViewById<EditText>(R.id.conferenceName)
        val roomName = editTextJoinMeeting.text.toString()

        if(roomName.length > 0)
        {
            val options = JitsiMeetConferenceOptions.Builder()
                .setRoom(roomName)
                .build()

            JitsiMeetActivity.launch(this,options)
        }


    }
}