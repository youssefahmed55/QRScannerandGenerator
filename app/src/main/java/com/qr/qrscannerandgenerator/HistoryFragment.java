package com.qr.qrscannerandgenerator;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import com.qr.qrscannerandgenerator.databinding.FragmentHistoryBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HistoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HistoryFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HistoryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HistoryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HistoryFragment newInstance(String param1, String param2) {
        HistoryFragment fragment = new HistoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private View view;
    private FragmentHistoryBinding binding;
    private MyRecycleAdapter myRecycleAdapter;
    private ArrayList<String> arrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialization View of Fragment
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        inti();
        load();        //load data that Saved Before to add it new QR Scanner
        onclickitem(); //On Click to item of Recycleview
        return view;
    }
    private  void inti(){
        myRecycleAdapter = new MyRecycleAdapter();
        arrayList= new ArrayList<>();
    }
    private void onclickitem(){
        myRecycleAdapter.setOnItemClick(new MyRecycleAdapter.OnItemClick() {
            @Override
            public void onClick2(int postion, View view) {
                LayoutInflater inflater = (LayoutInflater)
                        getActivity().getSystemService(LAYOUT_INFLATER_SERVICE);
                View popupView = inflater.inflate(R.layout.popup_window, null);

                //initialization PopupWindow
                final PopupWindow popupWindow = new PopupWindow(popupView, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

                popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
                TextView textView = popupView.findViewById(R.id.delete);
                TextView textView2 = popupView.findViewById(R.id.copy);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        arrayList.remove(postion);
                        save(arrayList);
                        myRecycleAdapter.notifyDataSetChanged();
                        popupWindow.dismiss();
                    }
                });
                textView2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //initialization ClipboardManager
                        ClipboardManager clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
                        ClipData clip = ClipData.newPlainText("Copied Text", arrayList.get(postion));
                        clipboard.setPrimaryClip(clip);
                        popupWindow.dismiss();
                        Toast.makeText(getActivity(), "Copied Text", Toast.LENGTH_SHORT).show();

                    }
                });
            }
        });
    }
    private void save(ArrayList<String> arrayList) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = gson.toJson(arrayList);
        sharedPreferences.edit().putString("key", json).apply();
    }

    private void load() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("data", MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("key", null);

        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();


        if (gson.fromJson(json, type) != null) {
            arrayList =gson.fromJson(json, type) ;
            myRecycleAdapter.setList(arrayList);                 //setArrayOfAdapter
            binding.RecycleHistory.setAdapter(myRecycleAdapter);
        }
    }
}