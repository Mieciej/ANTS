package com.maciej.ants;

public class Math {
    public static float[] haltonSequence(int base, int size){
        int n = 0;
        int d = 1;
        int i =0;
        float[] result = new float[size];
        while (i<size){
            int x = d -n;
            if (x==1){
                n=1;
                d*=base;
            }
            else {
                int y = d / base;
                while (x <= y) {
                    y /= base;
                }
                n = (base+1) *y -x;
            }
            result[i] = (float) n/(float) d;
            i++;
        }
        return result;
    }
}
