package it.unisa.walletmanagement.Control.Grafici.ChartType;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.unisa.walletmanagement.Control.Grafici.Chart;
import it.unisa.walletmanagement.Model.Dao.ContoDAO;
import it.unisa.walletmanagement.R;

public class Pie implements Chart {

    private PieChart chart;

    public Pie() {
    }

    @Override
    public void draw(AppCompatActivity activity, int id) {
        chart = activity.findViewById(id);

        // DAO query
        HashMap<String, Integer> map = new ContoDAO(activity.getApplicationContext()).doCountMovimentiPerConto();

        if(map.size() == 0) {
            chart.setNoDataText("No data...");
            chart.invalidate();
            return;
        }

        List<PieEntry> entries = new ArrayList<>();
        for(String label: map.keySet()){
            entries.add(new PieEntry(map.get(label), label));
        }

        PieDataSet set = new PieDataSet(entries, "Num. movimenti");
        set.setColors(new int[]{R.color.coral, R.color.yellow,
                R.color.violet, R.color.kelly_green,
                R.color.blue_grotto, R.color.tiger_lily},
                activity.getApplicationContext());
        PieData data = new PieData(set);
        chart.setData(data);
        chart.setEntryLabelColor(R.color.black);
        chart.getDescription().setEnabled(false);
        chart.invalidate(); // refresh
    }

    public void draw(AppCompatActivity activity, int id, HashMap<String, Integer> map, String label){
        chart = activity.findViewById(id);

        if(map.size() == 0) {
            chart.setNoDataText("No data...");
            chart.invalidate();
            return;
        }

        List<PieEntry> entries = new ArrayList<>();
        for(String item: map.keySet()){
            entries.add(new PieEntry(map.get(item), item));
        }

        PieDataSet set = new PieDataSet(entries, label);
        set.setColors(new int[]{R.color.coral, R.color.yellow,
                        R.color.violet, R.color.kelly_green,
                        R.color.blue_grotto, R.color.tiger_lily},
                activity.getApplicationContext());
        PieData data = new PieData(set);
        chart.setData(data);
        chart.setEntryLabelColor(R.color.black);
        chart.getDescription().setEnabled(false);
        chart.invalidate(); // refresh
    }

    @Override
    public void draw(AppCompatActivity activity, int id, String[] labels, float[] values, String label) {
        throw new UnsupportedOperationException("Metodo " + new Object(){}.getClass().getEnclosingMethod().getName() + " non implementato");
    }

    @Override
    public void draw(AppCompatActivity activity, int id, String[] labels, float[] group1, float[] group2, String label1, String label2) {
        throw new UnsupportedOperationException("Metodo " + new Object(){}.getClass().getEnclosingMethod().getName() + " non implementato");
    }
}




















