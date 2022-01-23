package com.example.gettogether

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_developer_info_frag.view.*

class developer_info_frag : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_developer_info_frag, container, false)

        view.developer_to_app.setOnClickListener(){
            findNavController().navigate(R.id.action_developer_info_frag_to_app_info_fragment)
        }

        view.To_Github.setOnClickListener()
        {
            goToGithub()
        }

        view.To_Instagram.setOnClickListener(){
            goToInstagram()
        }
        view.To_Linkedin.setOnClickListener(){
            goToLinkedin()
        }
        view.To_Twitter.setOnClickListener(){
            goToTwitter()
        }
        return  view
    }

    private fun goToTwitter() {
        val TwitterLInk="https://twitter.com/Shobhit76914952"
        val intent= Intent(Intent.ACTION_VIEW)
        intent.data= Uri.parse(TwitterLInk)
        startActivity(intent)

    }

    private fun goToLinkedin() {
        val linkkDinLink="https://www.linkedin.com/in/shobhit-singh-b4a852206"
        val intent= Intent(Intent.ACTION_VIEW)
        intent.data= Uri.parse(linkkDinLink)
        startActivity(intent)
    }

    private fun goToInstagram() {
        val instaUrl="https://www.instagram.com/rawat_senpai/"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(instaUrl)
        startActivity(intent)
    }

    private fun goToGithub() {
        val gitHubLink="https://github.com/Rawat-Senpai"
        val intent= Intent(Intent.ACTION_VIEW)
        intent.data= Uri.parse(gitHubLink)
        startActivity(intent)
    }


}