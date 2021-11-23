package etn.app.danghoc.shoppingadmin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.OpenableColumns;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.greenrobot.eventbus.EventBus;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.retrofit.IMyShoppingAPI;
import etn.app.danghoc.shoppingadmin.retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class QlBannerActivity extends AppCompatActivity {
    private static final int IMAGE_CODE = 1;

    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    //upload image
    StorageReference mStorageRef;
    String imagename;
    Uri imageUri;

    IMyShoppingAPI shoppingAPI;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ql_banner);
        ButterKnife.bind(this);
        compositeDisposable = new CompositeDisposable();
        mStorageRef = FirebaseStorage.getInstance().getReference();
        shoppingAPI = new RetrofitClient().getInstance(Common.API_RESTAURANT_ENDPOINT)
                .create(IMyShoppingAPI.class);
        initToolbar();
    }

    @OnClick(R.id.btn_choose_img)
    public void chooseImage(View view) {
        pickImage();
    }

    @OnClick(R.id.btn_add)
    public void addImageToFirebase() {
        uploadImagesToFirebase();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == IMAGE_CODE && resultCode == RESULT_OK) {

            if (data.getClipData() != null) {
                Toast.makeText(this, "chỉ được chọn một hình", Toast.LENGTH_SHORT).show();
            } else if (data.getData() != null) {
                imageUri = data.getData();
                imagename = getFileName(imageUri);
                image.setImageURI(imageUri);
            }
        }
    }

    public String getFileName(Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                cursor.close();
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        // tao so random
        double cc = Math.random();
        String str = String.valueOf(cc);
        str = str.replaceAll("[.]", "");
        return str + result;
    }

    private void pickImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, IMAGE_CODE);
    }

    private void uploadImagesToFirebase() {
        StorageReference mRef = mStorageRef.child("image").child(imagename);
        progress_bar.setVisibility(View.VISIBLE);
        mRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                mRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        uploadLinkImage(uri.toString());
                        progress_bar.setVisibility(View.INVISIBLE);
                        Toast.makeText(QlBannerActivity.this, "thêm banner thành công", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(QlBannerActivity.this, "[fail load link ] " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        progress_bar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progress_bar.setVisibility(View.INVISIBLE);
                Toast.makeText(QlBannerActivity.this, "[Fail upload iamge to firebase ] " + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void uploadLinkImage(String urlImage) {

        progress_bar.setVisibility(View.VISIBLE);
        compositeDisposable.add(shoppingAPI.uploadBanner(
                Common.API_KEY,
                urlImage
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    progress_bar.setVisibility(View.INVISIBLE);
                    if (model.isSuccess()) {
                        Toast.makeText(QlBannerActivity.this, "upload image success", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(QlBannerActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                }, throwable -> {
                    progress_bar.setVisibility(View.INVISIBLE);
                    Toast.makeText(QlBannerActivity.this,throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    private void initToolbar () {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }

    @Override
    public boolean onOptionsItemSelected (MenuItem item){ // button back
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}

