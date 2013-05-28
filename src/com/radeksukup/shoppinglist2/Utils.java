/**
 * 
 */
package com.radeksukup.shoppinglist2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.Normalizer;

import android.util.Base64;
import android.util.Base64InputStream;
import android.util.Base64OutputStream;

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
	
	public static String serializeObjectToString(ShoppingList object) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		
		oos.writeObject(object);
		byte[] data = baos.toByteArray();
		oos.close();
		baos.close();
		
		ByteArrayOutputStream baosOut = new ByteArrayOutputStream();
		Base64OutputStream base64 = new Base64OutputStream(baosOut, Base64.DEFAULT);
		base64.write(data);
		base64.close();
		baosOut.close();
		
		return new String(baosOut.toByteArray());
	}
	
	public static ShoppingList deserializeObjectFromString(String str) throws Exception {
		byte[] bytes = str.getBytes();
		if (bytes.length == 0) {
			return null;
		}
		ByteArrayInputStream bais = new ByteArrayInputStream(bytes);
		Base64InputStream base64 = new Base64InputStream(bais, Base64.DEFAULT);
		ObjectInputStream ois = new ObjectInputStream(base64);
		return (ShoppingList) ois.readObject();
	}

}
