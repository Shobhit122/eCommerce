package molu.example.ecommerce.adapter;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import molu.example.ecommerce.R;
import molu.example.ecommerce.domain.Address;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Context context;
    private List<Address> address;
    private int checkedPosition = 0;


    public AddressAdapter(Context context, List<Address> address) {
        this.context = context;
        this.address = address;

    }

    public void setAddress(List<Address> address){
        this.address = new ArrayList<>();
        this.address = address;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_address_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(address.get(position));
    }

    @Override
    public int getItemCount() {
        return address.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView address;
        private ImageView tickImg;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            address = itemView.findViewById(R.id.addressText);
            tickImg = itemView.findViewById(R.id.checkImg);
        }

        void bind(final Address address){
            if(checkedPosition == -1){
                tickImg.setVisibility(View.GONE);
            }else{
                if(checkedPosition == getAdapterPosition()){
                    tickImg.setVisibility(View.VISIBLE);
                }
                else{
                    tickImg.setVisibility(View.GONE);
                }
            }

            this.address.setText(address.getName());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tickImg.setVisibility(View.VISIBLE);
                    if(checkedPosition!=getAdapterPosition()){
                        notifyItemChanged(checkedPosition);
                        checkedPosition = getAdapterPosition();
                    }
                }
            });
        }

    }
    public Address getSelected(){
        if(checkedPosition!=-1){
            return address.get(checkedPosition);
        }
        return null;
    }
}
