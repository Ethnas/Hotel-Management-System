package no.ntnu.hmsproject.ui.hmsservice.damagereport;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import no.ntnu.hmsproject.MarginDecoration;
import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.adapter.DamageReportAdapter;
import no.ntnu.hmsproject.domain.DamageReport;
import no.ntnu.hmsproject.network.ApiLinks;
import no.ntnu.hmsproject.network.GsonRequest;

public class FragmentDamReportList extends Fragment {
    private RecyclerView recyclerView;
    private RelativeLayout relativeLayout;
    private ArrayList<DamageReport> damageReports = new ArrayList<>();
    private ArrayList<DamageReport> filteredReports = new ArrayList<>();
    private DamageReportAdapter damageReportAdapter = new DamageReportAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dam_report_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_dam_report);
        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(damageReportAdapter);
        recyclerView.addItemDecoration(new MarginDecoration(10));

        getDamage(() -> damageReportAdapter.notifyDataSetChanged());

        return view;
    }

    private void getDamage(Runnable callback){
        System.out.println("Got to get damage");
        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        GsonRequest gsonRequest = new GsonRequest(
                ApiLinks.GET_ALL_DAMAGE_REPORT_URL,
                Request.Method.GET,
                DamageReport[].class,
                new HashMap<String, String>(),
                new Response.Listener<DamageReport[]>() {
                    @Override
                    public void onResponse(DamageReport[] response) {
                        System.out.println("Got damage response");
                        System.out.println(response.length);
                        damageReports.clear();
                        damageReports.addAll(Arrays.asList(response));
                        System.out.println(damageReports);
                        damageReportAdapter.setDamageReportList(damageReports);
                        if (callback != null) {
                            callback.run();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                });
        requestQueue.add(gsonRequest);
    }
}