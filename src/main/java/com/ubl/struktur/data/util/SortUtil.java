package com.ubl.struktur.data.util;

import java.util.Comparator;

public class SortUtil {

	public enum SortType {ASC, DESC}
	
	// implementation sort algorithm using Quick Sort with Java Generic Type
	public static <T> T[] sorts(T[] list, Comparator<T> c, SortType type) {
		quicksort(list, 0, list.length-1, c, type);
		return list;
	}

	private static <T> void quicksort(T[] array, int startIndex, int endIndex, Comparator<T> c, SortType type) {
		// verify that the start and end index have not overlapped
		if (startIndex < endIndex) {
			// calculate the pivotIndex
			int pivotIndex = partition(array, startIndex, endIndex, c, type);
			// sort the left sub-array
			quicksort(array, startIndex, pivotIndex, c, type);
			// sort the right sub-array
			quicksort(array, pivotIndex + 1, endIndex, c, type);
		}
	}

	private static <T> int partition(T[] array, int startIndex, int endIndex, Comparator<T> c, SortType type) {
		int pivotIndex = (startIndex + endIndex) / 2;
		T pivotValue = array[pivotIndex];
		startIndex--;
		endIndex++;

		while (true) {
			// start at the FIRST index of the sub-array and increment
			// FORWARD until we find a value that is > pivotValue for ascend or is < pivotValue for descend
			do
				startIndex++;
			while (type == SortType.ASC ? c.compare(array[startIndex], pivotValue) < 0 : c.compare(array[startIndex], pivotValue) > 0);

			// start at the LAST index of the sub-array and increment
			// BACKWARD until we find a value that is < pivotValue for asend or is > pivotvalue for descend
			do
				endIndex--;
			while (type == SortType.ASC ? c.compare(array[endIndex], pivotValue) > 0 : c.compare(array[endIndex], pivotValue) < 0);

			if (startIndex >= endIndex)
				return endIndex;

			// swap values at the startIndex and endIndex
			T temp = array[startIndex];
			array[startIndex] = array[endIndex];
			array[endIndex] = temp;
		}
	}
}
