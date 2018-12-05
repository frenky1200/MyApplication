package com.example.myapplication.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.PopupMenu

import com.example.myapplication.R
import com.example.myapplication.activities.ReadActivity
import com.example.myapplication.data.entity.Excerption
import com.example.myapplication.data.entity.Media

import com.example.myapplication.MyApp.Companion.c
import com.example.myapplication.dsl.Ada
import com.example.myapplication.data.interfaces.IMediable
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.find

class ExcerptionAdapter(private val context: Context, private val medias: MutableList<Media>) : BaseAdapter() {

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
        var view = view
        val holder: ViewHolder

        if (view != null) {
            holder = view.tag as ViewHolder
        } else {
            view = Ada().createView(AnkoContext.create(context, this, false))
            holder = ViewHolder(view)
            view.tag = holder
        }

        customizeView(view, holder, medias[position])

        return view
    }


    private fun customizeView(view: View, holder: ViewHolder, media: Media) {

        val name = media.name
        val album = c.getAlbum(media.album).name
        val ad = AlertDialog.Builder(context)
        ad.setTitle("title")  // заголовок
        ad.setMessage("Delete?") // сообщение
        ad.setPositiveButton("Yes") { dialog, arg1 ->
            c.deleteMedia(media)
            Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show()
            medias.remove(media)
            notifyDataSetChanged()
        }
        ad.setNegativeButton("No") { dialog, arg1 -> Toast.makeText(context, "Not deleted", Toast.LENGTH_SHORT).show() }

        holder.textView3.text = name
        if (media.mediaType<IMediable>() is Excerption) {
            holder.textView2.text = album
            holder.textView5.text = ""
        } else {
            holder.textView5.text = album
            holder.textView2.text = ""
        }
        holder.imageDelete.setOnClickListener { view1 ->
            val popupMenu = PopupMenu(this.context, holder.imageDelete)
            popupMenu.inflate(R.menu.option_menu)
            popupMenu.setOnMenuItemClickListener(object : PopupMenu.OnMenuItemClickListener {
                override fun onMenuItemClick(item: MenuItem): Boolean {

                    when (item.itemId) {
                        R.id.action_update-> Toast.makeText(this@ExcerptionAdapter.context, "Saved", Toast.LENGTH_LONG).show()
                        R.id.action_delete -> {
                            ad.show()
                        }
                        else -> {
                        }
                    }
                    return false
                }
            })
            popupMenu.show()
        }
        view.setOnClickListener { v ->
            val intent = Intent(context, ReadActivity::class.java)
            intent.putExtra("id", media.id)
            context.startActivity(intent)
        }
    }

    internal class ViewHolder(view: View) {

        var textView3 = view.find<TextView>(R.id.textView3)
        var textView5 = view.find<TextView>(R.id.textView5)
        var textView2 = view.find<TextView>(R.id.textView2)
        var imageDelete = view.find<ImageButton>(R.id.imageDelete)

    }
}