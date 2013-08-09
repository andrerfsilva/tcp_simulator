package br.ufrj.ad.simulator.views;

import java.awt.Dimension;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

public class JanelaGraficoVazaoEventos extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFreeChart grafico;
	DefaultXYDataset ds;

	/*
	 * *
	 */
	public JanelaGraficoVazaoEventos(double[][] tempoXbps, long seed) {

		super("Gráfico: Vazão Média x Número de Eventos (seed=" + seed + ")");

		this.setPreferredSize(new Dimension(800, 600));
		this.setExtendedState(MAXIMIZED_BOTH);

		this.ds = new DefaultXYDataset();

		this.ds.addSeries("Vazão Tx0", tempoXbps);

		this.grafico = ChartFactory.createXYLineChart(
				"Vazão Média x Número de Eventos", "Número de Eventos",
				"Vazão Média (bps)", ds, PlotOrientation.VERTICAL, true, true,
				false);

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
				double[][] tempoXbps = new double[2][10];

				tempoXbps[0][0] = 0;
				tempoXbps[1][0] = 1500;

				tempoXbps[0][1] = 100;
				tempoXbps[1][1] = 3000;

				tempoXbps[0][2] = 200;
				tempoXbps[1][2] = 2450;

				tempoXbps[0][3] = 300;
				tempoXbps[1][3] = 3500;

				tempoXbps[0][4] = 400;
				tempoXbps[1][4] = 2200;

				tempoXbps[0][5] = 500;
				tempoXbps[1][5] = 10000;

				tempoXbps[0][6] = 600;
				tempoXbps[1][6] = 7000;

				tempoXbps[0][7] = 700;
				tempoXbps[1][7] = 500;

				tempoXbps[0][8] = 800;
				tempoXbps[1][8] = 12000;

				tempoXbps[0][9] = 900;
				tempoXbps[1][9] = 400;

				JFrame frame = new JanelaGraficoVazaoEventos(tempoXbps,
						19238091L);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}

}