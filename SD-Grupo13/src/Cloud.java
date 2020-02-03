import java.io.*;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Implementação da classe Cloud
 * Classe do sistema onde são armazenados os utilizadores e as músicas do sistema
 */
public class Cloud implements InterfaceCloud, Serializable{
    /* hash onde são guardados os utilizadores em que a chave é o nome deles */
    private HashMap<String,User> utilizadores;
    /* hash onde são guardadas as músicas em que a chave é o identificador delas */
    private HashMap<Integer,Musica> conteudo; //Tem de ter um id unico , titulo usado de forma temporaria
    ReentrantLock lock;
    private static final long serialVersionUID = 1L;

    /**
     * Construtor vazio
     */
    public Cloud(){

        File s = new File("ficheiros");
        if (!s.exists()) {
            s.mkdir();
        }

        File m = new File("Musicas");
        if(!m.exists()){
            m.mkdir();
        }

        this.utilizadores = new HashMap<>();
        File u = new File("ficheiros/utilizadores.ser");
        if(u.exists()) loadUtilizadores();

        this.conteudo = new HashMap<>();
        File c = new File("ficheiros/conteudos.ser");
        if(c.exists()) loadConteudos();

        this.lock = new ReentrantLock();
    }

    /**
     * retorna os conteúdos
     * @return conteudo
     */
    public HashMap<Integer,Musica> getConteudo(){
        return conteudo;
    }

    public HashMap<String,User> getUtilizadores(){ return utilizadores; }

    /**
     * cria um utilizador através do seu nome e password
     * @param nome
     * @param password
     * @throws ClienteJaExisteException
     */
    public boolean criaUtilizador(String nome, String password){
        lock.lock();
        if(!utilizadores.containsKey(nome)) {
            User novo = new User(nome, password);
            utilizadores.put(nome, novo);
            File diretoriaMae = new File(System.getProperty("user.dir"));
            File diretoria = new File(diretoriaMae, nome);
            diretoria.mkdir();
            writeUtilizadores();
            lock.unlock();
            return true;
        }
        lock.unlock();
        return false;
    }

    /**
     * autentica um utilizador através do seu nome e password
     * @param nome
     * @param password
     * @return
     */
    public boolean autenticaUtilizador(String nome, String password){
        lock.lock();
        if(utilizadores.containsKey(nome)){
            if(utilizadores.get(nome).getPassword().equals(password)){
                User u = getUser(nome);
                u.setAutenticado(1);
                writeUtilizadores();
                lock.unlock();
                return true;
            }
        }
        lock.unlock();
        return false;
    }

    public int publicaMusica( String titulo, String interprete, int ano, List<String> tags) throws IllegalArgumentException {
        lock.lock();
        int id = calculaID()+1;
        Musica m = new Musica(titulo+".mp3",interprete,ano,tags,id,0);
        conteudo.put(id,m);
        writeConteudos();
        lock.unlock();

        return id;
    }


    /**
     * procura todas as músicas com determinado tag
     * @param tag
     * @return lista
     */
    public List<Integer> procuraMusicaTag(String tag, PrintWriter out){
        List<Integer> lista = new ArrayList<>();
        lock.lock();
        for(int key : conteudo.keySet()){
            Musica m = conteudo.get(key);
            if(m.getTags().contains(tag)){
                lista.add(key);
                m.printMusica(out);
            }
        }
        lock.unlock();
        return lista;
    }

    /**
     * procura todas as músicas do interprete especificado
     * @param interprete
     * @return lista
     */
    public List<Integer> procuraMusicaInterprete(String interprete, PrintWriter out){
        List<Integer> lista = new ArrayList<>();
        lock.lock();
        for(int key : conteudo.keySet()){
            Musica m = conteudo.get(key);
            if(m.getInterprete().equals(interprete)){
                lista.add(key);
                m.printMusica(out);
            }
        }
        lock.unlock();
        return lista;
    }

    /**
     * retorna todas as músicas lançadas no ano especificado
     * @param ano
     * @return lista
     */
    public List<Integer> procuraMusicaAno(int ano, PrintWriter out){
        List<Integer> lista = new ArrayList<>();
        lock.lock();
        for(int key : conteudo.keySet()){
            Musica m = conteudo.get(key);
            if(m.getAno() == ano){
                lista.add(key);
                m.printMusica(out);
            }
        }
        lock.unlock();
        return lista;
    }

    /**
     * retorna todas as músicas lançadas com o título especificado
     * @param titulo
     * @return lista
     */
    public List<Integer> procuraMusicaTitulo(String titulo, PrintWriter out){
        List<Integer> lista = new ArrayList<>();
        lock.lock();
        for(int key : conteudo.keySet()){
            Musica m = conteudo.get(key);
            if(m.getTitulo().equals(titulo+".mp3")){
                lista.add(key);
                m.printMusica(out);
            }
        }
        lock.unlock();
        return lista;
    }

    public List<Integer> procuraMusicas(PrintWriter out){
        List<Integer> lista = new ArrayList<>();
        lock.lock();

        for(int key : conteudo.keySet()){
            Musica m = conteudo.get(key);
            lista.add(key);
            m.printMusica(out);
        }

        lock.unlock();
        return lista;
    }

    /**
     * guarda, através da serialização, a informação dos utilizadores num ficheiro
     */
    public void writeUtilizadores(){
        lock.lock();
        try {
            FileOutputStream fileOut = new FileOutputStream("ficheiros/utilizadores.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.utilizadores);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
        finally {
            lock.unlock();;
        }
    }

    /**
     * guarda, através da serialização, a informação dos conteudos num ficheiro
     */
    public void writeConteudos(){
        lock.lock();
        try {
            FileOutputStream fileOut = new FileOutputStream("ficheiros/conteudos.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(this.conteudo);
            out.close();
            fileOut.close();
        } catch (IOException i) {
            i.printStackTrace();
        }
        finally {
            lock.unlock();
        }
    }

    /**
     * carrega, através da serialização, a informação dos utilizadores de um ficheiro
     */
    public void loadUtilizadores(){
        try {
            FileInputStream fileIn = new FileInputStream("ficheiros/utilizadores.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.utilizadores = (HashMap) in.readObject();
            in.close();
            fileIn.close();

        }catch (IOException i) {
            i.printStackTrace();
            return;
        }catch (ClassNotFoundException c) {
            System.out.println("Class not found exception");
            c.printStackTrace();
            return;
        }
    }

    /**
     * carrega, através da serialização, a informação dos utilizadores de um ficheiro
     */
    public void loadConteudos(){
        try {
            FileInputStream fileIn = new FileInputStream("ficheiros/conteudos.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            this.conteudo = (HashMap) in.readObject();
            in.close();
            fileIn.close();

        }catch (IOException i) {
            i.printStackTrace();
            return;
        }catch (ClassNotFoundException c) {
            System.out.println("Class not found exception");
            c.printStackTrace();
            return;
        }
    }

    /**
     * calcula o ID da música a inserir
     * @return
     */
    public int calculaID(){
        lock.lock();
        int id = -1;
        id = conteudo.size();
        lock.unlock();

        return id;
    }

    /**
     * obtém a música do id dado
     * @param id
     * @return
     */
    public Musica getMusica(int id){
        return conteudo.get(id);
    }

    public boolean existeMusicaTitulo(String titulo){
        for(int key : conteudo.keySet()) {
            Musica m = conteudo.get(key);
            if (m.getTitulo().equals(titulo + ".mp3"))
                return true;
        }
        return false;
    }

    public User getUser(String nome){
        return utilizadores.get(nome);
    }

    public void alteraEstadoOFF(String nome){
        lock.lock();
            if(utilizadores.containsKey(nome)) {
                User u = getUser(nome);
                u.setAutenticado(0);
                writeUtilizadores();
            }
            lock.unlock();
    }

    /**
     * efetua o download de uma música para a pasta do utilizador, dando o identificador da música
     * @param identificador
     * @return
     */
    public void adicionaDownload(int identificador){
        lock.lock(); //acrescentei
        int numero = conteudo.get(identificador).getNumeroDeDownloads();
        numero++;
        conteudo.get(identificador).setNumeroDeDownloads(numero);
        writeConteudos();
        lock.unlock(); //acrescentei
    }
}

