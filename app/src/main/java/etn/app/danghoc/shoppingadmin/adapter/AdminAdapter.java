package etn.app.danghoc.shoppingadmin.adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import etn.app.danghoc.shoppingadmin.Interface.IClickAdmin;
import etn.app.danghoc.shoppingadmin.Interface.IOnRecycleViewClickListener;
import etn.app.danghoc.shoppingadmin.R;
import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.event_bus.UserItemClick;
import etn.app.danghoc.shoppingadmin.model.Admin;
import etn.app.danghoc.shoppingadmin.model.User;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.MyViewHolder>{

    Context context;
    List<Admin> userList;
    IClickAdmin iClickAdmin;

    public AdminAdapter(Context context, List<Admin> userList,IClickAdmin iClickAdmin) {
        this.context = context;
        this.userList = userList;
        this.iClickAdmin=iClickAdmin;
    }

    @NonNull
    @NotNull
    @Override
    public AdminAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new AdminAdapter.MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_admin,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(userList.get(position).getNameAdmin()==null){
            holder.txt_name.setText("tên: "+"test "+position);
        }else{
            holder.txt_name.setText("tên: "+userList.get(position).getNameAdmin());
        }

        holder.txt_user_name.setText("tên đăng nhập: "+userList.get(position).getIdAdmin());

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(userList.get(position).getIdAdmin().equals("root"))
                {
                    Toast.makeText(context, "đây là admin không được xoá", Toast.LENGTH_LONG).show();
                    return;
                }

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setMessage("bạn có thực sự muốn xoá");
                builder.setPositiveButton("xoá", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                            iClickAdmin.onClickButtonDelete(position);
                    }
                });
                builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                Dialog dialog=builder.create();
                dialog.show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return userList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {


        IOnRecycleViewClickListener listener;

        public void setListener(IOnRecycleViewClickListener listener) {
            this.listener = listener;
        }

        @BindView(R.id.txt_name)
        TextView txt_name;
        @BindView(R.id.txt_user_name)
        TextView txt_user_name;
        @BindView(R.id.btn_delete)
        TextView btn_delete;

        Unbinder unbinder;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            unbinder= ButterKnife.bind(this,itemView);
        }
    }
}