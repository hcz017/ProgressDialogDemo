package com.example.hcz.progressdialogdemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageUtils {
    private static final String TAG = "ImageUtils";
    public static void convertYUV2Jpeg(String inputPath, String outPath, int width, int height)
            throws IOException {

        File file = new File(inputPath);

        if (!file.exists()){
            Log.d(TAG, "convertYUV2Jpeg: file not exist, return");
            return;
        }

        byte[] nv21data = Files.readAllBytes(file.toPath());

        Bitmap bitmap = null;
        try {
            YuvImage image = new YuvImage(nv21data, ImageFormat.NV21, width, height, null);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            image.compressToJpeg(new Rect(0, 0, width, height), 100, outStream);

            bitmap = BitmapFactory.decodeByteArray(outStream.toByteArray(), 0, outStream.size());

            Path path = Paths.get(outPath);
            Files.write(path, outStream.toByteArray());

            outStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
