package com.example.myapplication.data.control;

import android.app.Activity;

import com.example.myapplication.data.entity.Album;
import com.example.myapplication.data.entity.Anime;
import com.example.myapplication.data.entity.Book;
import com.example.myapplication.data.entity.Film;
import com.example.myapplication.data.entity.Media;
import com.example.myapplication.data.entity.Music;
import com.example.myapplication.data.helpers.AlbumHelper;
import com.example.myapplication.data.helpers.AnimeHelper;
import com.example.myapplication.data.helpers.BookHelper;
import com.example.myapplication.data.helpers.FilmHelper;
import com.example.myapplication.data.helpers.MediaHelper;
import com.example.myapplication.data.helpers.MusicHelper;
import com.example.myapplication.data.interfaces.IMediable;

import java.util.List;

public class DBController extends Activity {

    final AlbumHelper q;
    final MediaHelper w;
    final BookHelper e;
    final FilmHelper r;
    final AnimeHelper t;
    final MusicHelper y;

    public DBController(Activity activity){
        this.q = new AlbumHelper(activity);
        this.w = new MediaHelper(activity);
        this.e = new BookHelper(activity);
        this.r = new FilmHelper(activity);
        this.t = new AnimeHelper(activity);
        this.y = new MusicHelper(activity);
    }


    public List<Album> getAllAlbum(String a){
        return q.getAllAlbums(a);
    }

    public void addAlbum(String newColl, String type){
        q.addAlbum(new Album(newColl, type));
    }

    public void deleteAlbum(Album album){
        q.deleteAlbum(album);
    }


    public List<Media> getAllMedia(String a){
        return w.getAllMedia(a);
    }

    public List<Media> findTags(String str){
        return w.findByStr(str);
    }

    public Media findByName(String name, String album){
        return w.findByName(name, album);
    }


    public void updateBook (Media media){
        w.updateMedia(media);
    }

    public <T extends IMediable> void updateMedia (T media){
        if(media instanceof Book){
            e.updateBook((Book)media);
        }if(media instanceof Music){
            y.updateMusic((Music)media);
        }if(media instanceof Anime){
            t.updateAnime((Anime)media);
        }if(media instanceof Film){
            r.updateFilm((Film)media);
        }
    }

    public void deleteMedia(Media media){
        w.deleteMedia(media);
        if (media.getType().equals("Music")){y.deleteMusic(y.getMusic(media.getId()));}
        if (media.getType().equals("Book")){e.deleteBook(e.getBook(media.getId()));}
        if (media.getType().equals("Anime")){t.deleteAnime(t.getAnime(media.getId()));}
        if (media.getType().equals("Film")){r.deleteFilm(r.getFilm(media.getId()));}
    }

    public int addMedia(String name, String type, String album){
        Media media = w.getMedia(w.addMedia(new Media(name, type, album)));
        if (media.getType().equals("Music")){y.addMusic(new Music("", media.getId()));}
        if (media.getType().equals("Book")){e.addBook(new Book("", media.getId()));}
        if (media.getType().equals("Anime")){t.addAnime(new Anime("", media.getId()));}
        if (media.getType().equals("Film")){r.addFilm(new Film("", media.getId()));}
        return media.getId();
    }

    @SuppressWarnings("unchecked")
    public <T extends IMediable> T getMedia1 (T media, int id){

        if(media instanceof Book){
            return (T) e.getBook(id);
        }if(media instanceof Music){
            return (T) y.getMusic(id);
        }if(media instanceof Anime){
            return (T) t.getAnime(id);
        }if(media instanceof Film){
            return (T) r.getFilm(id);
        }
        else{
            return media;
        }
    }

    public Media getMedia (int id){
        return w.getMedia(id);
    }

    
    public Media getLast(){
        return w.getMedia(w.addMedia(new Media("", "", "")));
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
