
package net.impjq.photo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;

import net.impjq.ftclient.BaseActivity;
import net.impjq.ftclient.Utils;
import net.impjq.ftclient.api.BaseAsyncTask.TaskListener;

public class CapturePhoto extends BaseActivity implements TaskListener {
    public static final int REQUEST_CODE_CAPTURE_PHOTO = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        capturePhotoUpload();
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
        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
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
                if (resultCode == RESULT_OK && null != extras) {
                    Bitmap b = (Bitmap) extras.get("data");
                    b = Utils.resizeBitmap(b, 320);

                    ImageView imageView = new ImageView(this);
                    imageView.setImageBitmap(b);
                    setContentView(imageView);
                    uploadPhoto(b);
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
        uploadPhoto.setBitmap(bitmap);
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

}
