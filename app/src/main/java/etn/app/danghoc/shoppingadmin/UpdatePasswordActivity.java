package etn.app.danghoc.shoppingadmin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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

public class UpdatePasswordActivity extends AppCompatActivity {

    @BindView(R.id.edit_pass_word_update)
    EditText edit_pass_word_update;

    IMyShoppingAPI shoppingAPI;
    CompositeDisposable compositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);
        ButterKnife.bind(this);
        compositeDisposable = new CompositeDisposable();
        shoppingAPI = new RetrofitClient().getInstance(Common.API_RESTAURANT_ENDPOINT)
                .create(IMyShoppingAPI.class);
        initToolbar();
    }


    @OnClick(R.id.btn_update_pass_word)
    public void updatePassword(View view) {
        if(edit_pass_word_update.getText().toString().length()<6){
            Toast.makeText(UpdatePasswordActivity.this, "mật khẩu phải tên 6 kí tự", Toast.LENGTH_SHORT).show();
        }
        compositeDisposable.add(shoppingAPI.updatePasswordAdmin(
                Common.API_KEY,
                edit_pass_word_update.getText().toString(),
                Common.currentUser.getIdAdmin()
        ).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (model.isSuccess()) {
                        Toast.makeText(UpdatePasswordActivity.this, "đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(UpdatePasswordActivity.this, model.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }, throwable -> {
                    Toast.makeText(UpdatePasswordActivity.this,throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    // button back
    @Override
    public boolean onOptionsItemSelected (MenuItem item){
        // handle arrow click here
        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initToolbar () {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }
}
