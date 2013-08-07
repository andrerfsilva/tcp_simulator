package br.ufrj.ad.simulator.views;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.text.Document;
import javax.swing.text.html.HTMLEditorKit;

public class JanelaResultadoSimulacaoHTML extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JEditorPane htmlView;
	private HTMLEditorKit kit;

	private JScrollPane janelaDeslizante;

	private Document doc;

	public JanelaResultadoSimulacaoHTML() {
		this.setTitle("Resultado Simulação");

		this.htmlView = new JEditorPane();
		this.htmlView.setEditable(false);

		this.kit = new HTMLEditorKit();

		this.htmlView.setEditorKit(kit);

		this.janelaDeslizante = new JScrollPane(this.htmlView);

		this.doc = this.kit.createDefaultDocument();

		this.htmlView.setDocument(this.doc);

		this.setSize(800, 600);
		this.setVisible(true);

		this.add(this.htmlView);
	}

	public void insertStringHTML(String resultadoHTML) {
		this.htmlView.setText(resultadoHTML);
	}

	public static void main(String[] args) {
		// Schedule a job for the event-dispatching thread:
		// creating and showing this application's GUI.
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				String htmlString = "<html>\n"
						+ "<body>\n"
						+ "<h1>Welcome!</h1>\n"
						+ "<h2>This is an H2 header</h2>\n"
						+ "<p>This is some sample text</p>\n"
						+ "<p><a href=\"http://devdaily.com/blog/\">devdaily blog</a></p>\n"
						+ "</body>\n";
				
				JanelaResultadoSimulacaoHTML janelaHTML = new JanelaResultadoSimulacaoHTML();
				
				janelaHTML.insertStringHTML(htmlString);
				
				janelaHTML.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	

			}
		});
	}

}
