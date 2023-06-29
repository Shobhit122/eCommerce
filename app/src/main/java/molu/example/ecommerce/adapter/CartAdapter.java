package molu.example.ecommerce.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import molu.example.ecommerce.ItemDetailActivity;
import molu.example.ecommerce.R;
import molu.example.ecommerce.domain.Helmet;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{


    private Context context;
    private List<String> cartItemList;
    private FirebaseFirestore db;

    private Map<String, String> sizeChosen;
    List<String> sizes;


    public CartAdapter(Context context, List<String> cartItemList) {
        this.context = context;
        this.cartItemList = cartItemList;
    }

    public Map<String, String> getSizeChosen() {
        return sizeChosen;
    }

    public List<String> getCartItemList() {

        return cartItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_cart_item, parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        db = FirebaseFirestore.getInstance();
        sizeChosen = new HashMap<>();
        db.collection("Helmets")
                .whereEqualTo("pid",cartItemList.get(position).split(" ")[0])
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isComplete()){
                            Helmet helmet = new Helmet();
                            helmet = task.getResult().getDocuments().get(0).toObject(Helmet.class);

                            sizes = new ArrayList<>();
                            Picasso.get()
                                    .load(helmet.getUrl())
                                    .into(holder.cartProdImg);
                            holder.cartProdName.setText(helmet.getName());
                            holder.cartProdPrice.setText("Rs. " + helmet.getPrice());
                            holder.cartProdMrp.setText("Rs. " + helmet.getMrp());
                            holder.cartProdMrp.setPaintFlags(holder.cartProdMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                            List<String> sizes = new ArrayList<>();
                            holder.sizeText.setText(cartItemList.get(holder.getAdapterPosition()).split(" ")[1]);



                            for(Map.Entry<String, Integer> size : helmet.getSize().entrySet()){
                                String entry = size.getKey().substring(0,2).trim();
                                sizes.add(entry);
                            }
                            ArrayAdapter spinnerAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, sizes);
                            spinnerAdapter.setDropDownViewResource(
                                    android.R.layout
                                            .simple_spinner_dropdown_item);
                            holder.cartSizeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                                    sizeChosen.put(cartItemList.get(holder.getAdapterPosition()), sizes.get(i));
                                    //Toast.makeText(context, cartItemList.get(holder.getAdapterPosition()) + adapterView.getItemAtPosition(i), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> adapterView) {

                                }
                            });
                            holder.cartSizeSpinner.setAdapter(spinnerAdapter);
                            holder.cartDeleteItemImg.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    sizeChosen.remove(cartItemList.get(holder.getAdapterPosition()));
                                    cartItemList.remove(holder.getAdapterPosition());
                                    db.collection("Users")
                                            .document(FirebaseAuth.getInstance().getUid())
                                            .update("cart", cartItemList)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if(task.isComplete()){
                                                        Toast.makeText(context, "Item removed!", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            }).addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(context, "Could'nt remove item", Toast.LENGTH_SHORT).show();
                                                    Log.d("CartAdapter", e.toString());
                                                }
                                            });

                                    notifyItemRemoved(holder.getAdapterPosition());
                                }
                            });






                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("CartAdapter", e.toString());
                    }
                });

    }

    @Override
    public int getItemCount() {
        return cartItemList.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{

        private ImageView cartProdImg;
        private TextView cartProdName;
        private TextView cartProdPrice;
        private TextView cartProdMrp;
        private Spinner cartSizeSpinner;
        private Spinner cartQtySpinner;
        private ImageView cartDeleteItemImg;
        private TextView sizeText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cartProdImg = itemView.findViewById(R.id.cartProdImg);
            cartProdName = itemView.findViewById(R.id.cartProdName);
            cartProdPrice = itemView.findViewById(R.id.cartProdPrice);
            cartProdMrp = itemView.findViewById(R.id.cartProdMrp);
            cartSizeSpinner = itemView.findViewById(R.id.cartSizeSpinner);
            cartQtySpinner = itemView.findViewById(R.id.cartQtySpinner);
            cartDeleteItemImg = itemView.findViewById(R.id.cartDeleteItemImg);
            sizeText = itemView.findViewById(R.id.sizeText);
        }
    }
}
