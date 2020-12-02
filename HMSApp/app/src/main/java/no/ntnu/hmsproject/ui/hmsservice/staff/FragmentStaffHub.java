package no.ntnu.hmsproject.ui.hmsservice.staff;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.ui.hmsservice.damagereport.FragmentDamReportMainDirections;


public class FragmentStaffHub extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_hub, container, false);


        //checkin, checkout, bookings, reports, roomview, roomstatus

        Button gotoCheckIn = (Button) view.findViewById(R.id.staffhub_checkin);
        Button gotoCheckOut = (Button) view.findViewById(R.id.staffhub_checkout);
        Button gotoBookings = (Button) view.findViewById(R.id.staffhub_booking); //TODO - ADD LATER
        Button gotoAccepting = (Button) view.findViewById(R.id.staffhub_accept);
        Button gotoReports = (Button) view.findViewById(R.id.staffhub_report);
        Button gotoRoomView = (Button) view.findViewById(R.id.staffhub_roomview);
        Button gotoRoomStatus = (Button) view.findViewById(R.id.staffhub_roomstatus);

        gotoCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentStaffHubDirections.actionNavStaffhubToCheckin();
                navController.navigate(action);
            }
        });

        gotoCheckOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentStaffHubDirections.actionNavStaffhubToCheckout();
                navController.navigate(action);
            }
        });

        /*
        gotoBookings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentStaffHubDirections.actionNavStaffhubToBookings();
                navController.navigate(action);
            }
        });
         */

        gotoAccepting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentStaffHubDirections.actionNavStafhfubToAccept();
                navController.navigate(action);
            }
        });

        gotoReports.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentStaffHubDirections.actionNavStaffhubToReports();
                navController.navigate(action);
            }
        });

        /*
        gotoRoomView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentStaffHubDirections.actionNavStaffhubToRoomViews();
                navController.navigate(action);
            }
        });

         */

        gotoRoomStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentStaffHubDirections.actionNavStaffhubToRoomstatus();
                navController.navigate(action);
            }
        });

        return view;
    }
}