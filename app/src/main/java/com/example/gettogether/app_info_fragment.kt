package com.example.gettogether

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import kotlinx.android.synthetic.main.fragment_app_info_fragment.view.*


class app_info_fragment : Fragment() {



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view= inflater.inflate(R.layout.fragment_app_info_fragment, container, false)


        view.app_to_developer.setOnClickListener(){
            findNavController().navigate(R.id.action_app_info_fragment_to_developer_info_frag)

        }
        return  view
    }


}