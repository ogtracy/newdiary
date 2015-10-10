package com.example.cse5324.newdiary2;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnFragmentInteractionListener}
 * interface.
 */
public class DiaryFragment extends Fragment implements AbsListView.OnItemClickListener {

    private OnFragmentInteractionListener mListener;
    private AbsListView mListView;
    private ListAdapter mAdapter;
    private List diaryListItemList;


    public static DiaryFragment newInstance() {
        DiaryFragment fragment = new DiaryFragment();
        return fragment;
    }

    public DiaryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        populateList();
        mAdapter = new DiaryListAdapter(getActivity(), diaryListItemList);
    }

    private void populateList(){
        diaryListItemList = new ArrayList();
        InputStream in;
        BufferedReader reader;
        String line;;

        int count =0;

        File file = new File(getActivity().getFilesDir().getAbsolutePath(), CreateNoteActivity.NOTESFILE);
        if (!file.exists()){
            diaryListItemList.add(new DiaryListItem("There are no diary entries to display"));
            return;
        }
        try {

            in = getActivity().openFileInput(CreateNoteActivity.NOTESFILE);
            reader = new BufferedReader(new InputStreamReader(in));
            line = reader.readLine();
            while(line != null && count < 20){
                String displayText = "";
                while (line.equals(CreateNoteActivity.SEPARATOR)){
                    line = reader.readLine();
                }
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(Long.parseLong(line));
                DateFormat df = DateFormat.getDateTimeInstance();
                displayText += df.format(cal.getTime());
                displayText += "\n";
                displayText += reader.readLine();
                displayText += "\n";
                line = reader.readLine();
                while (line != null && line!= "\u0004" && !line.equals(CreateNoteActivity.SEPARATOR)){
                    displayText += line;
                    displayText += "\n";
                    line = reader.readLine();
                }
                diaryListItemList.add(new DiaryListItem(displayText));
                line = reader.readLine();
                count++;
            }
            in.close();

        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_diaryitem, container, false);
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((MainActivity) activity).onSectionAttached(1);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        DiaryListItem item = (DiaryListItem)this.diaryListItemList.get(position);
        Toast.makeText(getActivity(), item.getItemTitle() + " Clicked!", Toast.LENGTH_LONG).show();
    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyView instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(String id);
    }

}
