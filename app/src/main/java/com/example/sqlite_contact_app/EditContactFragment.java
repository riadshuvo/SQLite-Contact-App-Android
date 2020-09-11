package com.example.sqlite_contact_app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.sqlite_contact_app.models.Contact;
import com.example.sqlite_contact_app.utils.ChangePhotoDialog;
import com.example.sqlite_contact_app.utils.Init;
import com.example.sqlite_contact_app.utils.UniversalImageLoader;

import de.hdodenhof.circleimageview.CircleImageView;

public class EditContactFragment extends Fragment implements ChangePhotoDialog.OnPhotoReceivedListener {
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

        //set the heading for the toolbar
        TextView heading = (TextView) view.findViewById(R.id.textContactToolbar);
        heading.setText(getString(R.string.edit_contact));

        //Required For Setting up the toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);


        //Get the Contact Information From Bundle
        mContact = getContactFromBundle();
        if(mContact != null){
            Log.d(TAG, "onCreateView EditContactFragment: contact received: "+mContact.getName());
            init();
        }


        //Navigation to the back arrow
        ImageView ivBackArrow = (ImageView) view.findViewById(R.id.ivBackArrow);
        ivBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked on back arrow");
                //remove previous fragment from the backstack (therefore navigating back)
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

        //Save Changes to the Contact Database
        ImageView ivCheckBox = (ImageView) view.findViewById(R.id.ivCheckMark);
        ivCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: saving the edited contact");
                //Execute the save method for database

            }
        });

        //Opening the Photo selection dialogue box or Phone Camera
        ImageView ivCamera = (ImageView) view.findViewById(R.id.ivCamera);
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /**
                 Make sure all permissions have been verified before opening the camera dialog
                 */

                for(int i = 0; i < Init.PERMISSIONS.length; i++){

                    /**
                     * Checking Every Single Permission that User has been Approved or Not
                     */
                    String[] permission = {Init.PERMISSIONS[i]};
                    if(((MainActivity)getActivity()).checkPermission(permission)){
                       if(i == Init.PERMISSIONS.length - 1){
                           Log.d(TAG, "onClick: Opening the image selection dialogue box");

                           ChangePhotoDialog dialog = new ChangePhotoDialog();
                           dialog.show(getFragmentManager(), getString(R.string.change_photo_dialog));
                           dialog.setTargetFragment(EditContactFragment.this,0);
                       }
                    }

                    else {
                        ((MainActivity)getActivity()).verifyPermissions(Init.PERMISSIONS);
                    }
                }


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
     * Must have to Override onOptionsItemSelected() and onOptionsItemSelected() function for showing the Menu Item into the Toolbar and performing the MenuBar
     * @param menu
     * @param inflater
     */
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.contact_menu,menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()){
            case R.id.menuitem_delete:
                Log.d(TAG, "onOptionsItemSelected: deleting contacts");
        }
        return super.onOptionsItemSelected(item);
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


    /**
     * Retrieves the selected image from the Bundle coming from ChangePhotoDialog By Taking Snapshot
     * @param bitmap
     */
    @Override
    public void getBitmapImage(Bitmap bitmap) {
        Log.d(TAG, "getBitmapImage: got the bitmap: " + bitmap);
        //get the bitmap from 'ChangePhotoDialog'
        if(bitmap != null) {
            //compress the image (if you like)
            ((MainActivity)getActivity()).compressBitmap(bitmap, 70);
            mContactImage.setImageBitmap(bitmap);
        }

    }

    /**
     * Retrieves the selected image from the Bundle coming from ChangePhotoDialog By Selecting Image From Memory
     * @param imagePath
     */
    @Override
    public void getImagePath(String imagePath) {
        Log.d(TAG, "getImagePath: got the image path: " + imagePath);

        if( !imagePath.equals("")){
            imagePath = imagePath.replace(":/", "://");
            mSelectedImagePath = imagePath;
            UniversalImageLoader.setImage(imagePath, mContactImage, null, "");
        }
    }

}
