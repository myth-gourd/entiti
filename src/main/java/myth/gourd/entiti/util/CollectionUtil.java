package myth.gourd.entiti.util;

import java.util.HashSet;
import java.util.Set;

public class CollectionUtil {
	public static <T> Set<T> toSet(T[] arr)
	{
		Set<T> set = new HashSet<T>();
		
		for(int i=0;i<arr.length;i++)
		{
			set.add(arr[i]);
		}
		return set;
	}
}
