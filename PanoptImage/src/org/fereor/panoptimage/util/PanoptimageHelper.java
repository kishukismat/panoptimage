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

package org.fereor.panoptimage.util;

import java.util.List;

import android.util.Log;

/**
 * Utility class to keep several static methods
 * 
 * @author "arnaud.p.fereor"
 */
public class PanoptimageHelper {
	public static final String DOT = ".";
	public static final String DDOT = "..";
	public static final String SLASH = "/";
	public static final String HTTPBASE = "://";
	public static final String HTTPPORT = ":";
	public static final String REGEXP_ALLIMAGES = ".+\\.jpg|.+\\.jpeg|.+\\.png|.+\\.gif|.+\\.JPG|.+\\.JPEG|.+\\.PNG|.+\\.GIF";
	public static final String REGEXP_DIRECTORY = "^[^\\\\.]*$";
	private static final String[][] HTML_ENCODING_MAP = {
			{ " ", "%", "$", "&", "+", ",", "/", ":", ";", "=", "?", "@", "<",
					">", "#", "à", "á", "â", "ã", "ä", "ç", "è", "é",
					"ê", "ë", "ì", "í", "î", "ï", "ñ", "ô", "ö", "û",
					"ü" },
			{ "%20", "%25", "%24", "%26", "%2B", "%2C", "%2F", "%3A", "%3B",
					"%3D", "%3F", "%40", "%3C", "%3E", "%23", "%c3%a0",
					"%c3%a1", "%c3%a2", "%c3%a3", "%c3%a4", "%c3%a7", "%c3%a8",
					"%c3%a9", "%c3%aa", "%c3%ab", "%c3%ac", "%c3%ad", "%c3%ae",
					"%c3%af", "%c3%b1", "%c3%b4", "%c3%b6", "%c3%bb", "%c3%bc" } };

	/**
	 * Format a string to a path using SLASH
	 * 
	 * @param path
	 * @return
	 */
	public static final String formatPath(List<String> path, String... others) {
		return formatPath(null, path, others);
	}

	/**
	 * Format a string to a path using SLASH
	 * 
	 * @param root
	 *            root path
	 * @param path
	 *            current path
	 * @param others
	 *            others as list
	 * @return
	 */
	public static final String formatPath(String root, List<String> path,
			String... others) {
		StringBuilder str = new StringBuilder();
		if (root != null) {
			str.append(root);
		}
		for (String cur : path) {
			str.append(cur).append(SLASH);
		}
		for (String cur : others) {
			str.append(cur).append(SLASH);
		}
		if (str.length() == 0)
			return "";
		if (SLASH.equals(str.substring(str.length() - 1, str.length()))) {
			return str.substring(0, str.length() - 1);
		}
		return str.toString();
	}

	/**
	 * Convert a char to hex string
	 * 
	 * @param ch
	 *            character to convert
	 * @return converted character
	 */
	private static char toHexChar(int ch) {
		return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
	}

	/**
	 * Test if a character is unsafe
	 * 
	 * @param ch
	 *            character to test
	 * @return true if unsafe
	 */
	private static boolean convertIt(char ch) {
		if (ch > 128 || ch < 0)
			return true;
		return " %$&+,/:;=?@<>#".indexOf(ch) >= 0;
	}

	/**
	 * Encode a URL to allow correct URL parsing by web server
	 * 
	 * @param url
	 *            url to encode
	 * @return url encoded
	 */
	public static String encodeUrl(String url) {
		String res = new String(url);
		for (int i = 0; i < HTML_ENCODING_MAP[0].length; i++) {
			res = res.replace(HTML_ENCODING_MAP[0][i], HTML_ENCODING_MAP[1][i]);
		}
		return res;
	}

	/**
	 * Decode a URL given by Web server
	 * 
	 * @param url
	 *            url to decode
	 * @return url decoded
	 */
	public static String decodeUrl(String url) {
		String res = new String(url);
		for (int i = 0; i < HTML_ENCODING_MAP[0].length; i++) {
			res = res.replace(HTML_ENCODING_MAP[1][i], HTML_ENCODING_MAP[0][i]);
		}
		return res;
	}

	public static void generateTable() {
		StringBuilder keyStr = new StringBuilder();
		StringBuilder resultStr = new StringBuilder();
		String path = " %$&+,/:;=?@<>#èéàùâäêëîïôöuü";
		for (char ch : path.toCharArray()) {
			if (convertIt(ch)) {
				keyStr.append("\"");
				keyStr.append(ch);
				keyStr.append("\", ");

				resultStr.append("\"");
				resultStr.append('%');
				resultStr.append(toHexChar(ch / 16));
				resultStr.append(toHexChar(ch % 16));
				resultStr.append("\", ");
			}
		}
		String key = keyStr.toString();
		String res = resultStr.toString();
		Log.d(PanoptimageConstants.TAGNAME, key + "-" + res);
	}
}
