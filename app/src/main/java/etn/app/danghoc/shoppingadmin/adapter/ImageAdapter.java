package etn.app.danghoc.shoppingadmin.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

import etn.app.danghoc.shoppingadmin.Interface.IClickDeleteImage;
import etn.app.danghoc.shoppingadmin.R;
import etn.app.danghoc.shoppingadmin.model.Banner;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.MyViewHolder>{
    Context context;
    List<Banner>mList;
    IClickDeleteImage clickDeleteImage;

    public ImageAdapter(Context context, List<Banner> mList, IClickDeleteImage clickDeleteImage) {
        this.context = context;
        this.mList = mList;
        this.clickDeleteImage = clickDeleteImage;
    }



    @NonNull
    @Override
    public ImageAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_image_banner, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.MyViewHolder holder, int position) {
        if(mList.get(holder.getAdapterPosition()).getUrlHinhAnh()!=null){
            Picasso.get().load(mList.get(position).getUrlHinhAnh()).into(holder.imageView);
        }

        holder.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickDeleteImage.onClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;
        ImageButton btn_delete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_pd);
            btn_delete=itemView.findViewById(R.id.btn_delete);
        }
    }
}
