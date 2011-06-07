
package net.impjq.photo;

import org.apache.http.entity.FileEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;

import net.impjq.ftclient.Utils;
import net.impjq.ftclient.api.BaseTask;
import net.impjq.httpclient.HttpClientHelper;

/**
 * Upload the Photo,if need you can delete the file with {@link #deleteFile()}
 * after upload finished. <br>
 * Here it provide two ways to upload photo:the {@link #mBitmap} and
 * {@link #mFilePath}.you just need fill one of these two values.
 * 
 * @author pjq0274
 */
public class UploadPhoto extends BaseTask {
    private Bitmap mBitmap = null;
    private String mFilePath = null;

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    public void setFilePath(String filePath) {
        mFilePath = filePath;
    }

    public String getFilePath() {
        return mFilePath;
    }

    public UploadPhoto() {
        // TODO Auto-generated constructor stub
        mServiceType = SERVICE_TYPE_UPLOAD_PHOTO;// Twitter
        mCommand = "UploadPhoto";
        mCommandId = PHOTO_UPLOAD_PHOTO;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        try {
            if (null != mBitmap) {
                executeMultipartPost();
            } else if (null != mFilePath) {
                executeMultipartPost(mFilePath);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void executeMultipartPost() throws Exception {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            getBitmap().compress(CompressFormat.JPEG, 20, bos);
            byte[] data = bos.toByteArray();
            ByteArrayBody bab = new ByteArrayBody(data, "forest.jpg");
            MultipartEntity reqEntity = new MultipartEntity(
                    HttpMultipartMode.BROWSER_COMPATIBLE);
            reqEntity.addPart("uploaded", bab);

            InputStream inputStream = executeRequest(reqEntity, true);

            String result = HttpClientHelper.getInstance().readFromInputStream(
                    inputStream);
            Utils.log(TAG, "result=" + result);
            mResponse = result;
        } catch (Exception e) {
            // handle exception here

        }
    }

    public void executeMultipartPost(String filePath) throws Exception {
        try {
            File file = new File(filePath);
            FileEntity fileEntity = new FileEntity(file, "image/jpeg");
            InputStream inputStream = executeRequest(fileEntity, true);
            String result = HttpClientHelper.getInstance().readFromInputStream(
                    inputStream);
            Utils.log(TAG, "result=" + result);
            mResponse = result;

            // deleteFile();
        } catch (Exception e) {
            // handle exception here

        }
    }

    public void deleteFile() {
        File file = new File(mFilePath);
        if (file.exists()) {
            file.delete();
        }
    }

}
