package simulador;


	import java.util.ArrayList;
	import java.util.Random;


	class Animal {

	    // --- ATRIBUTOS BÁSICOS ---
	    private String nombre;
	    private String especie;
	    private boolean esHerbivoro;
	    private int vida;               // 0 - 100
	    private int edad;               // en horas
	    private int horasSinComer;
	    private int horasSinAgua;
	    private boolean esAdulto;
	    private boolean estaVivo;
	    private boolean estaEnfermo;
	    private int horasEnfermo;
	    private int horasParaReproducirse; // cuándo se reproducirá aleatoriamente
	    private Bioma.BiomaTipo bioma;

	    // Tiempos de reproducción por especie (en horas)
	    private static final Random random = new Random();

	    // --- CONSTRUCTOR ---
	    public Animal(String nombre, String especie, boolean esHerbivoro, Bioma.BiomaTipo bioma) {
	        this.nombre = nombre;
	        this.especie = especie;
	        this.esHerbivoro = esHerbivoro;
	        this.bioma = bioma;
	        this.vida = 100;
	        this.edad = 0;
	        this.horasSinComer = 0;
	        this.horasSinAgua = 0;
	        this.estaVivo = true;
	        this.estaEnfermo = false;
	        this.horasEnfermo = 0;
	        this.esAdulto = true; // Los que el usuario agrega son adultos
	        this.horasParaReproducirse = calcularTiempoReproduccion();
	    }

	    // Constructor para crías
	    public Animal(String especie, boolean esHerbivoro, Bioma.BiomaTipo bioma, boolean esCria) {
	        this.nombre = especie; // nombre por defecto
	        this.especie = especie;
	        this.esHerbivoro = esHerbivoro;
	        this.bioma = bioma;
	        this.vida = 100;
	        this.edad = 0;
	        this.horasSinComer = 0;
	        this.horasSinAgua = 0;
	        this.estaVivo = true;
	        this.estaEnfermo = false;
	        this.horasEnfermo = 0;
	        this.esAdulto = false; // Las crías comienzan como bebés
	        this.horasParaReproducirse = calcularTiempoReproduccion() + 5; // Crías tardan 5h extra
	    }

	    public void recibirDanioAtaque(int danio, String causa) {
	        vida -= danio;
	        if (vida < 0) vida = 0;
	        if (vida == 0 && estaVivo) {
	            estaVivo = false;
	            System.out.println("  >> " + nombre + " ha muerto por " + causa + ".");
	        }
	    }
	    public void avanzarHora() {
	        if (!estaVivo) return;

	        edad++;
	        horasSinComer++;
	        horasSinAgua++;

	        // Las crías tardan 5h en ser adultas
	        if (!esAdulto && edad >= 5) {
	            esAdulto = true;
	            System.out.println("  🐣 ¡" + nombre + " (" + especie + ") ha alcanzado la fase adulta!");
	        }

	        // Penalización por hambre (cada 2h)
	        if (horasSinComer % 2 == 0 && horasSinComer > 0) {
	            int perdida = esHerbivoro ? 10 : 20;
	            perderVida(perdida, "hambre");
	        }

	        // Penalización por sed (cada 2h)
	        if (horasSinAgua % 2 == 0 && horasSinAgua > 0) {
	            perderVida(10, "sed");
	        }

	        // Enfermedad activa
	        if (estaEnfermo) {
	            horasEnfermo++;
	            if (horasEnfermo == 1) {
	                // Solo resta vida una vez al contagiarse
	            }
	            // Se recupera después de 6 horas
	            if (horasEnfermo >= 6) {
	                estaEnfermo = false;
	                horasEnfermo = 0;
	                System.out.println("  💊 " + nombre + " se ha recuperado de la enfermedad.");
	            }
	        }

	        verificarMuerte();
	    }

	    /**
	     * El animal come (herbívoro come planta, carnívoro come animal).
	     */
	    public void comer() {
	        horasSinComer = 0;
	        vida = Math.min(100, vida + 10);
	    }

	    /**
	     * El animal bebe agua.
	     */
	    public void beber() {
	        horasSinAgua = 0;
	    }

	    /**
	     * Contagia una enfermedad al animal (resta 30% de vida).
	     */
	    public void contraerEnfermedad() {
	        if (!estaEnfermo) {
	            estaEnfermo = true;
	            horasEnfermo = 0;
	            perderVida(30, "enfermedad");
	            System.out.println("  🤒 ¡" + nombre + " ha contraído una enfermedad!");
	        }
	    }

	    /**
	     * El animal come una planta venenosa (resta 50% de vida).
	     */
	    public void comerPlantaVenenosa() {
	        perderVida(50, "planta venenosa");
	        System.out.println("  ☠️  ¡" + nombre + " comió una planta venenosa!");
	    }

	    /**
	     * El animal se reproduce y devuelve una cría.
	     * Solo adultos pueden reproducirse.
	     */
	    public Animal reproducirse() {
	        if (!esAdulto || !estaVivo) return null;
	        horasParaReproducirse = calcularTiempoReproduccion();
	        Animal cria = new Animal(especie, esHerbivoro, bioma, true);
	        System.out.println("  🍼 ¡" + nombre + " tuvo una cría de " + especie + "!");
	        return cria;
	    }

	    /**
	     * Devuelve si este animal está listo para reproducirse en este turno.
	     */
	    public boolean listaParaReproducirse(int hora) {
	        return esAdulto && estaVivo && (hora % horasParaReproducirse == 0);
	    }

	    // --- UTILIDADES PRIVADAS ---

	    private void perderVida(int cantidad, String causa) {
	        vida -= cantidad;
	        if (vida < 0) vida = 0;
	        if (vida == 0) {
	            estaVivo = false;
	            System.out.println("  💀 " + nombre + " (" + especie + ") ha muerto por " + causa + ".");
	        }
	    }

	    private void verificarMuerte() {
	        if (vida <= 0) {
	            estaVivo = false;
	        }
	    }

	    private int calcularTiempoReproduccion() {
	        // Cada especie tiene un tiempo distinto (entre 8 y 24 horas)
	        return 8 + random.nextInt(17);
	    }

	    // --- GETTERS Y SETTERS ---

	    public String getNombre() { return nombre; }
	    public String getEspecie() { return especie; }
	    public boolean esHerbivoro() { return esHerbivoro; }
	    public int getVida() { return vida; }
	    public int getEdad() { return edad; }
	    public int getHorasSinComer() { return horasSinComer; }
	    public boolean estaVivo() { return estaVivo; }
	    public boolean estaEnfermo() { return estaEnfermo; }
	    public boolean esAdulto() { return esAdulto; }
	    public Bioma.BiomaTipo getBioma() { return bioma; }

	    public String getEstadoEspecial() {
	        if (estaEnfermo) return "🤒 Enfermo";
	        if (!esAdulto) return "🐣 Cría";
	        if (horasSinComer >= 4) return "😫 Hambriento";
	        return "✅ Normal";
	    }
	}


	/**
	 * ============================================================
	 *  CLASE: Ecosistema.java
	 *  RESPONSABLE: Integrante 2
	 *  DESCRIPCION: Gestiona todos los animales y plantas del mundo.
	 *               Controla el ciclo de horas, interacciones entre
	 *               especies, alimentación, reproducción y eventos.
	 * ============================================================
	 */
	class Ecosistema {

	    private ArrayList<Animal> animales;
	    private ArrayList<Planta> plantas;
	    private Bioma.BiomaTipo biomaTipo;
	    private int temperatura;
	    private String clima;
	    private static final Random random = new Random();
	    private boolean advertenciaEmitida = false;

	    // --- CONSTRUCTOR ---
	    public Ecosistema(Bioma.BiomaTipo bioma, int temperatura) {
	        this.animales = new ArrayList<>();
	        this.plantas = new ArrayList<>();
	        this.biomaTipo = bioma;
	        this.temperatura = temperatura;
	        this.clima = "Soleado";
	    }

	    // -------------------------------------------------------
	    // AVANZAR UNA HORA (núcleo de la simulación)
	    // -------------------------------------------------------
	    public void avanzarHora(int hora) {
	        System.out.println("\n--- [HORA " + hora + "] " + getDescripcionCiclo(hora) + " ---");

	        // 1. Avanzar estado de todas las plantas
	        for (Planta p : plantas) {
	            p.avanzarHora(temperatura, clima);
	        }

	        // 2. Herbívoros comen plantas
	        procesarAlimentacionHerbivoros();

	        // 3. Carnívoros cazan herbívoros
	        procesarAlimentacionCarnivoros();

	        // 4. Todos los animales beben (si hay agua disponible según clima)
	        procesarHidratacion();

	        // 5. Avanzar estado de todos los animales
	        ArrayList<Animal> nuevasCrias = new ArrayList<>();
	        for (Animal a : animales) {
	            if (a.estaVivo()) {
	                a.avanzarHora();

	                // Reproducción aleatoria
	                if (a.listaParaReproducirse(hora)) {
	                    Animal cria = a.reproducirse();
	                    if (cria != null) nuevasCrias.add(cria);
	                }
	            }
	        }
	        animales.addAll(nuevasCrias);

	        // 6. Evento aleatorio: enfermedad
	        if (random.nextInt(100) < 8) { // 8% de probabilidad por hora
	            contagiarEnfermedadAleatoria();
	        }

	        // 7. Eliminar animales muertos de la lista activa
	        animales.removeIf(a -> !a.estaVivo());
	        plantas.removeIf(p -> !p.estaViva());

	        advertenciaEmitida = false; // Resetear para que se pueda volver a emitir
	    }

	    // -------------------------------------------------------
	    // ALIMENTACIÓN HERBÍVOROS
	    // -------------------------------------------------------
	    private void procesarAlimentacionHerbivoros() {
	        for (Animal a : animales) {
	            if (!a.estaVivo() || !a.esHerbivoro()) continue;

	            // Buscar una planta viva para comer
	            boolean comio = false;
	            for (Planta p : plantas) {
	                if (p.estaViva() && !p.esVenenosa()) {
	                    a.comer();
	                    p.serComida();
	                    comio = true;
	                    break;
	                }
	            }

	            // Si no encontró planta sana, puede comer venenosa por accidente (15% de chance)
	            if (!comio) {
	                for (Planta p : plantas) {
	                    if (p.estaViva() && p.esVenenosa() && random.nextInt(100) < 15) {
	                        a.comerPlantaVenenosa();
	                        p.serComida();
	                        break;
	                    }
	                }
	            }
	        }
	    }

	    // -------------------------------------------------------
	    // ALIMENTACIÓN CARNÍVOROS
	    // -------------------------------------------------------
	    private void procesarAlimentacionCarnivoros() {
	        for (Animal carnivoro : animales) {
	            if (!carnivoro.estaVivo() || carnivoro.esHerbivoro()) continue;

	            // Buscar herbívoro vivo para cazar
	            for (Animal presa : animales) {
	                if (presa.estaVivo() && presa.esHerbivoro()) {
	                    carnivoro.comer();
	                    // La presa pierde el 40% de vida al ser atacada
	                    // (no muere siempre para mantener equilibrio)
	                    if (random.nextInt(100) < 40) {
	                        presa.comerPlantaVenenosa(); // Reutilizamos para simular daño de ataque
	                        System.out.println("  🦁 " + carnivoro.getNombre() + " cazó a " + presa.getNombre() + "!");
	                    }
	                    break;
	                }
	            }
	        }
	    }

	    // -------------------------------------------------------
	    // HIDRATACIÓN
	    // -------------------------------------------------------
	    private void procesarHidratacion() {
	        boolean hayAgua = clima.equals("Lluvioso") || clima.equals("Nevado") ||
	                          biomaTipo == Bioma.BiomaTipo.SELVA || biomaTipo == Bioma.BiomaTipo.GLACIAR;

	        for (Animal a : animales) {
	            if (a.estaVivo()) {
	                if (hayAgua || random.nextInt(100) < 60) {
	                    a.beber();
	                }
	            }
	        }
	    }

	    // -------------------------------------------------------
	    // ENFERMEDAD ALEATORIA
	    // -------------------------------------------------------
	    private void contagiarEnfermedadAleatoria() {
	        ArrayList<Animal> vivosAdultos = new ArrayList<>();
	        for (Animal a : animales) {
	            if (a.estaVivo() && a.esAdulto()) vivosAdultos.add(a);
	        }
	        if (!vivosAdultos.isEmpty()) {
	            Animal victima = vivosAdultos.get(random.nextInt(vivosAdultos.size()));
	            victima.contraerEnfermedad();
	        }
	    }

	    // -------------------------------------------------------
	    // GESTIÓN DE COLECCIONES
	    // -------------------------------------------------------
	    public void agregarAnimal(Animal a) { animales.add(a); }
	    public void agregarPlanta(Planta p) { plantas.add(p); }

	    public ArrayList<Animal> getAnimales() { return animales; }
	    public ArrayList<Planta> getPlantas() { return plantas; }

	    public int contarAnimales() {
	        int c = 0;
	        for (Animal a : animales) if (a.estaVivo()) c++;
	        return c;
	    }

	    public int contarPlantas() {
	        int c = 0;
	        for (Planta p : plantas) if (p.estaViva()) c++;
	        return c;
	    }

	    public boolean hayAnimalesVivos() { return contarAnimales() > 0; }

	    /**
	     * Devuelve true si quedan menos del 5% de animales respecto al inicio.
	     * Usamos un umbral fijo de 2 para simplificar.
	     */
	    public boolean quedanPocosAnimales() {
	        return contarAnimales() <= 2 && !advertenciaEmitida;
	    }

	    // -------------------------------------------------------
	    // CICLO DÍA / NOCHE
	    // -------------------------------------------------------
	    public String getDescripcionCiclo(int hora) {
	        int horaDelDia = hora % 24;
	        if (horaDelDia >= 6 && horaDelDia < 12)  return "🌅 Mañana";
	        if (horaDelDia >= 12 && horaDelDia < 18) return "☀️  Tarde";
	        if (horaDelDia >= 18 && horaDelDia < 22) return "🌇 Atardecer";
	        return "🌙 Noche";
	    }

	    // -------------------------------------------------------
	    // GETTERS DEL ECOSISTEMA
	    // -------------------------------------------------------
	    public Bioma.BiomaTipo getBiomaTipo() { return biomaTipo; }
	    public String getBioma() { return biomaTipo.toString(); }
	    public int getTemperatura() { return temperatura; }
	    public void setTemperatura(int t) { this.temperatura = t; }
	    public String getClima() { return clima; }
	    public void setClima(String c) { this.clima = c; }

		public boolean huboCriaQueCrecio() {
			// TODO Auto-generated method stub
			return false;
		}
	}

