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

	/**
	 * Caminho do arquivo onde são armazenados os parâmetros do simulador.
	 */
	public final static String pathParametrosSimulador = "parametros.txt";

	/**
	 * Maximum Segment Size = 1500 bytes.
	 */
	public final static int mss = 1500;

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

	/**
	 * Salva todas as alterações de parâmetros no arquivo.
	 * 
	 * @throws IOException
	 */
	public void salvar() throws IOException {
		FileOutputStream out = new FileOutputStream(
				Parametros.pathParametrosSimulador);

		this.store(out, "---No Comment---");
		out.close();
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

	private String getTPACK1Property() {
		return getProperty("TPACK1", "100");
	}

	private String getTPACK2Property() {
		return getProperty("TPACK2", "50");
	}

	/**
	 * Calcula a taxa do tráfego de fundo em Mbps. Por exemplo: Se o tamanho
	 * médio das rajadas é 10 pacotes e o intervalo médio entre chegadas for
	 * 24ms, então a taxa média deste tráfego de fundo é (1500 x 8 x 10)/24ms =
	 * 5 Mbps. Lembrando que todos os pacotes da rajada tem tamanho MSS = 1500
	 * bytes.
	 * 
	 * @return taxa do tráfego de fundo em Mbps
	 */
	public double getTaxaTrafegoDeFundoEmMbps() {
		return (mss * 8 * getMediaPacotesPorRajada() * 1E-3)
				/ getTempoMedioEntreRajadas();
	}

	/**
	 * Número médio de pacotes que chegam em uma rajada de tráfego de fundo. A
	 * rajada ten tamanho geométrico, ou seja, o número de pacotes é uma
	 * variável aleatória geométrica.
	 * 
	 * @return média do número de pacotes numa rajada de tráfego de fundo
	 */
	public long getMediaPacotesPorRajada() {
		return Long.parseLong(getMediaPacotesPorRajadaProperty());
	}

	/**
	 * Tempo médio entre as rajadas do tráfego de fundo (em milisegundos).
	 * Lembrando que o tempo entre as chegadas é uma variável aleatória
	 * exponencial com média = 1/(taxa de chegada), ou seja, a taxa = 1/média.
	 * 
	 * @return tempo médio entre rajadas de tráfego de fundo (em milisegundos)
	 */
	public double getTempoMedioEntreRajadas() {
		return Double.parseDouble(getTempoMedioEntreRajadasProperty());
	}

	/**
	 * Calcula o tempo de transmissão de um pacote no enlace de saída do
	 * roteador (em milisegundos). O tempo é calculado em função da taxa do
	 * enlace e do tamanho do pacote.
	 * 
	 * @param tamanhoPacote
	 *            tamanho do pacote (em bytes)
	 * @return tempo de transmissão do pacote no enlace de saída do roteador (em
	 *         milisegundos)
	 */
	public double tempoTransmissaoPacoteNoRoteador(long tamanhoPacote) {
		return (tamanhoPacote * 8 / getTaxaSaidaEnlaceDoRoteador()) * 1E3;
	}

	/**
	 * Taxa de saída no enlace do rotedor (em bps). Parâmetro Cg na definição do
	 * trabalho.
	 * 
	 * @return taxa de saída no enlace do rotedor (em bps)
	 */
	public double getTaxaSaidaEnlaceDoRoteador() {
		return Double.parseDouble(getCgProperty());
	}

	/**
	 * Maximum Segment Size. Nesse trabalho o valor de MSS = 1500 bytes.
	 * 
	 * @return MSS = 1500 bytes
	 */
	public int getMSS() {
		return mss;
	}

	/**
	 * Tempo de propagação de retorno do ACK para o grupo 1 (em milisegundos).
	 * 
	 * @return tempo de propagação de retorno do ACK para o grupo 1 (em
	 *         milisegundos)
	 */
	public double getTempoPropagacaoRetornoACKGrupo1() {
		return Double.parseDouble(getTPACK1Property());
	}

	/**
	 * Tempo de propagação de retorno do ACK para o grupo 2 (em milisegundos).
	 * 
	 * @return tempo de propagação de retorno do ACK para o grupo 2 (em
	 *         milisegundos)
	 */
	public double getTempoPropagacaoRetornoACKGrupo2() {
		return Double.parseDouble(getTPACK2Property());
	}
}
