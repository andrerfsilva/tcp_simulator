package br.ufrj.ad.simulator.exceptions;

/**
 * Exceção lançada quando tentar fazer o TxTCP enviar um pacote fora da janela
 * de congestionamento, ou seja, se chamar o método de enviar pacotes quando
 * todos os pacotes da janela já foram transmitidos.
 * 
 * @author André Ramos
 * 
 */
public class TxTCPNotReadyToSendException extends RuntimeException {

}
