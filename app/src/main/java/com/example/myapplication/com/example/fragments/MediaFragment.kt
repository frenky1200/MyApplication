package com.example.myapplication.com.example.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.myapplication.R
import com.example.myapplication.activities.MainActivity
import com.example.myapplication.adapters.ExcerptionAdapter
import com.example.myapplication.data.control.DBController
import com.example.myapplication.data.entity.Media
import kotlinx.android.synthetic.main.activity_media.*
import kotlinx.android.synthetic.main.content_media.*

class MediaFragment : Fragment() {

    private var anim: Animation? = null
    private lateinit var adapter: ExcerptionAdapter
    private var c: DBController? = null
    private var types: String? = null
    private var coll: String? = null
    private lateinit var list: MutableList<Media>

    private fun init() {
        val bundle = arguments
        coll = bundle.getString("album")
        types = MainActivity.collectionType.name
        c = DBController(activity)
        list = c!!.getAllMedia(coll)
        anim = AnimationUtils.loadAnimation(activity, R.anim.second)
        list.sortBy { media: Media -> media.name }
        adapter = ExcerptionAdapter(activity, R.layout.adapter_excerption, list)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_media, container, false)
        init()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MediaList.startAnimation(anim)
        MediaList.adapter = adapter
        imageButton1.setOnClickListener({onClick()})
    }

    private fun onClick() {
        val name = editMedia.text.toString()
        c!!.addMedia(name, types, coll)
        list.clear()
        list.addAll(c!!.getAllMedia(coll))
        adapter.notifyDataSetChanged()
    }
}
