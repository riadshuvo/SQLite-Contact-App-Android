package com.example.sqlite_contact_app;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.sqlite_contact_app.models.Contact;
import com.example.sqlite_contact_app.utils.ContactPropertyListAdapter;
import com.example.sqlite_contact_app.utils.UniversalImageLoader;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ContactFragment extends Fragment {
    private static final String TAG = "ContactFragment";

    public interface OnEditContactListener {
        public void onEditContactSelected(Contact contact);
    }

    OnEditContactListener mOnEditContactListener;

    private Toolbar toolbar;
    private Contact mContact;
    private TextView mContactName;
    private CircleImageView mContactImage;
    private ListView mListView;

    //this will evade the nullpointer exception when adding to new bundle from MainActivity
    public ContactFragment() {
        super();
        setArguments(new Bundle());
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        Log.d(TAG, "onCreateView: started");

        toolbar = (Toolbar) view.findViewById(R.id.contactToolbar);
        mContactName = (TextView) view.findViewById(R.id.contactName);
        mContactImage = (CircleImageView) view.findViewById(R.id.contactImage);

        mListView = (ListView) view.findViewById(R.id.lvContactProperties);

        mContact = getContactFromBundle();

        if (mContact != null) {
            Log.d(TAG, "onCreateView: contact received: " + mContact.getName());
        }

        //Required For Setting up the toolbar
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

        init();

        ImageView ivBackArrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on back arrow");
                //remove previous fragment from the backstack (therefore navigating back)
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });


        //navigate to the edit contact fragment to edit the contact selected
        ImageView ivEdit = (ImageView) view.findViewById(R.id.ivEdit);
        ivEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked the edit button");
                /**
                 * pass the selected Contact to the interface and send it to the MainActivity
                 */
                mOnEditContactListener.onEditContactSelected(mContact);

            }
        });

        return view;
    }

    private void init() {
        mContactName.setText(mContact.getName());
        UniversalImageLoader.setImage(mContact.getProfileImage(), mContactImage, null, "");

        ArrayList<String> properties = new ArrayList<>();
        properties.add(mContact.getPhonenumber());
        properties.add(mContact.getEmail());
        ContactPropertyListAdapter adapter = new ContactPropertyListAdapter(getActivity(), R.layout.layout_cardview, properties);
        mListView.setAdapter(adapter);
        mListView.setDivider(null);
    }

    /**
     * Must have to Override onOptionsItemSelected() and onOptionsItemSelected() function for showing the Menu Item into the Toolbar and performing the MenuBar
     *
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.contact_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menuitem_delete:
                Log.d(TAG, "onOptionsItemSelected: deleting contacts");

                DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
                Cursor cursor = databaseHelper.getContactID(mContact);

                int contactID = -1;
                while (cursor.moveToNext()) {
                    contactID = cursor.getInt(0); //getting the Database Id From #COL0
                }
                if (contactID > -1) {
                    if (databaseHelper.deleteContact(contactID) > 0) {
                        Toast.makeText(getActivity(), "Contact Deleted", Toast.LENGTH_SHORT).show();

                        //clear the arguments ont he current bundle since the contact is deleted
                        this.getArguments().clear();

                        //remove previous fragemnt from the backstack (therefore navigating back)
                        getActivity().getSupportFragmentManager().popBackStack();
                    } else {
                        Toast.makeText(getActivity(), "Database Error", Toast.LENGTH_SHORT).show();
                    }

                }

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Retrieve the selected Contact data from the Bundle come from MainActivity
     *
     * @return
     */
    private Contact getContactFromBundle() {
        Log.d(TAG, "getContactFromBundle: arguments " + getArguments());

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            return bundle.getParcelable(getString(R.string.contact));
        } else {
            return null;
        }
    }

    /**
     * Must have to Override onAttach() method to avoid
     * Attempt to invoke interface method 'OnEditContactListener.onEditContactSelected()'
     * on a null object reference
     *
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mOnEditContactListener = (OnEditContactListener) getActivity();
        } catch (Exception e) {
            Log.d(TAG, "onAttach: " + e.getMessage());
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Cursor cursor = databaseHelper.getContactID(mContact);

        int contactID = -1;
        while (cursor.moveToNext()) {
            contactID = cursor.getInt(0);
        }
        if (contactID > -1) { // If the contact doesn't still exists then anvigate back by popping the stack
            init();
        } else {
            this.getArguments().clear(); //optional clear arguments but not necessary
            getActivity().getSupportFragmentManager().popBackStack();
        }
    }

}
