package com.example.myapplication.com.example.myapplicationfragments

import android.app.Fragment
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import com.example.myapplication.activities.DialogActivity
import com.example.myapplication.R
import com.example.myapplication.activities.MainActivity
import com.example.myapplication.activities.ReadActivity
import com.example.myapplication.adapters.ExcerptionAdapter
import com.example.myapplication.data.Media
import com.example.myapplication.data.control.DBController
import kotlinx.android.synthetic.main.activity_media.*
import kotlinx.android.synthetic.main.content_media.*

class MediaFragment : Fragment() {

    private var anim: Animation? = null
    private lateinit var adapter: ExcerptionAdapter
    private var c: DBController? = null
    private var types: String? = null
    private var Coll: String? = null
    private lateinit var list: ArrayList<Media>

    private fun init() {
        val bundle = arguments
        Coll = bundle.getString("album")
        types = MainActivity.collectionType.name
        c = DBController(activity)
        list = ArrayList()
        list.addAll(c!!.getAllMedia(Coll))
        anim = AnimationUtils.loadAnimation(activity, R.anim.second)
        adapter = ExcerptionAdapter(activity, list, R.layout.adapter_excerption)
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
        //MediaList.onItemClickListener = AdapterView.OnItemClickListener { adapterView, _, i, _ -> onItem(adapterView, i)}
        //MediaList.onItemLongClickListener = AdapterView.OnItemLongClickListener {adapterView, _, i, _ -> onItemLong(adapterView, i)}
        imageButton1.setOnClickListener({onClick()})
    }

    private fun onItem(parent: AdapterView<*>, position: Int) {
        val media = parent.getItemAtPosition(position) as Media
        val a = Intent(activity, ReadActivity::class.java)
        a.putExtra("name", media.name)
        a.putExtra("id", media.id)
        startActivity(a)
    }

    private fun onItemLong(parent: AdapterView<*>, position: Int): Boolean {
        val media = parent.getItemAtPosition(position) as Media
        val intent = Intent(activity, DialogActivity::class.java)
        intent.putExtra("name", media.name)
        intent.putExtra("id", media.id)
        startActivity(intent)
        return false
    }

    private fun onClick() {
        val newColl = editMedia.text.toString()
        c!!.addMedia(newColl, types, Coll)
        list.clear()
        list.addAll(c!!.getAllMedia(Coll))
        adapter.notifyDataSetChanged()
    }
}
