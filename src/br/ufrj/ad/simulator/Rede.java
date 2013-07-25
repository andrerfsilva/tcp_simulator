package br.ufrj.ad.simulator;

/**
 * Essa classe representa todas as variáveis de estado da rede simulada, bem
 * como todas as ações tomadas quando algum evento ocorre.
 * 
 * @author André Ramos
 * 
 */
public class Rede {

	// variáveis gerais de estado
	private Random gerador;
	private double tempo; // marca o tempo simulado atual do sistema

	// parâmetros de entrada
	private double taxaTravegoFundo;

	// arquitetura da rede simulada
	private TxTCP[] transmissores;
	private RxTCP[] receptores;
	private Roteador roteador; // representa o gargalo da rede
	
	public Roteador getRoteador() {
		return roteador;
	}
	public void setRoteador(Roteador roteador) {
		this.roteador = roteador;
	}
	public TxTCP[] getTransmissores() {
		return transmissores;
	}
	public void setTransmissores(TxTCP[] transmissores) {
		this.transmissores = transmissores;
	}
	public RxTCP[] getReceptores() {
		return receptores;
	}
	public void setReceptores(RxTCP[] receptores) {
		this.receptores = receptores;
	}
}
