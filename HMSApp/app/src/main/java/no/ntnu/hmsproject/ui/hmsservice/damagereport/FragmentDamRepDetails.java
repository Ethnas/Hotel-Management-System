package no.ntnu.hmsproject.ui.hmsservice.damagereport;

import android.app.VoiceInteractor;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import no.ntnu.hmsproject.MarginDecoration;
import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.adapter.DamageReportImageAdapter;
import no.ntnu.hmsproject.domain.DamageImage;
import no.ntnu.hmsproject.domain.DamageReport;
import no.ntnu.hmsproject.domain.LoggedUser;
import no.ntnu.hmsproject.network.ApiLinks;
import no.ntnu.hmsproject.network.GsonRequest;
import no.ntnu.hmsproject.ui.hmsservice.booking.FragmentBookingDetails;


public class FragmentDamRepDetails extends Fragment {
    TextView id;
    TextView desc;
    TextView title;
    Button viewBooking;
    DamageReport damageReport;
    public List<DamageImage> images = new ArrayList<>();
    private DamageReportImageAdapter damageReportImageAdapter = new DamageReportImageAdapter();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(@Nullable LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dam_rep_details, container, false);
        this.id = view.findViewById(R.id.damrep_detail_id);
        this.title = view.findViewById(R.id.damrep_details_title);
        this.desc = view.findViewById(R.id.damrep_detail_desc);
        this.viewBooking = view.findViewById(R.id.damrep_view_booking);
        assert getArguments() != null;
        FragmentDamRepDetailsArgs args = FragmentDamRepDetailsArgs.fromBundle(getArguments());
        int reportId = args.getReportId();
        getDamageReport(reportId);
        getDamageImages(reportId);
        RecyclerView recyclerView = view.findViewById(R.id.recycler_view_dam_report_images);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(damageReportImageAdapter);
        recyclerView.addItemDecoration(new MarginDecoration(5));

        viewBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int bookingId = Integer.parseInt(damageReport.getBooking().getBookingId());
                FragmentDamRepDetailsDirections.ActionNavDamrepDetailsToBooking action = FragmentDamRepDetailsDirections.actionNavDamrepDetailsToBooking(bookingId);
                Navigation.findNavController(v).navigate(action);
            }
        });

        return view;
    }

    private void getDamageReport(int reportId) {
        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);

        String report = Integer.toString(reportId);

        String url = ApiLinks.GET_SPECIFIC_DAMAGE_REPORT + "?reportid=" + report;

        GsonRequest gsonRequest = new GsonRequest(
                url, Request.Method.GET,
                DamageReport.class,
                new HashMap<String, String>(),
                new Response.Listener<DamageReport>() {
                    @Override
                    public void onResponse(DamageReport response) {
                        damageReport = response;
                        setTextFields();
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

    private void getDamageImages(int reportId) {
        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        String report = Integer.toString(reportId);
        String url = ApiLinks.GET_DAMAGE_REPORT_IMAGES + "?reportid=" + report;
        HashMap<String, String> headers = new HashMap<>();
        String authToken = "Bearer " + LoggedUser.getInstance().getJwt();
        System.out.println(authToken);
        headers.put("Authorization", authToken);

        GsonRequest gsonRequest = new GsonRequest(
                url, Request.Method.GET,
                DamageImage[].class,
                headers,
                new Response.Listener<DamageImage[]>() {
                    @Override
                    public void onResponse(DamageImage[] response) {
                        images.clear();
                        images.addAll(Arrays.asList(response));
                        damageReportImageAdapter.setDamageImageList(images);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.println(error);
                    }
                }
        );
        requestQueue.add(gsonRequest);
    }

    public List<DamageImage> getImages() {
        return this.images;
    }

    private void setTextFields() {
        id.setText("Report ID: " + damageReport.getReportId());
        title.setText("Title: " + damageReport.getDamageTitle());
        desc.setText("Description: " + damageReport.getDamageDescription());
    }
}