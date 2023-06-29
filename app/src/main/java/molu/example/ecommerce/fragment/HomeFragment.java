package molu.example.ecommerce.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import molu.example.ecommerce.ItemActivity;
import molu.example.ecommerce.R;
import molu.example.ecommerce.adapter.CategoryAdapter;
import molu.example.ecommerce.adapter.FeaturedAdapter;
import molu.example.ecommerce.domain.Category;
import molu.example.ecommerce.domain.Helmet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    FirebaseFirestore mStore;
    //For Category RecyclerView
    private List<Category> mCategoryList;
    private CategoryAdapter mCategoryAdapter;
    private RecyclerView mCategoryRecyclerView;

    //For Featured RecyclerView
    private List<Helmet> mFeaturedList;
    private FeaturedAdapter mFeaturedAdapter;
    private RecyclerView mFeaturedRecyclerView;

    //For Best-Sell Recycler
    private List<Helmet> mBestSellList;
    private FeaturedAdapter mBestSellAdapter;
    private RecyclerView mBestSellRecyclerView;


    private ImageView fullFaceImg;
    private ImageView halfFaceImg;
    private ImageView flipUpImg;
    private ImageView motocrossImg;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);
        mStore = FirebaseFirestore.getInstance();



        getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//  set status text dark
        getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(),R.color.white));// set status background white
        //Category RecyclerView Code
//        mCategoryList = new ArrayList<>();
//        mStore.collection("Category")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @SuppressLint("NotifyDataSetChanged")
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if(task.isSuccessful()){
//                            for (QueryDocumentSnapshot document:
//                                 task.getResult()) {
//                                mCategoryList.add(document.toObject(Category.class));
//                                mCategoryAdapter.notifyDataSetChanged();
//                            }
//                        }
//                    }
//                }).addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });

       //Category RecyclerView
//        mCategoryAdapter = new CategoryAdapter(getContext(), mCategoryList);
//        mCategoryRecyclerView = view.findViewById(R.id.category_recycler);
//        mCategoryRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
//        mCategoryRecyclerView.setAdapter(mCategoryAdapter);

        //Featured RecyclerView code

        mFeaturedList = new ArrayList<>();
        mStore.collection("Helmets")
                .whereArrayContains("tags", "featured")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isComplete()){
                            for (QueryDocumentSnapshot document:
                                    task.getResult()) {
                                if(document.getLong("stock")>0) {
                                    mFeaturedList.add(document.toObject(Helmet.class));
                                    mFeaturedAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        mFeaturedAdapter = new FeaturedAdapter(getContext(), mFeaturedList);
        mFeaturedRecyclerView = view.findViewById(R.id.feature_recycler);
        mFeaturedRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL,false));
        mFeaturedRecyclerView.setAdapter(mFeaturedAdapter);


        //For Best Seller Recycler View
        mBestSellList = new ArrayList<>();
        mStore.collection("Helmets")
                .whereArrayContains("tags","best-sell")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isComplete()){
                            for (QueryDocumentSnapshot document:
                                 task.getResult()) {
                                if(document.getLong("stock")>0) {
                                    mBestSellList.add(document.toObject(Helmet.class));
                                    mBestSellAdapter.notifyDataSetChanged();
                                }
                            }
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(view.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

        mBestSellAdapter = new FeaturedAdapter(getContext(), mBestSellList);
        mBestSellRecyclerView = view.findViewById(R.id.bestsell_recycler);
        mBestSellRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(),RecyclerView.HORIZONTAL,false));
        mBestSellRecyclerView.setAdapter(mBestSellAdapter);



        fullFaceImg = view.findViewById(R.id.full_face_img);
        halfFaceImg = view.findViewById(R.id.half_face_img);
        flipUpImg = view.findViewById(R.id.flip_up_img);
        motocrossImg = view.findViewById(R.id.moto_img);


        fullFaceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemActivity.class);
                intent.putExtra("type", "fullface");
                startActivity(intent);
            }
        });

        halfFaceImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemActivity.class);
                intent.putExtra("type", "openface");
                startActivity(intent);
            }
        });


        flipUpImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemActivity.class);
                intent.putExtra("type", "flipup");
                startActivity(intent);
            }
        });

        motocrossImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemActivity.class);
                intent.putExtra("type", "offroad");
                startActivity(intent);
            }
        });

        view.findViewById(R.id.fea_see_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemActivity.class);
                intent.putExtra("type", "featured");
                startActivity(intent);
            }
        });
        view.findViewById(R.id.best_sell).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ItemActivity.class);
                intent.putExtra("type", "best-sell");
                startActivity(intent);
            }
        });

        return view;
    }
}