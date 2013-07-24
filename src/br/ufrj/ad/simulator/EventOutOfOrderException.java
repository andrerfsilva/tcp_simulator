package br.ufrj.ad.simulator;

/**
 * Essa exceção é usada para garantir se os eventos do simulador estão ocorrendo
 * todos em sua ordem no tempo. Será lançado quando a lista de eventos retornar
 * um evento cujo tempo de ocorrência seja menor que o tempo atual simulado, ou
 * seja, mostra que um evento deveria ter sido tratado antes, portanto há uma
 * inconsistência nos dados da simulação e a simulação deve ser abortada.
 * 
 * @author André Ramos
 * 
 */
public class EventOutOfOrderException extends RuntimeException {

}
