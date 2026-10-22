public class Fase3_Experimentos {
    public static void main(String[] args) {
        System.out.println("--- EXPERIMENTO A ---");
        Temperatura tempA = new Temperatura();
        Thread tA = new Thread(tempA, "sensor-Temp-ExpA");
        tA.start();
        // Imprime el informe sin esperar (estará vacío o incompleto)
        System.out.println("Informe inmediato: " + tempA.informe());
        try {
            tA.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Informe tras join(): " + tempA.informe());

        System.out.println("\n--- EXPERIMENTO B ---");
        // Llamar a run() en lugar de start() sobre un Thread de riego adaptado o simulado
        Riego riegoB = new Riego();
        long inicioB = System.currentTimeMillis();
        riegoB.run(); // Ejecutado por main
        long finB = System.currentTimeMillis();
        System.out.println("Tiempo tardado con run(): " + (finB - inicioB) + " ms");
        System.out.println("Estado del objeto al terminar: " + riegoB.getState());

        System.out.println("\n--- EXPERIMENTO C ---");
        Riego riegoC = new Riego();
        riegoC.start();
        try {
            riegoC.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            // Intentar arrancar un hilo que ya ha terminado (TERMINATED)
            riegoC.start();
        } catch (IllegalThreadStateException e) {
            System.out.println("Excepción capturada con éxito: " + e.getClass().getName());
            System.out.println("Significado: No se puede reiniciar un hilo que ya ha finalizado su ciclo de vida.");
        }
    }
}