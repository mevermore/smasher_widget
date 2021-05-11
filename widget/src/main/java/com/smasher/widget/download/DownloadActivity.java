package com.smasher.widget.download;

import android.app.DownloadManager;
import android.app.DownloadManager.Query;
import android.app.DownloadManager.Request;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.smasher.widget.base.BaseActivity;

import java.io.File;

public abstract class DownloadActivity extends BaseActivity {


    protected DownloadManager downloadManager;
    protected long downloadId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }


    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(receiver);
    }

    public long download(String url, String fullName) {
        Uri uri = Uri.parse(url);
        Request request = new Request(uri);
        //设置漫游条件下是否可以下载
        request.setAllowedOverRoaming(false);
        //在通知栏中显示，默认就是显示的
        request.setNotificationVisibility(Request.VISIBILITY_VISIBLE);
        //设置通知标题
        request.setTitle("通知标题，随意修改");
        //设置通知标题message
        request.setDescription("新版zip下载中...");
        request.setVisibleInDownloadsUi(true);


        File files = new File(getExternalFilesDir(null), "Downloads");
        if (!files.exists()) {
            files.mkdir();
        }
        //设置文件存放路径
        File file = new File(files, fullName);
        request.setDestinationUri(Uri.fromFile(file));


        if (downloadManager == null) {
            downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
        }
        //将下载请求加入下载队列，加入下载队列后会给该任务返回一个long型的id，通过该id可以取消任务，重启任务、获取下载的文件等等
        if (downloadManager != null) {
            return downloadId = downloadManager.enqueue(request);

        }
        return 0;
    }


    //广播监听下载的各个状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkStatus();
        }
    };


    //检查下载状态
    private void checkStatus() {
        Query query = new Query();
        //通过下载的id查找
        query.setFilterById(downloadId);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成
                    cursor.close();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    cursor.close();
                    break;
                default:
                    break;
            }
        }
    }
}