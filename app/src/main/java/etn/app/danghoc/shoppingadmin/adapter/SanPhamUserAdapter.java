package etn.app.danghoc.shoppingadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import etn.app.danghoc.shoppingadmin.R;
import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.model.SanPham;

public class SanPhamUserAdapter extends RecyclerView.Adapter<SanPhamUserAdapter.ViewHolder> {

    Context context;
    List<SanPham> sanPhams;

    public SanPhamUserAdapter(Context context, List<SanPham> cartItems) {
        this.context = context;
        this.sanPhams = cartItems;
    }

    @NonNull
    @NotNull
    @Override
    public SanPhamUserAdapter.ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        return new SanPhamUserAdapter.ViewHolder(LayoutInflater.
                from(context).inflate(R.layout.item_sanpham_user, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull SanPhamUserAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(sanPhams.get(position).getHinh())
                .into(holder.imgCart);
        holder.txtFoodName.setText(sanPhams.get(position).getTenSP());
        holder.txtFoodPrice.setText(Common.formatPrice(sanPhams.get(position).getGiaSP()));
    }

    @Override
    public int getItemCount() {
        return sanPhams.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private Unbinder unbinder;
        @BindView(R.id.imgCart)
        ImageView imgCart;
        @BindView(R.id.txt_price)
        TextView txtFoodPrice;
        @BindView(R.id.txtFoodName)
        TextView txtFoodName;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
