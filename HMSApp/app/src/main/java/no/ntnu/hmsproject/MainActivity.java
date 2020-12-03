package no.ntnu.hmsproject;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.ByteArrayOutputStream;
import java.io.File;

import no.ntnu.hmsproject.ui.login.ActivityLoginGoogle;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                takePhoto();
//            }
//        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_login_main, R.id.nav_booking, R.id.nav_staffhub_main, R.id.nav_contactus)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                int menuId = destination.getId();
                switch(menuId) {
                    case R.id.nav_login_main:
                        Toast.makeText(MainActivity.this, "Create/Login", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_booking:
                        Toast.makeText(MainActivity.this, "Booking", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_staffhub_main:
                        Toast.makeText(MainActivity.this, "Staff Hub", Toast.LENGTH_LONG).show();
                        break;
                    case R.id.nav_contactus:
                        Toast.makeText(MainActivity.this, "Contact Us", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        break;
                }
            }
        });
    }

//    public void takePhoto(){
//
//        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
//            mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
//            takePictureIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, mImageCaptureUri);
//            try {
//                takePictureIntent.putExtra("return-data", true);
//                startActivityForResult(takePictureIntent, 1);
//            } catch (ActivityNotFoundException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//        if (resultCode == Activity.RESULT_OK) {
//            Bundle bundle = intent.getExtras();
//            Bitmap photo = bundle.getParcelable("data");
//            ByteArrayOutputStream bos = new ByteArrayOutputStream();
//            photo.compress(Bitmap.CompressFormat.PNG, 100, bos);
//            byte[] image = bos.toByteArray();
//            File f = new File(mImageCaptureUri.getPath());
//            if (f.exists()) f.delete();
//        }
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check for existing Google Sign In account, if the user is already signed in
        // the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //TODO updateUI(account);

    }

}