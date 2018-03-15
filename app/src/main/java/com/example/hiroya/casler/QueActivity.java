package com.example.hiroya.casler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import io.realm.Realm;

public class QueActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_que);
        String tr = "佐藤先生は娘に嫌われている";
        TextView textView = (TextView)findViewById(R.id.textView2);
        textView.setText(tr);
        Realm realm = Realm.getDefaultInstance();
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Save saved = realm.where(Save.class).equalTo("id","coffie").findFirst();
                if(saved == null){
                    Save save = realm.createObject(Save.class,new String("coffie"));
                    save.setHoge(true);
                }

                //Save save = realm.where(Save.class).equalTo("id",intent.getStringExtra("title")).findFirst();
            }
        });

    }
    public void onClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
