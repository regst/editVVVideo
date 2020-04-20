package com.kui.wxvideoeditt;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.yanzhenjie.album.Action;
import com.yanzhenjie.album.Album;
import com.yanzhenjie.album.AlbumFile;

import com.zhihu.matisse.Matisse;

import com.ztq.ry.EsayVideoEditActivity;


import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {
    String video;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        video = "/storage/emulated/0/DCIM/pipixia/ac578bd1e04a457b8208cd2e560c41a4.mp4";

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
                            Intent intent = new Intent();
                            intent.putExtra(EsayVideoEditActivity.PATH, result.get(i).getPath());
                            intent.setClass(MainActivity.this, EsayVideoEditActivity.class);
                            startActivity(intent);

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
}
