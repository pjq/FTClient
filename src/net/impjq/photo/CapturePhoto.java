
package net.impjq.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import net.impjq.ftclient.BaseActivity;
import net.impjq.ftclient.FTPreference;
import net.impjq.ftclient.R;
import net.impjq.ftclient.Utils;
import net.impjq.ftclient.api.BaseAsyncTask.TaskListener;

public class CapturePhoto extends BaseActivity implements TaskListener, OnClickListener {
    public static final int REQUEST_CODE_CAPTURE_PHOTO = 10;

    private Button mCaptureButton;
    private ImageView mPreviewImageView;
    private Button mUploadPhotoButton;
    private Bitmap mBitmap;

    private String mFilePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.upload_photo);

        init();

        // capturePhotoUpload();
        capturePhotoLocal();
    }

    private void init() {
        mCaptureButton = (Button) findViewById(R.id.upload_photo_capture_photo_button);
        mPreviewImageView = (ImageView) findViewById(R.id.upload_photo_preview_imageview);
        mUploadPhotoButton = (Button) findViewById(R.id.upload_photo_upload_button);

        mCaptureButton.setOnClickListener(this);
        mUploadPhotoButton.setOnClickListener(this);
    }

    private void capturePhotoLocal() {
        // TODO Auto-generated method stub

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        String photoPath = Environment.getExternalStorageDirectory().toString()
                + "/FTClient";
        String photoName = Utils.createPhotoName();
        File out = new File(photoPath);
        if (!out.exists()) {
            out.mkdirs();
        }

        out = new File(photoPath, photoName);

        photoPath = photoPath + "/" + photoName;
        mFilePath = photoPath;
        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 100*1024);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_PHOTO);
    }

    private void capturePhotoUpload() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CODE_CAPTURE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, data);

        Bundle extras = null;
        if (null != data) {
            extras = data.getExtras();
        }

        switch (requestCode) {
            case REQUEST_CODE_CAPTURE_PHOTO: {
                if (resultCode == RESULT_OK && null != extras) {// Has extras
                    Bitmap b = (Bitmap) extras.get("data");
                    b = Utils.resizeBitmap(b, 320);
                    mBitmap = b;

                    mPreviewImageView.setImageBitmap(b);
                } else if (resultCode == RESULT_OK) {// No extras,if store in
                    // the local.

                    try {
                        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(mFilePath));

                        mPreviewImageView.setImageBitmap(b);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }

                break;
            }

            default:
                break;
        }
    }

    private void uploadPhoto(Bitmap bitmap) {
        // TODO Auto-generated method stub
        UploadPhoto uploadPhoto = new UploadPhoto();
        // uploadPhoto.setBitmap(bitmap);
        String serverUrl = FTPreference.getInstance(getApplicationContext()).getServerUrl();
        UploadPhoto.setServerUrl(serverUrl);
        uploadPhoto.setFilePath(mFilePath);
        runBaseTask(uploadPhoto, this);
    }

    @Override
    public void onTaskStart(Object task) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onTaskProgressUpdate(Object task, Integer... values) {
        // TODO Auto-generated method stub

    }

    @Override
    public void OnTaskFinished(Object task) {
        // TODO Auto-generated method stub

    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        int id = v.getId();

        switch (id) {
            case R.id.upload_photo_capture_photo_button: {
                capturePhotoLocal();
                break;
            }

            case R.id.upload_photo_upload_button: {
                uploadPhoto(mBitmap);
                break;
            }

            default:
                break;
        }
    }

}
