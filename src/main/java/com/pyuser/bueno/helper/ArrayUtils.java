package com.pyuser.bueno.helper;

/** in lieu of
 * import org.apache.commons.lang3.ArrayUtils;
 */

public class ArrayUtils {
    public static int indexOf(char[] array, char target ) {
        // https://www.techiedelight.com/find-index-element-array-java/
        for (int i = 0; i < array.length; i++)
            if (array[i] == target)
                return i;

        return -1;
    }
}
