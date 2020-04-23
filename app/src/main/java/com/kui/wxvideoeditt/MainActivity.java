package com.kui.wxvideoeditt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.esay.ffmtool.FfmpegTool;
import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import com.yanzhenjie.album.util.AlbumUtils;
import com.zhihu.matisse.Matisse;

import com.ztq.ry.EsayVideoEditActivity;


import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;


public class MainActivity extends AppCompatActivity {
    String video;
    TextView tv_lasd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_lasd = findViewById(R.id.tv_lasd);
        video = "/storage/emulated/0/DCIM/pipixia/ac578bd1e04a457b8208cd2e560c41a4.mp4";
        myHandler = new MyHandler(this);
        Message message = new Message();
        message.what = 1;
        message.obj = "asdljk;f";
        myHandler.sendMessage(message);


    }


    public void click(View view) {
//        Intent intent=new Intent();
//        intent.putExtra(EsayVideoEditActivity.PATH,video);
//        intent.setClass(this,EsayVideoEditActivity.class);
//        startActivity(intent);
//        Matisse.from(this)
//                .choose(MimeType.ofVideo())
//                .theme(R.style.Matisse_Dracula)
//                .countable(false)
//                .addFilter(new GifSizeFilter(320, 320, 5 * Filter.K * Filter.K))
//                .maxSelectable(9)
//                .originalEnable(true)
//                .maxOriginalSize(10)
//                .imageEngine(new PicassoEngine())
//                .forResult(23);

        Album.video(this)
                .multipleChoice()
                .columnCount(2)
                .selectCount(6)
                .camera(true)
                .onResult(new Action<ArrayList<AlbumFile>>() {
                    @Override
                    public void onAction(@NonNull ArrayList<AlbumFile> result) {
                        for (int i = 0; i < result.size(); i++) {
                            Log.e("result", "getBucketName=" + result.get(i).getBucketName());
                            Log.e("result", "getMimeType=" + result.get(i).getMimeType());
                            Log.e("result", "getPath=" + result.get(i).getPath());
                            Log.e("result", "getThumbPath=" + result.get(i).getThumbPath());
                            Log.e("result", "getAddDate=" + result.get(i).getAddDate());
                            Log.e("result", "getDuration=" + result.get(i).getDuration());
                            Log.e("result", "getLatitude=" + result.get(i).getLatitude());
                            Log.e("result", "getLongitude=" + result.get(i).getLongitude());
                            Log.e("result", "getMediaType=" + result.get(i).getMediaType());
                            Log.e("result", "getSize=" + result.get(i).getSize());

                            if (result.get(i).getDuration() / 1000 > 60) {
                                Intent intent = new Intent();
                                intent.putExtra(EsayVideoEditActivity.PATH, result.get(i).getPath());
                                intent.setClass(MainActivity.this, EsayVideoEditActivity.class);
                                startActivity(intent);
                            } else {
                                final String string = result.get(i).getPath();
                                ExecutorService executorService = Executors.newFixedThreadPool(1);
                                executorService.execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        String path = Environment.getExternalStorageDirectory().getAbsolutePath()
                                                + File.separator + "ztq" + File.separator + "compress";
                                        File file1 = new File(path);
                                        if (!file1.exists()) {
                                            file1.mkdirs();
                                        }
                                        FfmpegTool ffmpegTool = FfmpegTool.getInstance(MainActivity.this);
                                        ffmpegTool.compressVideo(string, path + File.separator, 3, new FfmpegTool.VideoResult() {
                                            @Override
                                            public void clipResult(int i, String s, String s1, boolean b, int i1) {
                                                String result = "压缩完成";
                                                if (!b) {
                                                    result = "压缩失败";
                                                }
                                                Log.i("click2", "s:" + s);//压缩前的视频
                                                Log.i("click2", "s1:" + s1);//压缩后的视频
                                                Toast.makeText(MainActivity.this, result + "sdafasfa", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                    }
                                });

                            }


                        }

//                        mAlbumFiles = result;
//                        mAdapter.notifyDataSetChanged(mAlbumFiles);
//                        mTvMessage.setVisibility(result.size() > 0 ? View.VISIBLE : View.GONE);
                    }
                })
                .onCancel(new Action<String>() {
                    @Override
                    public void onAction(@NonNull String result) {

                    }
                })
                .start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 23 && resultCode == RESULT_OK) {
            //   mAdapter.setData(Matisse.obtainResult(data), Matisse.obtainPathResult(data));
            Log.e("OnActivityResult ", String.valueOf(Matisse.obtainOriginalState(data)));
            Intent intent = new Intent(this, EsayVideoEditActivity.class);
            intent.putExtra(EsayVideoEditActivity.PATH, Matisse.obtainPathResult(data).get(0));
            startActivity(intent);
        }
    }

    private MyHandler myHandler;

    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> weakReference;

        public MyHandler(MainActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Log.e("---------", "---------" + msg.obj);
                    weakReference.get().tv_lasd.setText(msg.obj + "");
                    break;
            }
            super.handleMessage(msg);

        }
    }
}
