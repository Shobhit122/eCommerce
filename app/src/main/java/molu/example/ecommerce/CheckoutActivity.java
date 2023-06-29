package molu.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import molu.example.ecommerce.adapter.CartAdapter;
import molu.example.ecommerce.adapter.CheckoutAdapter;
import molu.example.ecommerce.databinding.ActivityCheckoutBinding;
import molu.example.ecommerce.domain.Helmet;
import molu.example.ecommerce.domain.User;

public class CheckoutActivity extends AppCompatActivity {
    private static ActivityCheckoutBinding binding;
    private String address;
    private String[] sizes;
    private String[] cart;
    private List<String> cartItemList;
    private static int priceTotal;
    private static int mrpTotal;
    private FirebaseFirestore db;
    private static CollectionReference reference;
    private float discountVal;
    private List<List<String>> cartSizeList;
    private CheckoutAdapter adapter;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_checkout);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));


        address = getIntent().getStringExtra("address");
        sizes = getIntent().getStringArrayExtra("sizes");
        cart = getIntent().getStringArrayExtra("cart");


        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        reference = db.collection("Helmets");
        binding.addressView.setText(address);

        priceTotal = 0;
        mrpTotal = 0;
        discountVal =0;
        cartSizeList = new ArrayList<>();



        for (String s:
             sizes) {
            String[] arr = s.split("\\s");
            cartSizeList.add(new ArrayList<String>(){
                {
                    add(arr[0]);
                    add(arr[1]);
                }
            });

        }
        db.collection("Users")
                .document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isComplete()){
                            User user = new User();

                            user = task.getResult().toObject(User.class);
                            if(user.getCart()== null){
                                Toast.makeText(CheckoutActivity.this, "Cart is Empty!", Toast.LENGTH_SHORT).show();
                            }else{
                                cartItemList = user.getCart();
                                adapter = new CheckoutAdapter(cartItemList,CheckoutActivity.this);
                                binding.recyclerViewForCheckout.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
                                binding.recyclerViewForCheckout.setAdapter(adapter);
                                fillPrices(cartItemList);

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }

    static void fillPrices(List<String> cartItemList){
        for (String item:
                cartItemList) {
            reference.whereEqualTo("pid", item.split(" ")[0])
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if(task.isComplete()){
                                for (QueryDocumentSnapshot document:
                                        task.getResult()) {
                                    Helmet helmet = document.toObject(Helmet.class);
                                    priceTotal = priceTotal + helmet.getPrice();
                                    mrpTotal = mrpTotal + helmet.getMrp();

                                }
                                binding.subtotalView.setText("Rs. " + mrpTotal);

                                binding.discount.setText("Rs. " + (mrpTotal - priceTotal));
                                binding.priceTotalView.setText("Rs. " + priceTotal);
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("CheckOutActivity", e.toString());
                        }
                    });
        }
    }
}