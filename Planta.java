
class Planta {

   
    public enum TipoPlanta {
        FRUTAL, ARBUSTO, PASTO, CACTUS, HELECHO, ALGA, HONGO
    }

 
    private String nombre;
    private TipoPlanta tipo;
    private boolean esVenenosa;
    private boolean estaViva;
    private int salud;          // 0 - 100
    private int horasDeVida;
    private Bioma.BiomaTipo bioma;

   
    public Planta(String nombre, Bioma.BiomaTipo bioma, boolean esVenenosa) {
        this.nombre = nombre;
        this.bioma = bioma;
        this.esVenenosa = esVenenosa;
        this.estaViva = true;
        this.salud = 100;
        this.horasDeVida = 0;
        this.tipo = asignarTipo(nombre);
    }

    
     
    public void avanzarHora(int temperatura, String clima) {
        if (!estaViva) return;
        horasDeVida++;

        
        boolean condicionExtrema = false;

        switch (bioma) {
            case DESIERTO:
                if (temperatura > 50 || clima.equals("Tormenta")) condicionExtrema = true;
                break;
            case GLACIAR:
                if (temperatura > 0) condicionExtrema = true;
                break;
            case SELVA:
                if (temperatura < 10 || temperatura > 45) condicionExtrema = true;
                break;
            case PRADERA:
                if (temperatura < -5 || temperatura > 45) condicionExtrema = true;
                break;
        }

        if (condicionExtrema) {
            salud -= 5;
            if (salud <= 0) {
                salud = 0;
                estaViva = false;
                System.out.println("  🍂 La planta " + nombre + " se ha marchitado por condiciones extremas.");
            }
        } else {
            
            if (clima.equals("Lluvioso") && salud < 100) {
                salud = Math.min(100, salud + 3);
            }
        }
    }

   
    public void serComida() {
        salud -= 30;
        if (salud <= 0) {
            salud = 0;
            estaViva = false;
        }
    }

    // -------------------------------------------------------
    // UTILIDADES PRIVADAS
    // -------------------------------------------------------
    private TipoPlanta asignarTipo(String nombre) {
        nombre = nombre.toLowerCase();
        if (nombre.contains("cactus")) return TipoPlanta.CACTUS;
        if (nombre.contains("helecho")) return TipoPlanta.HELECHO;
        if (nombre.contains("alga") || nombre.contains("musgo")) return TipoPlanta.ALGA;
        if (nombre.contains("hongo")) return TipoPlanta.HONGO;
        if (nombre.contains("pasto") || nombre.contains("hierba") || nombre.contains("gramilla")) return TipoPlanta.PASTO;
        if (nombre.contains("arbusto") || nombre.contains("arbusto espinoso")) return TipoPlanta.ARBUSTO;
        return TipoPlanta.FRUTAL;
    }

    // --- GETTERS ---
    public String getNombre() { return nombre; }
    public TipoPlanta getTipo() { return tipo; }
    public boolean esVenenosa() { return esVenenosa; }
    public boolean estaViva() { return estaViva; }
    public int getSalud() { return salud; }
    public int getHorasDeVida() { return horasDeVida; }
}


/**
 * ============================================================
 *  CLASE: Bioma.java
 *  RESPONSABLE: Integrante 3
 *  DESCRIPCION: Define los biomas disponibles y sus características:
 *               animales disponibles (herbívoros y carnívoros),
 *               plantas, rango de temperatura y descripciones.
 *               Es una clase de datos/configuración pura.
 * ============================================================
 */
class Bioma {

    // --- TIPOS DE BIOMA ---
    public enum BiomaTipo {
        DESIERTO, PRADERA, GLACIAR, SELVA
    }

    // -------------------------------------------------------
    // RANGO DE TEMPERATURA POR BIOMA
    // -------------------------------------------------------
    public static int[] rangoTemperatura(BiomaTipo bioma) {
        switch (bioma) {
            case DESIERTO: return new int[]{25, 60};
            case PRADERA:  return new int[]{-5, 40};
            case GLACIAR:  return new int[]{-40, 5};
            default:       return new int[]{15, 45}; // SELVA
        }
    }

    // -------------------------------------------------------
    // PLANTAS DE CADA BIOMA
    // -------------------------------------------------------
    public static String[] plantasDeBioma(BiomaTipo bioma) {
        switch (bioma) {
            case DESIERTO:
                return new String[]{
                    "Cactus saguaro",
                    "Cactus barril",
                    "Agave",
                    "Arbusto espinoso",
                    "Hierba de desierto",
                    "Planta de rocío (venenosa)"
                };
            case PRADERA:
                return new String[]{
                    "Pasto verde",
                    "Hierba alta",
                    "Gramilla",
                    "Trébol",
                    "Girasol de pradera",
                    "Arbusto silvestre (venenoso)"
                };
            case GLACIAR:
                return new String[]{
                    "Musgo ártico",
                    "Liquen glaciar",
                    "Hierba de nieve",
                    "Alga de hielo",
                    "Helecho polar",
                    "Hongo glaciar (venenoso)"
                };
            default: // SELVA
                return new String[]{
                    "Helecho gigante",
                    "Bromelia",
                    "Palmera tropical",
                    "Liana",
                    "Orquídea",
                    "Planta carnívora (venenosa)",
                    "Cacao silvestre"
                };
        }
    }

    // -------------------------------------------------------
    // DETERMINAR SI UNA PLANTA ES VENENOSA
    // -------------------------------------------------------
    public static boolean esPlantaVenenosa(String nombrePlanta) {
        return nombrePlanta.toLowerCase().contains("venenosa") ||
               nombrePlanta.toLowerCase().contains("venenoso");
    }

    // -------------------------------------------------------
    // HERBÍVOROS DE CADA BIOMA
    // -------------------------------------------------------
    public static String[] herbivoresDeBioma(BiomaTipo bioma) {
        switch (bioma) {
            case DESIERTO:
                return new String[]{
                    "Camello",
                    "Dromedario",
                    "Liebre del desierto",
                    "Tortuga del desierto",
                    "Iguana del desierto"
                };
            case PRADERA:
                return new String[]{
                    "Ciervo",
                    "Bisonte",
                    "Cebra",
                    "Gacela",
                    "Conejo de pradera",
                    "Caballo salvaje"
                };
            case GLACIAR:
                return new String[]{
                    "Reno",
                    "Foca",
                    "Morsa",
                    "Lemming",
                    "Buey almizclero"
                };
            default: // SELVA
                return new String[]{
                    "Gorila",
                    "Tapir",
                    "Perezoso",
                    "Loro",
                    "Mono araña",
                    "Venado de selva"
                };
        }
    }

    // -------------------------------------------------------
    // CARNÍVOROS DE CADA BIOMA
    // -------------------------------------------------------
    public static String[] carnivoresDeBioma(BiomaTipo bioma) {
        switch (bioma) {
            case DESIERTO:
                return new String[]{
                    "Serpiente cascabel",
                    "Escorpión gigante",
                    "Chacal del desierto",
                    "Halcón del desierto"
                };
            case PRADERA:
                return new String[]{
                    "León",
                    "Lobo de pradera",
                    "Guepardo",
                    "Hiena",
                    "Águila real"
                };
            case GLACIAR:
                return new String[]{
                    "Oso polar",
                    "Lobo ártico",
                    "Zorro ártico",
                    "Orca (zona costera)"
                };
            default: // SELVA
                return new String[]{
                    "Jaguar",
                    "Anaconda",
                    "Cocodrilo",
                    "Tigre de selva",
                    "Águila arpía"
                };
        }
    }


    public static String descripcion(BiomaTipo bioma) {
        switch (bioma) {
            case DESIERTO:
                return "🏜️  Vasto desierto árido con sol abrasador y pocas lluvias.";
            case PRADERA:
                return "🌾 Praderas abiertas con hierba alta y cielos despejados.";
            case GLACIAR:
                return "🧊 Glaciar polar con temperaturas extremas y hielo eterno.";
            default:
                return "🌿 Selva densa y húmeda llena de vida y peligros.";
        }
    }
}