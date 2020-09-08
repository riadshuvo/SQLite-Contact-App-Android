package com.example.sqlite_contact_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentTransaction;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.example.sqlite_contact_app.models.Contact;
import com.example.sqlite_contact_app.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends AppCompatActivity implements ViewContactsFragment.OnContactSelectedListener {
    private static final String TAG = "MainActivity";
    private static final int REQUEST_CODE = 1;


    @Override
    public void OnContactSelectedListener(Contact contact) {
        Log.d(TAG, "OnContactSelectedListener: contact selected from "
                + getString(R.string.view_contacts_fragment)
                + " " + contact.getName());

        ContactFragment contactFragment = new ContactFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(getString(R.string.contact), contact);
        contactFragment.setArguments(bundle);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_container, contactFragment);
        transaction.addToBackStack(getString(R.string.contact_fragment));
        transaction.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate: started");

        initImageLoader();

        init();
    }

    /**
     * initialize the first fragment (ViewContactsFragment)
     */
    private void init() {
        ViewContactsFragment fragment = new ViewContactsFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        // reaplce whatever is in the fragment_container view with this fragment,
        // amd add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    private void initImageLoader() {

        UniversalImageLoader universalImageLoader = new UniversalImageLoader(MainActivity.this);
        ImageLoader.getInstance().init(universalImageLoader.getConfig());

    }

    /**
     * Generalized Method for asking permissions. Can pass any array of permissions.
     * @param permissions
     */
    public void verifyPermissions(String[] permissions){
        Log.d(TAG, "verifyPermissions: Asking user for permissions.");
        ActivityCompat.requestPermissions(
                MainActivity.this,
                permissions,
                REQUEST_CODE
        );
    }

    /**
     * Check to see if permission was granted for the passed parameters
     * ONLY ONE PERMISSION MAY BE CHECKED AT A TIME
     * @param permissions
     * @return
     */
    public boolean checkPermission(String [] permissions){
        Log.d(TAG, "checkPermission: checking permission for: "+permissions[0]);

        int permissionRequest = ActivityCompat.checkSelfPermission(MainActivity.this, permissions[0]);

        if(permissionRequest != PackageManager.PERMISSION_GRANTED){
            Log.d(TAG, "checkPermission: \n Permission was not granted for "+permissions[0]);
            return false;
        }else {
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: request code "+requestCode);

        switch (requestCode){
            case REQUEST_CODE :
                for(int i = 0; i < permissions.length; i++){
                    if(grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        Log.d(TAG, "onRequestPermissionsResult: User has allowed permission to access: "+permissions[i]);
                    }else {
                        break;
                    }
                }
                break;
        }
    }
}