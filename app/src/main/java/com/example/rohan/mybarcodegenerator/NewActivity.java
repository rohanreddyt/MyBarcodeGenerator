package com.example.rohan.mybarcodegenerator;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

import leadtools.LTLibrary;
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
            String key = "08waXphTjJacbQLDQ9Fuk4CRW8BLLjEI69383qJp4DPqoMXYamPE1yuYknqpCmFEP5zAcu6aYiTXpOZGouGdl08jGqEnn0zu";
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
        mImageViewer = (RasterImageViewer) findViewById(R.id.imageviewer);
        mImageViewer.setNewImageResetOptions(ImageViewerNewImageResetOptions.NONE.getValue());
        mImageViewer.setSizeMode(ImageViewerSizeMode.FIT_WIDTH);
        RasterImage image = RasterImage.create(565, 75, 32, 0, RasterColor.fromArgb(0));
        BarcodeData data = BarcodeData.createDefaultBarcodeData(BarcodeSymbology.GS1_DATA_BAR_EXPANDED);
        OneDBarcodeWriteOptions options = (OneDBarcodeWriteOptions) mBarcodeEngine.getWriter().getDefaultOptions(BarcodeSymbology.GS1_DATA_BAR_EXPANDED);
        options.setTextPosition(BarcodeOutputTextPosition.NONE);
        String text = "960011111111111111121212121212";
        data.setValue(text);
        writer.writeBarcode(image, data, options);
        ((TextView)findViewById(R.id.sample_text)).setText(text);
        mImageViewer.setImage(image);
    }
}
