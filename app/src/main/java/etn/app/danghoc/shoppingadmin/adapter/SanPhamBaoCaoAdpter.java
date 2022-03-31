package etn.app.danghoc.shoppingadmin.adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import etn.app.danghoc.shoppingadmin.Interface.IClickSanPhamBaoCao;
import etn.app.danghoc.shoppingadmin.R;
import etn.app.danghoc.shoppingadmin.common.Common;
import etn.app.danghoc.shoppingadmin.model.SanPham;
import etn.app.danghoc.shoppingadmin.model.SanPhamBaoCao;

public class SanPhamBaoCaoAdpter extends RecyclerView.Adapter<SanPhamBaoCaoAdpter.ViewHolder>{
    Context context;
    List<SanPhamBaoCao> sanPhams;
    IClickSanPhamBaoCao iClickSanPhamBaoCao;

    public SanPhamBaoCaoAdpter(Context context, List<SanPhamBaoCao> cartItems,IClickSanPhamBaoCao iClickSanPhamBaoCao) {
        this.context = context;
        this.sanPhams = cartItems;
        this.iClickSanPhamBaoCao=iClickSanPhamBaoCao;
    }

    @NonNull
    @Override
    public SanPhamBaoCaoAdpter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SanPhamBaoCaoAdpter.ViewHolder(LayoutInflater.
                from(context).inflate(R.layout.item_report, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull SanPhamBaoCaoAdpter.ViewHolder holder, int position) {
        Glide.with(context).load(sanPhams.get(position).getHinh())
                .into(holder.imgCart);
        holder.txtFoodName.setText(sanPhams.get(position).getTenSP());
        holder.txtFoodPrice.setText(Common.formatPrice(sanPhams.get(position).getGiaSP()));



        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Bạn có thực sự  muốn xoá ?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "có",
                        (dialog, id) -> iClickSanPhamBaoCao.onClickButtonDelete(position));

                builder1.setNegativeButton(
                        "Không",
                        (dialog, id) -> dialog.cancel());

                AlertDialog alert11 = builder1.create();
                alert11.show();

            }
        });
        holder.btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder1 = new AlertDialog.Builder(context);
                builder1.setMessage("Bạn có muốn bỏ qua sản phẩm này?");
                builder1.setCancelable(true);
                builder1.setPositiveButton(
                        "có",
                        (dialog, id) -> iClickSanPhamBaoCao.onClickButtonUpdate(position));

                builder1.setNegativeButton(
                        "Không",
                        (dialog, id) -> dialog.cancel());

                AlertDialog alert11= builder1.create();
                alert11.show();


            }
        });

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
        @BindView(R.id.btn_delete)
        ImageButton btn_delete;
        @BindView(R.id.btn_edit)
        ImageButton btn_edit;
        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
