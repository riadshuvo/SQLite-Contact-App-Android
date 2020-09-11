package com.example.sqlite_contact_app;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddContactFragment extends Fragment implements ChangePhotoDialog.OnPhotoReceivedListener {
    private static final String TAG = "AddContactFragment";

    private Contact mContact;
    private EditText mName, mPhoneNumber, mEmail;
    private CircleImageView mContactImage;
    private Toolbar toolbar;
    private Spinner mSelectedDevice;
    private String mSelectedImagePath;
    private int mPreviousKeyStroke;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addcontact, container, false);

        toolbar = (Toolbar) view.findViewById(R.id.editContactToolbar);

        mName = (EditText) view.findViewById(R.id.etContactName);
        mPhoneNumber = (EditText) view.findViewById(R.id.etContactPhone);
        mEmail = (EditText) view.findViewById(R.id.etContactEmail);
        mContactImage = (CircleImageView) view.findViewById(R.id.contactImage);
        mSelectedDevice = (Spinner) view.findViewById(R.id.selectDevice);

        mSelectedImagePath = null;

        //Initialize the PhoneNumberFormattingTextWatcher for formatting the phonenumber
        mPhoneNumber.addTextChangedListener(new PhoneNumberFormattingTextWatcher());



        //set the heading for the toolbar
        TextView heading = (TextView) view.findViewById(R.id.textContactToolbar);
        heading.setText(getString(R.string.add_contact));

        //Required For Setting up the toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(toolbar);
        setHasOptionsMenu(true);

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

        //Save the new contact to the database
        ImageView ivCheckBox = (ImageView) view.findViewById(R.id.ivCheckMark);
        ivCheckBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String value=mPhoneNumber.getText().toString();
                Log.d(TAG, "onClick: saving the contact"+value);
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
                            dialog.setTargetFragment(AddContactFragment.this,0);
                        }
                    }

                    else {
                        ((MainActivity)getActivity()).verifyPermissions(Init.PERMISSIONS);
                    }
                }


            }
        });



        /**
         * Initialize the initOnTextChangeListener() for formatting the phonenumber
         */
       // initOnTextChangeListener();

        return view;

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



    /**
     * Initialize the onTextChangeListener for formatting the phonenumber
     */
//    private void initOnTextChangeListener(){
//
//        mPhoneNumber.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//
//                mPreviousKeyStroke = keyCode;
//
//                return false;
//            }
//        });
//
//        mPhoneNumber.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//                String number = s.toString();
//                Log.d(TAG, "afterTextChanged:  " + number);
//
//                if(number.length() == 3 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
//                        && !number.contains("(")){
//                    number = String.format("(%s", s.toString().substring(0,3));
//                    mPhoneNumber.setText(number);
//                    mPhoneNumber.setSelection(number.length());
//                }
//                else if(number.length() == 5 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
//                        && !number.contains(")")){
//                    number = String.format("(%s) %s",
//                            s.toString().substring(1,4),
//                            s.toString().substring(4,5));
//                    mPhoneNumber.setText(number);
//                    mPhoneNumber.setSelection(number.length());
//                }
//                else if(number.length() ==10 && mPreviousKeyStroke != KeyEvent.KEYCODE_DEL
//                        && !number.contains("-")){
//                    number = String.format("(%s) %s-%s",
//                            s.toString().substring(1,4),
//                            s.toString().substring(6,9),
//                            s.toString().substring(9,10));
//                    mPhoneNumber.setText(number);
//                    mPhoneNumber.setSelection(number.length());
//
//                }
//            }
//        });
//    }

}
