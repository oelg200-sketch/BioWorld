

	import java.util.ArrayList;
	import java.util.Random;

	public class Ecosistema {

	    private ArrayList<Animal> animales;
	    private ArrayList<Planta> plantas;
	    private Bioma.BiomaTipo biomaTipo;
	    private int temperatura;
	    private String clima;

	    private static final Random random = new Random();

	    public Ecosistema(Bioma.BiomaTipo bioma, int temperatura) {
	        this.animales = new ArrayList<Animal>();
	        this.plantas  = new ArrayList<Planta>();
	        this.biomaTipo = bioma;
	        this.temperatura = temperatura;
	        this.clima = "Soleado";
	    }

	    // -------------------------------------------------------
	    // AVANZAR UNA HORA
	    // -------------------------------------------------------
	    public void avanzarHora(int hora) {
	        System.out.println("\n--- [HORA " + hora + "] " + getDescripcionCiclo(hora) + " | " + biomaTipo + " | " + temperatura + "C | " + clima + " ---");

	        // 1. Avanzar plantas
	        for (int i = 0; i < plantas.size(); i++) {
	            plantas.get(i).avanzarHora(temperatura, clima);
	        }

	        // 2. Herbivoros comen plantas
	        procesarAlimentacionHerbivoros();

	        // 3. Carnivoros cazan herbivoros
	        procesarAlimentacionCarnivoros();

	        // 4. Hidratacion
	        procesarHidratacion();

	        // 5. Avanzar animales y reproduccion
	        ArrayList<Animal> nuevasCrias = new ArrayList<Animal>();
	        for (int i = 0; i < animales.size(); i++) {
	            Animal a = animales.get(i);
	            if (a.estaVivo()) {
	                a.avanzarHora();
	                if (a.listaParaReproducirse(hora)) {
	                    Animal cria = a.reproducirse();
	                    if (cria != null) nuevasCrias.add(cria);
	                }
	            }
	        }
	        animales.addAll(nuevasCrias);

	        // 6. Enfermedad aleatoria (8% por hora)
	        if (random.nextInt(100) < 8) {
	            contagiarEnfermedadAleatoria();
	        }

	        // 7. Limpiar muertos y plantas muertas
	        ArrayList<Animal> vivos = new ArrayList<Animal>();
	        for (int i = 0; i < animales.size(); i++) {
	            if (animales.get(i).estaVivo()) vivos.add(animales.get(i));
	        }
	        animales = vivos;

	        ArrayList<Planta> vivasP = new ArrayList<Planta>();
	        for (int i = 0; i < plantas.size(); i++) {
	            if (plantas.get(i).estaViva()) vivasP.add(plantas.get(i));
	        }
	        plantas = vivasP;
	    }

	    // -------------------------------------------------------
	    // ALIMENTACION HERBIVOROS
	    // -------------------------------------------------------
	    private void procesarAlimentacionHerbivoros() {
	        for (int i = 0; i < animales.size(); i++) {
	            Animal a = animales.get(i);
	            if (!a.estaVivo() || !a.esHerbivoro()) continue;

	            boolean comio = false;
	            for (int j = 0; j < plantas.size(); j++) {
	                Planta p = plantas.get(j);
	                if (p.estaViva() && !p.esVenenosa()) {
	                    a.comer();
	                    p.serComida();
	                    comio = true;
	                    break;
	                }
	            }

	            // Si no encontro planta sana, puede comer venenosa por accidente (15%)
	            if (!comio) {
	                for (int j = 0; j < plantas.size(); j++) {
	                    Planta p = plantas.get(j);
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
	    // ALIMENTACION CARNIVOROS
	    // -------------------------------------------------------
	    private void procesarAlimentacionCarnivoros() {
	        for (int i = 0; i < animales.size(); i++) {
	            Animal carnivoro = animales.get(i);
	            if (!carnivoro.estaVivo() || carnivoro.esHerbivoro()) continue;

	            for (int j = 0; j < animales.size(); j++) {
	                Animal presa = animales.get(j);
	                if (presa.estaVivo() && presa.esHerbivoro()) {
	                    carnivoro.comer();
	                    System.out.println("  >> " + carnivoro.getNombre() + " cazo a " + presa.getNombre() + "!");
	                    // 40% de chance de matar a la presa
	                    if (random.nextInt(100) < 40) {
	                        presa.recibirDanioAtaque(60, "ataque de " + carnivoro.getNombre());
	                    }
	                    break;
	                }
	            }
	        }
	    }

	    // -------------------------------------------------------
	    // HIDRATACION
	    // -------------------------------------------------------
	    private void procesarHidratacion() {
	        boolean hayAgua = clima.equals("Lluvioso") || clima.equals("Nevado")
	                       || biomaTipo == Bioma.BiomaTipo.SELVA
	                       || biomaTipo == Bioma.BiomaTipo.GLACIAR;

	        for (int i = 0; i < animales.size(); i++) {
	            Animal a = animales.get(i);
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
	        ArrayList<Animal> candidatos = new ArrayList<Animal>();
	        for (int i = 0; i < animales.size(); i++) {
	            Animal a = animales.get(i);
	            if (a.estaVivo() && a.esAdulto()) candidatos.add(a);
	        }
	        if (!candidatos.isEmpty()) {
	            Animal victima = candidatos.get(random.nextInt(candidatos.size()));
	            victima.contraerEnfermedad();
	        }
	    }

	    // -------------------------------------------------------
	    // CICLO DIA / NOCHE
	    // -------------------------------------------------------
	    public String getDescripcionCiclo(int hora) {
	        int h = hora % 24;
	        if (h >= 6  && h < 12) return "Manana";
	        if (h >= 12 && h < 18) return "Tarde";
	        if (h >= 18 && h < 22) return "Atardecer";
	        return "Noche";
	    }

	    // -------------------------------------------------------
	    // CONTADORES
	    // -------------------------------------------------------
	    public int contarAnimales() {
	        int c = 0;
	        for (int i = 0; i < animales.size(); i++) {
	            if (animales.get(i).estaVivo()) c++;
	        }
	        return c;
	    }

	    public int contarPlantas() {
	        int c = 0;
	        for (int i = 0; i < plantas.size(); i++) {
	            if (plantas.get(i).estaViva()) c++;
	        }
	        return c;
	    }

	    public boolean hayAnimalesVivos()    { return contarAnimales() > 0; }
	    public boolean quedanPocosAnimales() { return contarAnimales() > 0 && contarAnimales() <= 2; }

	    // -------------------------------------------------------
	    // AGREGAR
	    // -------------------------------------------------------
	    public void agregarAnimal(Animal a) { animales.add(a); }
	    public void agregarPlanta(Planta p) { plantas.add(p); }

	    // -------------------------------------------------------
	    // GETTERS / SETTERS
	    // -------------------------------------------------------
	    public ArrayList<Animal> getAnimales()    { return animales; }
	    public ArrayList<Planta>  getPlantas()    { return plantas; }
	    public Bioma.BiomaTipo    getBiomaTipo()  { return biomaTipo; }
	    public String             getBioma()      { return biomaTipo.toString(); }
	    public int                getTemperatura(){ return temperatura; }
	    public String             getClima()      { return clima; }
	    public void setTemperatura(int t)         { this.temperatura = t; }
	    public void setClima(String c)            { this.clima = c; }
	}
}
