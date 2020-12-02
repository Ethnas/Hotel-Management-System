package no.ntnu.hmsproject.ui.hmsservice.booking;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import no.ntnu.hmsproject.MarginDecoration;
import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.adapter.BookingListAdapter;
import no.ntnu.hmsproject.domain.Booking;
import no.ntnu.hmsproject.network.ApiLinks;
import no.ntnu.hmsproject.network.GsonRequest;

public class FragmentBookingList extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Booking> bookings = new ArrayList<>();
    private BookingListAdapter adapter = new BookingListAdapter();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_booking_list, container, false);

        recyclerView = view.findViewById(R.id.recycler_view_booking);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new MarginDecoration(10));

        getBookings(() -> adapter.notifyDataSetChanged());

        return view;
    }

    private void getBookings(Runnable callback) {
        Context context = getActivity();
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        GsonRequest gsonRequest = new GsonRequest(
                ApiLinks.GET_ALL_BOOKING_URL,
                Request.Method.GET,
                Booking[].class,
                new HashMap<String, String>(),
                new Response.Listener<Booking[]>() {
                    @Override
                    public void onResponse(Booking[] response) {
                        System.out.println("Got booking response");
                        bookings.clear();
                        bookings.addAll(Arrays.asList(response));
                        adapter.setBookingsList(bookings);
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
                }
        );
        requestQueue.add(gsonRequest);
    }
}