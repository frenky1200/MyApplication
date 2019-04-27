package com.example.myapplication.dsl

import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams.PARENT_ID
import com.example.myapplication.R
import com.example.myapplication.activities.ReadActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.constraint.layout.constraintLayout

class ExampleActivity : AnkoComponent<ReadActivity> {

    override fun createView(ui: AnkoContext<ReadActivity>): View = with(ui) {
        constraintLayout {

            imageView {
                backgroundResource = R.drawable.side_nav_bar
                id = Ids.imageView2

            }.lparams(width = matchParent, height = dip(0)){
                bottomToBottom = R.id.scrollView2
                endToEnd = PARENT_ID
                startToStart = PARENT_ID
                topToTop = PARENT_ID
            }

            videoView {
                id = Ids.videoView
                visibility = View.INVISIBLE

            }.lparams(width = wrapContent, height = wrapContent){
                endToEnd = PARENT_ID
                startToStart = PARENT_ID
                topToTop = PARENT_ID
            }

            scrollView {
                id = Ids.scrollView2

            }.lparams(width = dip(0), height = dip(0)){
                bottomToBottom = PARENT_ID
                endToEnd = PARENT_ID
                startToStart = PARENT_ID
                topToTop = R.id.videoView
            }
        }
    }

    private object Ids {
        val bottomNavigationView2 = 1
        val buttonplay = 2
        val buttonsearch = 3
        val buttonupd = 4
        val imageView2 = 5
        val scrollView2 = 6
        val videoView = 7
    }
}

class MusicActivity : AnkoComponent<ReadActivity> {

    override fun createView(ui: AnkoContext<ReadActivity>): View = with(ui) {
        constraintLayout {

            imageView {
                backgroundResource = R.drawable.side_nav_bar
                id = Ids.imageView2

            }.lparams(width = matchParent, height = dip(0)){
                bottomToBottom = R.id.scrollView2
                endToEnd = PARENT_ID
                startToStart = PARENT_ID
                topToTop = PARENT_ID
            }

            videoView {
                id = Ids.videoView
                visibility = View.INVISIBLE

            }.lparams(width = wrapContent, height = wrapContent){
                endToEnd = PARENT_ID
                startToStart = PARENT_ID
                topToTop = PARENT_ID
            }

            scrollView {
                id = Ids.scrollView2

            }.lparams(width = dip(0), height = dip(0)){
                bottomToBottom = PARENT_ID
                endToEnd = PARENT_ID
                startToStart = PARENT_ID
                topToTop = R.id.videoView
            }
        }
    }

    private object Ids {
        val bottomNavigationView2 = 1
        val buttonplay = 2
        val buttonsearch = 3
        val buttonupd = 4
        val imageView2 = 5
        val scrollView2 = 6
        val videoView = 7
    }
}

class VideoActivity : AnkoComponent<ReadActivity> {

    override fun createView(ui: AnkoContext<ReadActivity>): View = with(ui) {
        constraintLayout {

            imageView {
                backgroundResource = R.drawable.side_nav_bar
                id = Ids.imageView2

            }.lparams(width = matchParent, height = dip(0)){
                bottomToBottom = R.id.scrollView2
                endToEnd = PARENT_ID
                startToStart = PARENT_ID
                topToTop = PARENT_ID
            }

            videoView {
                id = Ids.videoView
                visibility = View.INVISIBLE

            }.lparams(width = wrapContent, height = wrapContent){
                endToEnd = PARENT_ID
                startToStart = PARENT_ID
                topToTop = PARENT_ID
            }

            scrollView {
                id = Ids.scrollView2

            }.lparams(width = dip(0), height = dip(0)){
                bottomToBottom = PARENT_ID
                endToEnd = PARENT_ID
                startToStart = PARENT_ID
                topToTop = R.id.videoView
            }
        }
    }

    private object Ids {
        val bottomNavigationView2 = 1
        val buttonplay = 2
        val buttonsearch = 3
        val buttonupd = 4
        val imageView2 = 5
        val scrollView2 = 6
        val videoView = 7
    }
}

class TextActivity : AnkoComponent<ReadActivity> {

    override fun createView(ui: AnkoContext<ReadActivity>): View = with(ui) {
        constraintLayout {

            imageView {
                backgroundResource = R.drawable.side_nav_bar
                id = Ids.imageView2

            }.lparams(width = matchParent, height = dip(0)){
                bottomToBottom = R.id.scrollView2
                endToEnd = PARENT_ID
                startToStart = PARENT_ID
                topToTop = PARENT_ID
            }

            videoView {
                id = Ids.videoView
                visibility = View.INVISIBLE

            }.lparams(width = wrapContent, height = wrapContent){
                endToEnd = PARENT_ID
                startToStart = PARENT_ID
                topToTop = PARENT_ID
            }

            scrollView {
                id = Ids.scrollView2

            }.lparams(width = dip(0), height = dip(0)){
                bottomToBottom = PARENT_ID
                endToEnd = PARENT_ID
                startToStart = PARENT_ID
                topToTop = R.id.videoView
            }
        }
    }

    private object Ids {
        val bottomNavigationView2 = 1
        val buttonplay = 2
        val buttonsearch = 3
        val buttonupd = 4
        val imageView2 = 5
        val scrollView2 = 6
        val videoView = 7
    }
}
