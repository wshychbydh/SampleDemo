package com.cool.eye.func.view.trend.helper;

import com.cool.eye.func.view.trend.mode.GoldNode;
import com.cool.eye.func.view.trend.mode.Node;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Random;

public class GoldTariffHelper {

    public static List<Node> mList = new ArrayList<>();
    public static List<GoldNode> mList2 = new ArrayList<>();

    static {
        Random random = new Random();
        mList.clear();
        String money = "";
        for (int i = 0; i < 24; i++) {
            Node gt = new Node();
            money = 266 + "." + random.nextInt(5);
            gt.setValueY(Float.valueOf(money));
            gt.setValueX(i);
            mList.add(gt);
        }


        Calendar instance = Calendar.getInstance();
        for (int i = 0; i < 7; i++) {
            instance.add(Calendar.DAY_OF_MONTH, 1);
            GoldNode gt = new GoldNode();
            money = 250 + random.nextInt(10) + "." + random.nextInt(99);
            gt.setValueY(Double.valueOf(money));
            gt.setValueX(instance.getTimeInMillis());
            mList2.add(gt);
        }
        sort();
    }

    private static void sort() {
        Collections.sort(mList2, new Comparator<GoldNode>() {
            @Override
            public int compare(GoldNode goldNode, GoldNode t1) {
                if (goldNode.getValueX() > t1.getValueX()) {
                    return 1;
                } else if (goldNode.getValueX() < t1.getValueX()) {
                    return -1;
                }
                return 0;
            }
        });
    }
}
