import java.io.*;
import java.net.Socket;
import java.util.Base64;

/**
 * Implementação da classe Cliente
 * Permite que um utilizador comunique com o sistema
 * 2019/2020
 */
class Cliente implements Runnable{

        private BufferedReader in;
        private PrintWriter out;
        private static final int MAXSIZE = 256;
        private static int sair = 0;

    public Cliente(BufferedReader in, PrintWriter out) {
        this.in = in;
        this.out = out;
    }

    public void run(){
            while (true){
                try {
                    String m = in.readLine();
                    if(sair == 1) break;
                    if(m == null) break;
                    System.out.println(m);
                    if(m.equals("Enter the song's path")){
                        String path = in.readLine();
                        File ficheiro = new File(path);
                        FileInputStream is = new FileInputStream(ficheiro);
                        byte[] buf = new byte[MAXSIZE];
                        for(int readNum;(readNum = is.read(buf))!=-1;) {
                            String contentFile = Base64.getEncoder().encodeToString(buf);
                            out.println(contentFile);
                            out.flush();
                        }
                        out.println("false");
                        out.flush();
                        is.close();
                    }
                    if(m.equals("Song being downloaded")){
                        String utilizador = in.readLine();
                        String nomeMusica = in.readLine();
                        String conteudo;
                        FileOutputStream fos;
                        if(System.getProperty("os.name").contains("Windows")){
                            File fich = new File(utilizador+"\\"+nomeMusica);
                            fos = new FileOutputStream(fich);
                        }
                        else {
                            File fich = new File(utilizador+"/"+nomeMusica);
                            fos = new FileOutputStream(fich);
                        }
                        while(true){
                            conteudo = in.readLine();
                            if (conteudo.equals("false")) break;
                            byte[] b = Base64.getDecoder().decode(conteudo.getBytes("UTF-8"));
                            fos.write(b);
                            fos.flush();
                        }
                        fos.close();
                    }
                    if(m.equals("Session closed")){
                        sair = 1;
                        Thread.currentThread().interrupt();
                        break;
                    }
                } catch (IOException | NullPointerException e) {
                    //System.out.println("Session closed");
                    sair = 1;
                    Thread.currentThread().interrupt();
                    break;
                }
            }
    }

        public static void main(String args[]) throws IOException {

            Socket socket = new Socket("127.0.0.1", 12345);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            PrintWriter out = new PrintWriter(socket.getOutputStream());

            BufferedReader ink = new BufferedReader(new InputStreamReader(System.in));

            Thread t = new Thread(new Cliente(in, out));
            t.start();

            String s;
            while (!(s = ink.readLine()).equals(" ")) {
                out.println(s);
                out.flush();
                if(sair == 1) break;
            }
            socket.shutdownOutput();
            socket.shutdownInput();
            socket.close();
        }
    }
