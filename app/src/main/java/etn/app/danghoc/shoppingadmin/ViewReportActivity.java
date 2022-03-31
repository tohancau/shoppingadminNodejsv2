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
import etn.app.danghoc.shoppingadmin.Interface.IClickSanPhamBaoCao;
import etn.app.danghoc.shoppingadmin.adapter.ImageAdapter;
import etn.app.danghoc.shoppingadmin.adapter.SanPhamBaoCaoAdpter;
import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.model.Banner;
import etn.app.danghoc.shoppingadmin.model.SanPhamBaoCao;
import etn.app.danghoc.shoppingadmin.retrofit.IMyShoppingAPI;
import etn.app.danghoc.shoppingadmin.retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class ViewReportActivity extends AppCompatActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recycler_view;

    List<SanPhamBaoCao> listSanPhamBaoCao;

    IMyShoppingAPI shoppingAPI;
    CompositeDisposable compositeDisposable;
    SanPhamBaoCaoAdpter adapterImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_report);
        compositeDisposable = new CompositeDisposable();
        shoppingAPI = new RetrofitClient().getInstance(Common.API_RESTAURANT_ENDPOINT)
                .create(IMyShoppingAPI.class);
        listSanPhamBaoCao=new ArrayList<>();
        ButterKnife.bind(this);
        loadSanPham();
    }

    private void loadSanPham() {
        compositeDisposable.add(shoppingAPI.getSanPhamByBaoCao(
                Common.API_KEY
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (model.isSuccess()) {
                        listSanPhamBaoCao=model.getResult();
                        adapterImage=new SanPhamBaoCaoAdpter(this, listSanPhamBaoCao, new IClickSanPhamBaoCao() {
                            @Override
                            public void onClickButtonDelete(int position) {
                                deleteSanPham(position);
                            }

                            @Override
                            public void onClickButtonUpdate(int position) {
                                    updateSanPham(position);
                            }
                        });
                        recycler_view.setHasFixedSize(true);
                        recycler_view.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL, false));
                        recycler_view.setAdapter(adapterImage);
                        adapterImage.notifyDataSetChanged();

                    }else{
                        Toast.makeText(ViewReportActivity.this,"empty project", Toast.LENGTH_SHORT).show();
                    }

                }, throwable -> {
                    Toast.makeText(ViewReportActivity.this,throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    private void updateSanPham(int position) {
        compositeDisposable.add(shoppingAPI.updateSanPhamBaoCao(
                Common.API_KEY,
                listSanPhamBaoCao.get(position).getIdSP(),
                0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if(model.isSuccess()){
                        listSanPhamBaoCao.clear();
                        adapterImage.notifyDataSetChanged();
                        loadSanPham();
                        Toast.makeText(this, "update success", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this,model.getMessage() , Toast.LENGTH_SHORT).show();
                    }

                },throwable -> {
                    Toast.makeText(this, "check sp exits cart"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    private void deleteSanPham(int position) {
        compositeDisposable.add(shoppingAPI.deleteProduct(
                Common.API_KEY,
               listSanPhamBaoCao.get(position).getIdSP()
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(deleteProductModel -> {
                    if(deleteProductModel.isSuccess()){
                        listSanPhamBaoCao.clear();
                        adapterImage.notifyDataSetChanged();
                        loadSanPham();
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