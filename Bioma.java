public class Bioma {

    public enum BiomaTipo {
        DESIERTO, PRADERA, GLACIAR, SELVA
    }

    public static int[] rangoTemperatura(BiomaTipo bioma) {
        if (bioma == BiomaTipo.DESIERTO) return new int[]{25, 60};
        if (bioma == BiomaTipo.PRADERA)  return new int[]{-5, 40};
        if (bioma == BiomaTipo.GLACIAR)  return new int[]{-40, 5};
        return new int[]{15, 45}; 
    }

    public static String[] plantasDeBioma(BiomaTipo bioma) {
        if (bioma == BiomaTipo.DESIERTO) {
            return new String[]{
                "Cactus saguaro",
                "Cactus barril",
                "Agave",
                "Arbusto espinoso",
                "Hierba de desierto",
                "Planta de rocio venenosa"
            };
        }
        if (bioma == BiomaTipo.PRADERA) {
            return new String[]{
                "Pasto verde",
                "Hierba alta",
                "Gramilla",
                "Trebol",
                "Girasol de pradera",
                "Arbusto silvestre venenoso"
            };
        }
        if (bioma == BiomaTipo.GLACIAR) {
            return new String[]{
                "Musgo artico",
                "Liquen glaciar",
                "Hierba de nieve",
                "Alga de hielo",
                "Helecho polar",
                "Hongo glaciar venenoso"
            };
        }
        // SELVA
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

    public static boolean esPlantaVenenosa(String nombrePlanta) {
        return nombrePlanta.toLowerCase().contains("venenosa") ||
               nombrePlanta.toLowerCase().contains("venenoso");
    }

    public static String[] herbivoresDeBioma(BiomaTipo bioma) {
        if (bioma == BiomaTipo.DESIERTO) {
            return new String[]{
                "Camello",
                "Dromedario",
                "Liebre del desierto",
                "Tortuga del desierto",
                "Iguana del desierto"
            };
        }
        if (bioma == BiomaTipo.PRADERA) {
            return new String[]{
                "Ciervo",
                "Bisonte",
                "Cebra",
                "Gacela",
                "Conejo de pradera",
                "Caballo salvaje"
            };
        }
        if (bioma == BiomaTipo.GLACIAR) {
            return new String[]{
                "Reno",
                "Foca",
                "Morsa",
                "Lemming",
                "Buey almizclero"
            };
        }
        // SELVA
        return new String[]{
            "Gorila",
            "Tapir",
            "Perezoso",
            "Loro",
            "Mono arana",
            "Venado de selva"
        };
    }

    public static String[] carnivoresDeBioma(BiomaTipo bioma) {
        if (bioma == BiomaTipo.DESIERTO) {
            return new String[]{
                "Serpiente cascabel",
                "Escorpion gigante",
                "Chacal del desierto",
                "Halcon del desierto"
            };
        }
        if (bioma == BiomaTipo.PRADERA) {
            return new String[]{
                "Leon",
                "Lobo de pradera",
                "Guepardo",
                "Hiena",
                "Aguila real"
            };
        }
        if (bioma == BiomaTipo.GLACIAR) {
            return new String[]{
                "Oso polar",
                "Lobo artico",
                "Zorro artico",
                "Orca"
            };
        }
      
        return new String[]{
            "Jaguar",
            "Anaconda",
            "Cocodrilo",
            "Tigre de selva",
            "Aguila harpia"
        };
    }
}