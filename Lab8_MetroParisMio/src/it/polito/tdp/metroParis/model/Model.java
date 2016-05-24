package it.polito.tdp.metroParis.model;

import java.util.*;

import org.jgrapht.GraphPath;
import org.jgrapht.Graphs;
import org.jgrapht.WeightedGraph;
import org.jgrapht.alg.DijkstraShortestPath;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLng;
import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.metroParis.DB.MetroDAO;

public class Model {
	
	List <Fermata> path;
	private double pathTempoTotale = 0;
	 private List<Fermata> stazioni = new ArrayList<>();
	 private List<Linea> linee = new ArrayList<>();
	 private List<Connessione> connessioni = new ArrayList<>();
	WeightedGraph<Fermata,DefaultWeightedEdge> grafo =null;
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
		
		grafo =   new SimpleWeightedGraph<Fermata, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(grafo, stazioni);
		
		for(Connessione c : connessioni)
		{
			Fermata stazP = c.getStazPartenza();
			Fermata stazA = c.getStazArrivo();
			LatLng coord1 = c.getStazPartenza().getCoords();
			LatLng coord2 = c.getStazArrivo().getCoords();
			System.out.println(stazP.getNome()+"--"+stazA.getNome());
			double distanza = LatLngTool.distance(coord1,coord2, LengthUnit.KILOMETER);
			System.out.println(distanza+"\n");
			double velocita = c.getLinea().getVelocità();
			System.out.println(velocita+"\n");

			double tempo = (distanza / velocita) * 60 * 60; //in secondi 
			System.out.println(tempo+"\n");
			
			
			Graphs.addEdge(grafo, stazP, stazA, tempo);
			
		}
	}


	public String calcolaPercorso(Fermata stazP, Fermata stazA) {
		String risultato="";
		DijkstraShortestPath<Fermata, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<Fermata, DefaultWeightedEdge>(grafo, stazP, stazA);
		
		path = Graphs.getPathVertexList(dijkstra.getPath()) ;
		pathTempoTotale = dijkstra.getPathLength();
		int tempoTotaleInSecondi= (int) pathTempoTotale;
		int ore = tempoTotaleInSecondi / 3600;
		int minuti = (tempoTotaleInSecondi % 3600) / 60;
		int secondi = tempoTotaleInSecondi % 60;
		int cont=0;
		
		// Nel calcolo del tempo non tengo conto della prima e dell'ultima
				if (path.size() - 1 > 0) {
					pathTempoTotale += (path.size() - 1) * 30;
				}
				risultato="Percorso:[ ";
			for (Fermata f: path)
			{
				if(cont>=4)
				{ risultato+= "\n";
					cont=0;
				}
				risultato +=f.getNome()+", ";
				cont++;
			}
			risultato +="] \n \n Tempo di percorrenza stimato: "+String.format("%02d:%02d:%02d", ore, minuti, secondi);
			
			return risultato;
			
	}

	
	
	
	
	

}
