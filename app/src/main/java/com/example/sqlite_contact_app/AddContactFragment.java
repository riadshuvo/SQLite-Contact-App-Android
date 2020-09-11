package com.example.sqlite_contact_app;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class AddContactFragment extends Fragment {
    private static final String TAG = "AddContactFragment";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addcontact, container, false);

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

        return view;
    }
}
