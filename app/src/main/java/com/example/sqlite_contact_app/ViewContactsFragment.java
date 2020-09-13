package com.example.sqlite_contact_app;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sqlite_contact_app.models.Contact;
import com.example.sqlite_contact_app.utils.ContactListAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

public class ViewContactsFragment extends Fragment {

    private static final String TAG = "ViewContactsFragment";
    private String testImageURL = "upload.wikimedia.org/wikipedia/commons/thumb/d/d7/Android_robot.svg/1200px-Android_robot.svg.png";

    public interface OnContactSelectedListener{
        public void OnContactSelectedListener(Contact contact);
    }
    OnContactSelectedListener mContactSelectedListener;

    public interface OnAddContactListener{
        public void onAddContact();
    }
    OnAddContactListener mOnAddContactListener;

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
        Log.d(TAG, "onCreateView: started");

        View view;
        view = inflater.inflate(R.layout.fragment_viewcontacts, container, false);

        viewContactsBar = (AppBarLayout) view.findViewById(R.id.viewContactsToolbar);
        searchBar = (AppBarLayout) view.findViewById(R.id.searchToolbar);
        contactsList = (ListView) view.findViewById(R.id.contactsList);
        mSearchContacts = (EditText) view.findViewById(R.id.etSearchContacts);

        setAppBarState(STANDARD_APPBAR);

        setUpContactList();

        //Navigate to add contacts fragment
        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fabAddContact);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked fab");
                mOnAddContactListener.onAddContact();
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
            mOnAddContactListener = (OnAddContactListener) getActivity();
        }catch (Exception e){
            Log.d(TAG, "onAttach: "+e.getMessage());
        }
    }



    private void setUpContactList(){
        final ArrayList<Contact> contacts = new ArrayList<>();
//        contacts.add(new Contact("Riead", "+880 1797-551221", "Mobile","mitch@tabian.ca", testImageURL));
//        contacts.add(new Contact("Shuvo", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
//        contacts.add(new Contact("Mitch Tabian", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
//        contacts.add(new Contact("Sabia", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
//        contacts.add(new Contact("Sabiha", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
//        contacts.add(new Contact("Kawsher", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
//        contacts.add(new Contact("Rafique", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
//        contacts.add(new Contact("Monir", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
//        contacts.add(new Contact("Sajid", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
//        contacts.add(new Contact("Joy", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
//        contacts.add(new Contact("Rifat", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
//        contacts.add(new Contact("Sairul", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
//        contacts.add(new Contact("Shibbir", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));
//        contacts.add(new Contact("Mizan", "(604) 855-1111", "Mobile","mitch@tabian.ca", testImageURL));


        DatabaseHelper databaseHelper = new DatabaseHelper(getActivity());
        Cursor cursor = databaseHelper.getAllContacts();

        //iterate through all the rows contained in the database
        if(!cursor.moveToNext()){
            Toast.makeText(getActivity(), "There are no contacts to show", Toast.LENGTH_SHORT).show();
        }
        while(cursor.moveToNext()){
            contacts.add(new Contact(
                    cursor.getString(1),//name
                    cursor.getString(2),//phone number
                    cursor.getString(3),//device
                    cursor.getString(4),//email
                    cursor.getString(5)//profile image uri
            ));
        }

        //sort the arraylist based on the contact name
        Collections.sort(contacts, new Comparator<Contact>() {
            @Override
            public int compare(Contact o1, Contact o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });

        Log.d(TAG, "setUpContactList: "+contacts.get(1).getProfileImage());

        adapter = new ContactListAdapter(getActivity(), R.layout.layout_contactslistitem, contacts, "");

        mSearchContacts.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String text = mSearchContacts.getText().toString().toLowerCase(Locale.getDefault());
                adapter.filter(text);
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

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
