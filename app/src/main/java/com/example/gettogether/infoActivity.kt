package com.example.gettogether

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_info.*
import postPakage.postActivity

private const val  APPurl="https://drive.google.com/file/d/1Sv8CiHy6ww6mPV548JLjLlQ9DLKcJWX1/view?usp=sharing"
class infoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_info)

        info_to_post.setOnClickListener(){
            val intent=Intent(this, postActivity::class.java)
            startActivity(intent)
        }


        shareThisApp.setOnClickListener(){
            val intent = Intent(Intent.ACTION_SEND)
            intent.type="text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, "Hey check this cool app created by Rawat_Senpai  $APPurl")
            val chooser= Intent.createChooser(intent,"Share this app using...")
            startActivity(chooser)
        }

    }
}