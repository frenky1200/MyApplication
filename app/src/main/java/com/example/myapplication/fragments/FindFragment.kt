package com.example.myapplication.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.example.myapplication.R
import com.example.myapplication.MyApp.Companion.c
import com.example.myapplication.adapters.ExcerptionAdapter
import com.example.myapplication.adapters.SecAdapter
import com.example.myapplication.data.entity.Media
import kotlinx.android.synthetic.main.activity_find.*
import java.util.*

class FindFragment : Fragment() {

    private var anim: Animation? = null
    private lateinit var list : ArrayList<Media>
    private lateinit var adapter : ExcerptionAdapter
    private lateinit var adapter2 : SecAdapter
    private lateinit var rew : Finder

    enum class Finder{

        Tags{
            override fun toString(): String {
                return "id in (SELECT id FROM media where upper(tags)"
            }
        },
        Name{
            override fun toString(): String {
                return "id in (SELECT id FROM media where upper(name)"
            }
        },
        Descriptions{
            override fun toString(): String {
                return "id in (SELECT idMedia FROM musics where upper(name)"
            }
        },
        Collections{
            override fun toString(): String {
                return "id in (SELECT id FROM media where upper(albums)"
            }
        };
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.activity_find, container, false)
        list = ArrayList()
        adapter = ExcerptionAdapter(activity!!, list)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        anim = AnimationUtils.loadAnimation(activity, R.anim.second)
        ListViewFind.adapter = adapter

        findbutton.setOnClickListener { onClick() }

        val types: Array<Finder> = Finder.values()
        for ((i:Int, s) in Finder.values().withIndex())
            types[i] = s
        adapter2 = SecAdapter( activity!!, R.layout.ada, types )
        spinner4.adapter = adapter2
        spinner4.setSelection( 0 )
    }

    private fun onClick(){
        ListViewFind.startAnimation(anim)
        list.clear()
        rew = spinner4.selectedItem as Finder
        list.addAll( c.findTags( rew.toString(), EditTextFind.text.toString() ) )
        for ( ( i, item) in list.withIndex() )
            item.name = "${i + 1}. ${item.name}"
        adapter.notifyDataSetChanged()
    }
}
