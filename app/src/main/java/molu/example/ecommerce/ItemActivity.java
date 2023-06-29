package molu.example.ecommerce;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import molu.example.ecommerce.adapter.FeaturedAdapter;
import molu.example.ecommerce.databinding.ActivityItemBinding;
import molu.example.ecommerce.domain.Helmet;

public class ItemActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    ActivityItemBinding binding;

    private List<Helmet> helmetList;

    private FeaturedAdapter adapter;
    private RecyclerView recyclerView;

    private CollectionReference reference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_item);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getWindow().setStatusBarColor(ContextCompat.getColor(getApplicationContext(),R.color.white));

        db = FirebaseFirestore.getInstance();

        helmetList = new ArrayList<>();

        String type = getIntent().getStringExtra("type");
        binding.headingText.setText(type.toUpperCase());

        reference = db.collection("Helmets");

        reference.whereArrayContains("tags",type)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isComplete()){
                            for (QueryDocumentSnapshot document:
                                    task.getResult()) {
                                if(document.getLong("stock")>0) {
                                    helmetList.add(document.toObject(Helmet.class));
                                    adapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ItemActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        adapter = new FeaturedAdapter(this, helmetList);
        binding.recyclerViewItemList.setLayoutManager(new GridLayoutManager(this,2));
        binding.recyclerViewItemList.setAdapter(adapter);








    }
}