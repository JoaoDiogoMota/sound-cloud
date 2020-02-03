/**
 * Implementação da classe de exceção para a existência de um cliente
 * 2019/2020
 */

public class ClienteJaExisteException extends Exception {
    public ClienteJaExisteException(){
        System.out.println("This client is already registered");
    }
}
