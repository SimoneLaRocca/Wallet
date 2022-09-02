package it.unisa.walletmanagement.Control.Grafici.ChartType;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.unisa.walletmanagement.Control.Grafici.Chart;
import it.unisa.walletmanagement.Control.Grafici.IndexAxisValueFormatter;
import it.unisa.walletmanagement.Model.Dao.MovimentoDAO;
import it.unisa.walletmanagement.R;

public class Line implements Chart {

    private LineChart chart;
    private String[] xAxisValues = {"S1", "S2", "S3", "S4", "S5"};

    public Line() {
    }

    @Override
    public void draw(AppCompatActivity activity, int id) {
        chart = activity.findViewById(id);

        // DAO query
        float[] group1 = new MovimentoDAO(activity.getApplicationContext()).doRetrieveForCurrentMonth(1);
        float[] group2 = new MovimentoDAO(activity.getApplicationContext()).doRetrieveForCurrentMonth(0);

        List<Entry> entriesGroup1 = new ArrayList<>();
        List<Entry> entriesGroup2 = new ArrayList<>();
        // fill the lists
        for(int i = 0; i < group1.length; i++) {
            entriesGroup1.add(new Entry(i, group1[i]));
            entriesGroup2.add(new Entry(i, group2[i]));
        }

        LineDataSet setComp1 = new LineDataSet(entriesGroup1, "Entrate");
        setComp1.setColors(new int[]{R.color.green}, activity.getApplicationContext());
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet setComp2 = new LineDataSet(entriesGroup2, "Uscite");
        setComp2.setColors(new int[]{R.color.red}, activity.getApplicationContext());
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineData data = new LineData(setComp1, setComp2);
        chart.setData(data);

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setGranularity(1f); // minimum axis-step (interval) is 1
        chart.setHighlightPerDragEnabled(false);
        chart.setHighlightPerTapEnabled(false);

        chart.invalidate();
    }

    @Override
    public void draw(AppCompatActivity activity, int id, String[] labels, float[] group1, float[] group2, String label1, String label2) {
        chart = activity.findViewById(id);

        List<Entry> entriesGroup1 = new ArrayList<>();
        List<Entry> entriesGroup2 = new ArrayList<>();
        // fill the lists
        for(int i = 0; i < group1.length; i++) {
            entriesGroup1.add(new Entry(i, group1[i]));
            entriesGroup2.add(new Entry(i, group2[i]));
        }

        LineDataSet setComp1 = new LineDataSet(entriesGroup1, label1);
        setComp1.setColors(new int[]{R.color.green}, activity.getApplicationContext());
        setComp1.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineDataSet setComp2 = new LineDataSet(entriesGroup2, label2);
        setComp2.setColors(new int[]{R.color.red}, activity.getApplicationContext());
        setComp2.setAxisDependency(YAxis.AxisDependency.LEFT);

        LineData data = new LineData(setComp1, setComp2);
        chart.setData(data);

        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        chart.getDescription().setEnabled(false);
        chart.getXAxis().setGranularity(1f); // minimum axis-step (interval) is 1
        chart.setHighlightPerDragEnabled(false);
        chart.setHighlightPerTapEnabled(false);

        chart.invalidate();
    }


    @Override
    public void draw(AppCompatActivity activity, int id, String[] labels, float[] values, String label) {
        throw new UnsupportedOperationException("Metodo " + new Object(){}.getClass().getEnclosingMethod().getName() + " non implementato");

    }

    @Override
    public void draw(AppCompatActivity activity, int id, HashMap<String, Integer> map, String label) {
        throw new UnsupportedOperationException("Metodo " + new Object(){}.getClass().getEnclosingMethod().getName() + " non implementato");
    }
}
