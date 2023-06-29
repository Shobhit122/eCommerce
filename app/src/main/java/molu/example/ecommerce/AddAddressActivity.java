package molu.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import molu.example.ecommerce.domain.User;

public class AddAddressActivity extends AppCompatActivity {

    private FirebaseFirestore mStore;

    private FirebaseAuth mAuth;
    private static User user;
    private List<String> addressList;

    private Button addAddressBtn;

    private EditText edt1;
    private EditText edt2;
    private EditText edt3;
    String newAddress = null;

    CollectionReference collection;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_address);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));

        mStore = FirebaseFirestore.getInstance();
        collection = mStore.collection("Users");
        mAuth = FirebaseAuth.getInstance();
        user = new User();


        edt1 = findViewById(R.id.addr1);
        edt2 = findViewById(R.id.addr2);
        edt3 = findViewById(R.id.addr3);

        addAddressBtn = findViewById(R.id.addButton);

        addAddressBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String str = edt1.getText().toString();

                str = str + ", " + edt2.getText();

                str = str + ", " + edt3.getText();

                newAddress = str;


                collection.document(mAuth.getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                user = task.getResult().toObject(User.class);

                                if(user.getAddress()!=null)
                                    addressList = user.getAddress();
                                else
                                    addressList = new ArrayList<>();


                                addressList.add(newAddress);
                                user.setAddress(addressList);

                                collection.document(mAuth.getUid())
                                        .update("address", addressList)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Toast.makeText(AddAddressActivity.this, "Address added successfully!", Toast.LENGTH_SHORT).show();
                                                finish();
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(AddAddressActivity.this, "Something Went wrong!", Toast.LENGTH_SHORT).show();
                                                Log.d("AddAddressActivity.class", e.toString());
                                            }
                                        });


                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(AddAddressActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                Log.d("AddAddressActivity!", e.toString());
                            }
                        });

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
                    Intent intent = new Intent(AddAddressActivity.this, CartActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.search){
                    Toast.makeText(AddAddressActivity.this, "Implement karunga!!!", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });







    }
}