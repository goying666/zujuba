package renchaigao.com.zujuba.util.DataFunctions;


import com.alibaba.fastjson.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Administrator on 2019/2/28/028.
 */

public class DataSort<T> {

    private T t;
    public  ArrayList<T> SequenceSort(ArrayList<T> a, final String param){
        Collections.sort(a, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
//                    从小到大 ：0：最小  size：最大
                JSONObject jsonO1 = JSONObject.parseObject(JSONObject.toJSONString(o1));
                JSONObject jsonO2 = JSONObject.parseObject(JSONObject.toJSONString(o2));
                return (int) (jsonO1.getLong(param) - jsonO2.getLong(param) );
            }
        });
        return new ArrayList<T>(a);
    }
    public  ArrayList<T> InvertedSort(ArrayList<T> a, final String param){
        Collections.sort(a, new Comparator<T>() {
            @Override
            public int compare(T o1, T o2) {
//                    从大到小
                JSONObject jsonO1 = JSONObject.parseObject(JSONObject.toJSONString(o1));
                JSONObject jsonO2 = JSONObject.parseObject(JSONObject.toJSONString(o2));
                return (int) (jsonO2.getLong(param) - jsonO1.getLong(param) );
            }
        });
        return new ArrayList<T>(a);
    }
}
