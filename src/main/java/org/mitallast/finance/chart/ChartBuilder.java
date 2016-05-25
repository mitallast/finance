package org.mitallast.finance.chart;

import org.jfree.chart.*;
import org.jfree.chart.block.BlockBorder;
import org.jfree.chart.plot.DefaultDrawingSupplier;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.jfree.ui.Layer;
import org.jfree.ui.RectangleInsets;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChartBuilder {
    private final static Color[] colors = new Color[]{
            new Color(0xFF, 102, 102),
            new Color(80, 112, 0xFF),
            new Color(34, 180, 0),
            new Color(224, 141, 0),
            new Color(244, 70, 0xFF),
            new Color(91, 184, 0xFF),
            new Color(145, 85, 73),
            new Color(76, 145, 142),
    };

    private final static BasicStroke markerStroke = new BasicStroke(2.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND,
            1.0f, new float[]{6.0f, 6.0f}, 0.0f);

    private final static StandardChartTheme theme = (StandardChartTheme) org.jfree.chart.StandardChartTheme.createJFreeTheme();

    static {
        theme.setChartBackgroundPaint(ChartColor.white);
        theme.setPlotBackgroundPaint(ChartColor.white);
        theme.setLegendBackgroundPaint(ChartColor.white);

        theme.setPlotOutlinePaint(ChartColor.white);
        theme.setLabelLinkPaint(ChartColor.white);

        theme.setRangeGridlinePaint(ChartColor.gray);
        theme.setDomainGridlinePaint(ChartColor.gray);

        theme.setAxisOffset(new RectangleInsets(0, 0, 0, 0));

        theme.setExtraLargeFont(new Font("Helvetica", Font.PLAIN, 24));
        theme.setLargeFont(new Font("Helvetica", Font.PLAIN, 18));
        theme.setRegularFont(new Font("Helvetica", Font.PLAIN, 14));
        theme.setDrawingSupplier(new DefaultDrawingSupplier(
                colors,
                DefaultDrawingSupplier.DEFAULT_FILL_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_PAINT_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_OUTLINE_STROKE_SEQUENCE,
                DefaultDrawingSupplier.DEFAULT_SHAPE_SEQUENCE
        ));
    }

    private final String title;
    private final TimeSeriesCollection collection = new TimeSeriesCollection();
    private final List<ValueMarker> markers = new ArrayList<>();

    private int width = 800;
    private int height = 600;

    public ChartBuilder(String title) {
        this.title = title;
    }

    public ChartBuilder(String title, int width, int height) {
        this.title = title;
        this.width = width;
        this.height = height;
    }

    public void addSeries(TimeSeries timeSeries) {
        collection.addSeries(timeSeries);
    }

    public void addMarker(double value) {
        Color color = colors[markers.size() % colors.length];
        markers.add(new ValueMarker(value, color, markerStroke, color, markerStroke, 0.5f));
    }

    public void addStdMarker(double value) {
        markers.add(new ValueMarker(value * 1, colors[3], markerStroke, colors[3], markerStroke, 0.5f));
        markers.add(new ValueMarker(value * 2, colors[1], markerStroke, colors[1], markerStroke, 0.5f));
        markers.add(new ValueMarker(value * 3, colors[2], markerStroke, colors[2], markerStroke, 0.5f));
        markers.add(new ValueMarker(value * -1, colors[3], markerStroke, colors[3], markerStroke, 0.5f));
        markers.add(new ValueMarker(value * -2, colors[1], markerStroke, colors[1], markerStroke, 0.5f));
        markers.add(new ValueMarker(value * -3, colors[2], markerStroke, colors[2], markerStroke, 0.5f));
    }

    public void render(HttpServletResponse response) throws IOException {
        JFreeChart chart = ChartFactory.createTimeSeriesChart(
                title,
                "",
                "",
                collection,
                true,
                false,
                false
        );
        for (ValueMarker marker : markers) {
            chart.getXYPlot().addRangeMarker(marker, Layer.BACKGROUND);
        }
        theme.apply(chart);
        chart.getLegend().setFrame(BlockBorder.NONE);
        ChartUtilities.writeChartAsPNG(response.getOutputStream(), chart, width, height);
        response.getOutputStream().close();
    }

}
