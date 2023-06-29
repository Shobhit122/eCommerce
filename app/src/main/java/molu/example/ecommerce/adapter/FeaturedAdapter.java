package molu.example.ecommerce.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import molu.example.ecommerce.ItemDetailActivity;
import molu.example.ecommerce.R;
import molu.example.ecommerce.domain.Helmet;

public class FeaturedAdapter extends RecyclerView.Adapter<FeaturedAdapter.ViewHolder> {

    private Context context;
    private List<Helmet> mFeaturedList;

    public FeaturedAdapter(Context context, List<Helmet> mFeaturedList) {
        this.context = context;
        this.mFeaturedList = mFeaturedList;
    }


    @NonNull
    @Override
    public FeaturedAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_helmet_item_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FeaturedAdapter.ViewHolder holder, int position) {
        Picasso.get()
                .load(mFeaturedList.get(position).getUrl())
//                .resize(180,180)
                .into(holder.productImage);
        holder.productName.setText(mFeaturedList.get(position).getName());
        String priceText = "Rs. " + mFeaturedList.get(position).getPrice();
        holder.productPrice.setText(priceText);


        holder.productImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("pid", mFeaturedList.get(holder.getAdapterPosition()).getPid());
                context.startActivity(intent);

            }
        });

        holder.productName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ItemDetailActivity.class);
                intent.putExtra("pid", mFeaturedList.get(holder.getAdapterPosition()).getPid());
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return mFeaturedList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView productImage;
        private TextView productPrice;
        private TextView productName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            productPrice = itemView.findViewById(R.id.price_text);
            productName = itemView.findViewById(R.id.prod_name_text);


        }
    }
}
