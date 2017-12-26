package sbs.thundercraft.co.zw.sbsfreelancer.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

import retrofit2.Callback;
import retrofit2.Response;
import sbs.thundercraft.co.zw.sbsfreelancer.MainActivity;
import sbs.thundercraft.co.zw.sbsfreelancer.R;
import sbs.thundercraft.co.zw.sbsfreelancer.inteface.Api;
import sbs.thundercraft.co.zw.sbsfreelancer.models.Customers;
import sbs.thundercraft.co.zw.sbsfreelancer.models.Documents;
import sbs.thundercraft.co.zw.sbsfreelancer.service.ApiUtils;
import sbs.thundercraft.co.zw.sbsfreelancer.utils.FileUtils;
import sbs.thundercraft.co.zw.sbsfreelancer.utils.PictureUtilities;
import sbs.thundercraft.co.zw.sbsfreelancer.utils.ApplicationUtilities;
import sbs.thundercraft.co.zw.sbsfreelancer.utils.WaitDialog;
import sbs.thundercraft.co.zw.sbsfreelancer.utils.WarningDialog;

import static sbs.thundercraft.co.zw.sbsfreelancer.MainActivity.getContextOfApplication;
import static sbs.thundercraft.co.zw.sbsfreelancer.fragments.OmniAccountOpenning.REQUEST_IMAGE_CAPTURE;
import static sbs.thundercraft.co.zw.sbsfreelancer.utils.ConfirmDialog.REQUEST_CONFRIM;
import static sbs.thundercraft.co.zw.sbsfreelancer.utils.ConfirmDialog.RESULT_CONFRIM;


public class ResidenceFragment extends Fragment {


    private Api mAPIService;
    private Uri residencePicture;
    public Uri pictureProfileUri;
    public Uri idPictureProfile;
    Bundle arguments;
    Context context;

    ImageView  imageClientID;
    Button btnGo;

    public ResidenceFragment() {
        // Required empty public constructor

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            arguments = getArguments();
        }


    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {


            try {

                if (pictureProfileUri == null) {
                    Log.i("FILE URI", "NULL");
                }
                Picasso.with(getActivity()).load(residencePicture.toString()).fit().centerCrop().into(imageClientID,
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_residence, container, false);

        mAPIService = ApiUtils.getAPIService();
        imageClientID =  view.findViewById(R.id.residence_picture);
        imageClientID.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                takePicture();
            }
        });
        context =  getActivity();

        btnGo = (Button) view.findViewById(R.id.id_residence_button);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                new WaitDialog().show(getFragmentManager(),"dialog");
          Customers customers = new Customers();

          customers.setName(String.valueOf(arguments.get("name")));
          customers.setSurname(String.valueOf(arguments.get("surname")));
          customers.setIdNumber(String.valueOf(arguments.get("IdNumber")));
          customers.setEmail(String.valueOf(arguments.get("address")));
          customers.setMsisdn(String.valueOf(arguments.get("phoneNumber")));
          customers.setCardNumber(String.valueOf( arguments.get("cardNumber")));


                List<Documents> list = new ArrayList<>();
                Documents iddocument = new Documents();
                iddocument.setDocumentType("ID");

                iddocument.setFileUrl(idPictureProfile);
                list.add(iddocument);

                Documents headShotdocument = new Documents();
                headShotdocument.setDocumentType("headShot");

                headShotdocument.setFileUrl(pictureProfileUri);
                list.add(headShotdocument);

                Documents residenceDocument = new Documents();
                residenceDocument.setDocumentType("residence");
                headShotdocument.setFileUrl(residencePicture);
                list.add(headShotdocument);
                customers.setDocuments(list);
                MultipartBody.Part idPartImage = prepareFilePart("idImage",idPictureProfile);
                MultipartBody.Part headshotPartImage = prepareFilePart("headshotImage",pictureProfileUri);
                MultipartBody.Part residencePartImage = prepareFilePart("residenceImage",residencePicture);

                send(customers,idPartImage,headshotPartImage,residencePartImage);


            }
        });
        return view;
    }


    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {

        File file = FileUtils.getFile(getActivity(), fileUri);

        // create RequestBody instance from file

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"),file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    public void takePicture() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        residencePicture = PictureUtilities.getOutputMediaFileUri(PictureUtilities.MEDIA_TYPE_IMAGE);
        takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, residencePicture);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }


    }

    public void send(Customers customer, MultipartBody.Part IdPicture,  MultipartBody.Part headshotPicture,  MultipartBody.Part residencePicture) {
        try {
            mAPIService.saveCustomer(customer, IdPicture, headshotPicture, residencePicture).enqueue(new Callback<Customers>() {
                @Override
                public void onResponse(Call<Customers> call, Response<Customers> response) {
                    ApplicationUtilities.dismisDialog(getFragmentManager());
                    if (response.isSuccessful()) {
                        showResponse(response.body().toString());

                    }
                }

                @Override
                public void onFailure(Call<Customers> call, Throwable t) {
                    ApplicationUtilities.dismisDialog(getFragmentManager());
                    new WarningDialog("Failure", "Transaction was unsuccessful");
                }

            });
        }catch (Exception d){

            AlertDialog.Builder builder;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                builder = new AlertDialog.Builder(context, android.R.style.Theme_Material_Dialog_Alert);
            } else {
                builder = new AlertDialog.Builder(context);
            }
            builder.setTitle("Failure")
                    .setMessage("Server unavailable will save and process request later please try again later")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    public void showResponse(String response) {
        ApplicationUtilities.showDialog(getFragmentManager(),"Response",response);
    }



    @Override
    public void onResume() {
        super.onResume();


    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

}
