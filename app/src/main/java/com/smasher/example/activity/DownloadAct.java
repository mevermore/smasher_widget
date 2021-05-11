package com.smasher.example.activity;

import android.app.DownloadManager;
import android.database.Cursor;
import android.util.Log;
import android.view.View;

import com.smasher.example.R;
import com.smasher.example.databinding.ActivityDownloadBinding;
import com.smasher.widget.download.DownloadActivity;

/**
 * ClassName:DownloadAct
 * Package:com.smasher.example.activity
 * Description:download example
 *
 * @date:2021/5/11 17:26
 */
public class DownloadAct extends DownloadActivity implements View.OnClickListener {


    private static final String TAG = "DownloadAct";
    ActivityDownloadBinding mBinding;
    private long id;
    String url = "https://download.qidian.com/apknew/source/QDReader.apk";


    @Override
    public View getRootView() {
        mBinding = ActivityDownloadBinding.inflate(getLayoutInflater());
        return mBinding.getRoot();
    }

    @Override
    public int getRootViewRes() {
        return 0;
    }

    @Override
    public void initView() {
        mBinding.button1.setOnClickListener(this);
        mBinding.button2.setOnClickListener(this);
        mBinding.button3.setOnClickListener(this);

    }

    @Override
    public void initData() {

    }


    //检查下载状态
    private void checkStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        //通过下载的id查找
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);
        StringBuilder builder = new StringBuilder(mBinding.textView.getText());
        String state = "";
        if (cursor.moveToFirst()) {
            int status = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                //下载暂停
                case DownloadManager.STATUS_PAUSED:
                    state = "STATUS_PAUSED";
                    break;
                //下载延迟
                case DownloadManager.STATUS_PENDING:
                    state = "STATUS_PENDING";
                    break;
                //正在下载
                case DownloadManager.STATUS_RUNNING:
                    state = "STATUS_RUNNING";
                    break;
                //下载完成
                case DownloadManager.STATUS_SUCCESSFUL:
                    //下载完成
                    Log.d(TAG, "checkStatus: ");
                    state = "STATUS_SUCCESSFUL";
                    cursor.close();
                    break;
                //下载失败
                case DownloadManager.STATUS_FAILED:
                    state = "STATUS_FAILED";
                    cursor.close();
                    break;
                default:
                    break;
            }
        }
        builder.append("\n").append(state);
        mBinding.textView.setText(builder);
    }

    private void checkType() {
        String type = downloadManager.getMimeTypeForDownloadedFile(id);
        StringBuilder builder = new StringBuilder(mBinding.textView.getText());
        builder.append("\n").append(type);
        mBinding.textView.setText(builder);
    }

    @Override
    public void onClick(View v) {
        int vid = v.getId();
        if (vid == R.id.button1) {
            id = download(url, "QDReader.apk");
        } else if (vid == R.id.button2) {
            checkStatus();
        } else if (vid == R.id.button3) {
            checkType();
        }
    }


}
