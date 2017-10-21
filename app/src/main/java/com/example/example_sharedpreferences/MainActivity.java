package com.example.example_sharedpreferences;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.icu.text.SimpleDateFormat;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.io.File;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE" };

    private final static String SharedPreferencesFileName = "config";
    private final static String Key_UserName = "UserName";
    private final static String Key_LoginData = "LoginData";
    private final static String Key_UserType = "UserType";
    private final static String MyFileName = "myfile.txt";

    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //sharedpreferences
    public void Button_Write(View view){
        preferences = getSharedPreferences(SharedPreferencesFileName,MODE_PRIVATE);
        editor = preferences.edit();
        //格式化日期，将日期按 年月日 时分秒 的形式转化为字符串
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String str_LoginDate = simpleDateFormat.format(new Date());
        //键值对
        editor.putString(Key_UserName, "Chan Lee");
        editor.putString(Key_LoginData, str_LoginDate);
        editor.putInt(Key_UserType, 1);
        //提交修改
        editor.apply();

    }

    public void Button_Read(View view){
        String str_UserName = preferences.getString(Key_UserName,null);
        String str_LoginDate = preferences.getString(Key_LoginData,null);
        int nUserType = preferences.getInt(Key_UserType,0);
        if(str_UserName != null && str_LoginDate != null){
            Toast.makeText(this,"用户名:" + str_UserName + "登录时间:" + str_LoginDate+ "用户类型:"+ nUserType,Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(this,"无数据",Toast.LENGTH_LONG).show();
        }
    }

    //Files
    public void Button_WriteF(View view){
        OutputStream out = null;
        try{
            FileOutputStream fileOutputStream = openFileOutput(MyFileName,MODE_PRIVATE);
            out = new BufferedOutputStream(fileOutputStream);
            String content = "hello world";
            try{
                out.write(content.getBytes(StandardCharsets.UTF_8));
            }
            finally {
                if(out != null){
                    out.close();
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    public void Button_ReadF(View view){
        InputStream in = null;
        try{
            FileInputStream fileInputStream = openFileInput(MyFileName);
            in = new BufferedInputStream(fileInputStream);

            int c;
            StringBuilder stringBuilder = new StringBuilder("");
            try{
                while ((c = in.read()) != -1){
                    stringBuilder.append((char)c);
                }
                Toast.makeText(MainActivity.this,stringBuilder.toString(),Toast.LENGTH_LONG).show();
            }
            finally{
                if(in != null){
                    in.close();
                }
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    //向SD写入文件
    //write
    public void Button_WriteSD(View view){
        OutputStream out_sd = null;

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this, "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File file = Environment.getExternalStorageDirectory();
                File myfile = new File(file.getCanonicalPath() + "/" + MyFileName);
                FileOutputStream fileOutputStream = new FileOutputStream(myfile);
                out_sd = new BufferedOutputStream(fileOutputStream);
                String content = "hello world";
                try{
                    out_sd.write(content.getBytes(StandardCharsets.UTF_8));
                }
                finally {
                    if(out_sd != null){
                        out_sd.close();
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }

    //从SD卡读取read
    public void Button_ReadSD(View view){
        InputStream in = null;

        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(this, "android.permission.READ_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(this, PERMISSIONS_STORAGE,REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        try{
            if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                File file = Environment.getExternalStorageDirectory();
                File myfile = new File(file + "/" + MyFileName);
                FileInputStream fileInputStream = new FileInputStream(myfile);
                in = new BufferedInputStream(fileInputStream);
                int c;
                StringBuilder stringBuilder = new StringBuilder("");
                try{
                    while((c = in.read()) != -1){
                        stringBuilder.append((char)c);
                    }
                    Toast.makeText(this,stringBuilder.toString() , Toast.LENGTH_LONG).show();//
                }
                finally {
                    if(in != null){
                        in.close();
                    }
                }
            }
        }
        catch(Exception e){
            e.printStackTrace();
        }

    }
}

