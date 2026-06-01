package simulador;

public class Planta {

    public enum TipoPlanta {
        FRUTAL, ARBUSTO, PASTO, CACTUS, HELECHO, ALGA, HONGO
    }

    private String nombre;
    private TipoPlanta tipo;
    private boolean esVenenosa;
    private boolean estaViva;
    private int salud;
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
                System.out.println("  >> La planta " + nombre + " se ha marchitado por condiciones extremas.");
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

    private TipoPlanta asignarTipo(String nombre) {
        nombre = nombre.toLowerCase();
        if (nombre.contains("cactus"))   return TipoPlanta.CACTUS;
        if (nombre.contains("helecho"))  return TipoPlanta.HELECHO;
        if (nombre.contains("alga") || nombre.contains("musgo")) return TipoPlanta.ALGA;
        if (nombre.contains("hongo"))    return TipoPlanta.HONGO;
        if (nombre.contains("pasto") || nombre.contains("hierba") || nombre.contains("gramilla")) return TipoPlanta.PASTO;
        if (nombre.contains("arbusto")) return TipoPlanta.ARBUSTO;
        return TipoPlanta.FRUTAL;
    }

    public String    getNombre()     { return nombre; }
    public TipoPlanta getTipo()      { return tipo; }
    public boolean   esVenenosa()    { return esVenenosa; }
    public boolean   estaViva()      { return estaViva; }
    public int       getSalud()      { return salud; }
    public int       getHorasDeVida(){ return horasDeVida; }
    public String    getTipoTexto()  { return esVenenosa ? "Venenosa" : "Normal"; }
}