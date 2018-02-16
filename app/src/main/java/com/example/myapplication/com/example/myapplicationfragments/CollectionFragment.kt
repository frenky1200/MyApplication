package com.example.myapplication.com.example.myapplicationfragments

import android.app.Fragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.example.myapplication.MainActivity

import com.example.myapplication.R
import com.example.myapplication.data.Album
import com.example.myapplication.data.control.DBController
import kotlinx.android.synthetic.main.activity_collection.*

class CollectionFragment : Fragment() {
    private var b: String? = null
    private lateinit var c: DBController
    private lateinit var list: MutableList<Album>
    private lateinit var anim: Animation
    private lateinit var adapter: ArrayAdapter<Album>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_collection, container, false)
        val bundle = arguments
        b = bundle.getString("z")
        activity.title = MainActivity.collectionType.name
        c = DBController(activity)
        list = c.getAllAlbum(b)
        anim = AnimationUtils.loadAnimation(activity, R.anim.second)
        adapter = ArrayAdapter(activity,
                android.R.layout.simple_list_item_1, list)
        return view
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        ListView.adapter = adapter
        ListView.startAnimation(anim)
        imageButton.setOnClickListener({onAddClick()})
        ListView.setOnItemLongClickListener({ adapterView: AdapterView<*>, _: View, i: Int, _: Long -> onListLongClick(adapterView, i)})
        ListView.setOnItemClickListener({ adapterView: AdapterView<*>, _: View, i: Int, _: Long -> onListClick(adapterView, i)})
    }

    private fun onAddClick() {
        val newColl = editColl!!.text.toString()
        c.addAlbum(newColl, b)
        list.clear()
        list.addAll(c.getAllAlbum(b))
        adapter.notifyDataSetChanged()
    }

    private fun onListLongClick(parent: AdapterView<*>, position: Int): Boolean {
        val album = parent.getItemAtPosition(position) as Album
        c.deleteAlbum(album)
        val list = c.getAllAlbum(b)
        val adapter = ArrayAdapter(activity,
                android.R.layout.simple_list_item_1, list)
        ListView!!.adapter = adapter
        return false
    }

    private fun onListClick(parent: AdapterView<*>, position: Int) {
        val album = parent.getItemAtPosition(position) as Album
        val bundle = Bundle()
        bundle.putString("album", album.name)
        bundle.putString("type", album.type)
        val myFragment = MediaFragment()
        myFragment.arguments = bundle
        val fragmentManager = fragmentManager
        fragmentManager.beginTransaction()
                .replace(R.id.content_main, myFragment)
                .addToBackStack("myStack")
                .commit()
    }
}