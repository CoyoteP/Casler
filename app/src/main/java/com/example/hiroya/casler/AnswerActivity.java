package com.example.hiroya.casler;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.realm.Realm;
import pycasl2.PyCasl2;
import pycomet2.PyComet2;

public class AnswerActivity extends AppCompatActivity {
    InputStream is = null;
    BufferedReader br = null;
    TableRow row;
    TableRow.LayoutParams row_layout_params;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
        Intent intent = getIntent();
        String data1 = intent.getStringExtra("DATA1");
        Log.i("data1", data1);
        TableLayout layout1 = (TableLayout) findViewById(R.id.layout1);
        table(data1, layout1);
        TableLayout layout2 = (TableLayout) findViewById(R.id.layout2);
        String title = intent.getStringExtra("title");
        String ans = openFile(title);

        Log.d("teensfuck",ans);
      table(ans,layout2);
        Button button = (Button)findViewById(R.id.button);
        if(data1.equals(ans)){
            button.setText("success!");
            Realm realm = Realm.getDefaultInstance();
            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    Save saved = realm.where(Save.class).equalTo("id",title).findFirst();
                    if(saved == null){
                        Save save = realm.createObject(Save.class,new String(title));
                        save.setHoge(true);
                    }

                    //Save save = realm.where(Save.class).equalTo("id",intent.getStringExtra("title")).findFirst();
                }
            });
        }else{
            button.setText("failed...");
        }
        //try {
        //    test();
        //} catch (IOException e) {
        //    e.printStackTrace();
        //}

    }

    private String openFile(String name) {
        String text = "";
        try {
            try {
                // assetsフォルダ内の sample.txt をオープンする
                is = this.getAssets().open(name + " - Copy.txt");
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
        } catch (Exception e) {
            // エラー発生時の処理
        }
        return text;

    }

    private void table(String text, TableLayout layout) {
        TableRow.LayoutParams row_layout_params = new TableRow.LayoutParams(-2, -2);
        String[] strs = text.split("\n");
        for (String str : strs) {
            row = new TableRow(this);
            row.setLayoutParams(row_layout_params);
            String[] words = str.split(":");

            for (String word : words) {
                textView = new TextView(this);
                textView.setText(word + " ");
                textView.setTextSize(18);
                row.addView(textView);

            }
            layout.addView(row);

        }
    }

    private void test() throws IOException {
        //TextView textView = (TextView)findViewById(R.id.textView);
        String path = "/data/data/Casler/files/Q.cas";
        FileOutputStream fos = openFileOutput("Q.cas", Context.MODE_PRIVATE);
        String str = "CASL\tSTART\n" +
                "LAD\tGR1,\t2\n" +
                "LAD\tGR2,\t1\n" +
                "ADDA\tGR1,\tGR2\n" +
                "REN\n" +
                "END";
        fos.write(str.getBytes());
        fos.close();

       // FileOutputStream fileout  = openFileOutput("Q.cas", Context.MODE_PRIVATE);
   // fileout.write(str.getBytes());
       // fileout.close();


        Log.d("test",path);
                PyCasl2.main(new String[]{path});
        PyComet2.main(new String[]{"-d","Q.com"});


        FileInputStream fileInputStream = openFileInput("last_state.txt");

        textView.setText(fileInputStream.toString());

    }
    public void onClick(View view){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}

