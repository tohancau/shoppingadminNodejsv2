package etn.app.danghoc.shoppingadmin.ui.home;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import etn.app.danghoc.shoppingadmin.R;
import etn.app.danghoc.shoppingadmin.adapter.UserAdapter;
import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.databinding.FragmentHomeBinding;
import etn.app.danghoc.shoppingadmin.event_bus.UpdateStatusUserEvent;
import etn.app.danghoc.shoppingadmin.event_bus.UserItemClick;
import etn.app.danghoc.shoppingadmin.model.User;
import etn.app.danghoc.shoppingadmin.retrofit.IMyShoppingAPI;
import etn.app.danghoc.shoppingadmin.retrofit.RetrofitClient;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    List<User>userList;
    Unbinder unbinder;

    @BindView(R.id.recycle_user)
    RecyclerView recycle_user;
    UserAdapter adapter,searchUserAdapter;

    CompositeDisposable compositeDisposable = new CompositeDisposable();
    IMyShoppingAPI shoppingAPI;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                new ViewModelProvider(this).get(HomeViewModel.class);

        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        unbinder= ButterKnife.bind(this,root);

        homeViewModel.getListMyViewProduct().observe(this,users -> {
            userList=users;
            displayUser();
        });
        setHasOptionsMenu(true);//enable menu in fragment
        shoppingAPI = RetrofitClient.getInstance(Common.API_RESTAURANT_ENDPOINT).create(IMyShoppingAPI.class);
        return root;
    }

    private void displayUser() {
        adapter=new UserAdapter(getActivity(),userList);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getContext());
        recycle_user.setLayoutManager(linearLayoutManager);
        recycle_user.setAdapter(adapter);

    }

    @Override
    public void onStart() {
        super.onStart();
        if(!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
        EventBus.getDefault().postSticky(new UserItemClick(false,99));

    }

    @Override
    public void onStop() {

  //      if(EventBus.getDefault().isRegistered(this))
//            EventBus.getDefault().unregister(this);

        super.onStop();

    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void UpdateStatusUserClick(UpdateStatusUserEvent event) {
        if(event.isSuccess()){
            int position=event.getPosition();
            if(userList.get(position).getTrangThai()==0){
                userList.get(position).setTrangThai(1);
                adapter.notifyDataSetChanged();
            }
            else {
                userList.get(position).setTrangThai(0);
                adapter.notifyDataSetChanged();
            }
        }
    }


    @Override
    public void onCreateOptionsMenu(@NonNull @NotNull Menu menu, @NonNull @NotNull MenuInflater inflater) {

        inflater.inflate(R.menu.menu_search, menu);

        MenuItem menuItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        searchView.setQueryHint("nhập số điện thoại cần tìm");
        //event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                startSearchFood(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        menuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                // khi cai action  view nay ket thuc se phuc hoi lai
                recycle_user.setAdapter(adapter);

                return true;
            }
        });

    }

    private void startSearchFood(String query) {

        query="%"+query+"%";
        compositeDisposable.add(shoppingAPI.searchUser(
                Common.API_KEY,
                query)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> {
                    if (model.isSuccess()) {

                      searchUserAdapter=new UserAdapter(getContext(),model.getResult());
                        recycle_user.setAdapter(searchUserAdapter);
                    } else {
                        if (model.getMessage().contains("empty")) {
                            Toast.makeText(getContext(), "not found", Toast.LENGTH_SHORT).show();
                            recycle_user.setAdapter(null);
                        }else{
                            Toast.makeText(getContext(), model.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, throwable -> {
                    Toast.makeText(getContext(), "[search]" + throwable.getMessage(), Toast.LENGTH_SHORT).show();
                }));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}