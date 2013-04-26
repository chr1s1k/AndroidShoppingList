/**
 * 
 */
package com.radeksukup.shoppinglist2;

import java.text.Normalizer;

/**
 * @author Radek Sukup
 *
 */
public class Utils {
	
	public static String removeDiacritics(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		str = str.replaceAll("[^\\p{ASCII}]", "");
		return str;
	}

}
