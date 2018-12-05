package com.example.myapplication.fragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.myapplication.R
import com.example.myapplication.activities.MainActivity
import com.example.myapplication.MyApp.Companion.c
import com.example.myapplication.adapters.ExcerptionAdapter
import com.example.myapplication.data.entity.Album
import com.example.myapplication.data.entity.Media
import kotlinx.android.synthetic.main.activity_media.*

class MediaFragment : Fragment() {

    private var anim: Animation? = null
    private lateinit var adapter: ExcerptionAdapter
    private var types: String? = null
    private var coll: Album = Album()
    private lateinit var list: MutableList<Media>

    private fun init() {
        val bundle = arguments
        types = MainActivity.collectionType.name
        coll = c.getAlbum( bundle.getInt("album") )
        list = c.getAllMedia(coll.id)
        anim = AnimationUtils.loadAnimation(activity, R.anim.second)
        list.sortBy { media -> media.name }
        adapter = ExcerptionAdapter( activity, list )
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_media, container, false)
        init()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        MediaList.startAnimation( anim )
        MediaList.adapter = adapter
        imageButton1.setOnClickListener { onClick() }
    }

    private fun onClick() {
        val name = editMedia.text.toString()
        c.addMedia( name, types, coll.id )
        list.clear()
        list.addAll( c.getAllMedia(coll.id) )
        adapter.notifyDataSetChanged()
    }
}
