package com.example.myapplication.data.entity

data class History(val id: Int,
val name: String,
val type: String,
val tags: String?,
val album: String?,
val insideUri: String?,
val outsideUri: String?) {

    override fun toString(): String {
        return id.toString() + ". " + album + " - " + name
    }
}