package no.ntnu.hmsproject.ui.hmsservice.damagereport;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.adapter.DamageReportAdapter;
import no.ntnu.hmsproject.domain.DamageReport;
import no.ntnu.hmsproject.network.ApiLinks;

public class FragmentDamReportList extends Fragment {
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private ArrayList<DamageReport> damageReports = new ArrayList<>();
    private DamageReportAdapter damageReportAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dam_report_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_dam_report);



        return view;
    }
}