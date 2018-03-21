package com.example.myapplication.com.example.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils

import com.example.myapplication.R
import com.example.myapplication.adapters.ExcerptionAdapter
import com.example.myapplication.data.entity.Media
import com.example.myapplication.data.control.DBController

import java.util.ArrayList
import kotlinx.android.synthetic.main.activity_find.*

class FindFragment : Fragment() {

    private var anim: Animation? = null
    private lateinit var c :DBController
    private lateinit var list : ArrayList<Media>
    private lateinit var adapter : ExcerptionAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_find, container, false)
        c = DBController(activity)
        list = ArrayList()
        adapter = ExcerptionAdapter(activity, list, R.layout.adapter_excerption)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        anim = AnimationUtils.loadAnimation(activity, R.anim.second)
        ListViewFind.adapter = adapter

        findbutton.setOnClickListener({onClick()})
    }

    private fun onClick(){
        ListViewFind.startAnimation(anim)
        list.clear()
        list.addAll(c.findTags(EditTextFind.text.toString()))
        adapter.notifyDataSetChanged()
    }
}