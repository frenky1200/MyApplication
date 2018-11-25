package com.example.myapplication.activities

import android.view.*
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout
import org.jetbrains.anko.constraint.layout.matchConstraint

class SomeActivity : AnkoComponent<HistoryActivity> {
    override fun createView(ui: AnkoContext<HistoryActivity>): View = with(ui){

        constraintLayout {
            listView {
                id = listView
            }.lparams( width = matchConstraint, height = matchConstraint ) {
                topToTop = PARENT_ID
                bottomToBottom = PARENT_ID
                leftToLeft = PARENT_ID
                rightToRight = PARENT_ID
            }
        }
    }

    companion object Ids {
        val listView = 1

    }
}