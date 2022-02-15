package com.example.qrscannerandgenerator;

import static android.content.Context.MODE_PRIVATE;
import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.qrscannerandgenerator.databinding.FragmentMainBinding;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.Reader;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements View.OnClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MainFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MainFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MainFragment newInstance(String param1, String param2) {
        MainFragment fragment = new MainFragment();
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
    private FragmentMainBinding binding;
    private static final String TAG = "MainFragment";
    private ArrayList<String> arrayList ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentMainBinding.inflate(inflater, container, false);
        view = binding.getRoot();
        inti();
        onpressback();  // if i click Back button
        load();         //load data that Saved Before to add it new QR Scanner
        return view;
    }
    private  void  inti(){
        binding.scanfromimageMain.setOnClickListener(this);
        binding.scanbycameraMain.setOnClickListener(this);
        binding.generateMain.setOnClickListener(this);
        binding.historyMain.setOnClickListener(this);
        arrayList = new ArrayList<>();
    }

    private void onpressback() {

        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
            @Override
            public void handleOnBackPressed() {
                getActivity().finish(); // Finish/Close APP
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.scanfromimage_main:
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK); //Go To get Photos from Photos or Galary
                photoPickerIntent.setType("image/*");                      //Appears All Photos
                startActivityForResult(photoPickerIntent, 1000);//get Result of intent with Request Code
                break;
            case R.id.scanbycamera_main:
                if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA)
                        == PackageManager.PERMISSION_DENIED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.CAMERA}, 50);
                }else {
                    Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_scanCameraFragment);
                }
                break;
            case R.id.generate_main:
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_generateFragment);
                break;
            case R.id.history_main:
                Navigation.findNavController(view).navigate(R.id.action_mainFragment_to_historyFragment);
                break;

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_OK) {
            getActivity().runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    try {

                        final Uri imageUri = data.getData();                //initialization Uri to save image that selected in it
                        Log.d(TAG, "onActivityResult: " + data.getData());

                        final InputStream imageStream = getActivity().getContentResolver().openInputStream(imageUri);

                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        try {

                            Bitmap bMap = selectedImage;


                            int[] intArray = new int[bMap.getWidth() * bMap.getHeight()];

                            bMap.getPixels(intArray, 0, bMap.getWidth(), 0, 0, bMap.getWidth(), bMap.getHeight());


                            LuminanceSource source = new RGBLuminanceSource(bMap.getWidth(), bMap.getHeight(), intArray);

                            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));


                            Reader reader = new MultiFormatReader();

                            Result result = reader.decode(bitmap);

                            final TextView message = new TextView(getActivity());     //initialization final TextView
                            FrameLayout container = new FrameLayout(getActivity());   //initialization FrameLayout
                            //set left Margin of Text in Message of Dialog
                            FrameLayout.LayoutParams params = new  FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
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


                        } catch (Exception e) {
                            Toast.makeText(getActivity(), "Crop image first Correctly then Scan it again please", Toast.LENGTH_LONG).show();
                            e.printStackTrace();

                        }

                        //  image_view.setImageBitmap(selectedImage);

                    } catch (FileNotFoundException e) {

                        e.printStackTrace();

                        Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();

                    }
                }
            });


        } else {

            Toast.makeText(getActivity(), "You haven't picked Image", Toast.LENGTH_LONG).show();

        }
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
          arrayList =gson.fromJson(json, type) ; //Save them in arrayList
        }
    }
}