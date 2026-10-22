import java.time.LocalTime;
import java.util.concurrent.ThreadLocalRandom;

public abstract class Sensor implements Runnable {
    protected String nombre;
    protected String unidad;
    protected long periodo; // en milisegundos
    protected int numLecturas;
    protected double umbralMin;
    protected double umbralMax;

    // Estadísticas
    protected int lecturasRealizadas = 0;
    protected double sumaValores = 0;
    protected double minValor = Double.MAX_VALUE;
    protected double maxValor = Double.MIN_VALUE;
    protected int alertas = 0;

    public Sensor(String nombre, String unidad, long periodo, int numLecturas, double umbralMin, double umbralMax) {
        this.nombre = nombre;
        this.unidad = unidad;
        this.periodo = periodo;
        this.numLecturas = numLecturas;
        this.umbralMin = umbralMin;
        this.umbralMax = umbralMax;
    }

    public abstract double medir();

    @Override
    public void run() {
        for (int i = 0; i < numLecturas; i++) {
            double valor = medir();
            lecturasRealizadas++;
            sumaValores += valor;

            if (valor < minValor) minValor = valor;
            if (valor > maxValor) maxValor = valor;

            String alertaStr = "";
            if (valor < umbralMin || valor > umbralMax) {
                alertas++;
                alertaStr = " -> ¡¡ALERTA!!";
            }

            System.out.println("[" + LocalTime.now() + "] Hilo: " + Thread.currentThread().getName()
                    + " | Sensor: " + nombre + " | Valor: " + String.format("%.2f", valor) + " " + unidad + alertaStr);

            try {
                Thread.sleep(periodo);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                break;
            }
        }
    }

    public String informe() {
        double media = lecturasRealizadas > 0 ? sumaValores / lecturasRealizadas : 0;
        return String.format("Sensor: %s | Lecturas: %d | Media: %.2f %s | Min: %.2f %s | Max: %.2f %s | Alertas: %d",
                nombre, lecturasRealizadas, media, unidad, minValor, unidad, maxValor, unidad, alertas);
    }
}