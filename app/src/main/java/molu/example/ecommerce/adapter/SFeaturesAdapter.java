package molu.example.ecommerce.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import molu.example.ecommerce.R;

public class SFeaturesAdapter extends RecyclerView.Adapter<SFeaturesAdapter.ViewHolder>{

    private Context context;
    private List<String> SFeaturesList;

    public SFeaturesAdapter(Context context, List<String> SFeaturesList) {
        this.context = context;
        this.SFeaturesList = SFeaturesList;
    }

    @NonNull
    @Override
    public SFeaturesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.single_salient_feature_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SFeaturesAdapter.ViewHolder holder, int position) {
        holder.featureTextView.setText(SFeaturesList.get(position));
    }

    @Override
    public int getItemCount() {
        return SFeaturesList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView featureTextView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            featureTextView = itemView.findViewById(R.id.featureTextView);
        }
    }
}
