package delta.alikeb.code.camerascanner;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

import delta.alikeb.code.camerascanner.helpers.MyConstants;
import delta.alikeb.code.camerascanner.libraries.NativeClass;

public class ImageEnhanceActivity extends AppCompatActivity {

    ImageView imageView;
    Bitmap selectedImageBitmap;

    Button RotateRight;
    Button RotateLeft;
    Button btnImageToGray;

    NativeClass nativeClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_enhance);

        initializeElement();
        initializeImage();
    }

    private void initializeElement() {

        nativeClass = new NativeClass();

        imageView = findViewById(R.id.imageView);
        btnImageToGray = findViewById(R.id.btnImageToGray);
        RotateRight = findViewById(R.id.RotateLeft);
        RotateLeft = findViewById(R.id.RotateRight);

        RotateLeft.setOnClickListener(RotateLeftClick);
        RotateRight.setOnClickListener(RotateRightClick);
        btnImageToGray.setOnClickListener(btnImageToGrayClick);

    }

    private void initializeImage() {

        selectedImageBitmap = MyConstants.selectedImageBitmap;
        MyConstants.selectedImageBitmap = null;

        imageView.setImageBitmap(selectedImageBitmap);

    }


    private View.OnClickListener RotateLeftClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO: create BW

            imageView.post(new Runnable() {
                @Override
                public void run() {
//                    final ExifInterface exif = new ExifInterface(path);
//                    final int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_UNDEFINED);
                    final Matrix matrix = new Matrix();
                    matrix.postRotate(-90);
                    final Bitmap bmRotated = Bitmap.createBitmap(selectedImageBitmap, 0, 0, selectedImageBitmap.getWidth(), selectedImageBitmap.getHeight(), matrix, true);
                   // selectedImageBitmap.recycle();
                    imageView.setImageBitmap(bmRotated);
                    imageView.animate().rotationBy(-90);

                }
            });
           // imageView.setImageBitmap(nativeClass.getBWBitmap(selectedImageBitmap));
        }
    };

    private View.OnClickListener RotateRightClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO: create magic color
            imageView.post(new Runnable() {
                @Override
                public void run() {
                    final Matrix matrix = new Matrix();
                    matrix.postRotate(90);
                    final Bitmap bmRotated = Bitmap.createBitmap(selectedImageBitmap, 0, 0, selectedImageBitmap.getWidth(), selectedImageBitmap.getHeight(), matrix, true);
                    imageView.setImageBitmap(bmRotated);
                    imageView.animate().rotationBy(90);
                }
            });
            //imageView.setImageBitmap(nativeClass.getMagicColorBitmap(selectedImageBitmap));
        }
    };

    private View.OnClickListener btnImageToGrayClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO: create Gray
            Bitmap bmp32 = selectedImageBitmap.copy(Bitmap.Config.ARGB_8888, true);
            Mat imgSource = new Mat();
            Utils.bitmapToMat(bmp32, imgSource);

            Imgproc.GaussianBlur(imgSource, imgSource, new Size(5, 5), 5);

            Mat gray = new Mat(imgSource.size(), CvType.CV_8UC1);
            Imgproc.cvtColor(imgSource, gray, Imgproc.COLOR_RGBA2GRAY);

            Imgproc.Canny(gray, gray, 50, 50);




            Bitmap bmp = null;
            bmp = Bitmap.createBitmap(gray.cols(), gray.rows(), Bitmap.Config.ARGB_8888);
            Utils.matToBitmap(gray, bmp);
            imageView.setImageBitmap(bmp);

        }
    };


}
