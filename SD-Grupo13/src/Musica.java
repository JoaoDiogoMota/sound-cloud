import java.io.PrintWriter;
import java.io.Serializable;
import java.util.List;

/**
 * Implementação da classe Música
 * Representa cada música do sistema, bem como o armazenamento da sua meta-informação variada (título, autor,
 * interprete, género).
 * 2019/2020
 */
public class Musica implements Serializable{
    /* título da música */
    private String titulo;
    /* interprete da música */
    private String interprete;
    /* ano de lançamento da música */
    private int ano;
    /* lista de tags da música */
    private List<String> tags;
    /* identificador único da música */
    private int identificador;
    /* número de downloads efetuados para a música */
    private int numeroDeDownloads;

    private static final long serialVersionUID = 1L;

    /**
     * Construtor parametrizado
     * @param titulo
     * @param interprete
     * @param ano
     * @param tags
     * @param identificador
     * @param numeroDeDownloads
     */
    public Musica(String titulo, String interprete, int ano, List<String> tags, int identificador, int numeroDeDownloads) {
        this.titulo = titulo;
        this.interprete = interprete;
        this.ano = ano;
        this.tags = tags;
        this.identificador = identificador;
        this.numeroDeDownloads = numeroDeDownloads;
    }

    /**
     * retorna o título da música
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * retorna o interprete da música
     */
    public String getInterprete() {
        return interprete;
    }

    /**
     * retorna o ano da música
     */
    public int getAno() {
        return ano;
    }

    /**
     * retorna as tags da música
     */
    public List<String> getTags() {
        return tags;
    }

    /**
     * retorna o identificador da música
     */
    public int getIdentificador() {
        return identificador;
    }

    /**
     * retorna o número de downloads da música
     */
    public int getNumeroDeDownloads() {
        return numeroDeDownloads;
    }

    /**
     * define o valor do identificador da música
     */
    public void setIdentificador(int identificador) {
        this.identificador = identificador;
    }

    /**
     * define o valor do número de downloads da música
     */
    public void setNumeroDeDownloads(int numeroDeDownloads) {
        this.numeroDeDownloads = numeroDeDownloads;
    }

    /**
     * imprime a meta-informação da música
     */
    public void printMusica(PrintWriter out) {
        out.println("*******************************");
        out.println("Título: " + titulo );
        out.println("Intérprete: " + interprete);
        out.println("Ano: " + ano);
        out.println("Tags: " + tags);
        out.println(" ");
        out.println("Identificador: " + identificador);
        out.println("Número de downloads: " + numeroDeDownloads);
        out.println("*******************************");
        out.println(" ");
    }

    public String toString() {
        return  "*******************************"+ '\'' +
                "Título: " + titulo + '\'' +
                "Intérprete: " + interprete + '\'' +
                "Ano: " + ano + '\'' +
                "Tags: " + tags + '\'' +
                " " + '\'' +
                "Identificador: " + identificador + '\'' +
                "Número de downloads: " + numeroDeDownloads + '\'' +
                "*******************************" + '\'' +
                " ";
    }
}
