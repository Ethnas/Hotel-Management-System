package no.ntnu.hmsproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.DamageReport;

public class DamageReportAdapter extends RecyclerView.Adapter<DamageReportAdapter.DamageReportViewHolder> {
    List<DamageReport> damageReportList;

    public DamageReportAdapter() {
        this.damageReportList = new ArrayList<>();
    }

    @NonNull
    @Override
    public DamageReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_damrep, parent, false);
        return new DamageReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DamageReportViewHolder holder, int position) {
        holder.damageReportId.setText(damageReportList.get(position).getBookingId());
        holder.damageReportTitle.setText(damageReportList.get(position).getDamageTitle());
        }

    @Override
    public int getItemCount() {
        return damageReportList.size();
    }

    public void setDamageReportList(ArrayList<DamageReport> list) {
        this.damageReportList = list;
        notifyDataSetChanged();
    }

    class DamageReportViewHolder extends RecyclerView.ViewHolder {
        View view;
        TextView damageReportId;
        TextView damageReportTitle;

        public DamageReportViewHolder(@NonNull View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            this.damageReportId = view.findViewById(R.id.damageReportId);
            this.damageReportTitle = view.findViewById(R.id.damageReportTitle);
        }
    }
}



