package com.ytempest.chartrepository.chart1;

import android.util.SparseArray;

import com.ytempest.chart.SimpleBarGraph;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author heqidu
 * @since 2019/12/1
 */
public class GraphAdapter implements SimpleBarGraph.Adapter {

    private static final float SCALE_START = 2.78F;
    private static final float SCALE_END = 2.96F;
    private static final int SCALE_COUNT = 10;
    private static final int PILLAR_TYPE_COUNT = 2;

    private List<SimpleBarGraph.Pillar> mPillarPillars = new ArrayList<>();
    private final Random mRandom;

    public GraphAdapter() {
        mRandom = new Random();


        mPillarPillars.add(new PillarPillar("1#", getRandomList(getPillarCount())));
        mPillarPillars.add(new PillarPillar("2#", getRandomList(getPillarCount())));
        mPillarPillars.add(new PillarPillar("3#", getRandomList(getPillarCount())));
        mPillarPillars.add(new PillarPillar("4#", getRandomList(getPillarCount())));
        mPillarPillars.add(new PillarPillar("5#", getRandomList(getPillarCount())));
    }

    private List<Float> getRandomList(int size) {
        List<Float> list = new ArrayList<>(size);
        float range = getScaleConfigs().getEnd() - getScaleConfigs().getStart();
        float per = range / getScaleConfigs().getCount();
        for (int i = 0; i < size; i++) {
            float random = mRandom.nextInt(getScaleConfigs().getCount()) + 1;
            list.add(getScaleConfigs().getStart() + random * per);
        }
        return list;
    }

    @Override
    public SimpleBarGraph.ScaleConfigs getScaleConfigs() {
        return new SimpleBarGraph.ScaleConfigs() {
            private SparseArray<String> cache = new SparseArray<>();

            @Override
            public float getStart() {
                return SCALE_START;
            }

            @Override
            public float getEnd() {
                return SCALE_END;
            }

            @Override
            public int getCount() {
                return SCALE_COUNT;
            }

            @Override
            public String getScaleText(int position) {
                String text = cache.get(position);
                if (text == null) {
                    text = String.format("%.2f", (getStart() + 0.02F * position));
                    cache.put(position, text);
                }
                return text;
            }
        };
    }

    @Override
    public List<SimpleBarGraph.PillarConfigs> getPillarConfigs() {
        ArrayList<SimpleBarGraph.PillarConfigs> pillarConfigs = new ArrayList<>();
        pillarConfigs.add(new PillarConfigs(0xFF707070, "Before Baking"));
        pillarConfigs.add(new PillarConfigs(0xFFFDB20D, "After Baking"));
        return pillarConfigs;
    }

    @Override
    public int getPillarCount() {
        return mPillarPillars.size();
    }

    @Override
    public int getPillarTypeCount() {
        return PILLAR_TYPE_COUNT;
    }

    @Override
    public SimpleBarGraph.Pillar getPillar(int position) {
        return mPillarPillars.get(position);
    }

    public static class PillarConfigs implements SimpleBarGraph.PillarConfigs {

        private final int color;
        private final String name;

        public PillarConfigs(int color, String name) {
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
