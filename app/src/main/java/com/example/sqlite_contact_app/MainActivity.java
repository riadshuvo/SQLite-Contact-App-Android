package com.example.sqlite_contact_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.sqlite_contact_app.models.Contact;
import com.example.sqlite_contact_app.utils.UniversalImageLoader;
import com.nostra13.universalimageloader.core.ImageLoader;

public class MainActivity extends AppCompatActivity implements ViewContactsFragment.OnContactSelectedListener {
    private static final String TAG = "MainActivity";


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

}