package simulador;

public class Bioma {

    public enum BiomaTipo {
        DESIERTO, PRADERA, GLACIAR, SELVA
    }

    public static int[] rangoTemperatura(BiomaTipo bioma) {
        switch (bioma) {
            case DESIERTO: return new int[]{25, 60};
            case PRADERA:  return new int[]{-5, 40};
            case GLACIAR:  return new int[]{-40, 5};
            default:       return new int[]{15, 45};
        }
    }

    public static String[] plantasDeBioma(BiomaTipo bioma) {
        switch (bioma) {
            case DESIERTO:
                return new String[]{
                    "Cactus saguaro",
                    "Cactus barril",
                    "Agave",
                    "Arbusto espinoso",
                    "Hierba de desierto",
                    "Planta de rocio venenosa"
                };
            case PRADERA:
                return new String[]{
                    "Pasto verde",
                    "Hierba alta",
                    "Gramilla",
                    "Trebol",
                    "Girasol de pradera",
                    "Arbusto silvestre venenoso"
                };
            case GLACIAR:
                return new String[]{
                    "Musgo artico",
                    "Liquen glaciar",
                    "Hierba de nieve",
                    "Alga de hielo",
                    "Helecho polar",
                    "Hongo glaciar venenoso"
                };
            default:
                return new String[]{
                    "Helecho gigante",
                    "Bromelia",
                    "Palmera tropical",
                    "Liana",
                    "Orquidea",
                    "Planta carnivora venenosa",
                    "Cacao silvestre"
                };
        }
    }

    public static boolean esPlantaVenenosa(String nombrePlanta) {
        return nombrePlanta.toLowerCase().contains("venenosa") ||
               nombrePlanta.toLowerCase().contains("venenoso");
    }

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
            default:
                return new String[]{
                    "Gorila",
                    "Tapir",
                    "Perezoso",
                    "Loro",
                    "Mono arana",
                    "Venado de selva"
                };
        }
    }

    public static String[] carnivoresDeBioma(BiomaTipo bioma) {
        switch (bioma) {
            case DESIERTO:
                return new String[]{
                    "Serpiente cascabel",
                    "Escorpion gigante",
                    "Chacal del desierto",
                    "Halcon del desierto"
                };
            case PRADERA:
                return new String[]{
                    "Leon",
                    "Lobo de pradera",
                    "Guepardo",
                    "Hiena",
                    "Aguila real"
                };
            case GLACIAR:
                return new String[]{
                    "Oso polar",
                    "Lobo artico",
                    "Zorro artico",
                    "Orca"
                };
            default:
                return new String[]{
                    "Jaguar",
                    "Anaconda",
                    "Cocodrilo",
                    "Tigre de selva",
                    "Aguila harpia"
                };
        }
    }

    public static String descripcion(BiomaTipo bioma) {
        switch (bioma) {
            case DESIERTO: return "Vasto desierto arido con sol abrasador y pocas lluvias.";
            case PRADERA:  return "Praderas abiertas con hierba alta y cielos despejados.";
            case GLACIAR:  return "Glaciar polar con temperaturas extremas y hielo eterno.";
            default:       return "Selva densa y humeda llena de vida y peligros.";
        }
    }
}