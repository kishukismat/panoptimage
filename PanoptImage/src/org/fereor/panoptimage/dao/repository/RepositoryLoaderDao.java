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

package org.fereor.panoptimage.dao.repository;

import java.util.ArrayList;
import java.util.List;

import org.fereor.panoptimage.dao.async.RepositoryDirListener;
import org.fereor.panoptimage.dao.async.RepositoryGetListener;
import org.fereor.panoptimage.exception.PanoptimageException;
import org.fereor.panoptimage.exception.PanoptimageFileNotFoundException;
import org.fereor.panoptimage.exception.PanoptimagePeerUnverifiedException;
import org.fereor.panoptimage.util.PanoptimageHelper;

/**
 * Interface able to analyze the content of a repository
 * 
 * @author "arnaud.p.fereor"
 */
public abstract class RepositoryLoaderDao<T> {
	/** parent parameter */
	protected T param;
	/** Root of directory to browse */
	protected String root;
	/** Current path */
	protected List<String> currentPath = new ArrayList<String>();

	/**
	 * Constructor for child classes
	 * 
	 * @param param parameter to use in the class
	 */
	protected RepositoryLoaderDao(T param) {
		this.param = param;
	}

	/**
	 * Change the repository location
	 * 
	 * @param path path to navigate to
	 * @return list of child locations
	 */
	public void cd(String path) {
		// test param values
		if (path == null || path.length() == 0) {
			return;
		} else if (PanoptimageHelper.DOT.equals(path.trim())) {
			// test param values
			return;
		} else if (PanoptimageHelper.DDOT.equals(path.trim()) && currentPath.size() != 0) {
			// if currentpath is not empty and .. is required
			currentPath.remove(currentPath.size() - 1);
		} else if (!path.trim().contains(PanoptimageHelper.SLASH)) {
			// if currentpath does not contain a /, add it
			currentPath.add(path.trim());
		} else {
			// recursive cd
			String subpath = path.substring(0, path.indexOf(PanoptimageHelper.SLASH));
			String postpath = path.substring(path.indexOf(PanoptimageHelper.SLASH) + 1, path.length());
			cd(subpath);
			cd(postpath);
		}
	}

	/**
	 * Returns the formatted value of the path
	 * 
	 * @return path formatted
	 */
	public String getformatedPath() {
		return PanoptimageHelper.formatPath(currentPath);
	}

	/**
	 * Returns true if the path is root
	 * 
	 * @return
	 */
	public boolean isRoot() {
		return currentPath.size() == 0;
	}

	/**
	 * Read the content of current path location
	 * 
	 * @param regexp regular expression to match to search for files
	 * @return list of child locations
	 * @throws PanoptimagePeerUnverifiedException 
	 */
	public abstract List<String> dir(String regexp, RepositoryDirListener<Long, List<String>> lsn)
			throws PanoptimageFileNotFoundException, PanoptimagePeerUnverifiedException;

	/**
	 * Get the content of a repository location
	 * 
	 * @param location location to get
	 * @return byte content of location
	 */
	public abstract RepositoryContent get(String location, RepositoryGetListener<Long, RepositoryContent> repositoryGetListener) throws PanoptimageException;

	/**
	 * Tests if a path exists (absolute or relative)
	 * 
	 * @param path path to test
	 * @return true if the path exists
	 * @throws PanoptimageFileNotFoundException
	 */
	public abstract boolean exists(String path);

	/**
	 * Tests if the path is a directory
	 * 
	 * @param path path to test
	 * @return true if the path is a directory
	 */
	public abstract boolean isDirectory(String path);

	/**
	 * Determines if we should dispaly a splash screen while loading
	 * 
	 * @return true is the splash has to be displayed
	 */
	public abstract boolean showSplashWhileLoading();
}
