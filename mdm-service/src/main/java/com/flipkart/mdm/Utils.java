package com.flipkart.mdm;

import java.util.Collection;
import java.util.Iterator;

/**
 * Created by setu.poddar on 11/08/17.
 */
public class Utils {
    public static  String convert(Collection<String> collection){
        StringBuffer sb = new StringBuffer();
        Iterator it = collection.iterator();
        while(it.hasNext()){
            sb.append(it.next());
            if(it.hasNext())
                sb.append(",");
        }

        return sb.toString();
    }
}
