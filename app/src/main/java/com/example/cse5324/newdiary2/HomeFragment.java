package com.example.cse5324.newdiary2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    private final int RESULT_LOAD_IMAGE =745;

    private FrameLayout base;

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home, container, false);
        base = (FrameLayout)v.findViewById(R.id.base);
        Button changeImg = (Button) v.findViewById(R.id.changeImageButton);
        changeImg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                changeImage();
            }
        });
        SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PROFILE, 0);
        if (settings.contains("home_image")){
            Drawable img = Drawable.createFromPath(settings.getString("home_image", ""));
            if (img!=null) {
                base.setBackground(img);
            }
        }
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case RESULT_LOAD_IMAGE: {
                if (resultCode == MainActivity.RESULT_OK && null!=data){
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };

                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    if (cursor == null){
                        return;
                    }
                    cursor.moveToFirst();

                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    base.setBackground(Drawable.createFromPath(picturePath));

                    SharedPreferences settings = getActivity().getSharedPreferences(LoginActivity.PROFILE, 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("home_image", picturePath);
                    editor.apply();
                }
            }

        }
    }

    private void changeImage(){
        Intent i = new Intent(
                Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, false);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }
}
