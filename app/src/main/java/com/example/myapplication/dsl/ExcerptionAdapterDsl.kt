package com.example.myapplication.dsl

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import com.example.myapplication.R
import com.example.myapplication.adapters.ExcerptionAdapter
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout

class Ada : AnkoComponent<ExcerptionAdapter> {

    override fun createView(ui: AnkoContext<ExcerptionAdapter>): View = with(ui) {
        constraintLayout {

            textView("") {
                id = Ids.textView3
                textSize = 15f

            }.lparams(width = dip(0), height = wrapContent){
                endToStart = Ids.imageDelete
                horizontalBias = 0.0f
                startToStart = PARENT_ID
                topToTop = PARENT_ID
            }

            textView("") {
                id = Ids.textView5
                textSize = 12f

            }.lparams(width = wrapContent, height = wrapContent){
                startToStart = PARENT_ID
                topToBottom = Ids.textView3
            }

            textView {
                id = Ids.textView2
                textAlignment = View.TEXT_ALIGNMENT_VIEW_END

            }.lparams(width = wrapContent, height = wrapContent){
                endToStart = Ids.imageDelete
                topToBottom = Ids.textView3
            }

            imageButton (android.R.drawable.ic_menu_delete) {
                id = Ids.imageDelete
            }.lparams(width = dip(45), height = dip(45)){
                endToEnd = PARENT_ID
                topToTop = PARENT_ID
            }

        }
    }

    private object Ids {
        val imageDelete = R.id.imageDelete
        val textView2 = R.id.textView2
        val textView3 = R.id.textView3
        val textView5 = R.id.textView5
    }
}