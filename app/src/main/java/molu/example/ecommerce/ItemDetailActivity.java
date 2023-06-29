package molu.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import molu.example.ecommerce.adapter.SFeaturesAdapter;
import molu.example.ecommerce.databinding.ActivityItemDetailBinding;
import molu.example.ecommerce.domain.Helmet;
import molu.example.ecommerce.domain.User;

public class ItemDetailActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private ActivityItemDetailBinding binding;
    private List<Helmet> helmetList;


    private List<String> SFeaturesList;
    private SFeaturesAdapter sFeaturesAdapter;
    private RecyclerView sFeaturesRecyclerView;

    private Spinner spinner;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    CollectionReference collectionReference = db.collection("Helmets");
    String pid;

    private String sizeChosen;

    List<String> sizes;
    private List<String> cart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item_detail);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));
        pid = getIntent().getStringExtra("pid");
        helmetList = new ArrayList<>();
        SFeaturesList = new ArrayList<>();

        sizes = new ArrayList<>();


        collectionReference.whereEqualTo("pid", pid)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @SuppressLint("NotifyDataSetChanged")
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document:
                                    task.getResult()) {
                                helmetList.add(document.toObject(Helmet.class));
                                //Toast.makeText(getApplicationContext(), document.getString("name"), Toast.LENGTH_SHORT).show();
                            }

                            Picasso.get()
                                    .load(helmetList.get(0).getUrl())
                                    .resize(300,300)
                                    .into(binding.prodImg);
                            binding.prodName.setText(helmetList.get(0).getName());
                            binding.prodPrice.setText("Rs. " + String.valueOf(helmetList.get(0).getPrice()));
                            binding.prodMrp.setText("Rs. " + String.valueOf(helmetList.get(0).getMrp()));
                            binding.prodMrp.setPaintFlags(binding.prodMrp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            SFeaturesList = helmetList.get(0).getFeatures();

                            FlexboxLayoutManager manager = new FlexboxLayoutManager(getApplicationContext());
                            manager.setFlexWrap(FlexWrap.WRAP);
                            manager.setFlexDirection(FlexDirection.ROW);
                            manager.setJustifyContent(JustifyContent.FLEX_START);
                            manager.setAlignItems(AlignItems.FLEX_START);

                            sFeaturesAdapter = new SFeaturesAdapter(getApplicationContext(), SFeaturesList);
                            binding.sfeaturesRecycler.setLayoutManager(manager);
                            binding.sfeaturesRecycler.setAdapter(sFeaturesAdapter);

                            for(Map.Entry<String, Integer> size : helmetList.get(0).getSize().entrySet()){
                                String entry = size.getKey();

                                if(size.getValue() > 0) {
                                    entry = entry + " : In - Stock" + "(" + size.getValue() + ")";
                                    sizes.add(entry);
                                }
                            }

                            ArrayAdapter spinnerAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_spinner_item, sizes);
                            spinnerAdapter.setDropDownViewResource(
                                    android.R.layout
                                            .simple_spinner_dropdown_item);
                            binding.sizeSpinner.setOnItemSelectedListener(ItemDetailActivity.this);
                            binding.sizeSpinner.setAdapter(spinnerAdapter);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ItemDetailActivity.this, "Something Went Wrong!! " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });



        binding.addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                db.collection("Users")
                        .document(FirebaseAuth.getInstance().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if(task.isComplete()) {
                                    User user = task.getResult().toObject(User.class);

                                    if (user.getCart() == null) {
                                        cart = new ArrayList<>();
                                    } else {
                                        cart = user.getCart();
                                    }
                                    if(!cart.contains(pid + " " + sizeChosen.split(" ")[0])) {
                                        cart.add(pid + " " + sizeChosen.split(" ")[0]);

                                        db.collection("Users")
                                                .document(FirebaseAuth.getInstance().getUid())
                                                .update("cart", cart)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isComplete()) {
                                                            Toast.makeText(ItemDetailActivity.this, "Item added to Cart!", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ItemDetailActivity.this, "Could'nt add item", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }else{
                                        Toast.makeText(ItemDetailActivity.this, "Item already in Cart", Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });
            }
        });

        binding.buyNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.collection("Users")
                        .document(FirebaseAuth.getInstance().getUid())
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                                if(task.isComplete()) {
                                    User user = task.getResult().toObject(User.class);

                                    if (user.getCart() == null) {
                                        cart = new ArrayList<>();
                                    } else {
                                        cart = user.getCart();
                                    }
                                    if(!cart.contains(pid + " " + sizeChosen.split(" ")[0])) {
                                        cart.add(pid + " " + sizeChosen.split(" ")[0]);

                                        db.collection("Users")
                                                .document(FirebaseAuth.getInstance().getUid())
                                                .update("cart", cart)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isComplete()) {
                                                            //Toast.makeText(ItemDetailActivity.this, "Item added to Cart!", Toast.LENGTH_SHORT).show();
                                                            Intent intent = new Intent(ItemDetailActivity.this, CartActivity.class);
                                                            startActivity(intent);
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ItemDetailActivity.this, "Could'nt add item", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }else{
                                        //Toast.makeText(ItemDetailActivity.this, "Item already in Cart", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(ItemDetailActivity.this, CartActivity.class);
                                        startActivity(intent);
                                    }

                                }

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });


            }
        });

        MaterialToolbar toolbar = findViewById(R.id.topAppBar);

        db.collection("Users")
                        .document(FirebaseAuth.getInstance().getUid())
                                .get()
                                        .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                            @Override
                                            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                                if(task.isComplete()){
                                                    User user = new User();
                                                    user = task.getResult().toObject(User.class);
                                                    List<String> fav = new ArrayList<>();
                                                    if(user.getFavorite()!=null){
                                                        fav = user.getFavorite();
                                                    }
                                                    if(fav.contains(pid)){
                                                        toolbar.getMenu().findItem(R.id.fav_fill).setVisible(true);
                                                    }else{
                                                        toolbar.getMenu().findItem(R.id.fav_emp).setVisible(true);
                                                    }
                                                }
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("ItemDetailActivity", e.toString());
                    }
                });

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
                    Intent intent = new Intent(ItemDetailActivity.this, CartActivity.class);
                    startActivity(intent);
                }
                if(item.getItemId() == R.id.search){
                    Toast.makeText(ItemDetailActivity.this, "Implement karunga!!!", Toast.LENGTH_SHORT).show();
                }
                if(item.getItemId() == R.id.fav_emp){
                    db.collection("Users")
                            .document(FirebaseAuth.getInstance().getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isComplete()){
                                        User user = new User();
                                        user = task.getResult().toObject(User.class);
                                        List<String> fav = new ArrayList<>();
                                        if(user.getFavorite() != null){
                                            fav = user.getFavorite();
                                        }
                                        fav.add(pid);
                                        db.collection("Users")
                                                .document(FirebaseAuth.getInstance().getUid())
                                                .update("favorite", fav)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isComplete()){
                                                            Toast.makeText(ItemDetailActivity.this, "Favorite added", Toast.LENGTH_SHORT).show();
                                                            toolbar.getMenu().findItem(R.id.fav_emp).setVisible(false);
                                                            toolbar.getMenu().findItem(R.id.fav_fill).setVisible(true);
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ItemDetailActivity.this, "Trouble adding favorite, try again later", Toast.LENGTH_SHORT).show();
                                                        Log.e("ItemDetailActivity", e.toString());
                                                    }
                                                });


                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ItemDetailActivity.this, "Trouble adding favorite, try again later", Toast.LENGTH_SHORT).show();
                                    Log.e("ItemDetailActivity", e.toString());
                                }
                            });
                }
                if(item.getItemId() == R.id.fav_fill){
                    db.collection("Users")
                            .document(FirebaseAuth.getInstance().getUid())
                            .get()
                            .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if(task.isComplete()){
                                        User user = new User();
                                        user = task.getResult().toObject(User.class);
                                        List<String> fav = new ArrayList<>();
                                        if(user.getFavorite() != null){
                                            fav = user.getFavorite();
                                        }
                                        fav.remove(pid);
                                        db.collection("Users")
                                                .document(FirebaseAuth.getInstance().getUid())
                                                .update("favorite", fav)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if(task.isComplete()){
                                                            Toast.makeText(ItemDetailActivity.this, "Favorite removed", Toast.LENGTH_SHORT).show();
                                                            toolbar.getMenu().findItem(R.id.fav_emp).setVisible(true);
                                                            toolbar.getMenu().findItem(R.id.fav_fill).setVisible(false);
                                                        }
                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                                    @Override
                                                    public void onFailure(@NonNull Exception e) {
                                                        Toast.makeText(ItemDetailActivity.this, "Trouble removing favorite, try again later", Toast.LENGTH_SHORT).show();
                                                        Log.e("ItemDetailActivity", e.toString());
                                                    }
                                                });


                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ItemDetailActivity.this, "Trouble adding favorite, try again later", Toast.LENGTH_SHORT).show();
                                    Log.e("ItemDetailActivity", e.toString());
                                }
                            });
                }


                return true;
            }
        });












    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        sizeChosen = sizes.get(i);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    public interface MyCallback{
        void onCallback(List<Helmet> helmetList);
    }
}