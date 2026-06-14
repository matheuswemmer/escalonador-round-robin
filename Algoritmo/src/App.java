import java.util.ArrayList;
import java.util.Random;

public class App {
    static int quantum = 2;
    static int totalProcessos = 5;
    static int seed = 4576;
    static int minIO = 2;
    static int maxIO = 5;
    static int minCPU = 4;
    static int maxCPU = 10;
    static String[] tiposIO = new String[] {"IMPRESSORA", "DISCO", "FITA", "NENHUM"};
    static String[] prioridade = new String[] {"ALTA", "BAIXA"};
    static String[] status = new String[] {"NOVO", "PRONTO", "EXECUTANDO", "BLOQUEADO", "FINALIZADO"};

    static PCB[] InicializarProcessos(Random rnd ) {
        PCB[] processos =new PCB[totalProcessos];
        String tipoIO;
        int tempoIO = 0;
        int pedidoIO = 0;

        for (int i = 0; i < totalProcessos; i++ ) {
            int pid = i;
            int ppid = 0;

            int tempoChegada = rnd.nextInt(10) + 1;
            int tempoServico = minCPU + rnd.nextInt(maxCPU - minCPU + 1);
            
 
            if (rnd.nextInt(4) == 3) {
                tipoIO = tiposIO[3];
            } else {
                tipoIO = tiposIO[rnd.nextInt(3)];
                pedidoIO = 1 + rnd.nextInt(tempoServico - 1);
                tempoIO = minIO + rnd.nextInt(maxIO - minIO + 1);
            }
 
            processos[i] = new PCB(pid, ppid, prioridade[0], status[0], tempoChegada, tempoServico, tipoIO, tempoIO, pedidoIO);
        }
        return processos;
    }

    static void atualizarProcesso (PCB processo, int tempo,  ArrayList<PCB> filaAlta, ArrayList<PCB> filaBaixa, ArrayList<PCB> filaIO) {
       
    }

    static void verificarChegada(PCB[] processos, int tempo, ArrayList<PCB> filaAlta) {
             
        }

    static void atualizarFilaIO(ArrayList<PCB> filaIO) {
        if (!filaIO.isEmpty()) {
            
            return;
        } return;
    }


    public static void main(String[] args) throws Exception {
        ArrayList<PCB> filaAlta = new ArrayList<>();
        ArrayList<PCB> filaBaixa = new ArrayList<>();
        ArrayList<PCB> filaIO = new ArrayList<>();
        int tempo = 0;
        int finalizados = 0;
        Random rnd = new Random(seed);
        

        System.out.printf("[config] quantum = %d | processos = %d | seed = %d\n", quantum, totalProcessos, seed);
        PCB[] processos = InicializarProcessos(rnd);
        
        while (finalizados < totalProcessos) {
        while (tempo < tempo + 1) {
            verificarChegada(processos, tempo, null);
            if (!filaAlta.isEmpty()) {
                PCB processoAtual = filaAlta.get(0);
                atualizarProcesso(processoAtual, tempo, filaAlta, filaBaixa, filaIO);
                atualizarFilaIO(filaIO);
            }
        tempo++; 
        }
        finalizados++;
        }
        
        System.out.printf("[fim] Processos finalizados: %d/%d | tempo total: %d", finalizados, totalProcessos, tempo);
        }
    }

    class PCB {
        int pid;
        int ppid;
        String prioridade;
        String status;
        int tempoChegada;
        int tempoServiço;
        int tempoExecuçao;
        String tipoIO;
        int tempoIO;
        int pedidoIO;
        
        public PCB(int pid, int ppid, String prioridade, String status, int tempoChegada, int tempoServiço,
                String tipoIO, int tempoIO, int pedidoIO) {
            this.pid = pid;
            this.ppid = ppid;
            this.prioridade = prioridade;
            this.status = status;
            this.tempoChegada = tempoChegada;
            this.tempoServiço = tempoServiço;
            this.tempoExecuçao = 0;
            this.tipoIO = tipoIO;
            this.tempoIO = tempoIO;
            this.pedidoIO = pedidoIO;
    }       
}
