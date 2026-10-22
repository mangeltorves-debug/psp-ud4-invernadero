public class Fase4_Vigilante {
    public static void main(String[] args) {
        Temperatura temp = new Temperatura();
        Humedad hum = new Humedad();
        Luz luz = new Luz();
        Riego riego = new Riego();

        Thread tTemp = new Thread(temp, "sensor-Temperatura");
        Thread tHum = new Thread(hum, "sensor-Humedad");
        Thread tLuz = new Thread(luz, "sensor-Luz");

        // Estados antes de arrancar
        System.out.println("Estados antes de start():");
        System.out.println("Temperatura: " + tTemp.getState());
        System.out.println("Riego: " + riego.getState());

        // Hilo vigilante demonio
        Thread vigilante = new Thread(() -> {
            while (true) {
                System.out.println("[VIGILANTE] Estados -> Temp: " + tTemp.getState()
                        + " | Hum: " + tHum.getState()
                        + " | Luz: " + tLuz.getState()
                        + " | Riego: " + riego.getState());
                try {
                    Thread.sleep(300);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }, "hilo-vigilante");

        vigilante.setDaemon(true); // Evita que el bucle infinito bloquee el cierre de la JVM
        vigilante.start();

        tTemp.start();
        tHum.start();
        tLuz.start();
        riego.start();

        try {
            tTemp.join();
            tHum.join();
            tLuz.join();
            riego.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Estados después de terminar los hilos:");
        System.out.println("Temperatura: " + tTemp.getState());
        System.out.println("Riego: " + riego.getState());
    }
}