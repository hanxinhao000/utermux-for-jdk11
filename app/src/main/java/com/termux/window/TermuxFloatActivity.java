package com.termux.window;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import androidx.annotation.Nullable;

/**
 * Simple activity which immediately launches {@link TermuxFloatService} and exits.
 */
public class TermuxFloatActivity extends Activity {

    private static final String RELOAD_STYLE_ACTION = "com.termux.app.for_jdk";


    private TextView getb_tv;
    private TextView loading_tv;
    private LinearLayout loading;
    private ProgressBar progress;
    private ImageView image;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity);
        getb_tv = findViewById(R.id.getb_tv);
        loading = findViewById(R.id.loading);
        loading_tv = findViewById(R.id.loading_tv);
        progress = findViewById(R.id.progress);
        image = findViewById(R.id.image);
        image.setVisibility(View.GONE);


        getb_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startService(new Intent(TermuxFloatActivity.this, TermuxFloatService.class));
                getb_tv.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                startInstall();

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        startService(new Intent(TermuxFloatActivity.this, TermuxFloatService.class));
       // finish();
    }

    private void startInstall(){


        loading_tv.setText("开始安装");

        new Thread(new Runnable() {
            @Override
            public void run() {
                File file = new File(TermuxFloatService.FILES_PATH + "/usr/share/jdk11/");
                if(!(file.exists())){
                    boolean mkdirs = file.mkdirs();
                    if(!mkdirs){
                        setText("目录不可写! " + file.getAbsolutePath());
                        return;
                    }
                }

                writerFile("run_jdk",new File(TermuxFloatService.FILES_PATH + "/usr/share/jdk11/run_jdk"),4096);

                TermuxFloatView.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd .. \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd usr \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd share \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd jdk11 \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("chmod 777 run_jdk\n");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }



                writerFile("openjdk11",new File(TermuxFloatService.FILES_PATH + "/usr/share/jdk11/openjdk-11.0.1.tar.xz"),4096);


                setText("文件写入完成!开始扩展");

                TermuxFloatView.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd ~ \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd .. \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd usr \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd share \n");
                TermuxFloatView.mTerminalView.sendTextToTerminal("cd jdk11 \n");
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                TermuxFloatView.mTerminalView.sendTextToTerminal("./run_jdk\n");

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                setText("扩展文件中,文件扩展较慢，请耐心等待...\n请勿切出(关闭)本程序,耐心等待完成!");

                runWindowsLog();

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }


            }
        }).start();


        // TermuxFloatView.mTerminalView.sendTextToTerminal("");
    }


    @Override
    protected void onStart() {
        super.onStart();
        isRun = true;
        registerReceiver(mBroadcastReceiever, new IntentFilter(RELOAD_STYLE_ACTION));
    }


    @Override
    protected void onStop() {
        super.onStop();
        isRun = false;
        unregisterReceiver(mBroadcastReceiever);

    }

    public void writerFile(String name, File mFile, int size) {

        try {
            InputStream open = getAssets().open(name);

            int len = 0;

            byte[] b = new byte[size];

            int temp = 1;
            if (!mFile.exists()) {
                mFile.createNewFile();
            }



            FileOutputStream fileOutputStream = new FileOutputStream(mFile);

            while ((len = open.read(b)) != -1) {
                temp +=size;

                setText("文件写入:" + (temp / 1024/1024) + " MB \n 231 MB");
                fileOutputStream.write(b, 0, len);
            }

            fileOutputStream.flush();
            open.close();
            fileOutputStream.close();
        } catch (Exception e) {
            setText("错误! " + e.toString());
        }

    }

    public void setText(String msg){

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                loading_tv.setText(msg);
            }
        });


    }


    private final BroadcastReceiver mBroadcastReceiever = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            setText("文件扩展完成...");
           // Toast.makeText(context, "文件扩展完成", Toast.LENGTH_SHORT).show();

        }
    };
    private boolean isRun = true;
    private void runWindowsLog(){


        new Thread(new Runnable() {
            @Override
            public void run() {

               while (isRun){

                   try {
                       Thread.sleep(100);
                   } catch (InterruptedException e) {
                       e.printStackTrace();
                   }

                   CharSequence text1 = TermuxFloatView.mTerminalView.getText555();



                   String s = text1.toString();

                   String[] split = s.split("\n");

                   try {
                       String s4 = split[split.length - 4];
                       String s3 = split[split.length - 3];
                       String s2 = split[split.length - 2];
                       String s1 = split[split.length - 1];


                       setText(s1);

                       if(s2.contains("F56D32FEF35RHT5IF2VB2X1")){
                           isRun = false;
                           setText("文件扩展完成...");
                           Thread.sleep(1000);
                           setText("正在加入启动项...");

                           ///data/data/com.termux/files/usr/etc/bash.bashrc
                           File file = new File(TermuxFloatService.FILES_PATH + "/usr/etc/bash.bashrc");

                           BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file)));

                           String txt = "";

                           String temp = "";

                           while ((temp = bufferedReader.readLine()) != null) {
                               txt = txt + temp + "\n";
                           }
                           bufferedReader.close();

                           txt+="\n";
                           txt+="export PATH=$PATH:/data/data/com.termux/files/usr/share/jdk11/openjdk-11.0.1/bin/\n";
                           txt+="export JAVA_HOME=/data/data/com.termux/files/usr/share/jdk11/openjdk-11.0.1/\n";


                           PrintWriter printWriter = new PrintWriter(new OutputStreamWriter(new FileOutputStream(file)));
                           printWriter.print(txt);
                           printWriter.flush();
                           printWriter.close();


                           Thread.sleep(1000);
                          runOnUiThread(new Runnable() {
                              @Override
                              public void run() {
                                  progress.setVisibility(View.GONE);
                                  image.setVisibility(View.VISIBLE);
                                  TermuxFloatView.mTerminalView.sendTextToTerminal("exit \n");
                              }
                          });

                           setText("完成!请重启UTermux之后输入java看看效果吧");
                       }

                       Log.e("XINHAO_HAN", "\n" + s4 + "\n" + s3 + "\n" + s2 + "\n" + s1);



                   }catch (Exception e){
                       e.printStackTrace();
                   }


               }


            }
        }).start();

    }
}
