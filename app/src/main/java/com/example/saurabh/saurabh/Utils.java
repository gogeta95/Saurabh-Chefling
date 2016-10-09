package com.example.saurabh.saurabh;

/**
 * Created by saurabh on 9/10/16.
 */

public class Utils {
    public static int searchString(String[] arr,String text){
        if (arr==null||arr.length==0)
            return -1;
        for (int i=0;i<arr.length;i++){
            if (arr[i].equals(text)){
                return i;
            }
        }
        return -1;
    }
    public static String getFormattedTime(int hourOfDay, int minute){
        String hours="";
        String minutes="";
        if (hourOfDay==1){
            hours="1 hr";
        }
        else if (hourOfDay>1){
            hours=(hourOfDay + " hrs");
        }
        if (minute==1){
            minutes=" 1 min";
        }
        else if(minute>1){
            minutes=(" "+minute+" mins");
        }
        return hours+minutes;
    }
}
