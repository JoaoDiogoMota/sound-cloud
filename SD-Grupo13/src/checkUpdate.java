import java.io.PrintWriter;

public class checkUpdate implements Runnable{
    private InterfaceCloud cloud;
    private PrintWriter out;
    private int nMusicas;

    public checkUpdate(InterfaceCloud cloud, PrintWriter out) {
        this.cloud = cloud;
        this.out = out;
        this.nMusicas = cloud.calculaID();
    }

    public void run(){
        while(true){
            int currentVal = cloud.calculaID();
            if(nMusicas != currentVal){
                nMusicas = currentVal;
                Musica m = cloud.getMusica(currentVal);
                out.println("##########################");
                out.println("Nova música adicionada:");
                out.println(m.getTitulo());
                out.println(m.getInterprete());
                out.println("##########################");
                out.flush();
            }
        }
    }
}
