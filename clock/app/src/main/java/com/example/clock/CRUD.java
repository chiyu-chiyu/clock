package com.example.clock;



import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CRUD extends AppCompatActivity {

    private String msg;

    private  Button add_time;
    private StringBuilder sb;
    private int count;
    private Button btn_back; //返回按钮
    private Button btn_finish;//完成按钮
    private Button btn_clear;//清楚所有数据按钮
    private TextView now_time;//当前时间
    private EditText data_information;//存储数据
    //与数据库操作相关的成员变量
    private com.example.clock.MyDbHelper myDbHelper;//数据库帮助类
    private SQLiteDatabase db;//数据库类
    private ContentValues values;//数据表的一些参数
    private static final String mTablename = "mymemo";//数据表的名称
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //建立联系
        btn_back = findViewById(R.id.btn_back);
        data_information = findViewById(R.id.data_information);
        now_time = findViewById(R.id.now_time);
        btn_finish = findViewById(R.id.btn_finish);
        btn_clear = findViewById(R.id.btn_clear);
        final Calendar c = Calendar.getInstance();
        c.setTimeInMillis(System.currentTimeMillis());//设置当前时间为c的时间
        final int hour = c.get(Calendar.HOUR_OF_DAY);
        final int minute = c.get(Calendar.MINUTE);
        add_time=findViewById(R.id.add_time);
        add_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                sb.append(count++);
//                sb.append("备忘录的内容为:"+data_information);
//                sb.append(msg);
//                sb.append("\n");
                new TimePickerDialog(CRUD.this, new TimePickerDialog.OnTimeSetListener(){
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        c.setTimeInMillis(System.currentTimeMillis());
                        c.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        c.set(Calendar.MINUTE, minute);
                        c.set(Calendar.SECOND, 0);
                        c.set(Calendar.MILLISECOND, 0);
                        AlarmManager am = (AlarmManager) CRUD.this.getSystemService(Context.ALARM_SERVICE);
                        Intent intent = new Intent(CRUD.this,AlarmReceive.class);
                        PendingIntent pi = PendingIntent.getBroadcast(CRUD.this, 0, intent, 0);
                        am.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
                        String tempHour = (hour+"").length()>1?hour+"":"0"+hour;
                        String tempMinute = (minute+"").length()>1?minute+"":"0"+minute;
                        Toast.makeText(CRUD.this, "设置时间为:"+tempHour+":"+tempMinute, Toast.LENGTH_SHORT).show();
                    }
                },
                        hour,
                        minute,
                        true).show();
            }
        });
//        sb = new StringBuilder();
//        Bundle bundle = this.getIntent().getExtras();
//        if(bundle != null){
//            new AlertDialog.Builder(CRUD.this)
//                    .setIcon(null)
//                    .setTitle("温馨提示")
//                    .setMessage("备忘录时间到了,请注意")
//                    .setPositiveButton("关闭", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                           CRUD.this.finish();
//                        }
//                    })
//                    .create().show();
//        }
        //初始化数据库工具类实例
        myDbHelper  = new MyDbHelper(this);
        //获取手机当前时间
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        final String str_time = simpleDateFormat.format(date);
        now_time.setText(str_time);
        //返回主界面
        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(com.example.clock.CRUD.this,MainActivity.class);
                startActivity(intent);
                queryData();
            }
        });
        //完成并执行添加操作
        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String input = data_information.getText().toString().trim();
                if (input.isEmpty()){
                    showMsg("请输入内容");
                }else {
                    addData(str_time.trim(),input);
                    finish();
                    queryData();
                }
            }
        });

        btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearData();
                showMsg("清理完成");
            }
        });

    }
    //添加数据方法
    private void addData(String now_time,String data_information){
        db = myDbHelper.getWritableDatabase();
        values = new ContentValues();
        values.put("now_time",now_time);
        values.put("information",data_information);
        db.insert(mTablename,null,values);
        showMsg("添加成功");
    }
    //清空所有数据的方法
    private void clearData(){
        db = myDbHelper.getWritableDatabase();
        db.delete(mTablename,null,null);
        String srtResult = "";
        Intent intent = new Intent(com.example.clock.CRUD.this,MainActivity.class);
        intent.putExtra("data",srtResult);
        startActivity(intent);
        showMsg("已经清空");
    }
    //显示消息方法
    private void showMsg(String msg){
        Toast.makeText(this,msg, Toast.LENGTH_SHORT).show();
    }
    //查询方法
    private void queryData(){
        db = myDbHelper.getReadableDatabase();
        Cursor cursor = db.query(mTablename,null,null,null,
                null,null,null);
        String srtResult = "";

        while (cursor.moveToNext()){
            srtResult += "\n" + cursor.getString(1);
            srtResult += "\n计划是：" + cursor.getString(2);
            srtResult += "\n";
        }
        Intent intent = new Intent(com.example.clock.CRUD.this,MainActivity.class);
        intent.putExtra("data",srtResult);
        startActivity(intent);
    }
}
