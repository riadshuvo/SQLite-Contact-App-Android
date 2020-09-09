package com.example.sqlite_contact_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.sqlite_contact_app.models.Contact;
import com.example.sqlite_contact_app.utils.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditContactFragment extends Fragment {
    private static final String TAG = "EditContactFragment";

    private Contact mContact;
    private EditText mName, mPhoneNumber, mEmail;
    private CircleImageView mContactImage;
    private Toolbar toolbar;
    private Spinner mSelectedDevice;
    private String mSelectedImagePath;


    //this will evade the nullpointer exception when adding to new bundle from MainActivity
    public EditContactFragment(){
        super();
        setArguments(new Bundle());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editcontact, container, false);
        Log.d(TAG, "onCreateView: started");

        toolbar = (Toolbar) view.findViewById(R.id.editContactToolbar);

        mName = (EditText) view.findViewById(R.id.etContactName);
        mPhoneNumber = (EditText) view.findViewById(R.id.etContactPhone);
        mEmail = (EditText) view.findViewById(R.id.etContactEmail);
        mContactImage = (CircleImageView) view.findViewById(R.id.contactImage);
        mSelectedDevice = (Spinner) view.findViewById(R.id.selectDevice);


        //Get the Contact Information From Bundle
        mContact = getContactFromBundle();
        if(mContact != null){
            Log.d(TAG, "onCreateView EditContactFragment: contact received: "+mContact.getName());
            init();
        }


        ImageView ivBackArrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on back arrow");
                //remove previous fragment from the backstack (therefore navigating back)
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });


        return view;
    }

    private void init(){
        mName.setText(mContact.getName());
        mPhoneNumber.setText(mContact.getPhonenumber());
        mEmail.setText(mContact.getEmail());
        UniversalImageLoader.setImage(mContact.getProfileImage(), mContactImage, null, "http://");

        //Setting the selected device to the Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.device_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSelectedDevice.setAdapter(adapter);
        int positon = adapter.getPosition(mContact.getDevice());
        mSelectedDevice.setSelection(positon);

    }

    /**
     * Retrieve the selected Contact data from the Bundle come from MainActivity
     * @return
     */
    private Contact getContactFromBundle(){
        Log.d(TAG, "getContactFromBundle: arguments "+getArguments());

        Bundle bundle = this.getArguments();
        if(bundle != null){
            return bundle.getParcelable(getString(R.string.contact));
        }else{
            return null;
        }
    }
}
