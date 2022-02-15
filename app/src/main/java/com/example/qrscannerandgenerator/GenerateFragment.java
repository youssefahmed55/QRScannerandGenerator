package com.example.qrscannerandgenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;
import com.example.qrscannerandgenerator.databinding.FragmentGenerateBinding;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GenerateFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GenerateFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public GenerateFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment GenerateFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GenerateFragment newInstance(String param1, String param2) {
        GenerateFragment fragment = new GenerateFragment();
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
    private FragmentGenerateBinding binding;
    private static final String TAG = "GenerateFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //initialization View of Fragment
        binding = FragmentGenerateBinding.inflate(inflater, container, false);
        view = binding.getRoot();

        binding.generatebytextGenerate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.edittextGenerate.getText().toString().trim().length() != 0) {
                    //Get input value from EditText
                    String sText = binding.edittextGenerate.getText().toString().trim();
                    //Initalaize multi format writer
                    MultiFormatWriter writer = new MultiFormatWriter();
                    try {
                        //Initalaize Bit matrix
                        BitMatrix matrix = writer.encode(sText, BarcodeFormat.QR_CODE, 350, 350);
                        //Initalaize Barcode encoder
                        BarcodeEncoder encoder = new BarcodeEncoder();
                        //Initalaize Bitmap
                        Bitmap bitmap = encoder.createBitmap(matrix);
                        //set Bitmap on imageview
                        binding.imageGenerate.setImageBitmap(bitmap);
                        //Initalaize input manager
                        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        //Hide Soft Keyboard
                        manager.hideSoftInputFromWindow(binding.edittextGenerate.getApplicationWindowToken(), 0);
                    } catch (Exception e) {
                        Log.d(TAG, "onClick: " + e.getMessage());
                    }
                }else{
                    Toast.makeText(getActivity(), "Please Write Text", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }
}