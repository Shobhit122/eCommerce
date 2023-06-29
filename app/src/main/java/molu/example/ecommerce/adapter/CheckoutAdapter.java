package molu.example.ecommerce.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;

import molu.example.ecommerce.R;
import molu.example.ecommerce.domain.Helmet;

public class CheckoutAdapter extends RecyclerView.Adapter<CheckoutAdapter.ViewHolder>{


    private List<String> cartSizeList;
    private Context context;
    private FirebaseFirestore db;

    public CheckoutAdapter(List<String> cartSizeList, Context context) {
        this.cartSizeList = cartSizeList;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_checkout_item, parent,false);
        return new CheckoutAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        db.collection("Helmets")
                .whereEqualTo("pid",cartSizeList.get(position).split(" ")[0])
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isComplete()){
                            Helmet helmet = new Helmet();

                            helmet = task.getResult().getDocuments().get(0).toObject(Helmet.class);
                            //Toast.makeText(context,helmet.getPid(), Toast.LENGTH_SHORT).show();
                            Picasso.get()
                                    .load(helmet.getUrl())
                                    .into(holder.checkProdImg);
                            holder.checkProdName.setText(helmet.getName());
                            holder.checkProdPrice.setText("Rs. " + helmet.getPrice());
                            holder.checkProdMrp.setText("Rs. " + helmet.getMrp());
                            holder.checkProdMrp.setPaintFlags(holder.checkProdMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            holder.checkSizeText.setText(cartSizeList.get(holder.getAdapterPosition()).split(" ")[1]);

                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("CheckoutAdapter", e.toString());
                    }
                });

    }

    @Override
    public int getItemCount() {
        return cartSizeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView checkProdImg;
        private TextView checkProdName;
        private TextView checkProdPrice;
        private TextView checkProdMrp;
        private TextView checkSizeText;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            checkProdImg = itemView.findViewById(R.id.checkProdImg);
            checkProdName = itemView.findViewById(R.id.checkProdName);
            checkProdPrice = itemView.findViewById(R.id.checkProdPrice);
            checkProdMrp = itemView.findViewById(R.id.checkProdMrp);
            checkSizeText = itemView.findViewById(R.id.checkSizeText);

        }
    }
}
