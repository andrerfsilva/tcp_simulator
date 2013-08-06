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
	public Rede(Parametros parametros) {

		int numTxGrupo1 = parametros.getEstacoesGrupo1();
		int numTxGrupo2 = parametros.getEstacoesGrupo2();
		String disciplinaRoteador = parametros.getDisciplinaRoteadorProperty();

		transmissores = new TxTCP[numTxGrupo1 + numTxGrupo2];
		receptores = new RxTCP[numTxGrupo1 + numTxGrupo2];

		int i = 0;
		while (i < numTxGrupo1) {
			transmissores[i] = new TxTCP(i);
			transmissores[i].setGrupo(1);
			transmissores[i].setRTT(2 * (parametros.getTP1() + parametros
					.getTPACK1()));
			receptores[i] = new RxTCP();
			i++;
		}
		while (i < numTxGrupo1 + numTxGrupo2) {
			transmissores[i] = new TxTCP(i);
			transmissores[i].setGrupo(2);
			transmissores[i].setRTT(2 * (parametros.getTP2() + parametros
					.getTPACK2()));
			receptores[i] = new RxTCP();
			i++;
		}

		if (disciplinaRoteador.equals("FIFO")) {
			roteador = new RoteadorFIFO();
		} else {
			roteador = new RoteadorRED();
		}

		roteador.setTamanhoBuffer(parametros.getTamanhoBufferRoteador());
		roteador.setReceptores(receptores);
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
