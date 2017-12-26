/**
 *
 * mobile-banking-core
 * Sort.java
 * 
 * Created by Sale on 06.05.2016.
 *
 */
package com.mobilebanking.core.uiutils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * 
 * <b>Author</b>:
 * <p>
 * Created by <b>Sale</b> on 06.05.2016.
 * </p>
 */
public class Sort {

	/**
	 * Sorts recent address alphabetically
	 * 
	 * @param addresses
	 * @return
	 */
	public static ArrayList<String> sortAlphabetically(ArrayList<String> addresses) {
		Collections.sort(addresses, new Comparator<String>() {

			@Override
			public int compare(String lhs, String rhs) {
				if (lhs == null || rhs == null)
					return 0;
				int result = lhs.compareToIgnoreCase(rhs);
				return result;
			}
		});

		return addresses;
	}

}
