package etn.app.danghoc.shoppingadmin.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.model.User;
import etn.app.danghoc.shoppingadmin.retrofit.IMyShoppingAPI;
import etn.app.danghoc.shoppingadmin.retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeViewModel extends ViewModel {


    private MutableLiveData<String> messageError;
    private MutableLiveData<List<User>> listMyViewUser;


    private IMyShoppingAPI myRestaurantAPI;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HomeViewModel() {
        myRestaurantAPI = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(IMyShoppingAPI.class);
    }

    public MutableLiveData<List<User>> getListMyViewProduct() {
        if(listMyViewUser==null)
        {
            listMyViewUser=new MutableLiveData<>();
            messageError=new MutableLiveData<>();
            loadListCart();
        }
        return listMyViewUser;
    }

    public MutableLiveData <String>getMessageError(){
        if(messageError==null)
            messageError=new MutableLiveData<>();
        return messageError;
    }

    public void onStop() {
        compositeDisposable.clear();
    }

    private void loadListCart() {
        compositeDisposable.
                add(myRestaurantAPI.getUserForAdmin(Common.API_KEY
                        )
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(userModel -> {
                            if(userModel.isSuccess())
                            {
                                listMyViewUser.setValue(userModel.getResult());
                            }

                        },throwable -> {
                            messageError.setValue(throwable.getMessage());
                        })
                );
    }

}