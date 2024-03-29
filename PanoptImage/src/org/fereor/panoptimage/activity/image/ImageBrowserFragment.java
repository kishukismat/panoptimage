// This file is part of panoptimage.
//
// panoptimage is free software: you can redistribute it and/or modify
// it under the terms of the GNU General Public License as published by
// the Free Software Foundation, either version 3 of the License, or
// (at your option) any later version.
//
// panoptimage is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
// GNU General Public License for more details.
//
// You should have received a copy of the GNU General Public License
// along with panoptimage.  If not, see <http://www.gnu.org/licenses/>

package org.fereor.panoptimage.activity.image;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.fereor.panoptimage.R;
import org.fereor.panoptimage.dao.async.RepositoryDirListener;
import org.fereor.panoptimage.util.PanoptimageConstants;
import org.fereor.panoptimage.util.PanoptimageHelper;
import org.fereor.panoptimage.util.PanoptimageMsg;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Fragment class for the Browse panel
 * 
 * @author "arnaud.p.fereor"
 */
public class ImageBrowserFragment extends Fragment implements RepositoryDirListener<Long, List<String>> {
	/** mark reference of list view to update it */
	private ListView lv = null;
	/** mark reference for loading message */
	private TextView msg = null;
	/** current path */
	private String path = null;
	/** mark if the root directory is displayed */
	private boolean root = false;

	/**
	 * The Fragment's UI is a
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_image_browse, container, false);
		lv = (ListView) v.findViewById(R.id.image_browse_list);
		msg = (TextView) v.findViewById(R.id.image_browse_loading);
		return v;
	}

	@Override
	public void onPreDir() {
		lv.setVisibility(View.INVISIBLE);
		msg.setVisibility(View.VISIBLE);
	}

	@Override
	public void onPostDir(List<String> rawdir) {
		if (getActivity() == null) {
			// fragment is no more in the activity (screen has rotated) : return
			return;
		}
		ArrayList<String> directories = new ArrayList<String>();
		// Include .. to the list
		if (!isRoot()) {
			directories.add(PanoptimageHelper.DDOT);
		}
		// Check results
		if (rawdir == null) {
			PanoptimageMsg.showErrorMsg(getActivity(), R.string.error_filenotfound, path);
		} else if (rawdir.contains(PanoptimageConstants.ERROR_FILE_NOT_FOUND)) {
			PanoptimageMsg.showErrorMsg(getActivity(), R.string.error_filenotfound, path);
		} else if (rawdir.contains(PanoptimageConstants.ERROR_PEER_UNVERIFIED)) {
			PanoptimageMsg.showErrorMsg(getActivity(), R.string.error_peerunverified);
		} else {
			// sort rawdir
			Collections.sort(rawdir);
			for (String it : rawdir) {
				if (!it.isEmpty()) {
					directories.add(it);
				}
			}
		}
		// hide loading message
		msg.setVisibility(View.INVISIBLE);
		// get content for the list
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.listrow_browse,
				R.id.create_row_message, directories);
		lv.setAdapter(adapter);
		lv.setVisibility(View.VISIBLE);
		// set listener
		lv.setOnItemClickListener((OnItemClickListener) getActivity());
	}

	@Override
	public void onDirProgressUpdate(Long... values) {
		// Do nothing
	}

	@Override
	public void onOEM(Throwable t) {
		PanoptimageMsg.showErrorMsg(getActivity(), R.string.error_outofmemory);
	}

	/**
	 * @return the root
	 */
	public boolean isRoot() {
		return root;
	}

	/**
	 * @param root the root to set
	 */
	public void setRoot(boolean root) {
		this.root = root;
	}

	/**
	 * @return the path
	 */
	public String getPath() {
		return path;
	}

	/**
	 * @param path the path to set
	 */
	public void setPath(String path) {
		this.path = path;
	}
}
