package br.ufrj.ad.simulator.models;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * Representa os parâmetros de entrada para a simulação definidos na página 10
 * da especificação do trabalho.
 * 
 * @author André Ramos
 * 
 */
public class Parametros extends Properties {

	public final static String pathParametrosSimulador = "parametros.txt";

	public Parametros() throws IOException {

		/*
		 * Inicializando os parâmetros salvos no arquivo.
		 */
		File fileParametros = new File(pathParametrosSimulador);
		fileParametros.createNewFile();
		FileInputStream in = new FileInputStream(fileParametros);
		this.load(in);
		in.close();
	}

	public String getCsProperty() {
		return this.getProperty("Cs", "1E9");
	}

	public double getCs() {
		return Double.parseDouble(this.getProperty("Cs", "1E9"));
	}

	public String getCgProperty() {
		return this.getProperty("Cg", "10E6");
	}

	public String getEstacoesGrupo1Property() {
		return this.getProperty("EstacoesGrupo1", "10");
	}

	public String getEstacoesGrupo2Property() {
		return this.getProperty("EstacoesGrupo2", "10");
	}

	public String getMediaPacotesPorRajadaProperty() {
		return this.getProperty("MediaPacotesPorRajada", "10");
	}

	public String getTamanhoBufferRoteadorProperty() {
		return this.getProperty("TamanhoBufferRoteador", "40");
	}

	public String getTempoMedioEntreRajadasProperty() {
		return this.getProperty("TempoMedioEntreRajadas", "24");
	}

	public String getTP1Property() {
		return this.getProperty("TP1", "100");
	}

	public String getTP2Property() {
		return this.getProperty("TP2", "50");
	}

	public String getDisciplinaRoteadorProperty() {
		return this.getProperty("DisciplinaRoteador", "RED");
	}

	public void salvar() throws IOException {
		FileOutputStream out = new FileOutputStream(
				Parametros.pathParametrosSimulador);

		this.store(out, "---No Comment---");
		out.close();
	}
}
