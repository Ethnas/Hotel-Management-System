package no.ntnu.hmsproject.ui.hmsservice.damagereport;

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
import no.ntnu.hmsproject.ui.hmsservice.booking.FragmentBookingMainDirections;


public class FragmentDamReportMain extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dam_report_main, container, false);


        Button gotoAddV = (Button) view.findViewById(R.id.damrep_main_nav_addreport);
        Button gotoRemV = (Button) view.findViewById(R.id.damrep_main_nav_remreport);
        Button gotoUpdV = (Button) view.findViewById(R.id.damrep_main_nav_updreport);
        Button gotoListV = (Button) view.findViewById(R.id.damrep_main_nav_getreports);

        gotoAddV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentDamReportMainDirections.actionNavDamrepMainToAdd();
                navController.navigate(action);
            }
        });

        gotoRemV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentDamReportMainDirections.actionNavDamrepMainToRem();
                navController.navigate(action);
            }
        });

        gotoUpdV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentDamReportMainDirections.actionNavDamrepMainToUpd();
                navController.navigate(action);
            }
        });

        gotoListV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentDamReportMainDirections.actionNavDamrepMainToList();
                navController.navigate(action);
            }
        });

        return view;
    }

}