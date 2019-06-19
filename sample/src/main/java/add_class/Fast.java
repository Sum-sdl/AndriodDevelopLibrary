package add_class;

import android.util.ArrayMap;
import android.util.SparseArray;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by sdl on 2018/8/10.
 */
public class Fast {

    public static void test(int[] args) {
        int size = args.length - 1;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - i; j++) {
                if (args[j] > args[j + 1]) {
                    int temp = args[j];
                    args[j] = args[j + 1];
                    args[j + 1] = temp;
                    print(args);
                    System.out.println("index=" + i+",j="+j);
                }
            }
        }
        print(args);
    }

    private static void print(int[] args) {
        for (int i = 0; i < args.length; i++) {
            System.out.print(args[i]);
        }

//        Collections.sort();

        HashMap hashMap = new HashMap();

        ArrayMap map = new ArrayMap();
        map.put(1, 1);
//        SparseArray array = new SparseArray();
//        array.put(9,0 );
//        array.get(9);
    }


    public static void main(String[] args) {
//        int[] d = new int[]{4, 1, 5, 2, 3, 7, 6, 8, 9, 0};
//        test(d);

        System.out.println(fomatBigNumber2(999991d,null));
        System.out.println(fomatBigNumber(999991.115666,null));
        System.out.println(fomatBigNumber2(999991.115666,null));
        System.out.println(fomatPriceNumber(999991.113666,null));
        System.out.println(fomatPriceNumber(999991.11,null));
        System.out.println(fomatPriceNumber(999991d,null));
    }


    public static String fomatBigNumber2(Double number, String fomat) {
        if (null == fomat) {
            fomat = "#.00";
        }
        DecimalFormat decimalFormat = new DecimalFormat(fomat);
        String fomatedNumberStr = decimalFormat.format(number);
        return fomatedNumberStr;
    }
    public static String fomatBigNumber(Double number, String fomat) {
        if (null == fomat) {
            fomat = "###,###,###.000";
        }
        DecimalFormat decimalFormat = new DecimalFormat(fomat);
        String fomatedNumberStr = decimalFormat.format(number);
        return fomatedNumberStr;
    }

    public static String fomatPriceNumber(Double number, String fomat) {
        if (null == fomat) {
            fomat = "#.####";
        }
        DecimalFormat decimalFormat = new DecimalFormat(fomat);
        String fomatedNumberStr = decimalFormat.format(number);
        return fomatedNumberStr;
    }

}
