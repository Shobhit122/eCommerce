package molu.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import molu.example.ecommerce.adapter.AddressAdapter;
import molu.example.ecommerce.domain.Address;
import molu.example.ecommerce.domain.User;

public class AddressActivity extends AppCompatActivity {

    private RecyclerView recyclerView;

    static List<Address> addresses = new ArrayList<>();
    private AddressAdapter adapter;

    private FirebaseFirestore mStore;
    private FirebaseAuth mAuth;

    private Button btn;
    private Button addAddressBtn;

    private String pid;

    private String[] sizes;
    private String[] cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));

        mStore = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        sizes = getIntent().getStringArrayExtra("sizes");
        cart = getIntent().getStringArrayExtra("cart");








        recyclerView = findViewById(R.id.addressRecyclerView);
        adapter = new AddressAdapter(this,addresses);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL, false));
        recyclerView.setAdapter(adapter);

        btn = findViewById(R.id.continueBtn);


//        Toast.makeText(this, mAuth.getUid(), Toast.LENGTH_SHORT).show();
        mStore.collection("Users")
                .document(mAuth.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isComplete()){
                            DocumentSnapshot document = task.getResult();
                            User user = new User();
                            user = document.toObject(User.class);
                            List<String> addressList = new ArrayList<>();



                            if(user.getAddress()!=null) {
                                addressList = user.getAddress();

                                if (addressList.size() == 0) {
                                    Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                                addresses.clear();
                                for (String address :
                                        addressList) {
                                    Address obj = new Address();
                                    obj.setName(address);
                                    addresses.add(obj);
                                }
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(AddressActivity.this, "Something went wrong!!", Toast.LENGTH_SHORT).show();
                        Log.d("AddressActivity",e.toString());

                    }
                });

        addAddressBtn = findViewById(R.id.addAddressBtn);

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddressActivity.this, AddAddressActivity.class);
                startActivity(intent);
                finish();
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
                    Intent intent = new Intent(AddressActivity.this, CartActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.search){
                    Toast.makeText(AddressActivity.this, "Implement karunga!!!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(adapter.getSelected() !=null){
                    //Toast.makeText(AddressActivity.this, adapter.getSelected().getName(), Toast.LENGTH_SHORT).show();

                    Intent intent;
                    intent = new Intent(AddressActivity.this, CheckoutActivity.class);
                    intent.putExtra("address", adapter.getSelected().getName());
                    intent.putExtra("sizes", sizes);
                    intent.putExtra("cart", cart);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(AddressActivity.this, "Nothing selected", Toast.LENGTH_SHORT).show();
                }
            }
        });



    }
}