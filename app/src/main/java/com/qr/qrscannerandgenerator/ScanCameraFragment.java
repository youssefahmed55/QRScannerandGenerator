package com.qr.qrscannerandgenerator;
import static android.content.Context.MODE_PRIVATE;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.Result;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ScanCameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ScanCameraFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ScanCameraFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ScanCameraFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ScanCameraFragment newInstance(String param1, String param2) {
        ScanCameraFragment fragment = new ScanCameraFragment();
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
    private CodeScanner mCodeScanner;
    private ArrayList<String> arrayList = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialization View of Fragment
        view = inflater.inflate(R.layout.fragment_scan_camera, container, false);
        //load data that Saved Before to add it new QR Scanner
        load();
        //initialization CodeScannerView
        CodeScannerView scannerView = view.findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(requireActivity(), scannerView);

        mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull Result result) {

                requireActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                            final TextView message = new TextView(getActivity());    //initialization final TextView
                            FrameLayout container = new FrameLayout(getActivity());  //initialization FrameLayout
                            //set left Margin of Text in Message of Dialog
                            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                            params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
                            message.setLayoutParams(params);
                            //initialization SpannableString to Make text like Hyper Text that you can click on it
                            final SpannableString s =
                                    new SpannableString(result.getText());
                            Linkify.addLinks(s, Linkify.WEB_URLS);
                            message.setText(s);
                            message.setMovementMethod(LinkMovementMethod.getInstance());
                            //Initalize alert dialog
                            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                            //set title
                            builder.setTitle("Result (Saved in History)");
                            //set message
                            container.addView(message);
                            builder.setView(container);
                            //builder.setMessage(s);   you can use it if you don't want margin left of text that in Message

                            //save it in sharedPreferences
                            arrayList.add(result.getText());
                            save(arrayList);
                            //set positive button
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //Dismiss dialog
                                    dialogInterface.dismiss();
                                }
                            });
                            //Show alert dialog
                            builder.show();

                    }


                });



            }
        });

        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                    mCodeScanner.startPreview();
            }
        });
        return  view;
    }


    @Override
    public void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    public void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }

    @Override
    public void onStop() {
        mCodeScanner.stopPreview();
        super.onStop();
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
            arrayList =gson.fromJson(json, type) ;   //save them in arrayList
        }
    }
}