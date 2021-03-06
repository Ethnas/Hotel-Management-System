package no.ntnu.hmsproject.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.DamageReport;
import no.ntnu.hmsproject.ui.hmsservice.damagereport.FragmentDamReportListDirections;

public class DamageReportListAdapter extends RecyclerView.Adapter<DamageReportListAdapter.DamageReportViewHolder> {
    public List<DamageReport> damageReportList;
    OnClickListener listener = position -> {};

    public DamageReportListAdapter() {
        this.damageReportList = new ArrayList<>();
    }

    public List<DamageReport> getDamageReportList() {
        return this.damageReportList;
    }

    public void setOnClickListener(OnClickListener listener) {
        this.listener = listener;
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
        holder.damageReportId.setText(damageReportList.get(position).getReportId());
        holder.damageReportTitle.setText(damageReportList.get(position).getDamageTitle());
        }

    @Override
    public int getItemCount() {
        return damageReportList.size();
    }

    interface OnClickListener {
        void onClick(int position);
    }

    public void setDamageReportList(ArrayList<DamageReport> list) {
        this.damageReportList = list;
        notifyDataSetChanged();
    }

    class DamageReportViewHolder extends RecyclerView.ViewHolder {
        TextView damageReportId;
        TextView damageReportTitle;

        public DamageReportViewHolder(@NonNull View view) {
            super(view);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView reportView = view.findViewById(R.id.damageReportId);
                    int reportId = Integer.parseInt(reportView.getText().toString());
                    System.out.println(reportId);
                    FragmentDamReportListDirections.ActionNavDamrepDetails action = FragmentDamReportListDirections.actionNavDamrepDetails(reportId);
                    Navigation.findNavController(view).navigate(action);
                }
            });
            this.damageReportId = view.findViewById(R.id.damageReportId);
            this.damageReportTitle = view.findViewById(R.id.damageReportTitle);
        }
    }
}



