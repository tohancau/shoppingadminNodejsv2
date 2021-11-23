package etn.app.danghoc.shoppingadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import etn.app.danghoc.shoppingadmin.Interface.IClickDeleteImage;
import etn.app.danghoc.shoppingadmin.adapter.ImageAdapter;
import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.model.Banner;
import etn.app.danghoc.shoppingadmin.retrofit.IMyShoppingAPI;
import etn.app.danghoc.shoppingadmin.retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewBannerActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    List<Banner> listBanner;

    IMyShoppingAPI shoppingAPI;
    CompositeDisposable compositeDisposable;
    ImageAdapter adapterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_banner);
        compositeDisposable = new CompositeDisposable();
        shoppingAPI = new RetrofitClient().getInstance(Common.API_RESTAURANT_ENDPOINT)
                .create(IMyShoppingAPI.class);
        listBanner=new ArrayList<>();
        ButterKnife.bind(this);
        initToolbar();
        loadBannerFromDatabase();
    }
    private void initToolbar () {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


    }


    private void loadBannerFromDatabase() {

        compositeDisposable.add(shoppingAPI.getBanner(
                Common.API_KEY
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (model.isSuccess()) {
                        listBanner=model.getResult();
                        displayBanner(listBanner);
                    }else{
                        Toast.makeText(ViewBannerActivity.this,"empty banner", Toast.LENGTH_SHORT).show();
                    }

                }, throwable -> {
                    Toast.makeText(ViewBannerActivity.this,throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    private void displayBanner(List<Banner> banners) {
        adapterImage=new ImageAdapter(this, banners, new IClickDeleteImage() {
            @Override
            public void onClick(int position) {

                compositeDisposable.add(shoppingAPI.deleteBanner(
                        Common.API_KEY,
                        listBanner.get(position).getUrlHinhAnh()
                ).subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(model -> {
                            if (model.isSuccess()) {
                                listBanner.remove(position);
                                adapterImage.notifyDataSetChanged();
                                Toast.makeText(ViewBannerActivity.this,"xoá thành công", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(ViewBannerActivity.this,model.getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }, throwable -> {
                            Toast.makeText(ViewBannerActivity.this,throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }));
            }
        });

        recycler_view.setHasFixedSize(true);
        recycler_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
        recycler_view.setAdapter(adapterImage);
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