
package net.impjq.photo;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import net.impjq.ftclient.BaseActivity;
import net.impjq.ftclient.FTPreference;
import net.impjq.ftclient.R;
import net.impjq.ftclient.UpdateStatusActivity;
import net.impjq.ftclient.Utils;
import net.impjq.ftclient.api.BaseTask;
import net.impjq.ftclient.api.BaseAsyncTask.TaskListener;

public class CapturePhoto extends BaseActivity implements TaskListener,
        OnClickListener {

    public static final String REQUEST_EXTRAL_URL = "url";

    /**
     * The Bitmap compress rate:1...100.
     */
    public static final int BITMAP_COMPRESS_RATE = 20;

    private Button mCaptureButton;
    private ImageView mPreviewImageView;
    private Button mUploadPhotoButton;
    private Button mChooseFromLocalButton;
    private ProgressBar mUploadProgressBar;
    private TextView mPhotoInfoTextView;

    private Bitmap mBitmap;

    private String mSdcardStorePath;
    private String mPhotoFilePath;

    private String mResultUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.upload_photo);

        init();

        capturePhotoUpload();
        // capturePhotoToLocal();
    }

    private void init() {
        mCaptureButton = (Button) findViewById(R.id.upload_photo_capture_photo_button);
        mPreviewImageView = (ImageView) findViewById(R.id.upload_photo_preview_imageview);
        mUploadPhotoButton = (Button) findViewById(R.id.upload_photo_upload_button);
        mChooseFromLocalButton = (Button) findViewById(R.id.upload_photo_choose_from_local_button);
        mUploadProgressBar = (ProgressBar) findViewById(R.id.upload_photo_progressbar);
        mPhotoInfoTextView = (TextView) findViewById(R.id.upload_photo_info_textview);

        mCaptureButton.setOnClickListener(this);
        mUploadPhotoButton.setOnClickListener(this);
        mChooseFromLocalButton.setOnClickListener(this);

        mSdcardStorePath = Environment.getExternalStorageDirectory().toString()
                + "/FTClient";
    }

    private File preparePath() {
        String photoName = Utils.createPhotoName();
        File out = new File(mSdcardStorePath);
        if (!out.exists()) {
            out.mkdirs();
        }

        out = new File(mSdcardStorePath, photoName);
        mPhotoFilePath = mSdcardStorePath + "/" + photoName;
        return out;
    }

    /**
     * It will store the Photo.
     */
    protected void capturePhotoToLocal() {
        // TODO Auto-generated method stub

        File out = preparePath();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri uri = Uri.fromFile(out);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        intent.putExtra(MediaStore.EXTRA_SIZE_LIMIT, 1024);
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);
        startActivityForResult(intent, UpdateStatusActivity.REQUEST_CODE_CAPTURE_PHOTO);
    }

    protected void capturePhotoUpload() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, UpdateStatusActivity.REQUEST_CODE_CAPTURE_PHOTO);
    }

    protected boolean storePhoto(Bitmap bitmap, String filePath) {
        boolean result = false;
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(filePath);
            result = mBitmap.compress(CompressFormat.PNG,
                    BITMAP_COMPRESS_RATE, fileOutputStream);
        } catch (Exception e) {
            // TODO: handle exception
        }

        return result;
    }

    // private Bitmap resizeBitmap(Bitmap bitmap, int w, int h) {
    // int width = bitmap.getWidth();
    // int height = bitmap.getHeight();
    //
    // float scaleWidth = (float) w / width;
    // float scaleHeight = (float) h / height;
    //
    // Matrix matrix = new Matrix();
    // matrix.postScale(scaleWidth, scaleHeight);
    // Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height,
    // matrix, false);
    // return resizedBitmap;
    // }

    /**
     * Store the current captured photo.
     * 
     * @return
     */
    protected boolean storePhoto() {
        boolean result = false;
        File out = preparePath();
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(out);
            result = mBitmap.compress(CompressFormat.PNG,
                    BITMAP_COMPRESS_RATE, fileOutputStream);

            updatePhotoInfo(out);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return result;
    }

    private void updatePhotoInfo(File file) {
        long size = file.length();
        String info = "size:" + size / 1024 + " KB\n   " + mPhotoFilePath;
        mPhotoInfoTextView.setText(info);
    }

    private void setProgressBarVisibility(int visibility) {
        mUploadProgressBar.setVisibility(visibility);
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
            case UpdateStatusActivity.REQUEST_CODE_CAPTURE_PHOTO: {
                if (resultCode == RESULT_OK && null != extras) {// Has extras
                    Bitmap b = (Bitmap) extras.get("data");
                    // b=resizeBitmap(b, 72, 72);
                    b = Utils.resizeBitmapHeight(b, 320);

                    mBitmap = b;
                    storePhoto();

                    uploadPhoto(mBitmap);
                    mPreviewImageView.setImageBitmap(b);
                } else if (resultCode == RESULT_OK) {// No extras,if store in
                    // the local.

                    try {
                        Bitmap b = BitmapFactory.decodeStream(new FileInputStream(
                                mPhotoFilePath));
                        mPreviewImageView.setImageBitmap(b);
                    } catch (FileNotFoundException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                break;
            }

            case UpdateStatusActivity.REQUEST_CODE_GALLERY: {
                mBitmap = null;

                if (resultCode == RESULT_OK && null != extras) {
                    try {
                        Bitmap b = data.getParcelableExtra("data");
                        b = Utils.resizeBitmapHeight(b, 320);

                        mBitmap = b;
                        storePhoto();

                        uploadPhoto(mBitmap);
                        mPreviewImageView.setImageBitmap(b);
                    } catch (Exception e) {
                        // TODO: handle exception
                    }
                }

                break;
            }

            default:
                break;
        }
    }

    private void returnResult() {
        Intent intent = new Intent();
        intent.setClass(this, UpdateStatusActivity.class);
        intent.putExtra(REQUEST_EXTRAL_URL, mResultUrl);

        setResult(RESULT_OK, intent);
        finish();
    }

    private void uploadPhoto(Bitmap bitmap) {
        // TODO Auto-generated method stub
        UploadPhoto uploadPhoto = new UploadPhoto();
        // uploadPhoto.setBitmap(bitmap);
        String serverUrl = FTPreference.getInstance(getApplicationContext())
                .getServerUrl();
        UploadPhoto.setServerUrl(serverUrl);
        uploadPhoto.setUserName(FTPreference.getInstance(getApplicationContext()).getUserName());
        uploadPhoto.setPassword(FTPreference.getInstance(getApplicationContext()).getPassword());
        uploadPhoto.setFilePath(mPhotoFilePath);
        runBaseTask(uploadPhoto, this);
    }

    @Override
    public void onTaskStart(Object task) {
        // TODO Auto-generated method stub
        setProgressBarVisibility(View.VISIBLE);
        mUploadPhotoButton.setEnabled(false);
    }

    @Override
    public void onTaskProgressUpdate(Object task, Integer... values) {
        // TODO Auto-generated method stub

    }

    @Override
    public void OnTaskFinished(Object task) {
        // TODO Auto-generated method stub
        setProgressBarVisibility(View.GONE);
        mResultUrl = ((BaseTask) task).getResponse();

        if (!Utils.isEmpty(mResultUrl)) {
            returnResult();
        }

        mUploadPhotoButton.setEnabled(true);
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub

        int id = v.getId();

        switch (id) {
            case R.id.upload_photo_capture_photo_button: {
                capturePhotoUpload();
                // capturePhotoToLocal();
                break;
            }

            case R.id.upload_photo_upload_button: {
                uploadPhoto(mBitmap);
                break;
            }

            case R.id.upload_photo_choose_from_local_button: {
                // TODO add the handle here.
                // preparePath();
                doPickPhotoFromGallery();
                break;
            }

            default:
                break;
        }
    }

    private Intent getPhotoPickIntent() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);
        intent.putExtra("outputY", 480);
        intent.putExtra("return-data", true);
        return intent;
    }

    private void doPickPhotoFromGallery() {
        try {
            // Launch picker to choose photo for selected contact
            final Intent intent = getPhotoPickIntent();
            startActivityForResult(intent, UpdateStatusActivity.REQUEST_CODE_GALLERY);
        } catch (ActivityNotFoundException e) {
            showToast(getString(R.string.choose_from_local_failed));
        }
    }

}
