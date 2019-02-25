package com.example.myapplication.data.control

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.myapplication.data.entity.History
import org.jetbrains.anko.db.*

class HisController(ctx: Context) : ManagedSQLiteOpenHelper(ctx, "HisDatabase", null, 1) {
    companion object {
        private var instance: HisController? = null

        @Synchronized
        fun getInstance(ctx: Context): HisController {
            if (instance == null) {
                instance = HisController(ctx.applicationContext)
            }
            return instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Here you create tables
        db.createTable("History", true,
                "id" to INTEGER + PRIMARY_KEY,// + UNIQUE
                "name" to TEXT,
                "type" to TEXT,
                "tags" to TEXT,
                "album" to TEXT,
                "insideUri" to TEXT,
                "outsideUri" to TEXT
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Here you can upgrade tables, as usual
        db.dropTable("User", true)
    }

    fun add( name: String, album: String){
        writableDatabase
        use{
            delete("History",
                    "name = \"$name\" and album = \"$album\"")
        }
        use{
            insert("History",
                    "name" to name,
                    "album" to album,
                    "type" to "Music")
        }
    }

    fun remove(id: Int){
        writableDatabase
        use{
            delete("History",
                    "id = $id")
        }
    }

    fun getAll(): ArrayList<History>{
        val a = ArrayList<History>()
        readableDatabase
        use{
            select("History",
                    "id",
                    "name",
                    "type",
                    "tags",
                    "album",
                    "insideUri",
                    "outsideUri"
            ).exec {
                while (moveToNext())
                    a.add(History( getInt(0),
                            getString(1),
                            getString(2),
                            getString(3),
                            getString(4),
                            getString(5),
                            getString(6)))
            }
        }
        return a
    }
}

// Access property for Context
val Context.database: HisController
    get() = HisController.getInstance(applicationContext)