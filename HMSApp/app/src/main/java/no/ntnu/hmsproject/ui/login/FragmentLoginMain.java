package no.ntnu.hmsproject.ui.login;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import no.ntnu.hmsproject.R;


public class FragmentLoginMain extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_main, container, false);

        Button submitV = (Button) view.findViewById(R.id.login_main_create);

        submitV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new Fragment();
                FragmentManager fragmentManager = getChildFragmentManager();

                switch (view.getId()) {
                    case R.id.login_main_create:
                        fragment = new FragmentCreate();
                        break;
                }

                getChildFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment, fragment)
                        .commit();

            }
        });

       return view;

    }
}