package com.ubl.struktur.data.util;

import java.util.function.Function;

public class SearchUtil {

	/**
	 * implementation search algorithm using Binnary Search with Java Generic Type nad custom key extractor
	 * 
	 * <p>The returned value is single object from array, because this operation 
	 * 		just meets first exactly match value then finish. Not like search filter with contains or like operator
	 * 
	 * Because the given key is string from user input, we need some functions that convert the generic array objects
	 * or field property of generic type to String value, so we can do compare operation
	 * 
	 * @param list is given array of object in generic type
	 * @param key is given value by user to perform search operation
	 * @param keyExtractor is java function for extract string value from field of given type
	 * 
	 * */
	public static <T> T binarySearchByStringField(T[] list, String key, Function<T, String> keyExtractor) {
	    int low = 0;
	    int high = list.length - 1;
	    int index = 0;

	    while (low <= high) {
	    	
	        int mid = (low + high) / 2;
	        int cmp = key.compareTo(keyExtractor.apply(list[mid]));
	        if (cmp == 0) {
	            index = mid;
	            return list[index];
	        } else if (cmp < 0) {
	            high = mid - 1;
	        } else {
	            low = mid + 1;
	        }
	    }
	    return null;
	}
}
