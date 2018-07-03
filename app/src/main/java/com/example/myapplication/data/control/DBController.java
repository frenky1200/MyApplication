package com.example.myapplication.data.control;

import android.annotation.SuppressLint;
import android.app.Activity;

import com.example.myapplication.data.entity.Album;
import com.example.myapplication.data.entity.Anime;
import com.example.myapplication.data.entity.Excerption;
import com.example.myapplication.data.entity.Film;
import com.example.myapplication.data.entity.Image;
import com.example.myapplication.data.entity.Media;
import com.example.myapplication.data.entity.Music;
import com.example.myapplication.data.helpers.AlbumHelper;
import com.example.myapplication.data.helpers.AnimeHelper;
import com.example.myapplication.data.helpers.FilmHelper;
import com.example.myapplication.data.helpers.ExcerptionHelper;
import com.example.myapplication.data.helpers.ImageHelper;
import com.example.myapplication.data.helpers.MediaHelper;
import com.example.myapplication.data.helpers.MusicHelper;
import com.example.myapplication.data.interfaces.IMediable;

import java.util.LinkedList;
import java.util.List;

@SuppressLint("Registered")
public class DBController extends Activity {

    final AlbumHelper q;
    final MediaHelper w;
    final FilmHelper e;
    final ExcerptionHelper r;
    final AnimeHelper t;
    final MusicHelper y;
    final ImageHelper u;

    public DBController(Activity activity){
        this.q = new AlbumHelper(activity);
        this.w = new MediaHelper(activity);
        this.e = new FilmHelper(activity);
        this.r = new ExcerptionHelper(activity);
        this.t = new AnimeHelper(activity);
        this.y = new MusicHelper(activity);
        this.u = new ImageHelper(activity);
    }


    public List<Album> getAllAlbum(String a){
        List<Album> list = q.getAllAlbums(a);
        //List<Album> list2 = DocumentController.Companion.getAllAlbums(a);
        //list.removeAll(list2);
        //list.addAll(list2);
        return list;
    }

    public void addAlbum(String newColl, String type){
        long id =
        q.addAlbum(new Album(newColl, type));
        //DocumentController.Companion.addAlbum(newColl, type, id);
    }

    public void deleteAlbum(Album album){
        q.deleteAlbum(album);
        //DocumentController.Companion.deleteAlbum(album);
    }


    public List<Media> getAllMedia(String a){
        return w.getAllMedia(a);
    }

    public List<Media> findTags(String s, String str){
        return w.findByStr(s, str);
    }

    public Media findByName(String name, String album){
        return w.findByName(name, album);
    }


    public void updateBook (Media media){
        w.updateMedia(media);
    }

    public <T extends IMediable> void updateMedia (T media){
        if(media instanceof Film){
            e.updateFilm((Film)media);
        }if(media instanceof Music){
            y.updateMusic((Music)media);
        }if(media instanceof Anime){
            t.updateAnime((Anime)media);
        }if(media instanceof Excerption){
            r.updateExcerption((Excerption)media);
        }if(media instanceof Image){
            u.updateImage((Image) media);
        }
    }

    public void deleteMedia(Media media){
        w.deleteMedia(media);
        if (media.getType().equals("Music")){y.deleteMusic(y.getMusic(media.getId()));}
        if (media.getType().equals("Film")){e.deleteFilm(e.getFilm(media.getId()));}
        if (media.getType().equals("Anime")){t.deleteAnime(t.getAnime(media.getId()));}
        if (media.getType().equals("Excerption")){r.deleteExcerption(r.getExcerption(media.getId()));}
        if (media.getType().equals("Image")){u.deleteImage(u.getImage(media.getId()));}
    }

    public int addMedia(String name, String type, String album){
        Media media = w.getMedia(w.addMedia(new Media(name, type, album)));
        if (media.getType().equals("Music")){y.addMusic(new Music("", media.getId()));}
        if (media.getType().equals("Film")){e.addFilm(new Film("", media.getId()));}
        if (media.getType().equals("Anime")){t.addAnime(new Anime("", media.getId()));}
        if (media.getType().equals("Excerption")){r.addExcerption(new Excerption("", media.getId()));}
        if (media.getType().equals("Image")){u.addImage(new Image("", media.getId()));}
        return media.getId();
    }

    @SuppressWarnings("unchecked")
    public <T extends IMediable> T getMedia1 (T media, int id){

        if(media instanceof Film){
            return (T) e.getFilm(id);
        }if(media instanceof Music){
            return (T) y.getMusic(id);
        }if(media instanceof Anime){
            return (T) t.getAnime(id);
        }if(media instanceof Excerption){
            return (T) r.getExcerption(id);
        }if(media instanceof Image){
            return (T) u.getImage(id);
        }
        else{
            return media;
        }
    }

    public Media getMedia (int id){
        return w.getMedia(id);
    }

    /////////////////////////////////////////////
    /////////////////////////////////////////////
    /////////////////////////////////////////////

    /////////////////////////////////////////////
    /////////////////////////////////////////////
    /////////////////////////////////////////////

    /////////////////////////////////////////////
    /////////////////////////////////////////////
    /////////////////////////////////////////////

}
