package com.example.hiroya.casler;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class QuestionActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editText;
    InputStream is = null;
    BufferedReader br = null;
    EditText save = null;
    String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);
        //ActionBar actionBar = getActionBar();
        //actionBar.hide();
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        Intent intent = getIntent();
        title = intent.getStringExtra("DATA1");
        String text = openFile(title);
        String[] strs = text.split("\n");
        TableLayout table = (TableLayout) findViewById(R.id.table);
        TableRow row;
        TableRow.LayoutParams row_layout_params = new TableRow.LayoutParams(-2, -2);
        //row = new TableRow(this);
        //TextView start = new TextView(this);
        //start.setText("CASL");
        //start.setTextSize(23);
        //row.addView(start);
        //TextView name = new TextView(this);
        //name.setText("START");
        //name.setTextSize(23);
        //row.addView(name);
       // table.addView(row);
        for(String str : strs) {
            row = new TableRow(this);
            row.setLayoutParams(row_layout_params);
            String[] words = str.split(":");

            for (String word : words) {
                if (word.equals("◆◆")) {
                    EditText editText = new EditText(this);
                    editText.setKeyListener(null);
                    editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                        @Override
                        public void onFocusChange(View v, boolean hasFocus) {
                            if (hasFocus) {
                                save = (EditText) v;
                            } else {
                                //離れた時
                            }
                        }
                    });
                    row.addView(editText);
                }else if (word.equals("●●")) {
                    EditText editText = new EditText(this);
                    editText.setTextSize(15);
                    row.addView(editText);
                } else {
                    TextView textView = new TextView(this);
                    textView.setText(word);
                    textView.setTextSize(14);
                    row.addView(textView);
                }

            }
            table.addView(row);
        }
        try {
            reg(title);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void reg(String title) throws IOException {
        TabHost tabhost = (TabHost)findViewById(R.id.tabHost);
        tabhost.setup();

        TabHost.TabSpec tab1 = tabhost.newTabSpec("tab1");
        tab1.setIndicator("問題");
        tab1.setContent(R.id.scrollView);
        tabhost.addTab(tab1);

        TabHost.TabSpec tab2 = tabhost.newTabSpec("tab2");
        tab2.setIndicator("レジスタ");
        tab2.setContent(R.id.reg);
        tabhost.addTab(tab2);
        tabhost.setCurrentTab(0);
        TextView textView = (TextView)findViewById(R.id.textView2);
        if(Integer.parseInt(title) <= 8){
            is = this.getAssets().open("aM.txt");
            br = new BufferedReader(new InputStreamReader(is));
            String txt = "";
            String line = br.readLine();
            while (line != null) {
                txt = txt + line + "\n";
                line = br.readLine();
            }
            textView.setText(txt);
            is = this.getAssets().open("toi_1-8.txt");
            br = new BufferedReader(new InputStreamReader(is));
            setTitle(br.readLine() );
        }else if(Integer.parseInt(title) <= 10){
            is = this.getAssets().open("bM.txt");
            br = new BufferedReader(new InputStreamReader(is));
            String txt = "";
            String line = br.readLine();
            while (line != null) {
                txt = txt + line + "\n";
                line = br.readLine();
            }
            textView.setText(txt);
            is = this.getAssets().open("toi_9-10.txt");
            br = new BufferedReader(new InputStreamReader(is));
            setTitle(br.readLine());
        }else if((Integer.parseInt(title) <= 11)){
            is = this.getAssets().open("toi_11.txt");
            br = new BufferedReader(new InputStreamReader(is));
            textView.setText("レジスタはありません");
            setTitle(br.readLine());
        }else{
            is = this.getAssets().open("toi12.txt");
            br = new BufferedReader(new InputStreamReader(is));
            textView.setText("レジスタはありません");
            setTitle(br.readLine());
        }


    }


    public void onLeftButtonTapped(View view){
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("DATA1", ((Button)view).getText());
        startActivity(intent);
    }
    public void onRightButtonTapped(View view){
        Intent intent = new Intent(this, QuestionActivity.class);
        intent.putExtra("DATA1", ((Button)view).getText());
        startActivity(intent);
    }
    private String openFile(String name){
        String text = "";
        try {
            try {
                // assetsフォルダ内の sample.txt をオープンする
                is = this.getAssets().open(name + ".txt");
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
    public void onClick(View v) {
        // TODO Auto-generated method stub
        if(save != null) {
            save.setText(((Button) v).getText().toString().toUpperCase());
            save.setTextSize(15);
        }
    }

    public void onAnsTapped(View v) {
        String text = "";
        TableLayout layout = (TableLayout) findViewById(R.id.table);
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);

            if (child instanceof TableRow) {
                TableRow row = (TableRow) child;

                for (int x = 0; x < row.getChildCount(); x++) {

                    View view = row.getChildAt(x);
                    if(view.getClass().getName().equals("android.widget.EditText")){
                        EditText edit = (EditText)view;
                        text = text + edit.getText() + ":";
                    }else if(view.getClass().getName().equals("android.widget.TextView")){
                        TextView textView = (TextView)view;
                        text = text + textView.getText() + ":";
                    }
                }
                text = text.substring(0,text.length()-1);

                text = text + "\n";
            }
        }
        Intent intent = new Intent(this, AnswerActivity.class);
        Log.i("data1",text);
        intent.putExtra("DATA1",text);
        intent.putExtra("title",title);
        startActivity(intent);
    }


        @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        // TODO Auto-generated method stub
        Log.v("KeyEvent", "KeyCode = " + event.getKeyCode());
        return super.dispatchKeyEvent(event);
        }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        return false;
    }

}
