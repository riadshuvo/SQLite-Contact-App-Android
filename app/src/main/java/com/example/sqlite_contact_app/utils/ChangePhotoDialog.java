package com.example.sqlite_contact_app.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.sqlite_contact_app.ContactFragment;
import com.example.sqlite_contact_app.R;

public class ChangePhotoDialog extends DialogFragment {
    private static final String TAG = "ChangePhotoDialog";

    public interface OnPhotoReceivedListener{
        public void getBitmapImage(Bitmap bitmap);
    }

    OnPhotoReceivedListener mOnPhotoReceived;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_changephoto, container, false);

        //initalize the textview for starting the camera
        TextView takePhoto = (TextView) view.findViewById(R.id.dialogTakePhoto);
        takePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: starting camera.");
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent, Init.CAMERA_REQUEST_CODE);

            }
        });

        //Initialize the textview for choosing an image from memory
        TextView selectPhoto = (TextView) view.findViewById(R.id.dialogChoosePhoto);
        selectPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: accessing phones memory.");

            }
        });

        // Cancel button for closing the dialog
        TextView cancelDialog = (TextView) view.findViewById(R.id.dialogCancel);
        cancelDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: closing dialog.");
                getDialog().dismiss();
            }
        });

        return view;
    }

    /**
     * Must have to Override onAttach() method to avoid
     * Attempt to invoke interface method 'OnEditContactListener.onEditContactSelected()'
     * on a null object reference
     * @param context
     */
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            /**
             * Sending the image bitmap Fragment(ChangePhotoDialog) to Fragment(EditContactFragment) throw the Interface
             */
            mOnPhotoReceived = (OnPhotoReceivedListener) getTargetFragment();
        }catch (Exception e){
            Log.d(TAG, "onAttach: "+e.getMessage());
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        /*
        REsults when taking a new image with camera
         */
        if(requestCode == Init.CAMERA_REQUEST_CODE && resultCode == Activity.RESULT_OK){
            Log.d(TAG, "onActivityResult: done taking a picture.");

            //get the new image bitmap
            Bitmap bitmap = (Bitmap) data.getExtras().get("data");
            Log.d(TAG, "onActivityResult: receieved bitmap: " + bitmap);

            //send the bitmap and fragment to the interface
            mOnPhotoReceived.getBitmapImage(bitmap);
            getDialog().dismiss();
        }
    }
}
