package com.mobilebanking.core.uiutils;

import java.util.Locale;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilebanking.core.R;
 
public class CountryAdapter extends ArrayAdapter<String> {
	private final Context context;
	private final String[] values;
	private int dropdownStyleId;

	public CountryAdapter(Context context, String[] values, int viewId, int dropdownStyleId) {
		super(context, R.layout.country_list_item, viewId, values);
		this.context = context;
		this.values = values;
		this.dropdownStyleId = dropdownStyleId;
	}
 
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
 
		View rowView = inflater.inflate(R.layout.country_list_item, parent, false);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imgViewFlag);
    	
    	imageView.setImageResource(context.getResources().getIdentifier("drawable/" + values[position], null, context.getPackageName()));
		
    	return rowView;
	}
	
	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);		
		View rowView = inflater.inflate(R.layout.country_list_item_dropdown, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.txtViewCountryName);
		ImageView imageView = (ImageView) rowView.findViewById(R.id.imgViewFlag);
		
		textView.setText(getCountryFullname(values[position]));
		textView.setTextAppearance(context,dropdownStyleId);

    	imageView.setImageResource(context.getResources().getIdentifier("drawable/" + values[position], null, context.getPackageName()));

		return rowView;
	}
	
	public static String getCountryZipCode(Context context, String ssid){

		String[] rl=context.getResources().getStringArray(R.array.CountryCodes);
	    for(int i=0;i<rl.length;i++){
	        String[] g =rl[i].split(",");
	        if(g[1].trim().equalsIgnoreCase(ssid.trim())){
	        	return g[0];
	        }
	    }
	    return null;
	}
	
	public String getCountryFullname(String ssid){
        Locale loc = new Locale("", ssid);      
        Log.d("COUNTRY CODE", loc.getCountry());
        String displayCountry = loc.getDisplayCountry().trim();
        String callingCode = getCountryZipCode(context, ssid);
        
        if(callingCode!=null){
        	return displayCountry + " ( + " + callingCode + " )";
        }
        return displayCountry;
    }
}