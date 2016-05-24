package it.polito.tdp.metroParis.model;

public class Connessione {

	private int id_connessione;
	private Linea linea;
	private Fermata stazPartenza;
	private Fermata stazArrivo;
	public Connessione(int id_connessione, Linea linea, Fermata myStazP, Fermata myStazA) {
		super();
		this.id_connessione = id_connessione;
		this.linea = linea;
		this.stazPartenza = myStazP;
		this.stazArrivo = myStazA;
	}
	public int getId_connessione() {
		return id_connessione;
	}
	public void setId_connessione(int id_connessione) {
		this.id_connessione = id_connessione;
	}
	public Linea getLinea() {
		return linea;
	}
	public void setLinea(Linea linea) {
		this.linea = linea;
	}
	
	public Fermata getStazPartenza() {
		return stazPartenza;
	}
	public void setStazPartenza(Fermata stazPartenza) {
		this.stazPartenza = stazPartenza;
	}
	public Fermata getStazArrivo() {
		return stazArrivo;
	}
	public void setStazArrivo(Fermata stazArrivo) {
		this.stazArrivo = stazArrivo;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_connessione;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Connessione other = (Connessione) obj;
		if (id_connessione != other.id_connessione)
			return false;
		return true;
	}
	
	
	
}
