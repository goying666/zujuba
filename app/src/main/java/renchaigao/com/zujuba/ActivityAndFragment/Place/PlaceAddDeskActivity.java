package renchaigao.com.zujuba.ActivityAndFragment.Place;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;

import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.ImgUtil;

public class PlaceAddDeskActivity extends BaseActivity {

    private ImageView deskImage;
    private TextView maxPeopleNum, minPeopleNum;
    private Button cancleButton, submitButton;
    private AlertDialog.Builder builder, builderPhoto;

    public static final int CHOOSE_PHOTO = 1;
    public static final int PHOTO = 2;

    @Override
    protected void InitView() {
        deskImage = (ImageView) findViewById(R.id.imageView25);
        maxPeopleNum = (TextView) findViewById(R.id.textView111);
        minPeopleNum = (TextView) findViewById(R.id.textView114);
        cancleButton = (Button) findViewById(R.id.button5);
        submitButton = (Button) findViewById(R.id.button5);
        builderPhoto = new AlertDialog.Builder(this);
        builderPhoto.setTitle("图片来源");
        builderPhoto.setMessage("选择图片获取方式");
        builderPhoto.setNegativeButton("本地相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
            }
        });
    }

    private String deskNumber;
    @Override
    protected void InitData() {
        deskNumber = getIntent().getStringExtra("deskNumber");
    }

    @Override
    protected void InitOther() {
        setClick();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_place_add_desk;
    }

    private void setClick() {
        deskImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builderPhoto.setPositiveButton("相机", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        takePhoto("photo"+deskNumber, PHOTO);
                    }
                });
                builderPhoto.show();
            }
        });
    }
    private Uri imageUri;
    private void takePhoto(String photoName, int requestCode) {
        File outputImage = new File(getExternalCacheDir(), photoName + ".jpg");
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT < 24) {
            imageUri = Uri.fromFile(outputImage);
        } else {
            imageUri = FileProvider.getUriForFile(PlaceAddDeskActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, requestCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
//                通过相册选择的图片和对应的显示设置；
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmapPhoto;
                    // 判断手机系统版本号
                    if (Build.VERSION.SDK_INT >= 19) {
                        // 4.4及以上系统使用这个方法处理图片
                        bitmapPhoto = ImgUtil.handleImageOnKitKat(this, data);
                    } else {
                        // 4.4以下系统使用这个方法处理图片
                        bitmapPhoto = ImgUtil.handleImageBeforeKitKat(this, data);
                    }
                    ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo1.jpg");
                    deskImage.setImageBitmap(bitmapPhoto);
                }
                break;
            case PHOTO:
                if (resultCode == RESULT_OK) {
                    Bitmap bitmapPhoto;
                    try {
                        // 将拍摄的照片显示出来
                        bitmapPhoto = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        deskImage.setAdjustViewBounds(true);
                        deskImage.setImageBitmap(bitmapPhoto);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;
        }
    }
}
