package molu.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import molu.example.ecommerce.adapter.CartAdapter;
import molu.example.ecommerce.databinding.ActivityCartBinding;
import molu.example.ecommerce.domain.User;

public class CartActivity extends AppCompatActivity {


    private ActivityCartBinding binding;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private List<String> cartItemList;
    private CartAdapter adapter;
    private Map<String,String> sizeChosen;
    private String address;
    private String[] sizes;
    private String[] cart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_cart);



        cartItemList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
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
                                Toast.makeText(CartActivity.this, "Cart is Empty!", Toast.LENGTH_SHORT).show();
                            }else{
                                cartItemList = user.getCart();
                                adapter = new CartAdapter(getApplicationContext(), cartItemList);
                                binding.recyclerViewForCart.setLayoutManager(new LinearLayoutManager(getApplicationContext(), RecyclerView.VERTICAL,false));
                                binding.recyclerViewForCart.setAdapter(adapter);

                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId() == R.id.cart){

                }
                if(item.getItemId() == R.id.search){
                    Toast.makeText(CartActivity.this, "Implement karunga!!!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });
        sizeChosen = new HashMap<>();
        binding.floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sizeChosen = adapter.getSizeChosen();
                sizes = new String[sizeChosen.size()];
                int i=0;
                for(Map.Entry<String,String> entry: sizeChosen.entrySet()){
                    sizes[i++] = (entry.getKey() + " " + entry.getValue());
                    //Toast.makeText(CartActivity.this, entry.getKey() + " " + entry.getValue(), Toast.LENGTH_SHORT).show();
                }


                i=0;
                cart = new String[adapter.getCartItemList().size()];
                for ( String item:
                        adapter.getCartItemList()) {
                    cart[i++] = item;
                }



                Intent intent = new Intent(CartActivity.this, AddressActivity.class);
                intent.putExtra("sizes",sizes);
                intent.putExtra("cart",cart);




                startActivity(intent);


            }
        });











    }
}