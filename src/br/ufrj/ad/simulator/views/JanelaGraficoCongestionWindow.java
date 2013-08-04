package br.ufrj.ad.simulator.views;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

public class JanelaGraficoCongestionWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFreeChart grafico;
	DefaultXYDataset ds;

	/*
	 * *
	 */
	public JanelaGraficoCongestionWindow(double[][] tempoXbit) {
		
		super("Gráfico cwnd/MSS x Tempo");

		this.setPreferredSize(new Dimension(800, 600));
		this.setExtendedState(MAXIMIZED_BOTH);

		this.ds = new DefaultXYDataset();

		this.ds.addSeries("Cwnd/MSS Tx0", tempoXbit);

		this.grafico = ChartFactory.createXYLineChart("cwnd/MSS x tempo",
				"tempo (ms)", "cwnd/MSS (pacotes)", ds,
				PlotOrientation.VERTICAL, true, true, false);

		ChartPanel cp = new ChartPanel(this.grafico);

		this.add(cp);

		this.pack();
		this.setVisible(true);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				double[][] tempoXbits = new double[2][10];

				tempoXbits[0][0] = 0;
				tempoXbits[1][0] = 1500;

				tempoXbits[0][1] = 100;
				tempoXbits[1][1] = 3000;

				tempoXbits[0][2] = 200;
				tempoXbits[1][2] = 2450;

				tempoXbits[0][3] = 300;
				tempoXbits[1][3] = 3500;

				tempoXbits[0][4] = 400;
				tempoXbits[1][4] = 2200;

				tempoXbits[0][5] = 500;
				tempoXbits[1][5] = 10000;

				tempoXbits[0][6] = 600;
				tempoXbits[1][6] = 7000;

				tempoXbits[0][7] = 700;
				tempoXbits[1][7] = 500;

				tempoXbits[0][8] = 800;
				tempoXbits[1][8] = 12000;

				tempoXbits[0][9] = 900;
				tempoXbits[1][9] = 400;

				JFrame frame = new JanelaGraficoCongestionWindow(tempoXbits);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}

}