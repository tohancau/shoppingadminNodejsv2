package etn.app.danghoc.shoppingadmin.ui.detail_user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import etn.app.danghoc.shoppingadmin.R;
import etn.app.danghoc.shoppingadmin.adapter.SanPhamUserAdapter;
import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.databinding.FragmentDetailUserBinding;
import etn.app.danghoc.shoppingadmin.event_bus.UpdateStatusUserEvent;
import etn.app.danghoc.shoppingadmin.model.SanPham;
import etn.app.danghoc.shoppingadmin.retrofit.IMyShoppingAPI;
import etn.app.danghoc.shoppingadmin.retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailUserFragment extends Fragment {

    private DetailUserViewModel detailUserViewModel;
    private FragmentDetailUserBinding binding;

    @BindView(R.id.txt_phone_number)
    TextView txt_phone_number;
    @BindView(R.id.txt_status)
    TextView txt_status;
    @BindView(R.id.txt_status_sp)
    TextView txt_status_sp;
    @BindView(R.id.txt_money)
    TextView txt_money;
    @BindView(R.id.btn_khoa)
    Button btn_khoa;
    @BindView(R.id.btn_mo_khoa)
    Button btn_mo_khoa;
    @BindView(R.id.recycle_sanpham)
    RecyclerView recycle_sanpham;
    Unbinder unbinder;

    SanPhamUserAdapter adapter;
    List<SanPham>sanPhamList;

    private IMyShoppingAPI myRestaurantAPI;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        detailUserViewModel =
                new ViewModelProvider(this).get(DetailUserViewModel.class);

        binding = FragmentDetailUserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        unbinder= ButterKnife.bind(this,root);
        initView();
        myRestaurantAPI= RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(IMyShoppingAPI.class);



       detailUserViewModel.getListMyViewProduct().observe(this,sanPhams -> {
           sanPhamList=sanPhams;
           if(sanPhams.size()>0)
           {
               recycle_sanpham.setVisibility(View.VISIBLE);
               txt_status_sp.setVisibility(View.GONE);
               displaySanPham();
           }
       });
        return root;
    }

    private void displaySanPham() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recycle_sanpham.setLayoutManager(linearLayoutManager);
        adapter = new SanPhamUserAdapter(getContext(), sanPhamList);
        recycle_sanpham.setAdapter(adapter);
    }

    private void initView() {
        try {
            txt_phone_number.setText(Common.userSelect.getPhoneUser());
            txt_status.setText(Common.convertStatusToString(Common.userSelect.getTrangThai()));
            txt_money.setText("số tiền :"+Common.userSelect.getAmountMoney()+"đ");
            if(Common.userSelect.getTrangThai()==0)//dang hoa dong
                btn_mo_khoa.setVisibility(View.GONE);
            else btn_khoa.setVisibility(View.GONE);
        }catch (Exception e){
            Toast.makeText(getContext(), "loi "+e.toString(), Toast.LENGTH_SHORT).show();
        }

    }


    @OnClick(R.id.btn_mo_khoa)
    void moKhoaClick() {
        compositeDisposable.
                add(myRestaurantAPI.updateStatusUser(
                        Common.API_KEY,
                        Common.userSelect.getIdUser(),
                        0
                        )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(model -> {
                                    if (model.isSuccess()) {
                                      btn_mo_khoa.setVisibility(View.GONE);
                                      btn_khoa.setVisibility(View.VISIBLE);
                                        txt_status.setText("đang hoạt động");

                                        EventBus.getDefault().postSticky(new UpdateStatusUserEvent(true,Common.positionUserSelect));
                                    }
                                    else {
                                        Toast.makeText(getContext(), "fail update"+model.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }, throwable -> {
                                    Toast.makeText(getContext(), "[update status]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                })
                );
    }
    @OnClick(R.id.btn_khoa)
    void khoaClick(){
        compositeDisposable.
                add(myRestaurantAPI.updateStatusUser(
                        Common.API_KEY,
                        Common.userSelect.getIdUser(),
                        1
                        )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(model -> {
                                    if (model.isSuccess()) {
                                        //Common.cartList.remove(position);
                                        btn_mo_khoa.setVisibility(View.VISIBLE);
                                        btn_khoa.setVisibility(View.GONE);
                                        txt_status.setText("đang bị khóa");
                                        EventBus.getDefault().postSticky(new UpdateStatusUserEvent(true,Common.positionUserSelect));
                                    }
                                    else {
                                        Toast.makeText(getContext(), "fail update"+model.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }, throwable -> {
                                    Toast.makeText(getContext(), "[update status]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                                })
                );
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}