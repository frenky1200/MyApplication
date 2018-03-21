package com.example.myapplication.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.activities.ReadActivity
import com.example.myapplication.data.entity.Media
import com.example.myapplication.data.control.DBController


class SecAdapter(private val context: Context, private val medias: MutableList<Media>, private val layoutId: Int) : BaseAdapter() {
    private var c: DBController? = null
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

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        @Suppress("NAME_SHADOWING")
        var view = view
        val holder: ExcerptionAdapter.ViewHolder

        if (view != null) {
            holder = view.tag as ExcerptionAdapter.ViewHolder
        } else {
            view = inflater.inflate(layoutId, parent, false)
            holder = ExcerptionAdapter.ViewHolder(view)
            view!!.tag = holder
        }

        customizeView(view, holder, medias[position])

        return view
    }


    private fun customizeView(view: View, holder: ExcerptionAdapter.ViewHolder, media: Media) {
        val Name = media.name
        val Album = media.album

        holder.textView3.text = Name
        holder.textView5.text = Album
        holder.imageDelete.setOnClickListener {
            c = DBController(context as Activity)
            c!!.deleteMedia(media)
            Toast.makeText(context, "Deleted", Toast.LENGTH_LONG).show()
            medias.remove(media)
            notifyDataSetChanged()
        }
        view.setOnClickListener {
            val intent = Intent(context, ReadActivity::class.java)
            intent.putExtra("name", media.name)
            intent.putExtra("id", media.id)
            context.startActivity(intent)
        }
    }

    internal class ViewHolder(view: View) {
        var textView5: TextView? = null
        var imageDelete: ImageButton? = null

    }
}