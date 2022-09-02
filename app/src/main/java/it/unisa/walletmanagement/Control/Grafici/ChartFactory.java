package it.unisa.walletmanagement.Control.Grafici;

import it.unisa.walletmanagement.Control.Grafici.ChartType.Bar;
import it.unisa.walletmanagement.Control.Grafici.ChartType.GroupedBar;
import it.unisa.walletmanagement.Control.Grafici.ChartType.Line;
import it.unisa.walletmanagement.Control.Grafici.ChartType.Pie;

public class ChartFactory {

    public ChartFactory() {
    }

    public Chart createChart(String chartType) {

        if (chartType == null) {
            return null;
        }
        if (chartType.equalsIgnoreCase("BAR")) {
            return new Bar();

        } else if (chartType.equalsIgnoreCase("GROUPED BAR")) {
            return new GroupedBar();

        } else if (chartType.equalsIgnoreCase("PIE")) {
            return new Pie();

        } else if (chartType.equalsIgnoreCase("LINE")) {
            return new Line();
        }

        return null;
    }
}
