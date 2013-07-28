package br.ufrj.ad.simulator.models;

/**
 * Essa classe representa a rede a ser simulada.
 * 
 * @author André Ramos
 * 
 */
public class Rede {

	// arquitetura da rede simulada
	private TxTCP[] transmissores;
	private RxTCP[] receptores;
	private Roteador roteador; // representa o gargalo da rede

	/**
	 * Inicializa todas as variáveis de estado da rede em função dos parâmetros.
	 * 
	 * @param numTxGrupo1
	 *            número de estações Tx do grupo 1
	 * @param numTxGrupo2
	 *            número de estações Tx do grupo 2
	 * @param disciplinaRoteador
	 *            disciplina de atendimento do roteador (RED ou FIFO)
	 */
	public Rede(int numTxGrupo1, int numTxGrupo2, String disciplinaRoteador) {
		transmissores = new TxTCP[numTxGrupo1 + numTxGrupo2];
		receptores = new RxTCP[numTxGrupo1 + numTxGrupo2];

		int i = 0;
		while (i < numTxGrupo1) {
			transmissores[i] = new TxTCP();
			transmissores[i].setGrupo(1);
			i++;
		}
		while (i < numTxGrupo1 + numTxGrupo2) {
			transmissores[i] = new TxTCP();
			transmissores[i].setGrupo(2);
			i++;
		}

		if (disciplinaRoteador.equals("FIFO")) {
			roteador = new RoteadorFIFO();
		} else {
			roteador = new RoteadorRED();
		}
	}

	public Roteador getRoteador() {
		return roteador;
	}

	public TxTCP[] getTransmissores() {
		return transmissores;
	}

	public RxTCP[] getReceptores() {
		return receptores;
	}
}
