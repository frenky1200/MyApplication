package com.example.myapplication.api;

import com.example.myapplication.model.TrackSearch;
import com.example.myapplication.modelLyr.LyricsGet;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MusixApi {

    @GET("ws/1.1/track.search?format=json")
    Call<TrackSearch> getData(@Query("q_track") String track, @Query("q_artist") String artist, @Query("quorum_factor") int one, @Query("apikey") String key);

    @GET("ws/1.1/track.lyrics.get?format=json")
    Call<LyricsGet> getLyric(@Query("track_id") int id, @Query("apikey") String key);
}
