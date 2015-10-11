/**
 *
 */
package gui;

import gui.chart.FrameCuriNNChart;
import gui.chart.FrameTimelineChart;
import gui.chart.FrameWundtChart;
import gui.common.CommonDesktopPane;
import gui.options.FrameOptions;


/**
 * @author dkapusta
 */
public class CuriWindow extends CommonDesktopPane {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private FrameField frameField;
    private FrameWundtChart frameWundtChart;
    private FrameTimelineChart frameTimelineChart;
    private FrameOptions frameOptions;
    private FrameCuriNNChart frameCuriNNChart;

    /**
     *
     */
    public CuriWindow() {
        frameField = new FrameField();
        frameWundtChart = new FrameWundtChart();
        frameTimelineChart = new FrameTimelineChart();
        frameOptions = new FrameOptions();
        frameCuriNNChart = new FrameCuriNNChart();
        openFrame(frameField);
        openFrame(frameTimelineChart);
        openFrame(frameWundtChart);
        openFrame(frameCuriNNChart);
        this.add(frameOptions, PREFS_LAYER);
        frameOptions.show();
        frameOptions.placeOnTheRightOf(frameField);
        frameWundtChart.placeAtTheBottomOf(frameOptions);
        frameTimelineChart.placeAtTheBottomOf(frameField);
        frameCuriNNChart.placeOnTheRightOf(frameOptions);
    }
}