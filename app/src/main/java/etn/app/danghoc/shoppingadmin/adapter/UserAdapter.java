package etn.app.danghoc.shoppingadmin.adapter;

import android.content.Context;
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
import etn.app.danghoc.shoppingadmin.Interface.IOnRecycleViewClickListener;
import etn.app.danghoc.shoppingadmin.R;
import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.event_bus.UserItemClick;
import etn.app.danghoc.shoppingadmin.model.User;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.MyViewHolder>{

    Context context;
    List<User>userList;

    public UserAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @NotNull
    @Override
    public UserAdapter.MyViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_user,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull UserAdapter.MyViewHolder holder, int position) {
        holder.txt_name.setText("tên: "+userList.get(position).getNameUser());
        holder.txt_phone_number.setText("sđt: "+userList.get(position).getPhoneUser());
        holder.txt_status.setText(new StringBuilder("trạng thái:")
                .append(Common.convertStatusToString(userList.get(position).getTrangThai())).toString());
        holder.setListener(new IOnRecycleViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                Common.userSelect=userList.get(position);
                Common.positionUserSelect=position;
                EventBus.getDefault().postSticky(new UserItemClick(true,position));

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
        @BindView(R.id.txt_phone_number)
        TextView txt_phone_number;
        @BindView(R.id.txt_status)
        TextView txt_status;

        Unbinder unbinder;

        public MyViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            unbinder= ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(view -> listener.onClick(view,getAdapterPosition()));
        }
    }
}
