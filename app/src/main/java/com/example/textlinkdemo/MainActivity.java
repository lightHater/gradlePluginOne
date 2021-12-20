package com.example.textlinkdemo;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Utils.getInstance(this.getApplicationContext()).setSmt("Activity2");

        try {
            Context context = createPackageContext("android", Context.CONTEXT_IGNORE_SECURITY);
            int resId = context.getResources().getIdentifier("config_cellBroadcastAppLinks", "bool", "android");
            boolean config = context.getResources().getBoolean(resId);

            Log.d("zilong", "config = " + config);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        /*final LayoutInflater inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.activity_main, null);

        setContentView(view);

        Log.d("ziloong", "view = " + view + " " + view.getContext());

        textView = view.findViewById(R.id.text);
        textView.setClickable(true);
        Log.d("ziloong", "textview = " + textView + " " + textView.getContext());

        click = view.findViewById(R.id.button);
        click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("https://baidu.com");
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getApplicationContext().startActivity(intent);

            }
        });


        Button button = view.findViewById(R.id.url);
        button.setText("xiba");
        Button bbt = findViewById(R.id.url);
        Log.d("ziloong", "button = "+ button + " bbt=" + bbt);

        bbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("ziloong", "bbt click");
                textView.setText("bbt click");
            }
        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Utils.magic(textView, "htttps://www.baidu.com");
                Log.d("ziloong", "url onClick");
                textView.setAutoLinkMask(Linkify.WEB_URLS|Linkify.EMAIL_ADDRESSES|Linkify.PHONE_NUMBERS);
                textView.setText("htttps://www.baidu.com");
            }
        });

        Button button1 = view.findViewById(R.id.num);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.magic(textView, "+33640565464");
            }
        });*/
    }
}
