import org.apache.commons.lang3.ArrayUtils;
import org.junit.Test;

import java.lang.reflect.Array;
import java.util.Arrays;

public class demo {
    @Test
    public void test1(){
        int a[]={0,1,5,6,7,9,14};
        int b[]={2,4,8,10,13};
        int c[]= ArrayUtils.addAll(a,b);
        for (int i : c) {
            System.out.print(i);
        }
    }
}
