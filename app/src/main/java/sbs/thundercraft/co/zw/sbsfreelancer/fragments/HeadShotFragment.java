package sbs.thundercraft.co.zw.sbsfreelancer.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import sbs.thundercraft.co.zw.sbsfreelancer.R;
import sbs.thundercraft.co.zw.sbsfreelancer.utils.PictureUtilities;

import static sbs.thundercraft.co.zw.sbsfreelancer.fragments.OmniAccountOpenning.PICTURE_ID;
import static sbs.thundercraft.co.zw.sbsfreelancer.fragments.OmniAccountOpenning.REQUEST_IMAGE_CAPTURE;
import static sbs.thundercraft.co.zw.sbsfreelancer.utils.ConfirmDialog.REQUEST_CONFRIM;
import static sbs.thundercraft.co.zw.sbsfreelancer.utils.ConfirmDialog.RESULT_CONFRIM;


public class HeadShotFragment extends Fragment {


    //private static final int REQUEST_CONFRIM = 100;
    private Uri pictureProfileUri;
    private Button btnGo;
    static final int PICTURE_PROFILE = 2002;
    private OnFragmentInteractionListener mListener;
    private ImageView imageView;
    Bundle arguments;
    private int pictureType;

    public HeadShotFragment() {

    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
           arguments = getArguments();

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_head_shot, container, false);

      imageView = view.findViewById(R.id.head_picture);
      imageView.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              takePicture();
          }
      });
        btnGo = view.findViewById(R.id.head_picture_button);
      btnGo.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {


              IDShotFragment fragment = new IDShotFragment();
              fragment.pictureProfileUri = pictureProfileUri;
              fragment.setArguments(arguments);
              FragmentTransaction transaction = getFragmentManager().beginTransaction();
              transaction.replace(R.id.fragment_container, fragment);
              transaction.addToBackStack(OmniAccountOpenning.class.getSimpleName());
              transaction.commit();

          }
      });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureType == PICTURE_ID) {
            // create a file to save the image
            pictureProfileUri =  PictureUtilities.getOutputMediaFileUri(PictureUtilities.MEDIA_TYPE_IMAGE);
            // set the image file name
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureProfileUri);
        } else  {
            pictureProfileUri = PictureUtilities.getOutputMediaFileUri(PictureUtilities.MEDIA_TYPE_IMAGE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, pictureProfileUri);
        }

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }



    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {


            try {

                    if (pictureProfileUri == null) {
                        Log.i("FILE URI", "NULL");
                    }
                    Picasso.with(getActivity()).load(pictureProfileUri.toString()).fit().centerCrop().into(imageView,
                            new Callback() {

                                @Override
                                public void onSuccess() {
                                }

                                @Override
                                public void onError() {
                                }
                            });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CONFRIM) {
            if (resultCode == RESULT_CONFRIM) {

            }
        }

    }

    private void savePicture(String filename, Bitmap b, Context ctx) {
        try {
            FileOutputStream out = ctx.openFileOutput(filename, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.JPEG, 50, out);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Bitmap loadPicture(String filename) {
        Bitmap bitmapFromFile = null;
        try {
            FileInputStream fis = getActivity().openFileInput(filename);
            bitmapFromFile = BitmapFactory.decodeStream(fis);
            fis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmapFromFile;

    }




    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
