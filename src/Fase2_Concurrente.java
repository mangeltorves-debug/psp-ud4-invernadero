public class Fase2_Concurrente {
    public static void main(String[] args) {
        Temperatura temp = new Temperatura();
        Humedad hum = new Humedad();
        Luz luz = new Luz();
        Riego riego = new Riego(); // Ya es Thread

        Thread tTemp = new Thread(temp, "sensor-Temperatura");
        Thread tHum = new Thread(hum, "sensor-Humedad");
        Thread tLuz = new Thread(luz, "sensor-Luz");

        long inicio = System.currentTimeMillis();

        tTemp.start();
        tHum.start();
        tLuz.start();
        riego.start(); // El riego se arranca directo al heredar de Thread

        try {
            tTemp.join();
            tHum.join();
            tLuz.join();
            riego.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        long fin = System.currentTimeMillis();

        System.out.println("\n--- INFORMES FINALES (CONCURRENTE) ---");
        System.out.println(temp.informe());
        System.out.println(hum.informe());
        System.out.println(luz.informe());
        System.out.println(riego.informe());
        System.out.println("Tiempo total concurrente: " + (fin - inicio) + " ms");
    }
}