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

}
