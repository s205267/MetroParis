package it.polito.tdp.metroParis.model;

import java.util.ArrayList;
import java.util.List;

import com.javadocmd.simplelatlng.LatLng;

public class Fermata {

	private int id_fermata;
	private String nome;
	private LatLng coords;
	private List<FermataSuLinea> fermateSuLinea=new ArrayList<>();

	public Fermata(int id_fermata, String nome, LatLng coords) {
		super();
		this.id_fermata = id_fermata;
		this.nome = nome;
		this.coords = coords;
	}
	public Fermata(int idStazP) {
this.id_fermata= idStazP;
	}
	
	public List<FermataSuLinea> getFermateSuLinea() {
		return fermateSuLinea;
	}
	public void setFermateSuLinea(List<FermataSuLinea> fermateSuLinea) {
		this.fermateSuLinea = fermateSuLinea;
	}
	public void addFermataSuLinea(FermataSuLinea fermataSuLinea) {
		this.fermateSuLinea.add(fermataSuLinea);
	}
	public int getId_fermata() {
		return id_fermata;
	}
	public String getNome() {
		return nome;
	}
	public LatLng getCoords() {
		return coords;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id_fermata;
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
		Fermata other = (Fermata) obj;
		if (id_fermata != other.id_fermata)
			return false;
		return true;
	}
	@Override
	public String toString() {
		return  nome;
	}
	
	
}
