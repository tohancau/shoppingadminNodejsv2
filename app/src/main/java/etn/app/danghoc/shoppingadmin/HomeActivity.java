package etn.app.danghoc.shoppingadmin;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.databinding.ActivityHomeBinding;
import etn.app.danghoc.shoppingadmin.event_bus.UserItemClick;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomeBinding binding;

    NavController navController;
    TextView txt_user_name, txt_user_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHome.toolbar);

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_slideshow)
                .setDrawerLayout(drawer)
                .build();
        navigationView = findViewById(R.id.nav_view);
         navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        txt_user_name = headerView.findViewById(R.id.txt_name);
        txt_user_phone = headerView.findViewById(R.id.txt_phone);

        txt_user_phone.setText(Common.currentUser.getPhoneAdmin());
        txt_user_name.setText(Common.currentUser.getNameAdmin());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
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

        if(EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().unregister(this);

        super.onStop();

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_signout) {
            signOut();
        }
        else if(id==R.id.nav_banner){
           startActivity(new Intent(HomeActivity.this,QlBannerActivity.class));
        }
        else if(id==R.id.nav_view_banner){
            startActivity(new Intent(HomeActivity.this,ViewBannerActivity.class));
        }
        else if(id==R.id.nav_view_report){
            startActivity(new Intent(HomeActivity.this,ViewReportActivity.class));
        }
        else if(id==R.id.nav_add_new_admin){
            startActivity(new Intent(HomeActivity.this,AddNewAdmin.class));
        }else if(id==R.id.nav_manage_admin){
            startActivity(new Intent(HomeActivity.this,ManagerAdmin.class));
        }
        else if(id==R.id.nav_update_password){
            startActivity(new Intent(HomeActivity.this,UpdatePasswordActivity.class));
        }


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void signOut() {
            FirebaseAuth.getInstance().signOut();
            Common.currentUser = null;
            startActivity(new Intent(HomeActivity.this, MainActivity.class));
            finish();

    }


    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    public void UserItemClick(UserItemClick event) {
        if (event.isSuccess())
           navController.navigate(R.id.nav_detai_user);
    }
}