package no.ntnu.hmsproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import no.ntnu.hmsproject.R;

public class DamageReportAdapter extends RecyclerView.Adapter<DamageReportAdapter.DamageReportViewHolder> {
    List<DamageReport> 

    public DamageReportAdapter() {

    }

    @NonNull
    @Override
    public DamageReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent,
                                             int viewType) {
        ConstraintLayout layout = (ConstraintLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_dam_report_list,
                        parent,
                        false);

        DamageReportViewHolder viewHolder = new DamageReportViewHolder(layout);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DamageReportViewHolder holder, int position) {


    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class DamageReportViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView damageReportId;
        TextView damageeReportTitle;

        public DamageReportViewHolder(@NonNull View view) {
            super(view);
            view.setOnClickListener (v -> onClick(getAdapterPosition()));
            this.damageReportId = view.findViewById(R.id.damageReportId);
            this.damageeReportTitle = view.findViewById(R.id.damageReportTitle);
        }
    }
}
