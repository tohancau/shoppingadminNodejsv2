package etn.app.danghoc.shoppingadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import etn.app.danghoc.shoppingadmin.Interface.IClickAdmin;
import etn.app.danghoc.shoppingadmin.adapter.AdminAdapter;
import etn.app.danghoc.shoppingadmin.adapter.ImageAdapter;
import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.model.Admin;
import etn.app.danghoc.shoppingadmin.model.Banner;
import etn.app.danghoc.shoppingadmin.retrofit.IMyShoppingAPI;
import etn.app.danghoc.shoppingadmin.retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ManagerAdmin extends AppCompatActivity {
    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    List<Admin> listBanner;

    IMyShoppingAPI shoppingAPI;
    CompositeDisposable compositeDisposable;
    AdminAdapter adapterImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_admin);

        compositeDisposable = new CompositeDisposable();
        shoppingAPI = new RetrofitClient().getInstance(Common.API_RESTAURANT_ENDPOINT)
                .create(IMyShoppingAPI.class);
        listBanner=new ArrayList<>();
        ButterKnife.bind(this);
        loadAdmins();
    }

    private void loadAdmins() {
        // con ketdd
        compositeDisposable.add(shoppingAPI.getAllAdmin(
                Common.API_KEY,
                Common.currentUser.getIdAdmin()
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (model.isSuccess()) {
                        listBanner=model.getResult();
                        adapterImage=new AdminAdapter(this, listBanner, new IClickAdmin() {
                            @Override
                            public void onClickButtonDelete(int position) {
                                deleteAdmin(position);
                                //todo
                            }
                        });
                        recycler_view.setHasFixedSize(true);
                        recycler_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
                        recycler_view.setAdapter(adapterImage);
                      //  adapterImage.notifyDataSetChanged();
                    }else{
                        Toast.makeText(ManagerAdmin.this,"empty banner", Toast.LENGTH_SHORT).show();
                    }

                }, throwable -> {
                    Toast.makeText(ManagerAdmin.this,throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    private void deleteAdmin(int position) {
        compositeDisposable.add(shoppingAPI.deleteAdmin(
                Common.API_KEY,
                listBanner.get(position).getIdAdmin()
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteProductModel -> {
                    if(deleteProductModel.isSuccess()){
                        listBanner.clear();
                        adapterImage.notifyDataSetChanged();
                        loadAdmins();
                        Toast.makeText(this, "delete success", Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(this, deleteProductModel.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                },throwable -> {
                    Toast.makeText(this, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }
}