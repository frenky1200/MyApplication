package com.example.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.example.myapplication.R
import com.example.myapplication.fragments.FindFragment


class SecAdapter(context: Context, private val layoutId: Int, private val medias: Array<FindFragment.Finder>) : BaseAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun getCount(): Int {
        return medias.size
    }

    override fun getItem(i: Int): Any {
        return medias[i]
    }

    override fun getItemId(i: Int): Long {
        return 0
    }

    override fun getView(position: Int, v: View?, parent: ViewGroup): View {
        var view = v
        val holder: ViewHolder

        if (view != null) {
            holder = view.tag as ViewHolder
        } else {
            view = inflater.inflate(layoutId, parent, false)
            holder = ViewHolder(view)
            view!!.tag = holder
        }

        customizeView(view, holder, medias[position])

        return view
    }


    private fun customizeView(view: View, holder: ViewHolder, media: FindFragment.Finder) {

        holder.textView3.text = media.name
    }

    internal class ViewHolder(view: View) {

        var textView3: TextView = view.findViewById(R.id.textView3)
    }
}