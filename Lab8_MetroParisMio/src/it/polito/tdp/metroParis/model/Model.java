package it.polito.tdp.metroParis.model;

import java.util.*;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.metroParis.DB.MetroDAO;

public class Model {
	
	List<DefaultWeightedEdge> pathEdgeList;
	private double pathTempoTotale = 0;
	 private List<Fermata> stazioni = new ArrayList<>();
	 private List<Linea> linee = new ArrayList<>();
	 private List<Connessione> connessioni = new ArrayList<>();
	 private List<FermataSuLinea> fermateSuLinea = new ArrayList<>();
	WeightedGraph<FermataSuLinea,DefaultWeightedEdge> grafo =null;
	public void getAllStazioni() {
		
		
		MetroDAO dao = new MetroDAO();
		stazioni.addAll(dao.getAllStazioni());
		
	}

	public List<Fermata> getStazioni() {
		return stazioni;
	}

	public void creaGrafo() {
		MetroDAO dao = new MetroDAO();
		linee = dao.getAllLinee();
		connessioni = dao.getConnessioni(stazioni,linee);
		fermateSuLinea = dao.getAllFermateSuLinea(stazioni,linee);
		
		grafo =   new DefaultDirectedWeightedGraph<FermataSuLinea, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, fermateSuLinea);
		
		for(Connessione c : connessioni)
		{
			Fermata stazP = c.getStazPartenza();
			Fermata stazA = c.getStazArrivo();
			LatLng coord1 = c.getStazPartenza().getCoords();
			LatLng coord2 = c.getStazArrivo().getCoords();
			
			double distanza = LatLngTool.distance(coord1,coord2, LengthUnit.KILOMETER);
			double velocita = c.getLinea().getVelocità();

			double tempo = (distanza / velocita) * 60 * 60; //in secondi 

			// Cerco la fermataSuLinea corretta all'interno della lista delle fermate
						List<FermataSuLinea> fermateSuLineaPerFermata = c.getStazPartenza().getFermateSuLinea();
						FermataSuLinea fslPartenza = fermateSuLineaPerFermata.get(fermateSuLineaPerFermata.indexOf(new FermataSuLinea(c.getStazPartenza(), c.getLinea())));

						// Cerco la fermataSuLinea corretta all'interno della lista delle fermate
						fermateSuLineaPerFermata = c.getStazArrivo().getFermateSuLinea();
						FermataSuLinea fslArrivo = fermateSuLineaPerFermata.get(fermateSuLineaPerFermata.indexOf(new FermataSuLinea(c.getStazArrivo(), c.getLinea())));

						if (fslPartenza != null && fslArrivo != null) {
							// Aggiungoun un arco pesato tra le due fermate
							Graphs.addEdge(grafo, fslPartenza, fslArrivo, tempo);
						} else {
							System.err.println("Non ho trovato fslPartenza o fslArrivo. Salto alle prossime");
						}
		}
						// FASE3: Aggiungo tutte le connessioni tra tutte le fermateSuLinee
						// della stessa Fermata
						for (Fermata fermata : stazioni) {
							for (FermataSuLinea fslP : fermata.getFermateSuLinea()) {
								for (FermataSuLinea fslA : fermata.getFermateSuLinea()) {
									if (!fslP.equals(fslA)) {
										Graphs.addEdge(grafo, fslP, fslA, fslA.getLinea().getIntervallo() * 60);
									}
								}
							}
						}
		
	}


	public void calcolaPercorso(Fermata partenza, Fermata arrivo) {

		DijkstraShortestPath<FermataSuLinea, DefaultWeightedEdge> dijkstra;

		// Usati per salvare i valori temporanei
		double pathTempoTotaleTemp;

		// Usati per salvare i valori migliori
		List<DefaultWeightedEdge> bestPathEdgeList = null;
		double bestPathTempoTotale = Double.MAX_VALUE;

		for (FermataSuLinea fslP : partenza.getFermateSuLinea()) {
			for (FermataSuLinea fslA : arrivo.getFermateSuLinea()) {
				dijkstra = new DijkstraShortestPath<FermataSuLinea, DefaultWeightedEdge>(grafo, fslP, fslA);

				pathTempoTotaleTemp = dijkstra.getPathLength();

				if (pathTempoTotaleTemp < bestPathTempoTotale) {
					bestPathTempoTotale = pathTempoTotaleTemp;
					bestPathEdgeList = dijkstra.getPathEdgeList();
				}
			}
		}

		pathEdgeList = bestPathEdgeList;
		pathTempoTotale = bestPathTempoTotale;

		if (pathEdgeList == null)
			throw new RuntimeException("Non è stato creato un percorso.");

		// Nel calcolo del tempo non tengo conto della prima e dell'ultima
		if (pathEdgeList.size() - 1 > 0) {
			pathTempoTotale += (pathEdgeList.size() - 1) * 30;
		}
	}
	
	public String getPercorsoEdgeList() {

		if (pathEdgeList == null)
			throw new RuntimeException("Non è stato creato un percorso.");

		StringBuilder risultato = new StringBuilder();
		risultato.append("Percorso:\n\n");

		Linea lineaTemp = grafo.getEdgeTarget(pathEdgeList.get(0)).getLinea();
		risultato.append("Prendo Linea: " + lineaTemp.getNome() + "\n[");

		for (DefaultWeightedEdge edge : pathEdgeList) {
			
			risultato.append(grafo.getEdgeTarget(edge).getNome());

			if (!grafo.getEdgeTarget(edge).getLinea().equals(lineaTemp)) {
				risultato.append("]\n\nCambio su Linea: " + grafo.getEdgeTarget(edge).getLinea().getNome() + "\n[");
				lineaTemp = grafo.getEdgeTarget(edge).getLinea();
				
			} else {
				risultato.append(", ");
			}
		}
		risultato.setLength(risultato.length() - 2);
		risultato.append("]");

		return risultato.toString();
	}

	public double getPercorsoTempoTotale() {

		if (pathEdgeList == null)
			throw new RuntimeException("Non è stato creato un percorso.");

		return pathTempoTotale;
	}
	
	
	
	
	

}
