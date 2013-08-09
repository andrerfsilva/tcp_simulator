package br.ufrj.ad.simulator.models;

import java.util.ArrayList;

import br.ufrj.ad.simulator.events.Evento;

public class FilaEventos {

	private ArrayList<Evento> eventos;

	public FilaEventos() {
		this.eventos = new ArrayList<Evento>();
	}

	public Evento poll() {
		
		if(this.eventos.size() == 0){
			return null;
		}
		
		Evento e = this.eventos.get(0);

		this.eventos.remove(0);

		return e;
	}

	public Evento peek() {
		if(this.eventos.size() == 0){
			return null;
		}
		
		return this.eventos.get(0);

	}

	public void add(Evento e) {
		int index = this.buscaBinaria(e);

		this.eventos.add(index, e);
	}

	public void remove(Evento e) {
		if(this.eventos.size() == 0 || e == null){
			return;
		}
		
		int index = this.buscaBinaria(e);
		
		if(index >= this.eventos.size()){
			return;
		}

		for (int i = index; i >= 0 && this.eventos.get(i).getTempoDeOcorrencia() == e
				.getTempoDeOcorrencia(); i--) {
			if (this.eventos.get(i).equals(e)) {
				this.eventos.remove(i);
				return;
			}
		}

		for (int i = index + 1; i < this.eventos.size() && this.eventos.get(i).getTempoDeOcorrencia() == e
				.getTempoDeOcorrencia(); i++) {
			if (this.eventos.get(i).equals(e)) {
				this.eventos.remove(i);
				return;
			}
		}
	}

	public int size() {
		return this.eventos.size();
	}

	/**
	 * Função que faz uma busca binária atrás da posição no vetor com o mesmo
	 * tempo de Ocorrência do evento procurado
	 * 
	 * @param e
	 *            Evento a ser procurado
	 * 
	 * @return Evento com o mesmo tempo de ocorrência.
	 */
	private int buscaBinaria(Evento e) {
		if (this.eventos.size() == 0) {
			return 0;
		}

		int lower = 0;
		int upper = this.size() - 1;
		int middle = 0;

		while (lower <= upper) {
			middle = (lower + upper) / 2;

			if (e.getTempoDeOcorrencia() == this.eventos.get(middle)
					.getTempoDeOcorrencia()) {
				return middle;
			} else if (e.getTempoDeOcorrencia() > this.eventos.get(middle)
					.getTempoDeOcorrencia()) {
				lower = middle + 1;
			} else {
				upper = middle - 1;
			}

		}

		return lower;
	}

}
