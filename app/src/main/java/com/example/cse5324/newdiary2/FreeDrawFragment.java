package com.example.cse5324.newdiary2;


import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.itextpdf.text.Image;

import java.util.UUID;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FreeDrawFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FreeDrawFragment extends Fragment implements View.OnClickListener {

    private float smallBrush, mediumBrush, largeBrush;
    private DrawingView drawView;
    private ImageButton currPaint;
    private ImageButton eraseBtn;
    private View rootView;

    public static FreeDrawFragment newInstance() {
        return new FreeDrawFragment();
    }

    public FreeDrawFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView =  inflater.inflate(R.layout.fragment_free_draw, container, false);

        drawView = (DrawingView) rootView.findViewById(R.id.drawing);
        LinearLayout paintLayout = (LinearLayout)rootView.findViewById(R.id.paint_colors);
        currPaint = (ImageButton)paintLayout.getChildAt(0);
        currPaint.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.paint_pressed));
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);
        ImageButton drawBtn = (ImageButton) rootView.findViewById(R.id.draw_btn);
        drawBtn.setOnClickListener(this);
        eraseBtn = (ImageButton)rootView.findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);
        ImageButton newBtn = (ImageButton) rootView.findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);
        ImageButton saveBtn = (ImageButton) rootView.findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);

        setColorListeners();
        return rootView;
    }

    private void setColorListeners() {

        View view = rootView.findViewById(R.id.color1);
        ImageButton imgButton = (ImageButton) view;
        imgButton.setOnClickListener(new MyColorClickListener());
        rootView.findViewById(R.id.color2).setOnClickListener(new MyColorClickListener());
        rootView.findViewById(R.id.color3).setOnClickListener(new MyColorClickListener());
        rootView.findViewById(R.id.color4).setOnClickListener(new MyColorClickListener());
        rootView.findViewById(R.id.color5).setOnClickListener(new MyColorClickListener());
        rootView.findViewById(R.id.color6).setOnClickListener(new MyColorClickListener());

        rootView.findViewById(R.id.color7).setOnClickListener(new MyColorClickListener());
        rootView.findViewById(R.id.color8).setOnClickListener(new MyColorClickListener());
        rootView.findViewById(R.id.color9).setOnClickListener(new MyColorClickListener());
        rootView.findViewById(R.id.color10).setOnClickListener(new MyColorClickListener());
        rootView.findViewById(R.id.color11).setOnClickListener(new MyColorClickListener());
        rootView.findViewById(R.id.color12).setOnClickListener(new MyColorClickListener());
    }

    public void onClick(View view){
        eraseBtn = (ImageButton)rootView.findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        if(view.getId()==R.id.draw_btn){
            //draw button clicked
            final Dialog brushDialog = new Dialog(getActivity());
            brushDialog.setTitle("Brush size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(smallBrush);
                    drawView.setLastBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(mediumBrush);
                    drawView.setLastBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });

            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setBrushSize(largeBrush);
                    drawView.setLastBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
        }
        else if(view.getId()==R.id.erase_btn){
            //switch to erase - choose size
            final Dialog brushDialog = new Dialog(getActivity());
            brushDialog.setTitle("Eraser size:");
            brushDialog.setContentView(R.layout.brush_chooser);
            ImageButton smallBtn = (ImageButton)brushDialog.findViewById(R.id.small_brush);
            smallBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(smallBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton mediumBtn = (ImageButton)brushDialog.findViewById(R.id.medium_brush);
            mediumBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(mediumBrush);
                    brushDialog.dismiss();
                }
            });
            ImageButton largeBtn = (ImageButton)brushDialog.findViewById(R.id.large_brush);
            largeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    drawView.setErase(true);
                    drawView.setBrushSize(largeBrush);
                    brushDialog.dismiss();
                }
            });
            brushDialog.show();
            drawView.setErase(false);
        }
        else if(view.getId()==R.id.new_btn){
            AlertDialog.Builder newDialog = new AlertDialog.Builder(getActivity());
            newDialog.setTitle("New drawing");
            newDialog.setMessage("Start new drawing (you will lose the current drawing)?");
            newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    drawView.startNew();
                    dialog.dismiss();
                }
            });
            newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener(){
                public void onClick(DialogInterface dialog, int which){
                    dialog.cancel();
                }
            });
            newDialog.show();//new button
        }
        else if(view.getId()==R.id.save_btn){
            AlertDialog.Builder saveDialog = new AlertDialog.Builder(getActivity());
            saveDialog.setTitle("Save drawing");
            saveDialog.setMessage("Save drawing to device Gallery?");
            saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    //save drawing
                }
            });
            saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            saveDialog.show();//save drawing
            drawView.setDrawingCacheEnabled(true);
            String imgSaved = MediaStore.Images.Media.insertImage(
                    getActivity().getContentResolver(), drawView.getDrawingCache(),
                    UUID.randomUUID().toString()+".png", "drawing");
            if(imgSaved!=null){
                Toast savedToast = Toast.makeText(getActivity().getApplicationContext(),
                        "Drawing saved to Gallery!", Toast.LENGTH_SHORT);
                savedToast.show();
            }
            else{
                Toast unsavedToast = Toast.makeText(getActivity().getApplicationContext(),
                        "Oops! Image could not be saved.", Toast.LENGTH_SHORT);
                unsavedToast.show();
            }
            drawView.destroyDrawingCache();
        }
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());
    }

    private void paintClicked(View view){
        if(view!=currPaint){
            //update color
            ImageButton imgView = (ImageButton)view;
            String color = view.getTag().toString();
            drawView.setColor(color);

            imgView.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.paint_pressed));
            currPaint.setImageDrawable(ContextCompat.getDrawable(getActivity(), R.drawable.paint));
            currPaint=(ImageButton)view;
        }
        //use chosen color
    }

    class MyColorClickListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            paintClicked(v);
        }
    }


}
