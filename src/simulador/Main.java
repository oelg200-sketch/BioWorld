package simulador;

import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    static Scanner scanner = new Scanner(System.in);
    static Ecosistema ecosistema;
    
    // Instancia global del reproductor para controlar la música del juego
    static Reproductor miRadio = new Reproductor();

    public static void main(String[] args) {
        // 1. Iniciar con la música del menú principal
        miRadio.encenderMusica("Hollow-Knight-OST-Enter-Hallownest_M4A_128K_.wav");
        
        mostrarBienvenida();
        configurarJuego();
        iniciarSimulacion();
    }

    // ------------------------------------------------------
    // BIENVENIDA
    // -------------------------------------------------------
    static void mostrarBienvenida() {
        System.out.println("╔══════════════════════════════════════════════════╗");
        System.out.println("║                                                  ║");
        System.out.println("║         🌍 Bienvenido a  B I O W O R L D        ║");
        System.out.println("║       Simulador de Ecosistemas Interactivo       ║");
        System.out.println("║                                                  ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
        System.out.println();
        System.out.println("En BioWorld podrás crear y gestionar tu propio");
        System.out.println("ecosistema con animales, plantas y biomas únicos.");
        System.out.println();
        presionarParaContinuar();
    }

    // -------------------------------------------------------
    // CONFIGURACION INICIAL DEL JUEGO
    // -------------------------------------------------------
    static void configurarJuego() {
        // 1. Elegir bioma
        Bioma.BiomaTipo bioma = elegirBioma();

        // 2. Elegir temperatura
        int temperatura = elegirTemperatura(bioma);

        // 3. Crear ecosistema
        ecosistema = new Ecosistema(bioma, temperatura);

        // 4. Agregar plantas al ecosistema
        agregarPlantas(bioma);

        // 5. Agregar animales al ecosistema
        agregarAnimales(bioma);

        System.out.println("\n✅ ¡Ecosistema configurado exitosamente!");
        System.out.println("   Bioma: " + bioma);
        System.out.println("   Temperatura: " + temperatura + "°C");
        System.out.println("   Plantas: " + ecosistema.contarPlantas());
        System.out.println("   Animales: " + ecosistema.contarAnimales());
        presionarParaContinuar();
    }

    // -------------------------------------------------------
    // ELEGIR BIOMA
    // -------------------------------------------------------
    static Bioma.BiomaTipo elegirBioma() {
        System.out.println("╔══════════════════════════════════╗");
        System.out.println("║        SELECCIONA UN BIOMA       ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. 🏜️ Desierto                  ║");
        System.out.println("║  2. 🌾 Praderas                  ║");
        System.out.println("║  3. 🧊 Glaciar                   ║");
        System.out.println("║  4. 🌿 Selva                     ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Elige una opción (1-4): ");

        int opcion = leerEntero(1, 4);
        switch (opcion) {
            case 1: return Bioma.BiomaTipo.DESIERTO;
            case 2: return Bioma.BiomaTipo.PRADERA;
            case 3: return Bioma.BiomaTipo.GLACIAR;
            default: return Bioma.BiomaTipo.SELVA;
        }
    }

    // -------------------------------------------------------
    // ELEGIR TEMPERATURA
    // -------------------------------------------------------
    static int elegirTemperatura(Bioma.BiomaTipo bioma) {
        int[] rango = Bioma.rangoTemperatura(bioma);
        System.out.println("\n🌡️ Rango de temperatura para " + bioma + ": " + rango[0] + "°C a " + rango[1] + "°C");
        System.out.print("Ingresa la temperatura inicial: ");
        return leerEntero(rango[0], rango[1]);
    }

    // -------------------------------------------------------
    // AGREGAR PLANTAS
    // -------------------------------------------------------
    static void agregarPlantas(Bioma.BiomaTipo bioma) {
        String[] plantasDisponibles = Bioma.plantasDeBioma(bioma);

        boolean agregarMas = true;
        while (agregarMas) {
            System.out.println("\n╔══════════════════════════════════╗");
            System.out.println("║          PLANTAS DEL BIOMA       ║");
            System.out.println("╚══════════════════════════════════╝");
            for (int i = 0; i < plantasDisponibles.length; i++) {
                System.out.println("  " + (i + 1) + ". " + plantasDisponibles[i]);
            }

            System.out.print("\n¿Qué planta deseas agregar? (1-" + plantasDisponibles.length + "): ");
            int idx = leerEntero(1, plantasDisponibles.length) - 1;
            System.out.print("¿Cuántas unidades de " + plantasDisponibles[idx] + "? (1-20): ");
            int cantidad = leerEntero(1, 20);

            boolean esVenenosa = Bioma.esPlantaVenenosa(plantasDisponibles[idx]);
            for (int i = 0; i < cantidad; i++) {
                ecosistema.agregarPlanta(new Planta(plantasDisponibles[idx], bioma, esVenenosa));
            }
            System.out.println("✅ " + cantidad + " " + plantasDisponibles[idx] + " agregada(s).");

            System.out.print("¿Agregar otra planta? (s/n): ");
            agregarMas = scanner.nextLine().trim().equalsIgnoreCase("s");
        }
    }

    // -------------------------------------------------------
    // AGREGAR ANIMALES
    // -------------------------------------------------------
    static void agregarAnimales(Bioma.BiomaTipo bioma) {
        String[] herbivoros = Bioma.herbivoresDeBioma(bioma);
        String[] carnivoros = Bioma.carnivoresDeBioma(bioma);

        boolean agregarMas = true;
        while (agregarMas) {
            System.out.println("\n╔══════════════════════════════════════╗");
            System.out.println("║          ANIMALES DEL BIOMA          ║");
            System.out.println("╠══════════════════════════════════════╣");
            System.out.println("║  HERBÍVOROS:                         ║");
            for (int i = 0; i < herbivoros.length; i++) {
                System.out.println("║     " + (i + 1) + ". 🌿 " + herbivoros[i]);
            }
            System.out.println("║  CARNÍVOROS:                         ║");
            for (int i = 0; i < carnivoros.length; i++) {
                System.out.println("║     " + (herbivoros.length + i + 1) + ". 🥩 " + carnivoros[i]);
            }
            System.out.println("╚══════════════════════════════════════╝");

            int totalOpciones = herbivoros.length + carnivoros.length;
            System.out.print("\n¿Qué animal deseas agregar? (1-" + totalOpciones + "): ");
            int idx = leerEntero(1, totalOpciones) - 1;

            String especie;
            boolean esHerbivoro;
            if (idx < herbivoros.length) {
                especie = herbivoros[idx];
                esHerbivoro = true;
            } else {
                especie = carnivoros[idx - herbivoros.length];
                esHerbivoro = false;
            }

            System.out.print("¿Cuántos " + especie + "? (1-10): ");
            int cantidad = leerEntero(1, 10);

            for (int i = 0; i < cantidad; i++) {
                System.out.print("¿Deseas ponerle nombre al " + especie + " #" + (i + 1) + "? (s/n): ");
                String nombre;
                if (scanner.nextLine().trim().equalsIgnoreCase("s")) {
                    System.out.print("Nombre: ");
                    nombre = scanner.nextLine().trim();
                } else {
                    nombre = especie;
                }
                ecosistema.agregarAnimal(new Animal(nombre, especie, esHerbivoro, bioma));
            }
            System.out.println("✅ " + cantidad + " " + especie + " agregado(s).");

            System.out.print("¿Agregar otro animal? (s/n): ");
            agregarMas = scanner.nextLine().trim().equalsIgnoreCase("s");
        }
    }

    // -------------------------------------------------------
    // SIMULACION PRINCIPAL
    // -------------------------------------------------------
    static void iniciarSimulacion() {
        System.out.println("\n🚀 ¡La simulación ha comenzado!");
        presionarParaContinuar();

        int hora = 0;

        while (ecosistema.hayAnimalesVivos()) {
            // Verificar advertencia de pocos animales
            if (ecosistema.quedanPocosAnimales()) {
                if (alertaPocosAnimales()) continue;
            }

            // Gestionar el cambio de audio según el ciclo de día o noche actual
            gestionarMusicaAmbiente(hora);

            // Mostrar estado del mundo
            mostrarEstadoMundo(hora);

            // Menú de turno
            mostrarMenuTurno(hora);

            // Avanzar horas
            int horasASaltar = preguntarHorasASaltar();
            for (int h = 0; h < horasASaltar; h++) {
                hora++;
                ecosistema.avanzarHora(hora);
                if (!ecosistema.hayAnimalesVivos()) break;
            }

            if (!ecosistema.hayAnimalesVivos()) break;
        }

        // Apagar la radio al terminar la simulación
        miRadio.detenerMusica();
        mostrarFinDeJuego(hora);
    }

    // -------------------------------------------------------
    // CONTROL DE MÚSICA DÍA / NOCHE
    // -------------------------------------------------------
    static void gestionarMusicaAmbiente(int hora) {
        String descripcionCiclo = ecosistema.getDescripcionCiclo(hora).toLowerCase();
        
        // Evaluamos el string que devuelve el ecosistema para saber si es de noche o de día
        if (descripcionCiclo.contains("noche")) {
            miRadio.encenderMusica("Hollow-Knight-OST-Dirtmouth_M4A_128K_.wav");
        } else {
            miRadio.encenderMusica("Hollow-Knight-OST-Greenpath_M4A_128K_.wav");
        }
    }

    // -------------------------------------------------------
    // MOSTRAR ESTADO DEL MUNDO
    // -------------------------------------------------------
    static void mostrarEstadoMundo(int hora) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.printf ("║  %-48s║%n", ecosistema.getDescripcionCiclo(hora));
        System.out.printf ("║  Hora: %-42s║%n", hora + "h  |  " + ecosistema.getBiomaTipo() + "  |  " + ecosistema.getTemperatura() + "°C");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.printf ("║  🌿 Plantas vivas: %-30s║%n", ecosistema.contarPlantas());
        System.out.printf ("║  🐾 Animales vivos: %-29s║%n", ecosistema.contarAnimales());
        System.out.println("╚══════════════════════════════════════════════════╝");

        // Mostrar animales
        ArrayList<Animal> animales = ecosistema.getAnimales();
        if (!animales.isEmpty()) {
            System.out.println("\n--- ESTADO DE ANIMALES ---");
            for (Animal a : animales) {
                if (a.estaVivo()) {
                    System.out.printf("  [%s] %s | Vida: %d%% | Hambre: %dh | %s%n",
                        a.esHerbivoro() ? "🌿" : "🥩",
                        a.getNombre(),
                        a.getVida(),
                        a.getHorasSinComer(),
                        a.getEstadoEspecial()
                    );
                }
            }
        }
    }

    // -------------------------------------------------------
    // MENÚ DE TURNO
    // -------------------------------------------------------
    static void mostrarMenuTurno(int hora) {
        System.out.println("\n╔══════════════════════════════════╗");
        System.out.println("║          MENÚ DE CONTROL         ║");
        System.out.println("╠══════════════════════════════════╣");
        System.out.println("║  1. ⏭️  Avanzar horas              ║");
        System.out.println("║  2. 🌡️  Cambiar temperatura       ║");
        System.out.println("║  3. ⛅  Cambiar clima             ║");
        System.out.println("║  4. 📋  Ver lista detallada       ║");
        System.out.println("║  5. 🚪 Salir del juego            ║");
        System.out.println("╚══════════════════════════════════╝");
        System.out.print("Opción: ");

        int op = leerEntero(1, 5);
        switch (op) {
            case 2:
                cambiarTemperatura();
                break;
            case 3:
                cambiarClima();
                break;
            case 4:
                mostrarListaDetallada();
                break;
            case 5:
                System.out.println("👋 Saliendo de BioWorld... ¡Hasta pronto!");
                miRadio.detenerMusica();
                System.exit(0);
                break;
            default:
                break; 
        }
    }

    // -------------------------------------------------------
    // PREGUNTAR CUÁNTAS HORAS SALTAR
    // -------------------------------------------------------
    static int preguntarHorasASaltar() {
        System.out.println("\n¿Deseas saltar horas?");
        System.out.println("  1. Sí, elegir cuántas");
        System.out.println("  2. No, avanzar 1 hora (1 minuto real)");
        System.out.print("Opción: ");
        int op = leerEntero(1, 2);
        if (op == 1) {
            System.out.print("¿Cuántas horas deseas saltar? (1-24): ");
            return leerEntero(1, 24);
        } else {
            System.out.println("⏳ Avanzando 1 hora...");
            try { Thread.sleep(500); } catch (InterruptedException e) { /* ignorar */ }
            return 1;
        }
    }

    // -------------------------------------------------------
    // CAMBIAR TEMPERATURA
    // -------------------------------------------------------
    static void cambiarTemperatura() {
        int[] rango = Bioma.rangoTemperatura(ecosistema.getBiomaTipo());
        System.out.println("🌡️ Temperatura actual: " + ecosistema.getTemperatura() + "°C");
        System.out.print("Nueva temperatura (" + rango[0] + " a " + rango[1] + "°C): ");
        int temp = leerEntero(rango[0], rango[1]);
        ecosistema.setTemperatura(temp);
        System.out.println("✅ Temperatura cambiada a " + temp + "°C");
    }

    // -------------------------------------------------------
    // CAMBIAR CLIMA
    // -------------------------------------------------------
    static void cambiarClima() {
        System.out.println("⛅ Selecciona el clima:");
        System.out.println("  1. ☀️  Soleado");
        System.out.println("  2. 🌧️  Lluvioso");
        System.out.println("  3. ❄️  Nevado");
        System.out.println("  4. 🌪️  Tormenta");
        System.out.print("Opción: ");
        int op = leerEntero(1, 4);
        String[] climas = {"Soleado", "Lluvioso", "Nevado", "Tormenta"};
        ecosistema.setClima(climas[op - 1]);
        System.out.println("✅ Clima cambiado a: " + climas[op - 1]);
    }

    // -------------------------------------------------------
    // VER LISTA DETALLADA
    // -------------------------------------------------------
    static void mostrarListaDetallada() {
        System.out.println("\n=== LISTA DETALLADA DE PLANTAS ===");
        for (Planta p : ecosistema.getPlantas()) {
            if (p.estaViva()) {
                System.out.printf("  🌱 %s | %s | Venenosa: %s%n",
                    p.getNombre(), p.getTipo(), p.esVenenosa() ? "⚠️ Sí" : "No");
            }
        }

        System.out.println("\n=== LISTA DETALLADA DE ANIMALES ===");
        for (Animal a : ecosistema.getAnimales()) {
            if (a.estaVivo()) {
                System.out.printf("  %s %s (%s) | Vida: %d%% | Edad: %dh | Hambre: %dh | Enfermo: %s%n",
                    a.esHerbivoro() ? "🌿" : "🥩",
                    a.getNombre(), a.getEspecie(),
                    a.getVida(), a.getEdad(),
                    a.getHorasSinComer(),
                    a.estaEnfermo() ? "🤒 Sí" : "No"
                );
            }
        }
        presionarParaContinuar();
    }

    // -------------------------------------------------------
    // ALERTA DE POCOS ANIMALES
    // -------------------------------------------------------
    static boolean alertaPocosAnimales() {
        System.out.println("\n⚠️ ¡ADVERTENCIA! Quedan muy pocos animales en tu ecosistema.");
        System.out.println("   ¿Deseas agregar más animales?");
        System.out.println("  1. Sí");
        System.out.println("  2. No, continuar");
        System.out.print("Opción: ");
        int op = leerEntero(1, 2);
        if (op == 1) {
            agregarAnimales(ecosistema.getBiomaTipo());
            return true;
        }
        return false;
    }

    // -------------------------------------------------------
    // FIN DEL JUEGO
    // -------------------------------------------------------
    static void mostrarFinDeJuego(int hora) {
        System.out.println("\n╔══════════════════════════════════════════════════╗");
        System.out.println("║            💀 FIN DE LA SIMULACIÓN               ║");
        System.out.println("╠══════════════════════════════════════════════════╣");
        System.out.println("║  Todos los animales han muerto.                  ║");
        System.out.printf ("║  El ecosistema duró: %-28s║%n", hora + " horas");
        System.out.println("║  Gracias por jugar BioWorld 🌍                   ║");
        System.out.println("╚══════════════════════════════════════════════════╝");
    }

    // -------------------------------------------------------
    // UTILIDADES DE ENTRADA
    // -------------------------------------------------------
    static int leerEntero(int min, int max) {
        while (true) {
            try {
                String linea = scanner.nextLine().trim();
                int valor = Integer.parseInt(linea);
                if (valor >= min && valor <= max) return valor;
                System.out.print("Por favor ingresa un número entre " + min + " y " + max + ": ");
            } catch (NumberFormatException e) {
                System.out.print("Entrada inválida. Ingresa un número entre " + min + " y " + max + ": ");
            }
        }
    }

    static void presionarParaContinuar() {
        System.out.println("\nPresiona ENTER para continuar...");
        try {
            scanner.nextLine();
        } catch (Exception e) { /* ignorar */ }
    }
}