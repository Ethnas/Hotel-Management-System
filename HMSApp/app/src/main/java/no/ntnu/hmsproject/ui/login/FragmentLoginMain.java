package no.ntnu.hmsproject.ui.login;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.ui.hmsservice.damagereport.FragmentDamReportMainDirections;


public class FragmentLoginMain extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_main, container, false);

        Button gotoCreate = (Button) view.findViewById(R.id.login_main_create);
        Button gotoLogin = (Button) view.findViewById(R.id.login_main_login);
        Button gotoGoogle = (Button) view.findViewById(R.id.login_main_google);

        gotoCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentLoginMainDirections.actionNavLoginMainToNavCreate();
                navController.navigate(action);
            }
        });

        gotoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentLoginMainDirections.actionNavLoginMainToNavLogin();
                navController.navigate(action);
            }
        });

        gotoGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                NavDirections action = FragmentLoginMainDirections.actionNavLoginMainToActivityLoginGoogle();
                navController.navigate(action);
            }
        });
        return view;
    }
}