package com.ytempest.chartrepository.chart1;

import android.util.SparseArray;

import com.ytempest.chart.SimpleBarGraph;

import java.util.ArrayList;
import java.util.List;

/**
 * @author heqidu
 * @since 2019/12/1
 */
public class GraphAdapter implements SimpleBarGraph.Adapter {

    private List<SimpleBarGraph.PillarConfig> mPillarConfigs = new ArrayList<>();
    private List<SimpleBarGraph.Pillar> mPillarPillars = new ArrayList<>();

    public GraphAdapter() {
        mPillarConfigs.add(new PillarConfig(0xFF707070, "Before Baking"));
        mPillarConfigs.add(new PillarConfig(0xFFFDB20D, "After Baking"));

        ArrayList<Float> data1 = new ArrayList<>();
        data1.add(2.86F);
        data1.add(2.91F);
        mPillarPillars.add(new PillarPillar("1#", data1));

        ArrayList<Float> data2 = new ArrayList<>();
        data2.add(2.90F);
        data2.add(2.82F);
        mPillarPillars.add(new PillarPillar("2#", data2));

        ArrayList<Float> data3 = new ArrayList<>();
        data3.add(2.84F);
        data3.add(2.95F);
        mPillarPillars.add(new PillarPillar("3#", data3));

        ArrayList<Float> data4 = new ArrayList<>();
        data4.add(2.88F);
        data4.add(2.90F);
        mPillarPillars.add(new PillarPillar("4#", data4));

        ArrayList<Float> data5 = new ArrayList<>();
        data5.add(2.80F);
        data5.add(2.86F);
        mPillarPillars.add(new PillarPillar("5#", data5));
    }

    private SimpleBarGraph.ScaleConfigs mScaleConfigs = new SimpleBarGraph.ScaleConfigs() {
        private SparseArray<String> cache = new SparseArray<>();

        @Override
        public float getStart() {
            return 2.78F;
        }

        @Override
        public float getEnd() {
            return 2.96F;
        }

        @Override
        public int getCount() {
            return 10;
        }

        @Override
        public String getText(int position) {
            String text = cache.get(position);
            if (text == null) {
                text = String.format("%.2f", (getStart() + 0.02F * position));
                cache.put(position, text);
            }
            return text;
        }
    };

    @Override
    public SimpleBarGraph.ScaleConfigs getScaleConfigs() {
        return mScaleConfigs;
    }

    @Override
    public List<SimpleBarGraph.PillarConfig> getPillarConfigs() {
        return mPillarConfigs;
    }

    @Override
    public int getDataCount() {
        return mPillarPillars.size();
    }

    @Override
    public int getTypeCount() {
        return 2;
    }

    @Override
    public SimpleBarGraph.Pillar getPillar(int position) {
        return mPillarPillars.get(position);
    }

    public static class PillarConfig implements SimpleBarGraph.PillarConfig {

        private final int color;
        private final String name;

        public PillarConfig(int color, String name) {
            this.color = color;
            this.name = name;
        }

        @Override
        public int getColor() {
            return color;
        }

        @Override
        public String getPillarName() {
            return name;
        }
    }

    public static class PillarPillar implements SimpleBarGraph.Pillar {
        private final String name;
        private final List<Float> peakList;

        public PillarPillar(String name, List<Float> list) {
            this.name = name;
            this.peakList = list;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public List<Float> getPeakList() {
            return peakList;
        }
    }
}
