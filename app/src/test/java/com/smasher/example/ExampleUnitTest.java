package com.smasher.example;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
        String ints = "0653";
        int a = Integer.parseInt(ints);
        System.out.println(a);

        long valueTen = System.currentTimeMillis();
        String valuesss = String.valueOf(valueTen).substring(0, 8);
        //将其转换为十六进制并输出
        System.out.println(valuesss);
        String strHex = Integer.toHexString(Integer.parseInt(valuesss));
        System.out.println(valueTen + " [十进制]---->[十六进制] " + strHex);
        //将十六进制格式化输出
        String strHex2 = String.format("%08x", valueTen);
        System.out.println(valueTen + " [十进制]---->[十六进制] " + strHex2);
    }
}