import java.io.Serializable;

/**
 * Implementação da classe User
 * Representa cada utilizador do sistema, bem como o armazenamento dos seus dados (nome, password)
 * 2019/2020
 */

public class User implements Serializable{
    /* nome do utilizador */
    String nome;
    /* password do utilizador */
    String password;
    /* 1 - caso o utilizador esteja autenticado; 0 - caso contrário */
    int autenticado;
    private static final long serialVersionUID = 1L;

    /**
     * Construtor vazio
     */
    public User() {
        this.nome = "n/a";
        this.password = null;
        this.autenticado = 0;
    }

    /**
     * Construtor parametrizado
     * @param nome
     * @param password
     */
    public User(String nome, String password) {
        this.nome = nome;
        this.password = password;
        this.autenticado = 0;
    }

    /**
     * devolve o nome do utilizador
     * @return nome
     */
    public String getNome() {
        return nome;
    }

    /**
     * atribui um nome ao utilizador
     * @param nome
     */
    public void setNome(String nome) {
        this.nome = nome;
    }

    /**
     * retorna a password de um utilizador
     * @return password
     */
    public String getPassword() {
        return password;
    }

    /**
     * atribui uma password ao utilizador
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    public int getAutenticado(){
        return autenticado;
    }

    public void setAutenticado(int autenticado){
        this.autenticado = autenticado;
    }

}
