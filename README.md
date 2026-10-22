# Invernadero con hilos
## Qué hace
Este proyecto simula un sistema automatizado de control de un invernadero mediante programación concurrente en Java. El sistema gestiona de forma simultánea múltiples sensores ambientales (como Temperatura, Humedad y Luz) que realizan lecturas periódicas y detectan anomalías/alertas, junto a un programador de riego que ejecuta ciclos alternos de apertura y cierre de válvulas controlando el volumen de agua consumido.
## Cómo ejecutarlo
1. Clona o abre el proyecto en tu entorno de desarrollo (compatible con IntelliJ IDEA).
2. Asegúrate de tener configurado un JDK compatible (Java 17 o superior recomendado).
3. Para ejecutar la versión secuencial (Fase 1), ejecuta la clase:
   - Fase1_Secuencial
4. Para ejecutar la versión concurrente o con vigilante, compila y ejecuta las fases correspondientes (ej. Fase2_Concurrente o Fase4_Vigilante).

## Captura ← una ejecución de la Fase 2
[Ejecución Fase 2 Concurrente](img/fase2.png)
## Resultados
Tabla: tiempo de la Fase 1 y de la Fase 2
Fase/Estrategia,    Descripción del Flujo,                                                          Tiempo Total de Ejecución
Fase 1 (Secuencial),Ejecución síncrona mediante llamadas directas a .run() en un único hilo (main).,Ej: ~15.200 ms 
Fase 2 (Concurrente),"Ejecución asíncrona mediante .start(), procesando todos los hilos en paralelo.",Ej: ~3.100 ms
Conclusión: La ejecución concurrente reduce drásticamente el tiempo total de procesamiento al solapar los tiempos de espera (Thread.sleep) de los diferentes sensores y actuadores.
## Análisis
### ¿Por qué compensa usar hilos en el invernadero?
Compensa principalmente por dos motivos de peso: la optimización radical del tiempo (rendimiento) y la independencia de los procesos (simultaneidad)
### Experimentos A, B y C (qué pasó y por qué)
Experimentos A, B y C
Experimento A (Secuencial - Fase 1): Demuestra el cuello de botella clásico. Los hilos no se paralelizan; el sistema debe terminar por completo un sensor (ej. Temperatura) antes de iniciar el siguiente, acumulando linealmente los tiempos de espera.

Experimento B (Concurrente - Fase 2): Permite comprobar la concurrencia real en el procesador. Todos los hilos arrancan casi al unísono y compiten por el tiempo de CPU, gestionando sus retardos de forma independiente.

Experimento C (Vigilante / Demonio - Fase 4): Analiza el ciclo de vida de los hilos y el comportamiento de hilos de soporte en segundo plano. El uso de un hilo demonio garantiza que la aplicación principal no se quede bloqueada indefinidamente si un hilo auxiliar de supervisión queda a la escucha, permitiendo un apagado limpio y seguro del sistema.
### Estados observados (estado · cuándo · qué línea lo provoca)
Tabla de Estados Observados del HiloDurante la monitorización en la Fase del Vigilante, los hilos atraviesan los siguientes estados del ciclo de vida de Java:
Estado del Hilo      Descripción/Momento de Observación
NEW                  Estado inmediatamente posterior a instanciar el hilo (new Thread(...)) antes de invocar .start().
RUNNABLEEl           hilo está listo para ejecutarse o ejecutándose activamente en la CPU.
TIMED_WAITINGEl      hilo se encuentra pausado temporalmente debido a la ejecución de un Thread.sleep(periodo).
TERMINATEDEl         hilo ha finalizado la ejecución de su método run() de manera exitosa o por interrupción.
¿Por qué termina el programa si el vigilante sigue en su bucle?
Motivo de finalización gracias al hilo Demonio
Los hilos demonio (daemon threads) son servicios de soporte en segundo plano que no impiden que la JVM (Java Virtual Machine) finalice. Cuando todos los hilos de usuario principales (non-daemon) mueren, la máquina virtual se apaga automáticamente sin necesidad de esperar a que los hilos demonio terminen sus bucles infinitos de supervisión, evitando fugas de procesos colgados en el sistema operativo.
### Thread frente a Runnable
Tabla con al menos 4 aspectos

Aspecto                  Thread(Extensión)                                                                                          Runnable(Implementación)

Representación           La propia clase es un hilo de ejecución.                                                                   Representa únicamente una tarea ejecutable que se le asigna a un hilo.

Forma de arranque        Se instancia el objeto y se invoca directamente hilo.start().                                              Requiere envolver la tarea en un objeto Thread: new Thread(tareaRunnable).start().

Obtención de nombre      Se accede directamente mediante getName().                                                                 Se obtiene de forma indirecta consultando el hilo actual: Thread.currentThread().getName().

Herencia y Reutilización Inflexible: Al extender Thread, la clase no puede heredar de ninguna otra clase en Java (herencia simple). Muy flexible: Permite que la clase extienda de otra superclase si fuera necesario, separando datos/lógica de la ejecución.

¿Por qué los sensores SOLO pueden ser Runnable en tu diseño?

Porque conceptualmente un sensor es un modelo de datos con lógica de negocio propia (lecturas, umbrales, estadísticas) que puede ser ejecutado como tarea concurrente. 
Implementar Runnable nos permite mantener una clase abstracta (Sensor) que centraliza toda la estadística común, dejando libertad a las subclases para heredar de otras estructuras en el futuro si el diseño lo exigiera, además de aplicar el principio de separación de responsabilidades (la tarea vs. el mecanismo del hilo).

¿Qué forma elegirías para el riego y por qué?

Porque el programador de riego actúa como un actuador autónomo y específico del sistema con su propio ciclo de vida e hilo dedicado (la entidad es físicamente un proceso de control concurrente con un nombre fijo y obligatorio "riego" definido en su propio constructor mediante super("riego")).
