import java.io.*;
import java.net.Socket;
import java.nio.file.Files;
import java.util.*;

/**
 * Implementação da classe ServerWorker
 *
 */

public class ServerWorker implements Runnable, Serializable {
    private InterfaceCloud cloud;
    private Socket socket;
    private int invalid = 0;
    private static final long serialVersionUID = 1L;
    private static final int MAXSIZE = 256;

    /**
     * construtor parameterizado
     * @param socket
     * @param cloud
     */
    public ServerWorker(Socket socket, InterfaceCloud cloud){
        this.cloud = cloud;
        this.socket = socket;
    }

    /**
     * menu inicial de apresentação
     * @param out
     */
    public void menuInicial(PrintWriter out){
        out.println(" ");
        out.println("***********************************************************");
        out.println("                 Available Actions: ");
        out.println("       criaUtilizador | autenticaUtilizador | sair ");
        out.println("***********************************************************");
        out.println(" ");
        out.flush();
    }

    /**
     * menu de utilizador de apresentação
     * @param out
     */
    public void menuUtilizador(PrintWriter out){
        out.println(" ");
        out.println("*******************************************************************************************************");
        out.println("                          Available Actions: ");
        out.println(" publicaMusica");
        out.println(" downloadMusica");
        out.println(" procuraMusicaTag | procuraMusicaAno | procuraMusicaTitulo | procuraMusicaInterprete | procuraMusicas");
        out.println(" sair");
        out.println("*******************************************************************************************************");
        out.println(" ");
        out.flush();
    }

    /**
     * regista um utilizador no sistema
     * @param in
     * @param out
     */
    public void registaUtilizador(BufferedReader in, PrintWriter out){
        out.println("Enter your username and password");
        out.flush();

        String[] mensagem = null;
        String cmd = null;
        try {
            cmd = in.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (cmd != null)
            mensagem = cmd.split(" ");

        String username = mensagem[0];
        String password = mensagem[1];

        try {
            if (cloud.criaUtilizador(username, password)) {
                out.println("User successfully registered");
                out.flush();
            } else {
                out.println("An user with this name is already registered");
                out.flush();
            }
        } catch (ClienteJaExisteException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * efetua a autenticação de um utilizador no sistema
     * @param in
     * @param out
     * @return username
     */
    public String autenticaUtilizador(BufferedReader in, PrintWriter out) {
        out.println("Enter your username and password");
        out.flush();

        String[] mensagem = null;
        String cmd = null;
        try {
            cmd = in.readLine();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        if (cmd != null)
            mensagem = cmd.split(" ");

        String username = mensagem[0];
        String password = mensagem[1];

        if(!cloud.getUtilizadores().containsKey(username)){
            invalid = 1;
            out.println("User does not exist");
            out.flush();
            return null;
        }

        if(cloud.getUser(username).getAutenticado()==1){
            invalid = 1;
            out.println("This user is already in session");
            out.flush();
            return null;
        }

        if(cloud.autenticaUtilizador(username, password)) {
            invalid = 0;
            out.println("Authentication successful");
            out.flush();
            return username;

        }
        else{
                cloud.alteraEstadoOFF(username);
                invalid = 1;
                out.println("Invalid name or password");
                out.flush();
                return null;
        }
    }

    /**
     * procura as músicas que contêm a tag indicada
     * @param in
     * @param out
     * @throws IOException
     */
    public void procuraMusicaTag(BufferedReader in, PrintWriter out) throws IOException{
        out.println("Enter the songs' tag");
        out.flush();

        String tag = in.readLine();
        out.println("Songs with the tag " + tag);
        cloud.procuraMusicaTag(tag, out);
        out.flush();
    }

    /**
     * procura as músicas que contêm o ano indicado
     * @param in
     * @param out
     * @throws IOException
     */
    public void procuraMusicaAno(BufferedReader in, PrintWriter out) throws IOException{
        out.println("Enter the songs' year");
        out.flush();

        String year = in.readLine();
        int ano = Integer.parseInt(year);
        out.println("Songs from the year " + ano);
        cloud.procuraMusicaAno(ano, out);
        out.flush();
    }

    /**
     * procura as músicas com o título indicado
     * @param in
     * @param out
     * @throws IOException
     */
    public void procuraMusicaTitulo(BufferedReader in, PrintWriter out) throws IOException{
        out.println("Enter the songs' title");
        out.flush();

        String titulo = in.readLine();
        out.println("Songs with the title " + titulo);
        cloud.procuraMusicaTitulo(titulo, out);
        out.flush();
    }

    /**
     * procura todas as músicas presentes na biblioteca de músicas
     * @param in
     * @param out
     * @throws IOException
     */
    public void procuraMusicas(BufferedReader in, PrintWriter out) throws IOException{
        out.println("Songs in the library");
        out.flush();
        cloud.procuraMusicas(out);
        out.flush();
    }

    /**
     * procura as músicas do interprete indicado
     * @param in
     * @param out
     * @throws IOException
     */
    public void procuraMusicaInterprete(BufferedReader in, PrintWriter out) throws IOException{
        out.println("Enter the songs' interpreter");
        out.flush();

        String interprete = in.readLine();
        out.println("Songs from the interpreter " + interprete);
        cloud.procuraMusicaInterprete(interprete, out);
        out.flush();
    }

    public void run(){
        try{
            // criar canais de leitura e escrita no socket
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(socket.getOutputStream());

            boolean exit = false;
            while(!exit) {
                String titulo = null;
                String interprete = null;
                int ano = 0;
                List<String> tags;

                menuInicial(out);
                String[] mensagem = null;
                String operacao = null;
                String cmd = in.readLine();
                if (cmd != null) {
                    operacao = cmd;
                } else {
                    exit = false;
                    continue;
                }

                switch (operacao) {
                    case "criaUtilizador":
                        registaUtilizador(in, out);
                        break;

                    case "sair":
                        exit = true;
                        out.println("Session closed");
                        out.flush();
                        break;

                    case "autenticaUtilizador":
                        String utilizadorUsado = autenticaUtilizador(in, out);
                        if (invalid == 1) break;
                        Thread threadUpdateVerify = new Thread(new checkUpdate(this.cloud,out));
                        threadUpdateVerify.start();
                        while (!exit) {
                            menuUtilizador(out);
                            try {
                                cmd = in.readLine();
                                if (cmd != null) {
                                    operacao = cmd;
                                } else {
                                    exit = false;
                                    continue;
                                }
                                switch (operacao) {
                                    case "publicaMusica":
                                        out.println("Enter the song's name");
                                        out.flush();
                                        try{
                                            titulo = in.readLine();
                                            if(cloud.existeMusicaTitulo(titulo)){
                                                out.println("The song was already published");
                                                out.flush();
                                                break;
                                            }
                                        }catch(Exception e){}
                                        out.println("Enter the song's interpreter");
                                        out.flush();
                                        try{
                                            interprete = in.readLine();
                                        }catch(Exception e){}
                                        out.println("Enter the song's year");
                                        out.flush();
                                        try{
                                            ano = Integer.parseInt(in.readLine());
                                        }catch(Exception e){}
                                        out.println("Enter the song's tags");
                                        out.flush();
                                        try {
                                            cmd = in.readLine();
                                        } catch (IOException ex) {
                                            ex.printStackTrace();
                                        }
                                        String str[] = null;
                                        if (cmd != null)
                                             str = cmd.split(",");
                                        tags = Arrays.asList(str);

                                        File file;
                                        String input = "";
                                        String contentFile;
                                        out.println("Enter the song's path");
                                        try {
                                            out.flush();
                                            String path = in.readLine();
                                            out.println(path);
                                            out.flush();
                                            FileOutputStream fos;
                                            if (System.getProperty("os.name").contains("Windows")) {
                                                file = new File("Musicas\\" + titulo + ".mp3");
                                                fos = new FileOutputStream(file);
                                            } else {
                                                file = new File("Musicas/" + titulo + ".mp3");
                                                fos = new FileOutputStream(file);
                                            }
                                            while (true) {
                                                contentFile = in.readLine();
                                                if (contentFile.equals("false")) break;
                                                byte[] b = Base64.getDecoder().decode(contentFile.getBytes("UTF-8"));
                                                fos.write(b);
                                                fos.flush();
                                            }
                                            fos.close();
                                            int id = cloud.publicaMusica(titulo, interprete, ano, tags);
                                            out.println("Song published with id number: " + id);
                                            out.flush();
                                        } catch (FileNotFoundException e) {
                                            System.out.println("File not found" + e);
                                        } catch (IOException ioe) {
                                            System.out.println("Exception while writing file " + ioe);
                                        } catch (IllegalArgumentException e) { }
                                        break;

                                    case "procuraMusicaTag":
                                        procuraMusicaTag(in, out);
                                        break;

                                    case "procuraMusicaAno":
                                        procuraMusicaAno(in, out);
                                        break;

                                    case "procuraMusicaTitulo":
                                        procuraMusicaTitulo(in, out);
                                        break;

                                    case "procuraMusicaInterprete":
                                        procuraMusicaInterprete(in, out);
                                        break;

                                    case "procuraMusicas":
                                        procuraMusicas(in,out);
                                        break;

                                    case "downloadMusica":
                                        out.println("Enter the id of the song you want to download");
                                        out.flush();
                                        int ide = Integer.parseInt(in.readLine());
                                        Musica mus = cloud.getMusica(ide);
                                        if(mus == null){
                                            out.println("This id doesn't belong to any song");
                                            out.flush();
                                            break;
                                        }
                                        out.println("Song being downloaded");
                                        out.flush();
                                        out.println(utilizadorUsado);
                                        out.flush();
                                        String tituloM = mus.getTitulo();
                                        out.println(tituloM);
                                        out.flush();
                                        FileInputStream fis;
                                        try{
                                            if(System.getProperty("os.name").contains("Windows")) {
                                                File fileD = new File("Musicas\\"+tituloM);
                                                fis = new FileInputStream(fileD);
                                            }
                                            else{
                                                File fileD = new File("Musicas/"+tituloM);
                                                fis = new FileInputStream(fileD);
                                            }
                                            byte[] buf = new byte[MAXSIZE];
                                            for(int readNum;(readNum = fis.read(buf))!=-1;) {
                                                String contentD = Base64.getEncoder().encodeToString(buf);
                                                out.println(contentD);
                                                out.flush();
                                            }
                                            out.println("false");
                                            out.flush();
                                            fis.close();

                                            cloud.adicionaDownload(ide);
                                            out.println("Song with the title "+tituloM+" downloaded");
                                            out.flush();
                                        }catch(IOException e){}
                                        break;

                                    case "sair":
                                        exit = true;
                                        cloud.alteraEstadoOFF(utilizadorUsado);
                                        invalid = 0;
                                        out.println("Session closed");
                                        out.flush();
                                        break;

                                    default:
                                        throw new InputError();
                                }
                            } catch (IOException | InputError e) { }
                        }
                }

            }
            out.flush();
            out.close();
        } catch (IOException e){}
        finally{
            try{
                socket.shutdownInput();
                socket.shutdownOutput();
                socket.close();
            } catch (IOException e){}
        }
    }
}
