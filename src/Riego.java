import java.time.LocalTime;

public class Riego extends Thread {
    private int ciclosRealizados = 0;
    private double litrosTotales = 0;

    public Riego() {
        super("riego"); // Nombre obligatorio del hilo
    }

    @Override
    public void run() {
        for (int i = 1; i <= 5; i++) {
            // 1. Abrir válvula y sumar litros
            ciclosRealizados++;
            litrosTotales += 2.5;
            System.out.println("[" + LocalTime.now() + "] Hilo: " + getName() + " | Ciclo " + i + ": Válvula ABIERTA (+2.5L)");

            // Pausa de 700 ms con la válvula abierta
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }

            // 2. Cerrar válvula (en los 5 ciclos)
            System.out.println("[" + LocalTime.now() + "] Hilo: " + getName() + " | Ciclo " + i + ": Válvula CERRADA");

            // Pausa de 700 ms con la válvula cerrada (excepto si quieres omitirla al terminar el último, pero mantenerla simétrica es lo habitual)
            try {
                Thread.sleep(700);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
        System.out.println("[" + LocalTime.now() + "] Hilo: " + getName() + " | Finalizado. " + informe());
    }

    public String informe() {
        return String.format("Ciclos: %d | Litros totales: %.1f L", ciclosRealizados, litrosTotales);
    }
}