
package net.impjq.photo;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import net.impjq.ftclient.Utils;
import net.impjq.ftclient.api.BaseTask;
import net.impjq.httpclient.HttpClientHelper;

public class UploadPhoto extends BaseTask {
    private Bitmap mBitmap;

    public void setBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
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
            executeMultipartPost();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void executeMultipartPost() throws Exception {
        try {
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            getBitmap().compress(CompressFormat.JPEG, 75, bos);
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

}
