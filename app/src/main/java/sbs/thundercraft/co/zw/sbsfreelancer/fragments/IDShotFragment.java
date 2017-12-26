package sbs.thundercraft.co.zw.sbsfreelancer.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import sbs.thundercraft.co.zw.sbsfreelancer.MainActivity;
import sbs.thundercraft.co.zw.sbsfreelancer.R;
import sbs.thundercraft.co.zw.sbsfreelancer.utils.PictureUtilities;

import com.squareup.picasso.Picasso;

import static sbs.thundercraft.co.zw.sbsfreelancer.fragments.OmniAccountOpenning.PICTURE_ID;
import static sbs.thundercraft.co.zw.sbsfreelancer.fragments.OmniAccountOpenning.REQUEST_IMAGE_CAPTURE;
import static sbs.thundercraft.co.zw.sbsfreelancer.utils.ConfirmDialog.REQUEST_CONFRIM;
import static sbs.thundercraft.co.zw.sbsfreelancer.utils.ConfirmDialog.RESULT_CONFRIM;


public class IDShotFragment extends Fragment {

    private Uri idPictureProfile;
    private ImageView idImageView;
    Bundle arguments;
    private OnFragmentInteractionListener mListener;
    ImageView  imageClientID;
    Button btnGo;
    public Uri pictureProfileUri;
    private Uri pictureIdUri;
    private int pictureType;
    public IDShotFragment() {
        // Required empty public constructor
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
        View view = inflater.inflate(R.layout.fragment_idshot, container, false);



        imageClientID =   view.findViewById(R.id.id_picture);
        if (idPictureProfile != null) {
            Picasso.with(getActivity()).load(idPictureProfile.toString()).fit().centerCrop().into(imageClientID);
        }
        // setImage(imageClientID, "clientIdPic.jpeg");
        imageClientID.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // FILE_LOCATION = "clientIdPic.jpeg";
                pictureType = PICTURE_ID;
                takePicture();
            }
        });

        btnGo =  view.findViewById(R.id.id_picture_button);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                ResidenceFragment fragment = new ResidenceFragment();
                fragment.idPictureProfile = idPictureProfile;
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
    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (pictureType == PICTURE_ID) {
            // create a file to save the image
            idPictureProfile =  PictureUtilities.getOutputMediaFileUri(PictureUtilities.MEDIA_TYPE_IMAGE);
            // set the image file name
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, idPictureProfile);
        } else  {
            idPictureProfile = PictureUtilities.getOutputMediaFileUri(PictureUtilities.MEDIA_TYPE_IMAGE);
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, idPictureProfile);
        }

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }

    }
    @Override
    public void onResume() {
        super.onResume();


    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {


            try {

                if (pictureProfileUri == null) {
                    Log.i("FILE URI", "NULL");
                }
                Picasso.with(getActivity()).load(idPictureProfile.toString()).fit().centerCrop().into(imageClientID,
                        new com.squareup.picasso.Callback() {

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

}
