import java.io.PrintWriter;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implementação da interface da Cloud
 * 2019/2020
 */

public interface InterfaceCloud{

    HashMap<Integer,Musica> getConteudo();

    HashMap<String,User> getUtilizadores();

    boolean criaUtilizador(String nome, String password)
        throws ClienteJaExisteException;

    boolean autenticaUtilizador(String nome, String password);

    int publicaMusica(String titulo, String interprete, int ano, List<String> tags)
            throws UnsupportedEncodingException;

    List<Integer> procuraMusicaTag(String tag, PrintWriter out);

    List<Integer> procuraMusicaInterprete(String interprete, PrintWriter out);

    List<Integer> procuraMusicaAno(int ano, PrintWriter out);

    List<Integer> procuraMusicaTitulo(String titulo, PrintWriter out);

    List<Integer> procuraMusicas(PrintWriter out);

    void writeUtilizadores();

    void writeConteudos();

    void loadUtilizadores();

    void loadConteudos();

    int calculaID();

    Musica getMusica(int id);

    User getUser(String nome);

    void alteraEstadoOFF(String nome);

    boolean existeMusicaTitulo(String titulo);

    void adicionaDownload(int id);
}
