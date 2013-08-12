package br.ufrj.ad.simulator.views;

import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;

/**
 * Exibe o gráfico da cwnd/MSS ao longo do tempo simulado.
 * 
 * @author André Ramos, Felipe Teixeira
 * 
 */
public class JanelaGraficoCongestionWindow extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	JFreeChart grafico;
	DefaultXYDataset ds;

	public JanelaGraficoCongestionWindow(ArrayList<double[][]> tempoXpacotes,
			long seed) {

		super("Gráfico: cwnd/MSS x Tempo (seed=" + seed + ")");

		this.setPreferredSize(new Dimension(600, 400));
		this.setExtendedState(MAXIMIZED_BOTH);

		this.ds = new DefaultXYDataset();

		for (int i = 0; i < tempoXpacotes.size(); i++) {
			this.ds.addSeries("Cwnd/MSS Tx" + i, tempoXpacotes.get(i));
		}

		this.grafico = ChartFactory.createXYLineChart("cwnd/MSS x Tempo",
				"Tempo (ms)", "cwnd/MSS (pacotes)", ds,
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
				double[][] tempoXbits1 = new double[2][10];

				tempoXbits1[0][0] = 0;
				tempoXbits1[1][0] = 1500;

				tempoXbits1[0][1] = 100;
				tempoXbits1[1][1] = 3000;

				tempoXbits1[0][2] = 200;
				tempoXbits1[1][2] = 2450;

				tempoXbits1[0][3] = 300;
				tempoXbits1[1][3] = 3500;

				tempoXbits1[0][4] = 400;
				tempoXbits1[1][4] = 2200;

				tempoXbits1[0][5] = 500;
				tempoXbits1[1][5] = 10000;

				tempoXbits1[0][6] = 600;
				tempoXbits1[1][6] = 7000;

				tempoXbits1[0][7] = 700;
				tempoXbits1[1][7] = 500;

				tempoXbits1[0][8] = 800;
				tempoXbits1[1][8] = 12000;

				tempoXbits1[0][9] = 900;
				tempoXbits1[1][9] = 400;

				ArrayList<double[][]> amostras = new ArrayList<double[][]>();
				
				double[][] tempoXbits2 = new double[2][10];

				tempoXbits2[0][0] = 0;
				tempoXbits2[1][0] = 1200;

				tempoXbits2[0][1] = 100;
				tempoXbits2[1][1] = 2500;

				tempoXbits2[0][2] = 200;
				tempoXbits2[1][2] = 2500;

				tempoXbits2[0][3] = 300;
				tempoXbits2[1][3] = 4000;

				tempoXbits2[0][4] = 400;
				tempoXbits2[1][4] = 2000;

				tempoXbits2[0][5] = 500;
				tempoXbits2[1][5] = 9000;

				tempoXbits2[0][6] = 600;
				tempoXbits2[1][6] = 8000;

				tempoXbits2[0][7] = 700;
				tempoXbits2[1][7] = 7500;

				tempoXbits2[0][8] = 800;
				tempoXbits2[1][8] = 800;

				tempoXbits2[0][9] = 900;
				tempoXbits2[1][9] = 200;

				amostras.add(tempoXbits1);
				amostras.add(tempoXbits2);

				JFrame frame = new JanelaGraficoCongestionWindow(amostras,
						93428423L);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			}
		});
	}

}