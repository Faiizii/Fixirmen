package com.app.fixirman.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;

import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;

public class UploadImageUtils {
    private final String TAG = "UploadImageUtil";
    private Context context;
    private String fileName;
    private RequestBody reqFile;
    //Image
    private Bitmap myBitmap;
    private Uri picUri;
    private File f;
    private String content_type;

    public UploadImageUtils(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public Intent getPickImageChooserIntent() {

        // Determine Uri of camera image to save.
        Uri outputFileUri = getCaptureImageOutputUri();

        List<Intent> allIntents = new ArrayList<>();
        PackageManager packageManager = context.getPackageManager();

        // collect all camera intents
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        List<ResolveInfo> listCam = packageManager.queryIntentActivities(captureIntent, 0);
        for (ResolveInfo res : listCam) {
            Intent intent = new Intent(captureIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            if (outputFileUri != null) {
                intent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            }
            allIntents.add(intent);
        }

        // collect all gallery intents
        Intent galleryIntent = new Intent(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        List<ResolveInfo> listGallery = packageManager.queryIntentActivities(galleryIntent, 0);
        for (ResolveInfo res : listGallery) {
            Intent intent = new Intent(galleryIntent);
            intent.setComponent(new ComponentName(res.activityInfo.packageName, res.activityInfo.name));
            intent.setPackage(res.activityInfo.packageName);
            allIntents.add(intent);
        }

        // the main intent is the last in the list (fucking android) so pickup the useless one
        Intent mainIntent = allIntents.get(allIntents.size() - 1);
        for (Intent intent : allIntents) {
            try {
                if (intent.getComponent().getClassName().equals("com.android.documentsui.DocumentsActivity")) {
                    mainIntent = intent;
                    break;
                }
            }catch (Exception e){
                Log.e(TAG, "getPickImageChooserIntent: ", e);
            }
        }
        allIntents.remove(mainIntent);

        // Create a chooser from the main intent
        Intent chooserIntent = Intent.createChooser(mainIntent, "Select source");

        // Add all other intents
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, allIntents.toArray(new Parcelable[allIntents.size()]));

        return chooserIntent;
    }

    private Uri getCaptureImageOutputUri() {
        Uri outputFileUri = null;
        f = context.getExternalCacheDir();
        if (f != null) {
            outputFileUri = Uri.fromFile(new File(f.getPath(), fileName));
        }
        return outputFileUri;
    }

    private File getImgFileFromBitmap(Bitmap bitmap) {
        File f = new File(context.getCacheDir(), String.valueOf(System.currentTimeMillis())+".png");
        try {
            f.createNewFile();
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 0 /*ignored for PNG*/, bos);
            byte[] bitmapdata = bos.toByteArray();

//write the bytes in file
            FileOutputStream fos = new FileOutputStream(f);
            fos.write(bitmapdata);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            InputStream is = new BufferedInputStream(new FileInputStream(f));
            content_type = URLConnection.guessContentTypeFromStream(is);
            Log.e("TAG", "content_type: "+content_type);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return f;
    }

    private Uri getPickImageResultUri(Intent data) {
        boolean isCamera = true;
        if (data != null) {
            String action = data.getAction();
            isCamera = action != null && action.equals(MediaStore.ACTION_IMAGE_CAPTURE);
        }


        return isCamera ? getCaptureImageOutputUri() : data.getData();
    }
    private Bitmap rotateImageIfRequired(Bitmap img, Uri selectedImage) throws IOException {

        ExifInterface ei = new ExifInterface(selectedImage.getPath());
        int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);

        switch (orientation) {
            case ExifInterface.ORIENTATION_ROTATE_90:
                return rotateImage(img, 90);
            case ExifInterface.ORIENTATION_ROTATE_180:
                return rotateImage(img, 180);
            case ExifInterface.ORIENTATION_ROTATE_270:
                return rotateImage(img, 270);
            default:
                return img;
        }
    }
    private Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 0) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }
    public void prepareUploadData(Intent data) throws Exception{
            if(getPickImageResultUri(data) != null){
                picUri = getPickImageResultUri(data);
                try {
                    try {
                        myBitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), picUri);
                    }catch (OutOfMemoryError error){
                        Log.e(TAG, "getUploadParams: ",error.getCause() );
                        throw new Exception("File size is too large to upload");
                    }
                    if(myBitmap == null)
                    {
                        throw new Exception("Image not selected properly. Please Try Again");
                    }
                    myBitmap = getResizedBitmap(myBitmap, 500);
                    try {
                        myBitmap = rotateImageIfRequired(myBitmap, picUri);
                    } catch (Exception e) {
                        Log.e(TAG, "getUploadParams: ",e );
                    }
                    f = getImgFileFromBitmap(myBitmap);
                    if(f != null){
                        if(content_type == null){
                            String extension = MimeTypeMap.getFileExtensionFromUrl(f.getPath());
                            if (extension != null) {
                                content_type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                                Log.e(TAG,  "content type "+content_type );
                            }else{
                                throw  new Exception("file extension not found in selected image.Try another image");
                            }
                        }
                        reqFile = RequestBody.create(
                                MediaType.parse(content_type),
                                f);
                    }else{
                        throw new Exception("File not found. Please Reselect Image");
                    }
                } catch (IOException e) {
                    Log.e(TAG, "getUploadParams: ", e);
                    throw new Exception("File not found Exception");
                }

            }else{
                throw new Exception("Looks like image is not properly selected. Please Reselect Image");
            }
    }
    public MultipartBody.Part getImageBodyPart(@NonNull String paramName){
        if(f != null && reqFile != null)
            return MultipartBody.Part.createFormData(paramName, f.getName(), reqFile);
        else
            return null;
    }

    public Bitmap getMyBitmap() {
        return myBitmap;
    }


}
