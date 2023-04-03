package uz.mirzokhidkh.springbootthreads.util;

import java.util.HashMap;
import java.util.Map;

public class Util {
    public static HashMap<String, Integer> counterThreadTaskMap = new HashMap<>();

    public synchronized static void putToMap(String key) {
        counterThreadTaskMap.put(key, counterThreadTaskMap.getOrDefault(key, 0) + 1);
    }

    public static int getSum() {
        int sum = 0;
        for (String key : counterThreadTaskMap.keySet())
            sum += counterThreadTaskMap.get(key);
        return sum;
    }

    public static void getStatisticForEachThread() {
        for (Map.Entry m : counterThreadTaskMap.entrySet()) {
            System.out.println("'"+m.getKey() + "' did " + m.getValue() + " tasks.");
        }
    }



}
