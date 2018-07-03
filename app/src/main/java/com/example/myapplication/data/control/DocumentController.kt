package com.example.myapplication.data.control

import android.os.AsyncTask
import android.support.annotation.IntegerRes
import android.widget.Toast
import com.example.myapplication.activities.MainActivity
import com.example.myapplication.data.entity.Album
import ru.profit_group.scorocode_sdk.Callbacks.CallbackDocumentSaved
import ru.profit_group.scorocode_sdk.Callbacks.CallbackFindDocument
import ru.profit_group.scorocode_sdk.Callbacks.CallbackRemoveDocument
import ru.profit_group.scorocode_sdk.scorocode_objects.Document
import ru.profit_group.scorocode_sdk.scorocode_objects.DocumentInfo
import ru.profit_group.scorocode_sdk.scorocode_objects.Query
import ru.profit_group.scorocode_sdk.Responses.data.ResponseRemove
import java.util.*
import javax.security.auth.callback.Callback


class DocumentController{
    companion object  {

        fun addAlbum(newColl:String, type:String, id:Long){
            val doc = Document("albums")
            val query = Query("albums")
            query.equalTo("name", newColl)
            query.findDocuments(object : CallbackFindDocument {
                override fun onDocumentFound(documentInfos: List<DocumentInfo>?) {
                    if ((documentInfos != null) && (documentInfos.count()==0)){
                        doc.setField("id", id.toInt())
                        doc.setField("name", newColl)
                        doc.setField("type", type)
                        doc.setField("alternate", "")
                        doc.saveDocument(object : CallbackDocumentSaved {
                                override fun onDocumentSaved() {
                                }

                                override fun onDocumentSaveFailed(errorCode: String, errorMessage: String) {
                                }
                            })
                    }
                }
                override fun onDocumentNotFound(errorCode: String, errorMessage: String) {
                }
            })
        }

        fun deleteAlbum(album:Album){
            val query = Query("albums")
            query.equalTo("name", album.name)
            query.removeDocument(object : CallbackRemoveDocument {
                override fun onRemoveSucceed(responseRemove: ResponseRemove) {
                    //succeed. See responseRemove to findout how many documents was removed
                    //and get list of removed documents
                }

                override fun onRemoveFailed(errorCode: String, errorMessage: String) {
                    //error during remove operation
                }
            })
        }

        fun getAllAlbums(s:String):List<Album>{
            val list = LinkedList<Album>()
            val album = Album()
            val query = Query("albums")
            query.equalTo("type", s)
            query.findDocuments(object : CallbackFindDocument {
                override fun onDocumentFound(documentInfos: List<DocumentInfo>?) {
                    for (doc in documentInfos!!){
                        album.type = doc.get("type").toString()
                        album.id = (doc.get("id") as Double).toInt()
                        album.name = doc.get("name").toString()
                        list.add(album)
                    }
                }
                override fun onDocumentNotFound(errorCode: String, errorMessage: String) {
                }
            })
            return list
        }
    }
}
