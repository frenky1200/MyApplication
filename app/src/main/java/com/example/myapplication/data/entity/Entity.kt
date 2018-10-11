package com.example.myapplication.data.entity

import com.example.myapplication.data.interfaces.IMediable

data class Entity(var idmedia: Int?) : IMediable {
    override fun getName(): String {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getId(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun setName(name: String?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun toString(): String {
        return id.toString() + ". " + name
    }
}