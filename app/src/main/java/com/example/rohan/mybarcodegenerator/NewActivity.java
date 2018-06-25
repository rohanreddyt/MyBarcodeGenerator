package com.example.rohan.mybarcodegenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import leadtools.LTLibrary;
import leadtools.LeadRect;
import leadtools.Platform;
import leadtools.RasterColor;
import leadtools.RasterImage;
import leadtools.RasterSupport;
import leadtools.barcode.BarcodeData;
import leadtools.barcode.BarcodeEngine;
import leadtools.barcode.BarcodeOutputTextPosition;
import leadtools.barcode.BarcodeSymbology;
import leadtools.barcode.BarcodeWriter;
import leadtools.barcode.OneDBarcodeWriteOptions;
import leadtools.controls.ImageViewerNewImageResetOptions;
import leadtools.controls.ImageViewerSizeMode;
import leadtools.controls.RasterImageViewer;
import leadtools.converters.ConvertFromImageOptions;
import leadtools.converters.RasterImageConverter;

public class NewActivity extends AppCompatActivity {

    private RasterImageViewer mImageViewer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_activity);

        String sharedLibsPath = "";
        if (Build.VERSION.SDK_INT < 9)
            sharedLibsPath = String.format("%s/lib/", this.getApplicationInfo().dataDir);
        else
            sharedLibsPath = this.getApplicationInfo().nativeLibraryDir;

        // Load LEADTOOLS native libraries
        try {
            Platform.setLibPath(sharedLibsPath);
            Platform.loadLibrary(LTLibrary.LEADTOOLS);
            Platform.loadLibrary(LTLibrary.BARCODE);

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Initialize and set license
        try {
            String key = "your key here which can be found in .key file";
            RasterSupport.setLicense(this, getResources().openRawResource(R.raw.eval_license_files), key);
        } catch (Exception ex) {
            ex.printStackTrace();
            finish();
        }

        // Ensure that the LEADTOOLS kernel is not expired
        if (RasterSupport.getKernelExpired()) {
            finish();
        }

        BarcodeEngine mBarcodeEngine = new BarcodeEngine();

        BarcodeWriter writer = mBarcodeEngine.getWriter();
        BarcodeData data = BarcodeData.createDefaultBarcodeData(BarcodeSymbology.GS1_DATA_BAR_EXPANDED);
        String text = "960000987654321123121212121212";
        data.setValue(text);
        ImageView myImage = ((ImageView)findViewById(R.id.myImage));
        OneDBarcodeWriteOptions options = (OneDBarcodeWriteOptions) mBarcodeEngine.getWriter().getDefaultOptions(BarcodeSymbology.GS1_DATA_BAR_EXPANDED);
        options.setTextPosition(BarcodeOutputTextPosition.NONE);
        options.setUseXModule(true);
        options.setXModule(1);

//        mImageViewer = (RasterImageViewer) findViewById(R.id.imageviewer);
//        mImageViewer.setNewImageResetOptions(ImageViewerNewImageResetOptions.NONE.getValue());
        data.setBounds(LeadRect.create(0,0,565 ,100));
        RasterImage image = RasterImage.create(data.getBounds().getWidth(), data.getBounds().getHeight(), 1, 0, RasterColor.fromArgb(0));


        writer.writeBarcode(image, data, options);
        ((TextView)findViewById(R.id.sample_text)).setText(text);
        Bitmap bmp =RasterImageConverter.convertToBitmap(image, null);
        myImage.setImageBitmap(bmp);
//        mImageViewer.setImage(image);
    }
}
