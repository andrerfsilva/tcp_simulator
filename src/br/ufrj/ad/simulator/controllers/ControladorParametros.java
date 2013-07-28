package br.ufrj.ad.simulator.controllers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JFrame;

import br.ufrj.ad.simulator.models.ParametrosSimulador;
import br.ufrj.ad.simulator.views.JanelaParametros;

/**
 * Controla os eventos da janela de parâmetros.
 * 
 * @author André Ramos
 * 
 */
public class ControladorParametros implements ActionListener {

	private ParametrosSimulador parametrosSimulador;
	private JanelaParametros janelaParametros;

	public ControladorParametros() throws IOException {

		/*
		 * Inicializando os parâmetros salvos no arquivo.
		 */
		parametrosSimulador = new ParametrosSimulador();
		janelaParametros = new JanelaParametros();

		janelaParametros.getTextFieldCs().setText(
				parametrosSimulador.getCsProperty());
		janelaParametros.getTextFieldCg().setText(
				parametrosSimulador.getCgProperty());
		janelaParametros.getTextFieldEstacoesGrupo1().setText(
				parametrosSimulador.getEstacoesGrupo1Property());
		janelaParametros.getTextFieldEstacoesGrupo2().setText(
				parametrosSimulador.getEstacoesGrupo2Property());
		janelaParametros.getTextFieldMediaPacotesPorRajada().setText(
				parametrosSimulador.getMediaPacotesPorRajadaProperty());
		janelaParametros.getTextFieldTamanhoBufferRoteador().setText(
				parametrosSimulador.getTamanhoBufferRoteadorProperty());
		janelaParametros.getTextFieldTempoMedioEntreRajadas().setText(
				parametrosSimulador.getTempoMedioEntreRajadasProperty());
		janelaParametros.getTextFieldTP1().setText(
				parametrosSimulador.getTP1Property());
		janelaParametros.getTextFieldTP2().setText(
				parametrosSimulador.getTP2Property());

		janelaParametros.getComboBoxTamanhoBufferRoteador().setSelectedItem(
				parametrosSimulador.getDisciplinaRoteadorProperty());

		janelaParametros.getButtonSalvar().addActionListener(this);
		janelaParametros.getButtonCancelar().addActionListener(this);

	}

	public static void main(String args[]) throws IOException {
		ControladorParametros controlador = new ControladorParametros();
		controlador.janelaParametros
				.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if ("Salvar".equals(e.getActionCommand())) {
			try {

				parametrosSimulador.setProperty("TamanhoBufferRoteador",
						janelaParametros.getTextFieldTamanhoBufferRoteador()
								.getText());
				parametrosSimulador.setProperty("DisciplinaRoteador",
						(String) janelaParametros
								.getComboBoxTamanhoBufferRoteador()
								.getSelectedItem());
				parametrosSimulador.setProperty("Cs", janelaParametros
						.getTextFieldCs().getText());
				parametrosSimulador.setProperty("Cg", janelaParametros
						.getTextFieldCg().getText());
				parametrosSimulador.setProperty("TP1", janelaParametros
						.getTextFieldTP1().getText());
				parametrosSimulador.setProperty("TP2", janelaParametros
						.getTextFieldTP2().getText());
				parametrosSimulador
						.setProperty("EstacoesGrupo1", janelaParametros
								.getTextFieldEstacoesGrupo1().getText());
				parametrosSimulador
						.setProperty("EstacoesGrupo2", janelaParametros
								.getTextFieldEstacoesGrupo2().getText());
				parametrosSimulador.setProperty("MediaPacotesPorRajada",
						janelaParametros.getTextFieldMediaPacotesPorRajada()
								.getText());
				parametrosSimulador.setProperty("TempoMedioEntreRajadas",
						janelaParametros.getTextFieldTempoMedioEntreRajadas()
								.getText());

				parametrosSimulador.salvar();
				
				janelaParametros.close();

			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else if ("Cancelar".equals(e.getActionCommand())) {
			janelaParametros.close();
		}

	}
}
