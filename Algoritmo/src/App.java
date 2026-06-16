import java.util.ArrayList;
import java.util.Random;

public class App {

static class PCB {
        int pid;
        int ppid;
        String prioridade;
        String status;
        int tempoChegada;
        int tempoServico;
        int tempoExecucao;
        int tempoQuantum;
        String tipoIO;
        int tempoIO;
        int pedidoIO;
        int tempoEspera;
        int tempoFinalizacao;


        public PCB(int pid, int ppid, String prioridade, String status, int tempoChegada, int tempoServico,
            String tipoIO, int tempoIO, int pedidoIO) {
            this.pid = pid;
            this.ppid = ppid;
            this.status = status;
            this.prioridade = prioridade;
            this.tempoChegada = tempoChegada;
            this.tempoServico = tempoServico;
            this.tempoExecucao = 0;
            this.tempoQuantum = 0;
            this.tipoIO = tipoIO;
            this.tempoIO = tempoIO;
            this.pedidoIO = pedidoIO;
            this.tempoEspera = 0;
            this.tempoFinalizacao = 0;
    }       
    }
        
    // variaveis de simulação
    static int quantum = 2;
    static int totalProcessos = 5;
    static int seed = 4576;

    // Variáveis de controle do CPU
    static int minIO = 2;
    static int maxIO = 5;
    static int minCPU = 4;
    static int maxCPU = 10;
    static int finalizados = 0;
    static int preempcoes = 0;
    static PCB processoAtual = null;
    static int ioImpressora = 0;
    static int ioDisco = 0;
    static int ioFita = 0;
    static int ticksOciosos = 0;
    

    static String[] tiposIO = new String[] {"IMPRESSORA", "DISCO", "FITA", "NENHUM"};
    static String[] prioridade = new String[] {"ALTA", "BAIXA"};
    static String[] status = new String[] {"NOVO", "PRONTO", "EXECUTANDO", "BLOQUEADO", "FINALIZADO"};

    static void log(int tempo, String mensagem) {
    System.out.printf("[t=%03d] %s%n", tempo, mensagem);
}

    static PCB[] inicializarProcessos(Random rnd) {
 
        PCB[] processos = new PCB[totalProcessos];
        String tipoIO;
        
        // Cria os processos a partir de seed fixa no random rnd
        for (int i = 0; i < totalProcessos; i++ ) {
            int pid = i + 1;
            int ppid = 0;
            int tempoIO = 0;
            int pedidoIO = 0;

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

    static void verificarChegada(PCB[] processos, int tempoAtual, ArrayList<PCB> filaAlta) {
        for (int i = 0; i < processos.length; i++){
            PCB processo = processos[i];
            if (processo.tempoChegada == tempoAtual && processo.status.equals(status[0])) { 
                    filaAlta.add(processos[i]);
                    processo.status = status[1];
                    log(tempoAtual, "P" + processo.pid + " criado -> fila ALTA");
            } 
        }    
    }

    static void atualizarFila (int tempo,  ArrayList<PCB> filaAlta, ArrayList<PCB> filaBaixa, ArrayList<PCB> filaIO) {
        
        PCB processo = !filaAlta.isEmpty() ? filaAlta.get(0) : !filaBaixa.isEmpty() ? filaBaixa.get(0) : null;
        
        if (processoAtual != null && processoAtual != processo) {
                processoAtual.tempoQuantum = 0;
            }
            processoAtual = processo;
        
        if (processo != null) {
            processo.status = status[2]; // Executando
            String filaOrigem = filaAlta.contains(processo) ? "ALTA" : "BAIXA";
            if (processo.tempoQuantum == 0) log(tempo, "CPU executa P" + processo.pid + " [fila " + filaOrigem + "]...");
            processo.tempoExecucao++;
            processo.tempoServico--;
            processo.tempoQuantum++;
        } else {
            ticksOciosos++;
            return;
        }
        // Finalizou
        if (processo.tempoServico == 0){
            processo.status = status[4]; // Finalizado
            finalizados++;
            filaAlta.remove(processo);
            filaBaixa.remove(processo);
            processo.tempoFinalizacao = tempo;
            log(tempo, "P" + processo.pid + " finalizou");
            return; // processo finalizado nao deve ser avaliado para I/O ou preempcao
        }
        
        if (!processo.tipoIO.equals("NENHUM") && processo.tempoExecucao == processo.pedidoIO) { 
            processo.status = status[3]; // Bloqueado
            processo.tempoQuantum = 0;
            filaIO.add(processo);
            log(tempo, "P" + processo.pid + " solicitou I/O " + processo.tipoIO + " por " + processo.tempoIO + " unidades");
            filaAlta.remove(processo);
            filaBaixa.remove(processo);
            return;
        }   else if (processo.tempoQuantum == quantum) { // Preempção
                if (filaAlta.contains(processo)) {
                    filaAlta.remove(processo);
                    processo.status = status[1]; // Pronto
                    filaBaixa.add(processo);
                    processo.tempoQuantum = 0;
                    processo.prioridade = prioridade[1];
                    preempcoes++;
                    log(tempo, "P" + processo.pid + " sofreu preempcao -> fila BAIXA");
                } else if(filaBaixa.contains(processo)) {
                    filaBaixa.remove(processo);
                    processo.tempoQuantum = 0;
                    filaBaixa.add(processo);
                    log(tempo, "P" + processo.pid + " sofreu preempcao -> fila BAIXA");
                    preempcoes++;
                }
            }
    }       
    
    static void atualizarFilaIO(int tempo, ArrayList<PCB> filaAlta, ArrayList<PCB> filaBaixa, ArrayList<PCB> filaIO) {
        if (!filaIO.isEmpty()) {
                for (PCB processo : filaIO) {
                processo.tempoIO--;
                    if (processo.tempoIO == 0){
                    processo.status = status[1]; // Pronto
                        if (processo.tipoIO.equals("DISCO")){
                            ioDisco++;
                            processo.prioridade = prioridade[1];
                            filaBaixa.add(processo);
                            log(tempo, "P" + processo.pid + " retornou do " + processo.tipoIO + " -> fila " + (processo.prioridade.equals("BAIXA") ? "BAIXA" : "ALTA"));
                        } else {
                            if (processo.tipoIO.equals("IMPRESSORA")) ioImpressora++;
                            else if (processo.tipoIO.equals("FITA")) ioFita++;
                            processo.prioridade = prioridade[0];
                            filaAlta.add(processo);
                            log(tempo, "P" + processo.pid + " retornou do " + processo.tipoIO + " -> fila " + (processo.prioridade.equals("BAIXA") ? "BAIXA" : "ALTA"));
                        }
                    }
                } 
                filaIO.removeIf(processo -> processo.tempoIO == 0);
                return;
        }
    }
    static void atualizarEspera(ArrayList<PCB> filaALta,ArrayList<PCB> filaBaixa){
        for (PCB pcb : filaBaixa) {
            if (pcb.status.equals(status[1])){
                pcb.tempoEspera++;
            }
        }
        for (PCB pcb : filaALta) {
            if (pcb.status.equals(status[1])){
                pcb.tempoEspera++;
            }
        }
    }

    public static void main(String[] args) throws Exception {
        ArrayList<PCB> filaAlta = new ArrayList<>();
        ArrayList<PCB> filaBaixa = new ArrayList<>();
        ArrayList<PCB> filaIO = new ArrayList<>();
        int tempo = 0;
        Random rnd = new Random(seed);
        

        System.out.printf("[config] quantum = %d | processos = %d | seed = %d\n", quantum, totalProcessos, seed);
        PCB[] processos = inicializarProcessos(rnd);


        while (finalizados < totalProcessos) {
                atualizarFilaIO(tempo, filaAlta, filaBaixa, filaIO);
                verificarChegada(processos, tempo, filaAlta);
                atualizarEspera(filaAlta, filaBaixa);
                atualizarFila(tempo, filaAlta, filaBaixa, filaIO);
                tempo++; 
        }
        double esperaMedia = 0;
        double turnaroundMedio = 0;
        for (PCB p : processos) {
            esperaMedia += p.tempoEspera;
            turnaroundMedio += (p.tempoFinalizacao - p.tempoChegada);
        }
        esperaMedia /= totalProcessos;
        turnaroundMedio /= totalProcessos;

        double percentualOcioso = (ticksOciosos * 100.0) / tempo;

        System.out.printf("[fim] Processos finalizados: %d/%d | tempo total: %d | preempcoes: %d%n", finalizados, totalProcessos, tempo, preempcoes);
        System.out.printf("[fim] I/O -> DISCO: %d | FITA: %d | IMPRESSORA: %d%n", ioDisco, ioFita, ioImpressora);
        System.out.printf("[fim] Espera media: %.1f | Retorno medio: %.1f%n", esperaMedia, turnaroundMedio);
        System.out.printf("[fim] CPU ociosa: %d ticks (%.1f%%)%n", ticksOciosos, percentualOcioso);
        
        System.out.printf("[fim] Processos finalizados: %d/%d | tempo total: %d | preempcoes: %d ", finalizados, totalProcessos, tempo, preempcoes);
        }
    }
