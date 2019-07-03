package com.example.myapplication.dsl

import android.view.Gravity
import android.view.View
import com.example.myapplication.R
import com.example.myapplication.activities.MainActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.design.floatingActionButton
import org.jetbrains.anko.design.navigationView
import org.jetbrains.anko.support.v4.drawerLayout

class MainDsl : AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>): View = with(ui) {

        drawerLayout {
            id = drawer_layout

            include<View>(R.layout.content_main).lparams(width = matchParent, height = matchParent)
            relativeLayout{
                /*floatingActionButton {
                    imageResource = android.R.drawable.ic_dialog_email
                    isClickable = true
                    isFocusable = true

                }.lparams {
                    margin = dip(10)
                    alignParentBottom()
                    alignParentEnd()
                }*/
            }
            navigationView {
                fitsSystemWindows = true
                id = nav_view
                inflateMenu(R.menu.activity_main_drawer)
            }.lparams(width = wrapContent, height = matchParent, gravity = Gravity.START)
        }
    }

    companion object Ids {
        const val drawer_layout = 1
        const val nav_view = 2
    }
}