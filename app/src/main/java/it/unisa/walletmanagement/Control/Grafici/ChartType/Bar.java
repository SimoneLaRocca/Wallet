package it.unisa.walletmanagement.Control.Grafici.ChartType;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

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
import it.unisa.walletmanagement.Control.Grafici.MyBarDataSet;
import it.unisa.walletmanagement.Model.Dao.MovimentoDAO;
import it.unisa.walletmanagement.R;

public class Bar implements Chart {

    private BarChart chart;
    private String[] xAxisValues = {"Lun", "Mar", "Mer", "Gio", "Ven", "Sab", "Dom"};

    public Bar() {
    }

    @Override
    public void draw(AppCompatActivity activity, int id) {
        chart = activity.findViewById(id);

        // DAO query
        float[] list_entrate = new MovimentoDAO(activity.getApplicationContext()).doRetrieveForCurrentWeek(1);
        float[] list_uscite = new MovimentoDAO(activity.getApplicationContext()).doRetrieveForCurrentWeek(0);

        List<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i < 7; i++){
            if(i == 0){
                entries.add(new BarEntry(6f, list_entrate[i]-list_uscite[i]));
                continue;
            }
            entries.add(new BarEntry(i-1, list_entrate[i]-list_uscite[i]));
        }

        MyBarDataSet set = new MyBarDataSet(entries, "Movimenti");
        set.setColors(ContextCompat.getColor(activity.getApplicationContext(), R.color.green),
                ContextCompat.getColor(activity.getApplicationContext(), R.color.red),
                ContextCompat.getColor(activity.getApplicationContext(), R.color.gray));

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width

        chart.setData(data);

        // Set the value formatter
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.getDescription().setEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setHighlightPerTapEnabled(false);

        //chart.setNoDataText("Aggiungi nuovi movimenti...");

        chart.invalidate(); // refresh
    }

    public void draw(AppCompatActivity activity, int id, String[] labels, float[] values, String label) {
        chart = activity.findViewById(id);

        List<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i < values.length; i++){
            entries.add(new BarEntry(i, values[i]));
        }

        BarDataSet set = new BarDataSet(entries, label);

        BarData data = new BarData(set);
        data.setBarWidth(0.9f); // set custom bar width

        chart.setData(data);

        // Set the value formatter
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(labels));
        chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        chart.getAxisRight().setEnabled(false);
        chart.setFitBars(true); // make the x-axis fit exactly all bars
        chart.getDescription().setEnabled(false);
        chart.setHighlightPerDragEnabled(false);
        chart.setHighlightPerTapEnabled(false);

        chart.invalidate(); // refresh
    }

    @Override
    public void draw(AppCompatActivity activity, int id, String[] labels, float[] group1, float[] group2, String label1, String label2) {
        throw new UnsupportedOperationException("Metodo " + new Object(){}.getClass().getEnclosingMethod().getName() + " non implementato");
    }

    @Override
    public void draw(AppCompatActivity activity, int id, HashMap<String, Integer> map, String label) {
        throw new UnsupportedOperationException("Metodo " + new Object(){}.getClass().getEnclosingMethod().getName() + " non implementato");
    }
}
