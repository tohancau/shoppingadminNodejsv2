package etn.app.danghoc.shoppingadmin.ui.detail_user;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.model.SanPham;
import etn.app.danghoc.shoppingadmin.model.User;
import etn.app.danghoc.shoppingadmin.retrofit.IMyShoppingAPI;
import etn.app.danghoc.shoppingadmin.retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class DetailUserViewModel extends ViewModel {

    private MutableLiveData<String> messageError;
    private MutableLiveData<List<SanPham>> listSanPham;

    private IMyShoppingAPI myRestaurantAPI;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public DetailUserViewModel() {
        myRestaurantAPI = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(IMyShoppingAPI.class);
    }


    public MutableLiveData<List<SanPham>> getListMyViewProduct() {
        if(listSanPham==null)
        {
            listSanPham=new MutableLiveData<>();
            messageError=new MutableLiveData<>();
            loadListSP();
        }
        return listSanPham;
    }

    private void loadListSP() {
        compositeDisposable.
                add(myRestaurantAPI.getSanPhamByUser(  Common.API_KEY,
                        Common.userSelect.getIdUser()
                        )
                                .subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(userModel -> {
                                    if(userModel.isSuccess())
                                    {
                                        listSanPham.setValue(userModel.getResult());
                                    }
                                    else {
                                        messageError.setValue(userModel.getMessage());
                                    }

                                },throwable -> {
                                    messageError.setValue(throwable.getMessage());
                                })
                );
    }
    public MutableLiveData <String>getMessageError(){
        if(messageError==null)
            messageError=new MutableLiveData<>();
        return messageError;
    }


}