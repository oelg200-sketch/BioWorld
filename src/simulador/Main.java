
package simulador;

import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Ecosistema ecosistema;
    static Clip clipMusicaFondo;
    
    // Se cambia a no-final para poder resetearla al reiniciar el juego
    static volatile int horaGlobal = 6; 
    static String trackActual = "";

    // -------------------------------------------------------
    // REPRODUCIR EFECTOS DE SONIDO CORTOS
    // -------------------------------------------------------
    static void reproducirSonido(String archivo) {
        try {
            File f = new File(archivo);
            if (!f.exists()) return; 
            AudioInputStream audio = AudioSystem.getAudioInputStream(f);
            Clip clip = AudioSystem.getClip();
            clip.open(audio);
            clip.start();
            new Thread(() -> {
                try {
                    Thread.sleep(Math.min(clip.getMicrosecondLength() / 1000, 1000));
                } catch (Exception e) {}
            }).start();
        } catch (Exception e) {}
    }

    // -------------------------------------------------------
    // REPRODUCIR MÚSICA DE FONDO
    // -------------------------------------------------------
    static void cambiarMusicaFondo(String archivo) {
        if (trackActual.equals(archivo)) {
            return; 
        }
        try {
            if (clipMusicaFondo != null) {
                if (clipMusicaFondo.isRunning()) {
                    clipMusicaFondo.stop();
                }
                clipMusicaFondo.close();
            }
            File f = new File(archivo);
            if (!f.exists()) return;

            AudioInputStream audio = AudioSystem.getAudioInputStream(f);
            clipMusicaFondo = AudioSystem.getClip();
            clipMusicaFondo.open(audio);
            clipMusicaFondo.loop(Clip.LOOP_CONTINUOUSLY); 
            clipMusicaFondo.start();
            trackActual = archivo; 
        } catch (Exception e) {}
    }

    static final String SONIDO_AGREGAR = "Button_Plate-Click-_Minecraft-Sound_-Sound-Effect-for-editing_M4A_128K_ (1).wav";
    static final String SONIDO_MUERTE  = "Minecraft-hit-sound-1080p60fps_M4A_128K_.wav";
    static final String SONIDO_CRECER  = "Level_XP-Sounds-_Minecraft_-Sound-Effects-for-editing_M4A_128K_.wav";

    // -------------------------------------------------------
    // MAIN: BUCLE GLOBAL DE REINICIO
    // -------------------------------------------------------
    public static void main(String[] args) {
        while (true) {
            // Resetear valores iniciales para una nueva partida
            horaGlobal = 6;
            trackActual = "";
            
            cambiarMusicaFondo("Hollow-Knight-OST-Dirtmouth_M4A_128K_.wav");
            mostrarBienvenida();
            configurarJuego();
            iniciarSimulacion();
            
            // Si el método iniciarSimulacion termina, significa que todos murieron o se forzó el colapso
            mostrarFinDeJuego(horaGlobal);
            
            System.out.print("  ¿Deseas iniciar otra vez? (s/n): ");
            String respuesta = scanner.next().trim().toLowerCase();
            if (!respuesta.equals("s")) {
                System.out.println("\n  Gracias por jugar BioWorld. ¡Hasta la proxima!");
                if (clipMusicaFondo != null) clipMusicaFondo.close();
                break; // Rompe el bucle global y cierra el programa en paz
            }
            System.out.println("\n  Reiniciando el universo...");
        }
    }

    static void mostrarBienvenida() {
        System.out.println("\n\u001B[32m"); 
        System.out.println("      ____  _  ____ _ _ _ ____ ____  _    ___  ");
        System.out.println("      |__]  |  |  | | | | |  | |__/  |    |  \\ ");
        System.out.println("      |__]  |  |__| |_|_| |__| |  \\  |___ |__/ ");
        System.out.println("   ================================================= ");
        System.out.println("       SIMULADOR DE ECOSISTEMAS INTERACTIVO v2.0     ");
        System.out.println("   ================================================= \u001B[0m");
        System.out.println("\n    Crea, altera y observa la evolucion de la vida,");
        System.out.println("    biomas, climas y cadenas alimenticias reales.\n");
        presionarParaContinuar();
    }

    static void configurarJuego() {
        Bioma.BiomaTipo bioma = elegirBioma();
        int temperatura = elegirTemperatura(bioma);
        ecosistema = new Ecosistema(bioma, temperatura);

        agregarPlantas(bioma);
        agregarAnimales(bioma);

        System.out.println("\n\u001B[36m┌────────────────────────────────────────┐");
        System.out.println("│       ECOSISTEMA CONFIGURADO          │");
        System.out.println("└────────────────────────────────────────┘\u001B[0m");
        System.out.printf("  Bioma       : %s\n", bioma);
        System.out.printf("  Temperatura : %d C\n", temperatura);
        System.out.printf("  Plantas     : %d unidades\n", ecosistema.contarPlantas());
        System.out.printf("  Animales    : %d unidades\n", ecosistema.contarAnimales());
        System.out.println("\u001B[36m──────────────────────────────────────────\u001B[0m");
        presionarParaContinuar();
    }

    static Bioma.BiomaTipo elegirBioma() {
        System.out.println("\n\u001B[34m┌────────────────────────────────────────┐");
        System.out.println("│          SELECCIONA UN BIOMA           │");
        System.out.println("└────────────────────────────────────────┘\u001B[0m");
        System.out.println("  [1] Desierto (Calor extremo y sequia)");
        System.out.println("  [2] Praderas (Clima templado y balanceado)");
        System.out.println("  [3] Glaciar  (Frio polar e invierno eterno)");
        System.out.println("  [4] Selva    (Humedad alta y abundante flora)");
        System.out.print("\n Elige una opcion (1-4): ");

        int op = leerEntero(1, 4);
        if (op == 1) return Bioma.BiomaTipo.DESIERTO;
        if (op == 2) return Bioma.BiomaTipo.PRADERA;
        if (op == 3) return Bioma.BiomaTipo.GLACIAR;
        return Bioma.BiomaTipo.SELVA;
    }

    static int elegirTemperatura(Bioma.BiomaTipo bioma) {
        int[] rango = Bioma.rangoTemperatura(bioma);
        System.out.println("\n Rango permitido para " + bioma + ": [" + rango[0] + "C a " + rango[1] + "C]");
        System.out.print(" Ingresa la temperatura inicial: ");
        return leerEntero(rango[0], rango[1]);
    }

    static void agregarPlantas(Bioma.BiomaTipo bioma) {
        String[] disponibles = Bioma.plantasDeBioma(bioma);
        int[] cantidades = new int[disponibles.length];

        System.out.println("\n\u001B[32m┌────────────────────────────────────────┐");
        System.out.println("│          SELECCION DE FLORA            │");
        System.out.println("└────────────────────────────────────────┘\u001B[0m");
        System.out.println("Especies nativas recomendadas:");
        for (int i = 0; i < disponibles.length; i++) {
            String etiqueta = Bioma.esPlantaVenenosa(disponibles[i]) ? "  [VENENOSA]" : "  [Segura]";
            System.out.println("  [" + (i + 1) + "] " + disponibles[i] + etiqueta);
        }

        System.out.println("\n  ¿Cuantas unidades deseas sembrar? (Max 20)");
        for (int i = 0; i < disponibles.length; i++) {
            System.out.print("  -> " + disponibles[i] + ": ");
            cantidades[i] = leerEntero(0, 20);
        }

        int totalAgregadas = 0;
        for (int i = 0; i < disponibles.length; i++) {
            if (cantidades[i] > 0) {
                boolean venenosa = Bioma.esPlantaVenenosa(disponibles[i]);
                for (int j = 0; j < cantidades[i]; j++) {
                    ecosistema.agregarPlanta(new Planta(disponibles[i], bioma, venenosa));
                }
                totalAgregadas += cantidades[i];
            }
        }

        if (totalAgregadas > 0) reproducirSonido(SONIDO_AGREGAR);
    }

    static void agregarAnimales(Bioma.BiomaTipo bioma) {
        String[] herbivoros = Bioma.herbivoresDeBioma(bioma);
        String[] carnivoros = Bioma.carnivoresDeBioma(bioma);
        int totalEspecies   = herbivoros.length + carnivoros.length;

        int[]      cantidades   = new int[totalEspecies];
        String[][] nombres      = new String[totalEspecies][10]; 

        System.out.println("\n\u001B[33m┌────────────────────────────────────────┐");
        System.out.println("│          SELECCION DE FAUNA            │");
        System.out.println("└────────────────────────────────────────┘\u001B[0m");
        System.out.println(" HERBIVOROS DISPONIBLES:");
        for (int i = 0; i < herbivoros.length; i++) System.out.println("  [" + (i + 1) + "] " + herbivoros[i]);
        
        System.out.println("\n CARNIVOROS DISPONIBLES:");
        for (int i = 0; i < carnivoros.length; i++) System.out.println("  [" + (herbivoros.length + i + 1) + "] " + carnivoros[i]);

        scanner.nextLine(); 

        System.out.println("\n  Cantidad a spawnear (Max 10 por especie):");
        
        for (int i = 0; i < herbivoros.length; i++) {
            System.out.print("  -> " + herbivoros[i] + ": ");
            cantidades[i] = leerEntero(0, 10);
            scanner.nextLine(); 
            for (int j = 0; j < cantidades[i]; j++) {
                System.out.print("      Nombre para " + herbivoros[i] + " #" + (j + 1) + " (Presiona ENTER para omitir): ");
                String n = scanner.nextLine().trim();
                nombres[i][j] = n.isEmpty() ? herbivoros[i] : n;
            }
        }
        
        for (int i = 0; i < carnivoros.length; i++) {
            int idx = herbivoros.length + i;
            System.out.print("  -> " + carnivoros[i] + ": ");
            cantidades[idx] = leerEntero(0, 10);
            scanner.nextLine(); 
            for (int j = 0; j < cantidades[idx]; j++) {
                System.out.print("      Nombre para " + carnivoros[i] + " #" + (j + 1) + " (Presiona ENTER para omitir): ");
                String n = scanner.nextLine().trim();
                nombres[idx][j] = n.isEmpty() ? carnivoros[i] : n;
            }
        }

        for (int i = 0; i < totalEspecies; i++) {
            if (cantidades[i] > 0) {
                String especie = i < herbivoros.length ? herbivoros[i] : carnivoros[i - herbivoros.length];
                boolean esHerbivoro = (i < herbivoros.length);
                for (int j = 0; j < cantidades[i]; j++) {
                    ecosistema.agregarAnimal(new Animal(nombres[i][j], especie, esHerbivoro, bioma));
                }
            }
        }

        reproducirSonido(SONIDO_AGREGAR);
    }

    // -------------------------------------------------------
    // AVANCE TEMPORAL COMPARTIDO
    // -------------------------------------------------------
    static synchronized void ejecutarAvanceDeHora() {
        ArrayList<Animal> copiaAntes = new ArrayList<>(ecosistema.getAnimales());
        ArrayList<String> nombresCriasAntes = new ArrayList<>();
        for (Animal a : copiaAntes) {
            if (a.estaVivo() && !a.esAdulto()) {
                nombresCriasAntes.add(a.getNombre());
            }
        }

        horaGlobal++;
        ecosistema.avanzarHora(horaGlobal);

        if (horaGlobal % 5 == 0) {
            System.out.println("\n\u001B[35m[REPRODUCCION] Han pasado 5 horas. El ecosistema procesa crias...\u001B[0m");
        }

        int horaDelDia = horaGlobal % 24;
        boolean esDia = (horaDelDia >= 6 && horaDelDia < 18);

        String trackSiguiente = esDia 
            ? "Hollow-Knight-OST-Greenpath_M4A_128K_.wav" 
            : "Hollow-Knight-OST-Enter-Hallownest_M4A_128K_.wav";
        
        cambiarMusicaFondo(trackSiguiente);

        boolean alguienCrecio = false;
        for (Animal a : ecosistema.getAnimales()) {
            if (a.estaVivo() && a.esAdulto() && nombresCriasAntes.contains(a.getNombre())) {
                alguienCrecio = true;
                System.out.println("\n\u001B[32m[EVOLUCION] " + a.getNombre() + " ha alcanzado su etapa adulta!\u001B[0m");
            }
        }

        if (alguienCrecio || ecosistema.huboCriaQueCrecio()) {
            reproducirSonido(SONIDO_CRECER);
        }
    }

    // -------------------------------------------------------
    // SIMULACION PRINCIPAL
    // -------------------------------------------------------
    static void iniciarSimulacion() {
        System.out.println("\n\u001B[32m>>> El motor de BioWorld se ha iniciado correctamente. \u001B[0m");
        presionarParaContinuar();

        cambiarMusicaFondo("Hollow-Knight-OST-Greenpath_M4A_128K_.wav");

        Thread hiloReloj = new Thread(() -> {
            while (ecosistema.hayAnimalesVivos()) {
                try {
                    Thread.sleep(60000); 
                    
                    int animalesAntesDeAvanzar = ecosistema.contarAnimales();
                    ejecutarAvanceDeHora();
                    
                    int vivosDespues = ecosistema.contarAnimales();
                    if (vivosDespues < animalesAntesDeAvanzar) {
                        reproducirSonido(SONIDO_MUERTE);
                    }
                } catch (InterruptedException e) {
                    break;
                }
            }
        });
        
        hiloReloj.setDaemon(true);
        hiloReloj.start();

        while (ecosistema.hayAnimalesVivos()) {

            if (ecosistema.quedanPocosAnimales()) {
                System.out.println("\n\u001B[31m  ALERTA CRITICA: Peligro de extincion inminente.\u001B[0m");
                System.out.print("  ¿Quieres inyectar mas fauna al ecosistema? (s/n): ");
                if (scanner.next().equalsIgnoreCase("s")) {
                    agregarAnimales(ecosistema.getBiomaTipo());
                    continue;
                }
            }

            mostrarEstadoMundo(horaGlobal);
            int opcion = mostrarMenuTurno();

            if (opcion == 1) {
                System.out.print("\n  ¿Cuantas horas quieres adelantar en el tiempo de golpe? (1-24): ");
                int horasASaltar = leerEntero(1, 24);
                
                System.out.println("\n\u001B[35m  Avanzando " + horasASaltar + " horas...\u001B[0m");
                int vivosAntesDelSalto = ecosistema.contarAnimales();
                
                for (int i = 0; i < horasASaltar; i++) {
                    ejecutarAvanceDeHora();
                    if (!ecosistema.hayAnimalesVivos()) break;
                }
                
                int vivosDespuesDelSalto = ecosistema.contarAnimales();
                if (vivosDespuesDelSalto < vivosAntesDelSalto) {
                    reproducirSonido(SONIDO_MUERTE);
                }
                
            } else if (opcion == 2) {
                cambiarTemperatura();
            } else if (opcion == 3) {
                cambiarClima();
            } else if (opcion == 4) {
                mostrarListaDetallada();
            } else if (opcion == 5) {
                System.out.println("\n  MENU DE INYECCION BIOLOGICA:");
                System.out.println("  [1] Sembrar nuevas plantas");
                System.out.println("  [2] Spawnear nuevos animales");
                System.out.print("  Selecciona que deseas agregar: ");
                int seleccionVida = leerEntero(1, 2);
                if (seleccionVida == 1) {
                    agregarPlantas(ecosistema.getBiomaTipo());
                } else {
                    agregarAnimales(ecosistema.getBiomaTipo());
                }
            } else if (opcion == 6) {
                // CORRECCIÓN SOLICITADA: En lugar de System.exit(0), rompemos el ciclo para ir al menú de reinicio
                System.out.println("\nColapsando simulacion voluntariamente...");
                break; 
            }
        }
        
        hiloReloj.interrupt();
    }

    static void mostrarEstadoMundo(int hora) {
        int horaDelDia = hora % 24;
        boolean esDia = (horaDelDia >= 6 && horaDelDia < 18);
        
        String iconoCiclo = esDia ? "BUEN DIA" : "BUENA NOCHE";
        String colorCiclo = esDia ? "\u001B[33m" : "\u001B[34m"; 

        String horaFormateada = String.format("%02d:00", horaDelDia);

        System.out.println("\n" + colorCiclo + "┌────────────────────────────────────────────────────────┐");
        System.out.printf("│  %-52s │\n", iconoCiclo + " - " + ecosistema.getDescripcionCiclo(hora));
        System.out.printf("│   Reloj: %-13s |  Bioma: %-19s │\n", horaFormateada + " (" + hora + "h total)", ecosistema.getBioma());
        System.out.printf("│   Temp: %-14s |  Clima: %-19s │\n", ecosistema.getTemperatura() + "C", ecosistema.getClima());
        System.out.println("├────────────────────────────────────────────────────────┤");
        System.out.printf("│   Plantas vivas: %-10d |  Animales vivos: %-10d │\n", ecosistema.contarPlantas(), ecosistema.contarAnimales());
        System.out.println("└────────────────────────────────────────────────────────┘\u001B[0m");

        ArrayList<Animal> animales = ecosistema.getAnimales();
        if (!animales.isEmpty()) {
            System.out.println("\n  MONITOR DE FAUNA:");
            for (Animal a : animales) {
                if (a.estaVivo()) {
                    String badge = a.esHerbivoro() ? "\u001B[32m[HERB]\u001B[0m" : "\u001B[31m[CARN]\u001B[0m";
                    System.out.printf("  %s %-12s - Vida: %3d%% | Ayuno: %-2s hr | Estado: %s\n", 
                        badge, 
                        a.getNombre(), 
                        a.getVida(), 
                        a.getHorasSinComer(), 
                        a.getEstadoEspecial());
                }
            }
        }
    }

    static int mostrarMenuTurno() {
        System.out.println("\n\u001B[36m PANEL DE DIOS ──────────────────────┐\u001B[0m");
        System.out.println("  [1] Forzar un adelanto temporal manual");
        System.out.println("  [2] Alterar temperatura del bioma");
        System.out.println("  [3] Provocar un cambio de clima");
        System.out.println("  [4] Ver base de datos del mundo");
        System.out.println("  [5] Sembrar flora / Spawnear fauna"); 
        System.out.println("  [6] Colapsar universo simulado");
        System.out.println("\u001B[36m─────────────────────────────────────┘\u001B[0m");
        System.out.print("  Selecciona tu accion: ");
        return leerEntero(1, 6);
    }

    static void cambiarTemperatura() {
        int[] rango = Bioma.rangoTemperatura(ecosistema.getBiomaTipo());
        System.out.println("\n  Temperatura actual del ecosistema: " + ecosistema.getTemperatura() + "C");
        System.out.print("  Ingresa el nuevo valor [" + rango[0] + " a " + rango[1] + "]: ");
        int t = leerEntero(rango[0], rango[1]);
        ecosistema.setTemperatura(t);
        System.out.println("   Ola de aclimatacion completada.");
    }

    static void cambiarClima() {
        System.out.println("\n  MENU DE CONTROL CLIMATICO:");
        System.out.println("  [1] Soleado");
        System.out.println("  [2] Lluvioso");
        System.out.println("  [3] Nevado");
        System.out.println("  [4] Tormenta");
        System.out.print("  Desata un clima: ");
        int op = leerEntero(1, 4);
        String[] climas = {"Soleado", "Lluvioso", "Nevado", "Tormenta"};
        ecosistema.setClima(climas[op - 1]);
        System.out.println("   Modificacion atmosferica enviada.");
    }

    static void mostrarListaDetallada() {
        System.out.println("\n\u001B[32m=====================================================");
        System.out.println("                ESTADISTICAS DE FLORA                ");
        System.out.println("=====================================================\u001B[0m");
        ArrayList<Planta> plantas = ecosistema.getPlantas();
        for (Planta p : plantas) {
            if (p.estaViva()) {
                String ven = p.esVenenosa() ? "VENENOSA" : "Normal";
                System.out.printf("   Nombre: %-12s | Salud: %-3d | Tipo: %s\n", p.getNombre(), p.getSalud(), ven);
            }
        }

        System.out.println("\n\u001B[33m=====================================================");
        System.out.println("                ESTADISTICAS DE FAUNA                ");
        System.out.println("=====================================================\u001B[0m");
        ArrayList<Animal> animales = ecosistema.getAnimales();
        for (Animal a : animales) {
            if (a.estaVivo()) {
                String etapa = a.esAdulto() ? "Adulto" : "Cria";
                String enfermedad = a.estaEnfermo() ? "Si" : "No";
                System.out.printf("   %-10s (%-8s) | Edad: %-3sh | Enfermo: %-5s | %s\n", 
                    a.getNombre(), a.getEspecie(), a.getEdad(), enfermedad, etapa);
            }
        }
        System.out.println("\u001B[33m=====================================================\u001B[0m");
        presionarParaContinuar();
    }

    static void mostrarFinDeJuego(int hora) {
        if (clipMusicaFondo != null) clipMusicaFondo.stop();
        reproducirSonido(SONIDO_MUERTE);

        System.out.println("\n\u001B[31m");
        System.out.println("                       ____ _ _  _ ");
        System.out.println("                       |___ | |\\ | ");
        System.out.println("                       |    | | \\| ");
        System.out.println("   ================================================= ");
        System.out.println("        LOS ANIMALES MURIERON, JUEGO TERMINADO       ");
        System.out.println("   ================================================= \u001B[0m");
        System.out.println("     Tu civilizacion/ecosistema colapso.");
        System.out.println("     El universo resistio un total de: " + hora + " horas.\n");
    }

    static int leerEntero(int min, int max) {
        while (true) {
            try {
                int valor = Integer.parseInt(scanner.next().trim());
                if (valor >= min && valor <= max) return valor;
                System.out.print("  Seleccion invalida. Intenta denuevo (" + min + "-" + max + "): ");
            } catch (NumberFormatException e) {
                System.out.print("  Tipo de dato incorrecto. Ingresa un numero (" + min + "-" + max + "): ");
            }
        }
    }

    static void presionarParaContinuar() {
        System.out.println("\n\u001B[37m  [Presiona ENTER para continuar...]\u001B[0m");
        try {
            System.in.read();
            scanner.nextLine();
        } catch (Exception e) { }
    }
}