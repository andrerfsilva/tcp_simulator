package br.ufrj.ad.simulator.models;

import java.util.ArrayList;

import br.ufrj.ad.simulator.statistics.Random;

/**
 * Roteador que implementa a disciplina WFQ de uma maneira genérica
 * 
 * @author Felipe Teixeira
 * 
 */
public class RoteadorWFQ extends Roteador {

	private int tamanhoFilasDeEntrada;
	private ArrayList<ArrayList<Pacote>> filasDeEntrada;

	public RoteadorWFQ() {
		super();

		this.tamanhoFilasDeEntrada = 8;
		this.filasDeEntrada = new ArrayList<ArrayList<Pacote>>();

	}

	/*
	 * Recebe o Pacote diretamente na fila de Saída. (non-Javadoc)
	 * 
	 * @see
	 * br.ufrj.ad.simulator.models.Roteador#receberPacote(br.ufrj.ad.simulator
	 * .models.Pacote, double)
	 */
	@Override
	public boolean receberPacote(Pacote p, double tempoAtualSimulado) {
		
		if (this.getNumeroPacotes() < getTamanhoBuffer()) {
			return this.receberPacoteFilaDeEntrada(p);
		}

		return false;

	}

	/**
	 * 
	 * Procura o pacote em sua respectiva fila, se ela estiver cheia, ela não é
	 * adicionada, se não estiver ela é adicionada, se não há nenhuma fila
	 * correspondente ao pacote adicionado, é criado uma nova fila para
	 * adicioná-lo.
	 * 
	 * @param p
	 * @return Se conseguiu adicionar o pacote
	 */
	private boolean receberPacoteFilaDeEntrada(Pacote p) {
		for (int i = 0; i < filasDeEntrada.size(); i++) {
			if (filasDeEntrada.get(i).get(0).getDestino() == p.getDestino()) {
				if (filasDeEntrada.get(i).size() < this.tamanhoFilasDeEntrada) {
					filasDeEntrada.get(i).add(p);

					return true;
				} else {
					return false;
				}
			}
		}

		ArrayList<Pacote> novaFilaDeEntrada = new ArrayList<Pacote>();
		novaFilaDeEntrada.add(p);

		filasDeEntrada.add(novaFilaDeEntrada);

		return true;
	}

	@Override
	public SACK enviarProximoPacote(double tempoAtualSimulado) {
		this.rodarClassificador();

		return super.enviarProximoPacote(tempoAtualSimulado);
	}

	/**
	 * Coloca os pacotes que estão na fila de entrada na fila de saída segundo o
	 * roteador WFQ
	 */
	private void rodarClassificador() {
		double somatorioDePrioridades = 0;

		for (int i = 0; i < this.filasDeEntrada.size(); i++) {
			/*
			 * Se for tráfego de fundo terá prioridade 1, se não for terá
			 * prioridade do número de destino + 1 (Para não existir prioridade
			 * 0)
			 */
			if (this.filasDeEntrada.get(i).get(0).getDestino() == -1) {
				somatorioDePrioridades += 1;
			} else {
				somatorioDePrioridades += this.filasDeEntrada.get(i).get(0)
						.getDestino() + 1;
			}
		}

		Random gerador = new Random();

		ArrayList<Pacote> amostras = new ArrayList<Pacote>();

		for (int i = 0; i < this.filasDeEntrada.size(); i++) {
			amostras.add(this.filasDeEntrada.get(i).get(0));
		}

		/**
		 * Irá colocar todos os pacotes da fila de entrada na fila de saída.
		 */
		while (this.filasDeEntrada.size() != 0) {

			for (int i = 0; i < this.filasDeEntrada.size(); i++) {
				double probabilidade;

				if (this.filasDeEntrada.get(i).get(0).getDestino() == -1) {
					probabilidade = 1 / somatorioDePrioridades;
				} else {
					probabilidade = (this.filasDeEntrada.get(i).get(0)
							.getDestino() + 1)
							/ somatorioDePrioridades;
				}

				if (gerador.nextDouble() < probabilidade) {
					this.buffer.add(this.filasDeEntrada.get(i).get(0));

					this.filasDeEntrada.get(i).remove(0);
				}
			}

			/**
			 * Atualiza as probabilidades conforme o seu peso no somatório de
			 * prioridades, se uma fila ficar vazia, não tem porque a prioridade
			 * dela ser levada em conta, e retira as filas de entrada que
			 * estiverem vazias.
			 */
			for (int i = this.filasDeEntrada.size() - 1; i >= 0; i--) {
				if (this.filasDeEntrada.get(i).size() == 0) {
					this.filasDeEntrada.remove(i);

					if (amostras.get(i).getDestino() == -1) {
						somatorioDePrioridades -= 1;
					} else {
						somatorioDePrioridades -= (amostras.get(i).getDestino() + 1);
					}

					amostras.remove(i);
				}
			}
		}
	}
	
	@Override
	public int getNumeroPacotes() {
		int somatorioDePacotes = 0;

		for (int i = 0; i < this.filasDeEntrada.size(); i++) {
			somatorioDePacotes += this.filasDeEntrada.get(i).size();
		}

		somatorioDePacotes += buffer.size();
		
		return somatorioDePacotes;
	}

	public ArrayList<ArrayList<Pacote>> getFilasDeEntrada() {
		return filasDeEntrada;
	}

	public int getTamanhoFilasDeEntrada() {
		return tamanhoFilasDeEntrada;
	}
}
