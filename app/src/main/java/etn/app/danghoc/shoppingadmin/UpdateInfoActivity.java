package etn.app.danghoc.shoppingadmin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import dmax.dialog.SpotsDialog;
import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.retrofit.IMyShoppingAPI;
import etn.app.danghoc.shoppingadmin.retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class UpdateInfoActivity extends AppCompatActivity {


    IMyShoppingAPI myRestaurantAPI;
    CompositeDisposable compositeDisposable = new CompositeDisposable();
    AlertDialog dialog;

    @BindView(R.id.edt_user_name)
    EditText edt_user_name;
    @BindView(R.id.btn_update)
    Button btn_update;
    @BindView(R.id.toolbar)
    Toolbar toolbar;



    @Override
    protected void onDestroy() {
        compositeDisposable.clear();
        super.onDestroy();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_info);
        ButterKnife.bind(this);
        init12();
        initView();
    }


    // clos this activity
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        toolbar.setTitle("Update information");
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        btn_update.setOnClickListener(view -> {
            dialog.show();
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (user != null) {

                compositeDisposable.add(myRestaurantAPI.updateUserInfo(Common.API_KEY,
                        user.getPhoneNumber(),
                        edt_user_name.getText().toString(),
                        user.getUid())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(updateUserModel -> {
                            dialog.dismiss();
                            compositeDisposable.add(myRestaurantAPI.getAdmin(Common.API_KEY,user.getUid())
                                    .subscribeOn(Schedulers.io())
                                    .observeOn(AndroidSchedulers.mainThread())
                                    .subscribe(userModel -> {

                                        if (userModel.isSuccess())
                                        {
                                            //gio lam ghem login
                                            Toast.makeText(this, "cap nhap thanh cong va vao phan home", Toast.LENGTH_SHORT).show();
                                            Common.currentUser=userModel.getResult().get(0);
                                            startActivity(new Intent(UpdateInfoActivity.this,HomeActivity.class));
                                            //  startActivity(new Intent(UpdateInfoActivity.this,ActivityTest.class));
                                            finish();
                                        }
                                        else
                                        {
                                            Toast.makeText(this, "get user "+userModel.getMessage(), Toast.LENGTH_SHORT).show();
                                        }

                                    },throwable -> {
                                        Toast.makeText(this, "[get  user ]", Toast.LENGTH_SHORT).show();
                                    }));

                        },throwable -> {
                            dialog.dismiss();
                            Toast.makeText(this, "[update info]"+throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }))
                ;



            } else
            {
                Toast.makeText(this, "not sign , please sign", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UpdateInfoActivity.this,MainActivity.class));

                finish();
            }
        });
    }

    private void init12() {
        dialog = new SpotsDialog.Builder().setCancelable(false).setContext(this).build();
        myRestaurantAPI = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(IMyShoppingAPI.class);

    }
}