package com.example.myapplication.data.interfaces;

import java.util.List;

public interface IHelper {
   <T extends IMediable> T getById (int id);
   <T extends IMediable> void add (T t);
   <T extends IMediable> T get (int id);
   <T extends IMediable> List<T> getAll (String type);
   <T extends IMediable> int update (T t);
   <T extends IMediable> void delete (T t);
   <T extends IMediable> List<T> findbystr (String str);
}
