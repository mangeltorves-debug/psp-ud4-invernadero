public class Fase1_Secuencial {
    public static void main(String[] args) {
        Temperatura temp = new Temperatura();
        Humedad hum = new Humedad();
        Luz luz = new Luz();
        Riego riego = new Riego();

        long inicio = System.currentTimeMillis();

        // Ejecución secuencial llamando a run() directamente
        temp.run();
        hum.run();
        luz.run();
        riego.run();

        long fin = System.currentTimeMillis();

        System.out.println("\n--- INFORMES FINALES (SECUENCIAL) ---");
        System.out.println(temp.informe());
        System.out.println(hum.informe());
        System.out.println(luz.informe());
        System.out.println(riego.informe());
        System.out.println("Tiempo total secuencial: " + (fin - inicio) + " ms");
    }
}