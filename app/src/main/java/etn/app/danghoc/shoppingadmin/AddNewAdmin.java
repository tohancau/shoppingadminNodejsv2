package etn.app.danghoc.shoppingadmin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.retrofit.IMyShoppingAPI;
import etn.app.danghoc.shoppingadmin.retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class AddNewAdmin extends AppCompatActivity {

    @BindView(R.id.edt_user_name)
    EditText edt_user_name;
    @BindView(R.id.edt_name)
    EditText edt_name;
    @BindView(R.id.edit_phone_number)
    EditText edit_phone_number;

    IMyShoppingAPI shoppingAPI;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_admin);
        ButterKnife.bind(this);
        compositeDisposable = new CompositeDisposable();
        shoppingAPI = new RetrofitClient().getInstance(Common.API_RESTAURANT_ENDPOINT)
                .create(IMyShoppingAPI.class);
    }

    @OnClick(R.id.btn_add_admin)
    public void addAdmin(View view) {
        Log.d("cc",""+edt_name.getText().toString());
        compositeDisposable.add(shoppingAPI.addAdmin(
                Common.API_KEY,
                edt_name.getText().toString(),
                edt_user_name.getText().toString()
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (model.isSuccess()) {
                        Toast.makeText(AddNewAdmin.this, "Thêm admin thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(AddNewAdmin.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Toast.makeText(AddNewAdmin.this,throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }
}