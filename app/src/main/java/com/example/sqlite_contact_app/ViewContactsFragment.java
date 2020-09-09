package com.example.sqlite_contact_app;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sqlite_contact_app.models.Contact;
import com.example.sqlite_contact_app.utils.ContactListAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class ViewContactsFragment extends Fragment {

    private static final String TAG = "ViewContactsFragment";
    private String testImageURL = "upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/1200px-Android_robot.svg.png";

    public interface OnContactSelectedListener{
        public void OnContactSelectedListener(Contact contact);
    }

    OnContactSelectedListener mContactSelectedListener;

    private static final int STANDARD_APPBAR = 0;
    private static final int SEARCH_APPBAR = 1;

    private int mAppBarState;


    private AppBarLayout viewContactsBar, searchBar;
    private ContactListAdapter adapter;
    private ListView contactsList;
    private EditText mSearchContacts;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.fragment_viewcontacts, container, false);


        viewContactsBar = (AppBarLayout) view.findViewById(R.id.viewContactsToolbar);
        searchBar = (AppBarLayout) view.findViewById(R.id.searchToolbar);
        Log.d(TAG, "onCreateView: started");
        contactsList = (ListView) view.findViewById(R.id.contactsList);

        setAppBarState(STANDARD_APPBAR);

        setUpContactList();

        //Navigate to add contacts fragment
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked fab");
            }
        });

        ImageView ivSearchContacts = (ImageView) view.findViewById(R.id.ivSearchIcon);
        ivSearchContacts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on search icon");
                toggleToolBarState();
            }
        });

        ImageView ivBackArrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on back arrow");
                toggleToolBarState();

            }
        });

        return view;
    }




    /**
     * Must have to Override onAttach() method to avoid
     * Attempt to invoke interface method 'ViewContactsFragment$OnContactSelectedListener.OnContactSelectedListener()'
     * on a null object reference
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mContactSelectedListener = (OnContactSelectedListener) getActivity();
        }catch (Exception e){
            Log.d(TAG, "onAttach: "+e.getMessage());
        }
    }



    private void setUpContactList(){
        final ArrayList<Contact> contacts = new ArrayList<>();
        contacts.add(new Contact("Gary the Guy", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));

        adapter = new ContactListAdapter(getActivity(), R.layout.layout_contactslistitem, contacts, "https://");
        contactsList.setAdapter(adapter);

        contactsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d(TAG, "onClick: nevigating to "+ getString(R.string.add_contact_fragment));


                /**
                 * pass selected the contact to the interface and send it to the MainActivity
                 */
                mContactSelectedListener.OnContactSelectedListener(contacts.get(position));
            }
        });

    }

    /**
     * Initiates the appbar state toggle
     */
    private void toggleToolBarState() {
        Log.d(TAG, "toggleToolBarState: toggling AppBarState.");
        if(mAppBarState == STANDARD_APPBAR){
            setAppBarState(SEARCH_APPBAR);
        }else{
            setAppBarState(STANDARD_APPBAR);
        }

    }

    /**
     * Sets the appbar state for either the search 'mode' or 'standard' mode
     * @param state
     */
    private void setAppBarState(int state) {
        Log.d(TAG, "setAppBarState: changing app bar state to: " + state);

        mAppBarState = state;

        if(mAppBarState == STANDARD_APPBAR){
            searchBar.setVisibility(View.GONE);
            viewContactsBar.setVisibility(View.VISIBLE);

            //hide the keyboard
            View view = getView();
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            try{
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }catch (NullPointerException e){
                Log.d(TAG, "setAppBarState: NullPointerException: " + e.getMessage());
            }
        }

        else if(mAppBarState == SEARCH_APPBAR){
            viewContactsBar.setVisibility(View.GONE);
            searchBar.setVisibility(View.VISIBLE);

            //open the keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        setAppBarState(STANDARD_APPBAR);
    }
}
