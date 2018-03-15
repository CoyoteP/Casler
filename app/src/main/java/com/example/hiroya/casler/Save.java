package com.example.hiroya.casler;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by hiroya on 2017/11/24.
 */

public class Save extends RealmObject {
    @PrimaryKey
    private String id;
    private boolean hoge;

    public void setId(String id){
        this.id = id;
    }
    public  String getId(){
        return this.id;
    }
    public  void setHoge(boolean hoge){
        this.hoge = hoge;
    }
    public boolean getHoge(){
        return  hoge;
    }
}
