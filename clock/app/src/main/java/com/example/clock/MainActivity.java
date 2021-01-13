package com.example.clock;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    private String msg;
    private Button btn_add;
    private TextView data;
    private Button btn_select;
    private EditText view_select;
    private  Button add_time;
    private StringBuilder sb;
    private int count;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_time=findViewById(R.id.add_time);                                           //均是和点击按钮建立关联
        btn_add=findViewById(R.id.btn_add);
        btn_select=findViewById(R.id.btn_select);
        view_select=findViewById(R.id.view_select);
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(MainActivity.this, CRUD.class);
                startActivity(intent);
            }
        });

        data=findViewById(R.id.data);
        Intent i = getIntent();
        String get_data = i.getStringExtra("data");
        data.setText(get_data);
        //查询监听
        btn_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = view_select.getText().toString().trim();    //得到的是事件的文本文档
                String get = data.getText().toString().trim();              //得到的是事件的时间
                //判断查询信息输入是否为空，并返回值
                if (!input.isEmpty()) {
                    int a = get.indexOf(input); //内容第一次出现的位置
                    //判断查询内容是否存在
                    if ( a != -1 ){
                        String front = get.substring(0,a+1);    //截取字符串前面的东西
                        String after = get.substring(a+1,get.length()); //截取字符串后面的东西
                        int a1 = front.lastIndexOf("2021"); //前一段数据最后一次出现的位置
                        int b1 = after.indexOf("2021"); //后一段数据第一次出现的位置
                        String front2 ;
                        String after2 ;
                        //防止查询数据为第一位，出现错误
                        if (a1 == -1) {
                            front2 = front;
                        }else{
                            String front1 = front.substring(a1, front.length());
                            front2 = front1;
                        }
                        //防止查询数据为最后一位,出现错误
                        if (b1 == -1){
                            after2 = after;
                        }else {
                            String after1 = after.substring(0,b1);
                            after2 = after1;
                        }
                        showMsg(front2 + after2 );//显示查询结果信息
                    }else {
                        showMsg("无匹配数据");
                    }
                }else{
                    showMsg("请输入查询的数据");
                }
            }

        });

    }
    //显示提示消息
    private void showMsg(String msg){
        Toast.makeText(this,msg,Toast.LENGTH_SHORT).show();
    }
}

