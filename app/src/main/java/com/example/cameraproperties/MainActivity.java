package com.example.cameraproperties;

import android.content.Context;
import android.graphics.ImageFormat;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraManager;
import android.os.Bundle;
import android.util.Range;
import android.util.Size;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.layout);

        CameraManager manager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
        try {
            for (String cameraId : manager.getCameraIdList()) {
                TableLayout table = new TableLayout(this);
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(cameraId);

                addRowToTable(table, "Camera ID", cameraId);

                Integer lensFacing = characteristics.get(CameraCharacteristics.LENS_FACING);
                String facing = (lensFacing != null && lensFacing == CameraCharacteristics.LENS_FACING_FRONT) ? "front" : "back";
                addRowToTable(table, "Facing", facing);

                Size pixelArraySize = characteristics.get(CameraCharacteristics.SENSOR_INFO_PIXEL_ARRAY_SIZE);
                addRowToTable(table, "Pixel Array Size", String.valueOf(pixelArraySize));

                Size[] jpegSizes = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                        .getOutputSizes(ImageFormat.JPEG);
                if (jpegSizes != null && jpegSizes.length > 0) {
                    int maxWidth = 0;
                    int maxHeight = 0;
                    for (Size size : jpegSizes) {
                        if (size.getWidth() > maxWidth) {
                            maxWidth = size.getWidth();
                            maxHeight = size.getHeight();
                        }
                    }
                    addRowToTable(table, "Max image dimensions", maxWidth + "x" + maxHeight);
                    double megapixels = maxWidth * maxHeight / 1e6;
                    addRowToTable(table, "Megapixels", String.format("%.2f MP", megapixels));
                }

                Float focalLength = characteristics.get(CameraCharacteristics.LENS_INFO_AVAILABLE_FOCAL_LENGTHS)[0];
                addRowToTable(table, "Focal Length", String.valueOf(focalLength));

                Range<Integer>[] fpsRanges = characteristics.get(CameraCharacteristics.CONTROL_AE_AVAILABLE_TARGET_FPS_RANGES);
                if(fpsRanges != null && fpsRanges.length > 0) {
                    addRowToTable(table, "FPS Range", fpsRanges[0].toString());
                }

                int[] faceDetectModes = characteristics.get(CameraCharacteristics.STATISTICS_INFO_AVAILABLE_FACE_DETECT_MODES);
                if (faceDetectModes != null) {
                    String faceDetectSupport = (faceDetectModes.length > 0) ? "Supported" : "Not Supported";
                    addRowToTable(table, "Face Detection", faceDetectSupport);
                }

                Boolean flashAvailable = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
                String flashSupport = (flashAvailable != null && flashAvailable) ? "Available" : "Not Available";
                addRowToTable(table, "Flash", flashSupport);

                layout.addView(table);

                // Add space between tables
                View spacer = new View(this);
                spacer.setLayoutParams(new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        50));  // Here 50 is the height of the spacer view (change as needed)
                layout.addView(spacer);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addRowToTable(TableLayout table, String label, String value) {
        TableRow row = new TableRow(this);

        TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(0, 0, 0, 20);  // Here 20 is the bottom margin (change as needed)
        row.setLayoutParams(layoutParams);

        TextView labelView = new TextView(this);
        labelView.setText(label);
        labelView.setPadding(10, 10, 10, 10);  // Here 10 is the padding (change as needed)
        labelView.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_shape));
        row.addView(labelView);

        TextView valueView = new TextView(this);
        valueView.setText(value);
        valueView.setPadding(10, 10, 10, 10);  // Here 10 is the padding (change as needed)
        valueView.setBackground(ContextCompat.getDrawable(this, R.drawable.cell_shape));
        row.addView(valueView);

        table.addView(row);
    }
}
