package it.polito.tdp.metroParis.DB;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import com.javadocmd.simplelatlng.LatLng;

import it.polito.tdp.metroParis.model.Connessione;
import it.polito.tdp.metroParis.model.Fermata;
import it.polito.tdp.metroParis.model.Linea;

public class MetroDAO {
	

	public List<Fermata> getAllStazioni(){

		List<Fermata> stazioni = new ArrayList<>() ;
		Connection conn = DBConnect.getConnection();
		String sql = "SELECT * FROM fermata ORDER BY nome ;";
		PreparedStatement st;
		
		try {
			st =conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next())
			{
				int id_fermata = res.getInt("id_fermata");
				String nome = res.getString("nome");
				double coordx = res.getDouble("coordX");
				double coordy = res.getDouble("coordY");
				
				Fermata f = new Fermata(id_fermata,nome, new LatLng(coordx, coordy));
				stazioni.add(f);
			}
			return stazioni;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
		
	}

	public List<Linea> getAllLinee() {
		List<Linea> linee = new ArrayList<>() ;
		Connection conn = DBConnect.getConnection();
		String sql = "SELECT * FROM linea ORDER BY id_linea ;";
		PreparedStatement st;
		
		try {
			st =conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			
			while(res.next())
			{
				int id_linea = res.getInt("id_linea");
				String nome = res.getString("nome");
				double velocita = res.getDouble("velocita");
				double intervallo = res.getDouble("intervallo");
				String colore = res.getString("colore");
				
				Linea l = new Linea(id_linea,nome, velocita,intervallo,colore);
				linee.add(l);
			}
			return linee;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public List<Connessione> getConnessioni(List<Fermata> stazioni, List<Linea> linee) {
		String sql = "select id_connessione, id_linea, id_stazP, id_stazA from connessione";
		List<Connessione> connessioni = new ArrayList<Connessione>();

		try {
			
			Connection conn = DBConnect.getConnection();
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while(res.next())
			{
				int id_connessione = res.getInt("id_connessione");
				int idLinea = res.getInt("id_linea");
				int idStazP = res.getInt("id_stazP");
				int idStazA = res.getInt("id_stazA");
				
				Linea myLinea = linee.get(linee.indexOf(new Linea(idLinea)));
				Fermata myStazP = stazioni.get(stazioni.indexOf(new Fermata(idStazP)));
				Fermata myStazA = stazioni.get(stazioni.indexOf(new Fermata(idStazA)));

				Connessione cnx = new Connessione(id_connessione, myLinea, myStazP, myStazA);

				connessioni.add(cnx);

			}
			return connessioni;

			}
		catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
		
	
	
	}
}
