package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model 
{
	private ImdbDAO dao;
	private Map<Integer, Actor> idMapAttori;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	private Simulatore sim;
	
	public Model()
	{
		dao = new ImdbDAO();
		this.idMapAttori = dao.mapAllActors();
	}
	
	public List<String> getGeneri()
	{
		List<String> ret = dao.listAllGenres();
		
		return ret;
	}
	
	public void creaGrafo(String genere)
	{
		// creo il grafo
		this.grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		// aggiungo i vertici
		Graphs.addAllVertices(grafo, dao.getVertici(genere, idMapAttori));
		System.out.println(this.grafo.vertexSet().size());
		
		// aggiungo gli archi
		for(Adiacenza a: dao.getAdiacenze(genere, idMapAttori))
		{
			if(grafo.getEdge(a.getA1(), a.getA2()) == null)
			{
				Graphs.addEdgeWithVertices(grafo, a.getA1(), a.getA2(), a.getPeso());
			}
		}
		
		System.out.println(this.grafo.edgeSet().size());
	}

	public int nArchi()
	{
		return grafo.edgeSet().size();
	}
	
	public int nVertici()
	{
		return grafo.vertexSet().size();
	}
	
	public List<Actor> getVertici()
	{
		List<Actor> lista = new ArrayList<Actor>(grafo.vertexSet());
		
		Collections.sort(lista);
		
		return lista;
	}
	
	public List<Actor> getSimili(Actor attore)
	{
		ConnectivityInspector ispettore = new ConnectivityInspector(grafo);
		Set<Actor> connessi = ispettore.connectedSetOf(attore);
		
		List<Actor> lista = new ArrayList<Actor>(connessi);
		lista.remove(attore);
		
		Collections.sort(lista);
		
		return lista;
	}
	
	public boolean simula(int giorni)
	{
		if(grafo == null)
			return false;
		
		sim = new Simulatore(giorni, grafo);
		sim.init();
		sim.run();
		
		return true;
	}
	
	public int getPauseSimulazione()
	{
		return sim.getPause();
	}
	
	public Collection<Actor> getIntervistati()
	{
		return sim.getIntervistati();
	}

}
