package org.fereor.panoptimage.activity.create;

import java.util.List;

import org.fereor.panoptimage.R;
import org.fereor.panoptimage.util.PanoptesTypeEnum;

import android.app.Activity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CreateParamsSpinnerAdapter extends ArrayAdapter<Pair<CharSequence, PanoptesTypeEnum>> {
	
	/** parent activity */
	private Activity container;

	/**
	 * Default constructor
	 * 
	 * @param context
	 * @param textViewResourceId
	 * @param
	 */
	public CreateParamsSpinnerAdapter(Activity context, List<Pair<CharSequence, PanoptesTypeEnum>> data,
			int textViewResourceId) {
		super(context, textViewResourceId, data);
		this.container = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView v = (TextView) super.getView(position, convertView, parent);
		v.setText(getItem(position).first);
		v.setCompoundDrawablesWithIntrinsicBounds(getItem(position).second.iconxs(), 0, 0, 0);
		return v;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		View row = convertView;
		if (row == null) {
            LayoutInflater inflater = container.getLayoutInflater();
            row = inflater.inflate(R.layout.spinner_createparam, parent, false);
		}

		// get item to display
		Pair<CharSequence, PanoptesTypeEnum> item = getItem(position);

		if (item != null) {
			// Parse the data from each object and set it.
			TextView text = (TextView) row.findViewById(R.id.spinner_row_text);
			text.setText(item.first);
			text.setCompoundDrawablesWithIntrinsicBounds(item.second.iconxs(), 0, 0, 0);
		}

		return row;
	}

}
