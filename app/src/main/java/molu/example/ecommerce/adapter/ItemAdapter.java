package molu.example.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import molu.example.ecommerce.R;
import molu.example.ecommerce.domain.Helmet;

public class ItemAdapter extends RecyclerView.Adapter<ItemAdapter.ViewHolder> {



    private Context context;
    private List<Helmet> helmetList;


    public ItemAdapter(Context context, List<Helmet> helmetList) {
        this.context = context;
        this.helmetList = helmetList;
    }




    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_helmet_item_card,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
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
