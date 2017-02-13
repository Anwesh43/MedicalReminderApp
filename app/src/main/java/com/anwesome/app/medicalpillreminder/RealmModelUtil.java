package com.anwesome.app.medicalpillreminder;

import android.content.Context;
import com.anwesome.app.medicalpillreminder.models.Pill;
import com.anwesome.app.medicalpillreminder.schemas.PillModule;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

/**
 * Created by anweshmishra on 12/02/17.
 */
public class RealmModelUtil {
    Realm realm;
    public RealmModelUtil(Context context) {
        Realm.init(context);
        realm = Realm.getInstance(new RealmConfiguration.Builder().name("pillsdb.realm").modules(new PillModule()).build());
    }
    public void changePillNumber(int id,int dir) {
        realm.beginTransaction();
        Pill pill = realm.where(Pill.class).equalTo("id",id).findFirst();
        if(pill!=null) {
            pill.changePill(dir);
        }
        realm.commitTransaction();

    }
    public RealmResults<Pill> getPills() {
        realm.beginTransaction();
        RealmResults<Pill> pillResults = realm.where(Pill.class).findAll();
        realm.commitTransaction();
        return pillResults;
    }
    public void createPill(final String name,final int numberOfPills) {
        final Class<Pill> pillClass = Pill.class;
        realm.beginTransaction();
        RealmResults<Pill> results = realm.where(pillClass).findAll();
        final int newId = results.size()+1;
        realm.commitTransaction();
        realm.beginTransaction();
        Pill pill = realm.createObject(Pill.class);
        pill.setId(newId);
        pill.setName(name);
        pill.setPillsNumber(numberOfPills);
        realm.commitTransaction();
    }
}
