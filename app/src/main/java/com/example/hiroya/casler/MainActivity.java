package com.example.hiroya.casler;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {
    InputStream is = null;
    BufferedReader br = null;
    private Realm mRealm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = new Button(this);
        // ボタンのテキスト
        button.setText("display");
        // ボタンのID
        button.setId(View.generateViewId());
        String text = openFile("title.txt");
        LinearLayout layout = (LinearLayout)findViewById(R.id.linear);
        String[] strs = text.split("\n");
        Drawable btn_color = ResourcesCompat.getDrawable(getResources(), R.drawable.button_background, null);
        Drawable btn_color_ans = ResourcesCompat.getDrawable(getResources(), R.drawable.button_background_ans, null);
        Drawable btn_color_que = ResourcesCompat.getDrawable(getResources(), R.drawable.button_background_que, null);

        LinearLayout.LayoutParams buttonLayoutParams = new LinearLayout.LayoutParams(
                192, 192);
        buttonLayoutParams.setMargins(0,0,100,0);
        mRealm.init(this);
        mRealm = Realm.getDefaultInstance();
        int i = 1;
        for(String str : strs) {
            Button button1 = new Button(this);
            Save save = mRealm.where(Save.class).equalTo("id",str).findFirst();

            if(save != null){
                button1.setBackground(btn_color_ans);
            }else if(i == 1) {
                button1.setBackground(btn_color_que);
                i = 0;
            }else{
                button1.setBackground(btn_color);
                button1.setEnabled(false);
            }
            if(str.equals("coffie")){
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onCoffie(v);
                    }
                });
            }else {
                button1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onQuestionButtonTapped(v);
                    }
                });
            }

            button1.setText(str);
            button1.setLayoutParams(buttonLayoutParams);
            layout.addView(button1);

        }

    }
    public void onQuestionButtonTapped(View view){
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("DATA1", ((Button)view).getText());
        startActivity(intent);
    }

    public void onCoffie(View view){
        Intent intent = new Intent(this, QueActivity.class);
        startActivity(intent);
    }
    public void onReset(View view){
        mRealm.beginTransaction();
        RealmResults<Save> beanList = mRealm.where(Save.class).findAll();

            beanList.deleteAllFromRealm();

        mRealm.commitTransaction();
        Intent intent = getIntent();
        overridePendingTransition(0, 0);
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        finish();

        overridePendingTransition(0, 0);
        startActivity(intent);
    }


    private String openFile(String name){
        String text = "";
        try {
            try {
                // assetsフォルダ内の sample.txt をオープンする
                is = this.getAssets().open("title.txt");
                br = new BufferedReader(new InputStreamReader(is));

                // １行ずつ読み込み、改行を付加する
                String str;
                while ((str = br.readLine()) != null) {
                    text += str + "\n";
                }
            } finally {
                if (is != null) is.close();
                if (br != null) br.close();
            }
        } catch (Exception e){
            // エラー発生時の処理
        }
        return text;
    }
    @Override
    public void onDestroy() {

        super.onDestroy();
        mRealm.close();
    }

}

