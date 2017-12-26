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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import sbs.thundercraft.co.zw.sbsfreelancer.MainActivity;
import sbs.thundercraft.co.zw.sbsfreelancer.R;
import sbs.thundercraft.co.zw.sbsfreelancer.utils.ApplicationUtilities;
import sbs.thundercraft.co.zw.sbsfreelancer.utils.ConfirmDialog;
import sbs.thundercraft.co.zw.sbsfreelancer.utils.WarningDialog;

import static sbs.thundercraft.co.zw.sbsfreelancer.utils.ApplicationUtilities.validateNotEmpty;

public class  OmniAccountOpenning  extends Fragment implements OnClickListener{



	static final int PICTURE_ID = 2001;
	static final int PICTURE_PROFILE = 2002;

	static final int REQUEST_IMAGE_CAPTURE = 1002345;

	Activity parent;
	ImageView imageClient, imageClientID;



	private Uri pictureIdUri;
	private Uri pictureProfileUri;
	private int pictureType;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRetainInstance(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		super.onCreateView(inflater, container, savedInstanceState);
		View view = inflater.inflate(R.layout.fragment_omni_account, container, false);
		Button btnContnue = view.findViewById(R.id.continue_button);
		btnContnue.setOnClickListener(this);
		return view;
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if (savedInstanceState != null) {
			if (savedInstanceState.getString("idImage") != null) {
				pictureIdUri = Uri.parse(savedInstanceState.getString("idImage"));
			}
			if (savedInstanceState.getString("profileImage") != null) {
				pictureProfileUri = Uri.parse(savedInstanceState.getString("profileImage"));
			}
		}

	}

	private void setImage(ImageView image, String fileName) {
		Bitmap bitmap = loadPicture(fileName);
		if (bitmap != null) {
			image.setImageBitmap(bitmap);
		}
	}



	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {

			try {
				if (pictureType == PICTURE_ID) {
					if (pictureIdUri == null) {
						Log.i("FILE URI", "NULL");
					}
					Picasso.with(getActivity()).load(pictureIdUri.toString()).fit().centerCrop().into(imageClientID,
							new Callback() {

								@Override
								public void onSuccess() {
								}

								@Override
								public void onError() {
								}
							});
				} else if (pictureType == PICTURE_PROFILE) {
					if (pictureProfileUri == null) {
						Log.i("FILE URI", "NULL");
					}
					Picasso.with(getActivity()).load(pictureProfileUri.toString()).fit().centerCrop().into(imageClient,
							new Callback() {

								@Override
								public void onSuccess() {
								}

								@Override
								public void onError() {
								}
							});
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (requestCode == ConfirmDialog.REQUEST_CONFRIM) {
			if (resultCode == ConfirmDialog.RESULT_CONFRIM) {

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
			FileInputStream fis = parent.openFileInput(filename);
			bitmapFromFile = BitmapFactory.decodeStream(fis);
			fis.close();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bitmapFromFile;

	}

	@Override
	public void onClick(View v) {



		EditText name = (EditText) ApplicationUtilities.findViewById(getActivity(),R.id.first_name_edit_text);
		EditText surname = (EditText) ApplicationUtilities.findViewById(getActivity(),R.id.last_name_edit_text);
		EditText idNumber = (EditText) ApplicationUtilities.findViewById(getActivity(),R.id.national_id_edit_text);
		EditText phoneNumber = (EditText) ApplicationUtilities.findViewById(getActivity(),R.id.msisdn_edit_text);
		EditText address = (EditText)ApplicationUtilities.findViewById(getActivity(),R.id.address_line_1_edit_text);
		EditText cardNumber =  (EditText)ApplicationUtilities.findViewById(getActivity(),R.id.card_edit_text);
		boolean valid = true;

		valid = valid & validateNotEmpty(name);
		valid = valid & validateNotEmpty(surname);
		valid = valid & validateNotEmpty(idNumber);
		valid = valid & validateNotEmpty(phoneNumber);
		valid = valid & validateNotEmpty(address);
		valid = valid & validateNotEmpty(cardNumber);

		if (!valid) {
			new WarningDialog(getString(R.string.warning), getString(R.string.validation_error_missing_fields)).show(getFragmentManager(), null);
			return;
		}

		Bundle arguments = new Bundle();

		arguments.putString("name", name.getText().toString());
		arguments.putString("surname", surname.getText().toString());
		arguments.putString("IdNumber", idNumber.getText().toString());
		arguments.putString("address", address.getText().toString());
		arguments.putString("phoneNumber", phoneNumber.getText().toString());
		arguments.putString("cardNumber", cardNumber.getText().toString());


		//TODO call nex page from here
		//new WaitDialog().show(getFragmentManager(), WAIT_DIALOG);
		//TransactionManager.getInstance(getActivity()).requestMposInstantBanking(null, pan, arguments, pictureIdUri,
		//	pictureProfileUri);



		 Fragment fragment = new HeadShotFragment();
		fragment.setArguments(arguments);
		FragmentTransaction transaction = getFragmentManager().beginTransaction();
		transaction.replace(R.id.fragment_container, fragment);
		transaction.addToBackStack(OmniAccountOpenning.class.getSimpleName());
		transaction.commit();

	}


	@Override
	public void onAttach(Activity activity) {

		super.onAttach(activity);
		parent = activity;
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		if (pictureIdUri != null)
			outState.putString("idImage", pictureIdUri.toString());
		if (pictureProfileUri != null)
			outState.putString("profileImage", pictureProfileUri.toString());
		super.onSaveInstanceState(outState);
	}

	private boolean checkEditTextIsNotEmpty(EditText editText) {
		return editText.getText().toString().length() != 0;
	}

	private boolean checkEditTextIsNotEmpty(TextView textView) {
		return textView.getText().toString().length() != 0;
	}

}
