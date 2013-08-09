package br.ufrj.ad.simulator.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.JFrame;

import br.ufrj.ad.simulator.models.Parametros;
import br.ufrj.ad.simulator.views.JanelaParametros;

/**
 * Controla os eventos da janela de parâmetros.
 * 
 * @author André Ramos, Wellington Mascena, Felipe Teixeira
 * 
 */
public class ControladorParametros implements ActionListener {

	private Parametros parametros;
	private JanelaParametros janelaParametros;

	public ControladorParametros() throws IOException {

		/*
		 * Inicializando os parâmetros salvos no arquivo.
		 */
		parametros = new Parametros();
		janelaParametros = new JanelaParametros();

		janelaParametros.getTextFieldCs().setText(parametros.getCsProperty());
		janelaParametros.getTextFieldCg().setText(parametros.getCgProperty());
		janelaParametros.getTextFieldEstacoesGrupo1().setText(
				parametros.getEstacoesGrupo1Property());
		janelaParametros.getTextFieldEstacoesGrupo2().setText(
				parametros.getEstacoesGrupo2Property());
		janelaParametros.getTextFieldMediaPacotesPorRajada().setText(
				parametros.getMediaPacotesPorRajadaProperty());
		janelaParametros.getTextFieldTamanhoBufferRoteador().setText(
				parametros.getTamanhoBufferRoteadorProperty());
		janelaParametros.getTextFieldTempoMedioEntreRajadas().setText(
				parametros.getTempoMedioEntreRajadasProperty());
		janelaParametros.getTextFieldTP1().setText(parametros.getTP1Property());
		janelaParametros.getTextFieldTP2().setText(parametros.getTP2Property());
		janelaParametros.getTextFieldTPACK1().setText(
				parametros.getTPACK1Property());
		janelaParametros.getTextFieldTPACK2().setText(
				parametros.getTPACK2Property());

		janelaParametros.getComboBoxTamanhoBufferRoteador().setSelectedItem(
				parametros.getDisciplinaRoteadorProperty());

		janelaParametros.getTextFieldNumeroEventosPorRodada().setText(
				parametros.getNumeroEventosPorRodadaProperty());

		janelaParametros.getCheckBoxTrafegoFundo().setSelected(
				parametros.getHabilitarTrafegoFundo());

		janelaParametros.getTextFieldNumeroAmostrasCwndGrafico().setText(
				parametros.getNumeroAmostrasCwndGraficoProperty());

		janelaParametros.getCheckBoxFixarSeedInicial().setSelected(
				parametros.getFixarSeedInicial());

		janelaParametros.getTextFieldSeedInicial().setText(
				parametros.getSeedInicialProperty());

		janelaParametros.getButtonSalvar().addActionListener(this);
		janelaParametros.getButtonCancelar().addActionListener(this);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("Salvar".equals(e.getActionCommand())) {
			try {

				parametros.setProperty("TamanhoBufferRoteador",
						janelaParametros.getTextFieldTamanhoBufferRoteador()
								.getText());
				parametros.setProperty("DisciplinaRoteador",
						(String) janelaParametros
								.getComboBoxTamanhoBufferRoteador()
								.getSelectedItem());
				parametros.setProperty("Cs", janelaParametros.getTextFieldCs()
						.getText());
				parametros.setProperty("Cg", janelaParametros.getTextFieldCg()
						.getText());
				parametros.setProperty("TP1", janelaParametros
						.getTextFieldTP1().getText());
				parametros.setProperty("TP2", janelaParametros
						.getTextFieldTP2().getText());
				parametros.setProperty("TPACK1", janelaParametros
						.getTextFieldTPACK1().getText());
				parametros.setProperty("TPACK2", janelaParametros
						.getTextFieldTPACK2().getText());
				parametros.setProperty("EstacoesGrupo1", janelaParametros
						.getTextFieldEstacoesGrupo1().getText());
				parametros.setProperty("EstacoesGrupo2", janelaParametros
						.getTextFieldEstacoesGrupo2().getText());
				parametros.setProperty("MediaPacotesPorRajada",
						janelaParametros.getTextFieldMediaPacotesPorRajada()
								.getText());
				parametros.setProperty("TempoMedioEntreRajadas",
						janelaParametros.getTextFieldTempoMedioEntreRajadas()
								.getText());
				parametros.setProperty("HabilitarTrafegoFundo",
						janelaParametros.getCheckBoxTrafegoFundo().isSelected()
								+ "");
				parametros.setProperty("NumeroEventosPorRodada",
						janelaParametros.getTextFieldNumeroEventosPorRodada()
								.getText());
				parametros.setProperty("NumeroAmostrasCwndGrafico",
						janelaParametros
								.getTextFieldNumeroAmostrasCwndGrafico()
								.getText());
				parametros.setProperty("FixarSeedInicial", janelaParametros
						.getCheckBoxFixarSeedInicial().isSelected() + "");
				parametros.setProperty("SeedInicial", janelaParametros
						.getTextFieldSeedInicial().getText());

				parametros.salvar();

				janelaParametros.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if ("Cancelar".equals(e.getActionCommand())) {
			janelaParametros.close();
		}
	}

	public static void main(String args[]) throws IOException {
		ControladorParametros controlador = new ControladorParametros();
		controlador.janelaParametros
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
}
