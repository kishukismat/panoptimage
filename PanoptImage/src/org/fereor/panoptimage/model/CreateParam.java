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

package org.fereor.panoptimage.model;

import android.os.Parcelable;

/**
 * Common class to represent all parameters
 * 
 * @author "arnaud.p.fereor"
 */
public abstract class CreateParam implements Parcelable {
	/**
	 * Return key value of this parameter
	 * @return key
	 */
	public abstract String getKey();
	
	/**
	 * Return if this parameter has data
	 * @return true if the parameter has data to display, false else
	 */
	public abstract boolean hasData();
}
