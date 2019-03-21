package renchaigao.com.zujuba.ActivityAndFragment.Function;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.renchaigao.zujuba.dao.Address;
import com.renchaigao.zujuba.mongoDB.info.user.UserInfo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonRecycleAdapter;
import renchaigao.com.zujuba.ActivityAndFragment.AdapterBasic.CommonViewHolder;
import renchaigao.com.zujuba.ActivityAndFragment.BaseActivity;
import renchaigao.com.zujuba.ActivityAndFragment.Store.CreateStoreActivity;
import renchaigao.com.zujuba.R;
import renchaigao.com.zujuba.util.DataPart.DataUtil;
import renchaigao.com.zujuba.util.ImgUtil;

import static com.renchaigao.zujuba.PropertiesConfig.ConstantManagement.ADDRESS_CLASS_STORE;

public class CallUsActivity extends BaseActivity {

    private AppCompatSpinner spinner;
    private TextInputEditText textInputEditText;
    private ImageView addImage;
    private Button submit;
    private RecyclerView recyclerView;
    private UserInfo userInfo;
    private LinearLayoutManager layoutManager;
    private ImageAdapter imageAdapter;

    private void initToolbar() {
        ConstraintLayout toolbar = (ConstraintLayout) findViewById(R.id.callus_toolbar);
        ((TextView) toolbar.findViewById(R.id.textView146)).setText("信息反馈");
        ((TextView) toolbar.findViewById(R.id.textView147)).setText("");
        ImageView goback = (ImageView) toolbar.findViewById(R.id.imageView33);
        goback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void InitView() {
        initToolbar();
        spinner = (AppCompatSpinner) findViewById(R.id.callus_spinner);
        textInputEditText = (TextInputEditText) findViewById(R.id.textInputLayout);
        addImage = (ImageView) findViewById(R.id.add_image);
        submit = (Button) findViewById(R.id.button16);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        initRecyclerView();
    }

    private Uri imageUri;

    private void takePhoto(String photoName) {
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
            imageUri = FileProvider.getUriForFile(CallUsActivity.this, "com.example.cameraalbumtest.fileprovider", outputImage);
        }
        // 启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CHOOSE_CHAMERA);
    }

    ArrayList<Bitmap> bitmapArrayList = new ArrayList<>();
    private final static int CHOOSE_CHAMERA = 111;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case CHOOSE_CHAMERA:
                if (resultCode == RESULT_OK) {
                    try {
                        // 将拍摄的照片显示出来
                        bitmapArrayList.add(BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                break;

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
                    bitmapArrayList.add(bitmapPhoto);
//                    ImgUtil.bitmapToFile(bitmapPhoto, getExternalCacheDir() + "/photo1.jpg");

                }
                break;
        }
        updateRecyclerView();
    }

    @Override
    protected void InitData() {
        userInfo = DataUtil.GetUserInfoData(this);
    }

    private AlertDialog.Builder builderPhoto;
    public static final int CHOOSE_PHOTO = 2;
    public static final int TAKE_PHOTO = 1;

    private void openAlbum() {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent, CHOOSE_PHOTO); // 打开相册
    }


    @Override
    protected void InitOther() {
        builderPhoto = new AlertDialog.Builder(CallUsActivity.this);
        builderPhoto.setTitle("图片来源");
        builderPhoto.setMessage("选择图片获取方式");
        builderPhoto.setNegativeButton("本地相册", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                openAlbum();
            }
        });
        builderPhoto.setPositiveButton("相机", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                takePhoto("photo");
            }
        });
        addImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builderPhoto.show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openAlbum();
                } else {
                    Toast.makeText(this, "You denied the permission", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_call_us;
    }


    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        imageAdapter = new ImageAdapter(CallUsActivity.this, bitmapArrayList);
        recyclerView.setAdapter(imageAdapter);
        recyclerView.setHasFixedSize(true);
    }

    private void updateRecyclerView() {
        imageAdapter.updateResults(bitmapArrayList);
        imageAdapter.notifyDataSetChanged();
    }

    private class ImageAdapter extends CommonRecycleAdapter<Bitmap> {

        private Context mContext;

        public ImageAdapter(Context context, ArrayList<Bitmap> dataList) {
            super(context, dataList, R.layout.card_activity_call_us_image);
            this.mContext = context;
        }

        @Override
        public void bindData(CommonViewHolder holder, Bitmap data) {
            holder.setImageBitmap(R.id.card_image_callus, data);

        }
    }
}
