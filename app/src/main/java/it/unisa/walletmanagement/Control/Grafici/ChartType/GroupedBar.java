package it.unisa.walletmanagement.Control.Grafici.ChartType;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.unisa.walletmanagement.Control.Grafici.Chart;
import it.unisa.walletmanagement.Control.Grafici.IndexAxisValueFormatter;
import it.unisa.walletmanagement.Model.Dao.MovimentoDAO;
import it.unisa.walletmanagement.R;

public class GroupedBar implements Chart {

    private BarChart chart;
    private String[] xAxisValues = {"S1", "S2", "S3", "S4", "S5"};

    public GroupedBar() {
    }

    @Override
    public void draw(AppCompatActivity activity, int id) {
        chart = activity.findViewById(id);

        // DAO query
        float[] group1 = new MovimentoDAO(activity.getApplicationContext()).doRetrieveForCurrentMonth(1);
        float[] group2 = new MovimentoDAO(activity.getApplicationContext()).doRetrieveForCurrentMonth(0);

        List<BarEntry> entriesGroup1 = new ArrayList<>();
        List<BarEntry> entriesGroup2 = new ArrayList<>();
        // fill the lists
        for(int i = 0; i < group1.length; i++) {
            entriesGroup1.add(new BarEntry(i, group1[i]));
            entriesGroup2.add(new BarEntry(i, group2[i]));
        }

        BarDataSet set1 = new BarDataSet(entriesGroup1, "Entrate");
        BarDataSet set2 = new BarDataSet(entriesGroup2, "Uscite");
        set1.setColors(new int[]{R.color.green}, activity.getApplicationContext());
        set2.setColors(new int[]{R.color.red}, activity.getApplicationContext());

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
        BarData data = new BarData(set1, set2);
        data.setBarWidth(barWidth); // set the width of each bar
        chart.setData(data);

        // Set the value formatter
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        chart.groupBars(-0.5f, groupSpace, barSpace); // perform the "explicit" grouping
        chart.getDescription().setEnabled(false);

        chart.invalidate(); // refresh
    }

    @Override
    public void draw(AppCompatActivity activity, int id, String[] labels, float[] group1, float[] group2,  String label1, String label2) {
        chart = activity.findViewById(id);

        List<BarEntry> entriesGroup1 = new ArrayList<>();
        List<BarEntry> entriesGroup2 = new ArrayList<>();
        // fill the lists
        for(int i = 0; i < group1.length; i++) {
            entriesGroup1.add(new BarEntry(i, group1[i]));
            entriesGroup2.add(new BarEntry(i, group2[i]));
        }

        BarDataSet set1 = new BarDataSet(entriesGroup1, label1);
        BarDataSet set2 = new BarDataSet(entriesGroup2, label2);
        set1.setColors(new int[]{R.color.green}, activity.getApplicationContext());
        set2.setColors(new int[]{R.color.red}, activity.getApplicationContext());

        float groupSpace = 0.06f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.45f; // x2 dataset
        // (0.02 + 0.45) * 2 + 0.06 = 1.00 -> interval per "group"
        BarData data = new BarData(set1, set2);
        data.setBarWidth(barWidth); // set the width of each bar
        chart.setData(data);

        // Set the value formatter
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        chart.groupBars(-0.5f, groupSpace, barSpace); // perform the "explicit" grouping
        chart.getDescription().setEnabled(false);

        chart.invalidate(); // refresh
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
