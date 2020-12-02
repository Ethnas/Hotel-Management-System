package no.ntnu.hmsproject.ui.hmsservice.booking;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import no.ntnu.hmsproject.MainActivity;
import no.ntnu.hmsproject.R;
import no.ntnu.hmsproject.domain.LoggedUser;


public class FragmentBookingMain extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_booking_main, container, false);


        Button gotoAddV = (Button) view.findViewById(R.id.booking_main_nav_addbooking);
        Button gotoRemV = (Button) view.findViewById(R.id.booking_main_nav_rembooking);
        Button gotoUpdV = (Button) view.findViewById(R.id.booking_main_nav_updbooking);

        gotoAddV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                //navController.navigate();
            }
        });

        return view;
    }
}