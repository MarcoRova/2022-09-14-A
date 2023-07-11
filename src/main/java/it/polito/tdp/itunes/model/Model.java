package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	
	private ItunesDAO dao;
	private Graph<Album, DefaultEdge>grafo;
	private List<Album> vertici;
	private Map<Integer, Album> albumIdMap;
	
	private int dimMax ;
	private List<Album> setMax ;
	
	
	public Model() {
		this.dao = new ItunesDAO();
		this.albumIdMap = new HashMap<>();
		this.setMax = new ArrayList<>();
	} 
	
	public void creaGrafo(double durata) {
		
		this.grafo = new SimpleGraph<Album, DefaultEdge>(DefaultEdge.class);
		
		this.vertici = new LinkedList<>(this.dao.getVertici(durata));
		
		for(Album a : vertici) {
			this.albumIdMap.put(a.getAlbumId(), a);
		}
		
		Graphs.addAllVertices(this.grafo, vertici);
		
		List<Arco> archi = this.dao.getArchi();
		
		for(Arco e : archi) {
			
			if(this.albumIdMap.containsKey(e.getId1()) && this.albumIdMap.containsKey(e.getId2())) {
				
				this.grafo.addEdge(this.albumIdMap.get(e.getId1()), this.albumIdMap.get(e.getId2()));
				
			}
		}
		
	}

	public Graph<Album, DefaultEdge> getGrafo() {
		return grafo;
	}

	public List<Album> getVertici() {
		return vertici;
	}
	
	
	public String infoGrafo() {
		return "Grafo creato!\n#Vertici: "+this.grafo.vertexSet().size()+"\n#Archi: "+this.grafo.edgeSet().size();
	}

	
	public Set<Album> getComponente(Album a){
		
		ConnectivityInspector<Album, DefaultEdge> ci = new ConnectivityInspector<>(this.grafo);
		return ci.connectedSetOf(a);
		
	}
	
	public double durataComponete(Album a) {
		
		double durata = 0.0;
		
		Set<Album> compontente = getComponente(a);
		
		for(Album al : compontente) {
			
			durata += al.getDurata();
			
		}
		return durata;
	}
	
	public Set<Album> getSetAlbum(Album a1, double soglia){
		
		if(a1.getDurata()>soglia) {
			return null;
		}
		
		List<Album> parziale = new ArrayList<>();
		
		List<Album> componente = new ArrayList<>(this.getComponente(a1));
		
		componente.remove(a1);
		
		this.dimMax = 1;
		
		this.setMax = new ArrayList<>(parziale);
		
		cerca(parziale, 0, 0.0, soglia-a1.getDurata(), componente);
		
		Set<Album> result = new HashSet<>(setMax);
		
		result.add(a1);
		
		return result;
	}

	public void cerca(List<Album> parziale, int livello, double dParziale, double dTot, List<Album> componente) {
		
		if(parziale.size()>dimMax) {
			dimMax = parziale.size();
			setMax = new ArrayList<>(parziale);
		}
		
		for(Album nuovo: componente) {
			if( (livello==0 || nuovo.getAlbumId()>parziale.get(parziale.size()-1).getAlbumId()) && 
					dParziale+nuovo.getDurata()<=dTot ) {
				parziale.add(nuovo) ;
				cerca(parziale, livello+1, dParziale+nuovo.getDurata(), dTot, componente) ;
				parziale.remove(parziale.size()-1) ;
			}
		}	
	}
	
}
 