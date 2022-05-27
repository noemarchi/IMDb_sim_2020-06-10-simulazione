package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;



public class Simulatore 
{
	// PARAMETRI DI SIMULAZIONE
	private int nGiorni;
	
	// OUTPUT DELLA SIMULAZIONE
	private int pause;
	private Map<Integer, Actor> intervistati;
	
	// STATO DEL MONDO SIMULATO
	private List<Actor> attoriDisponibili;
	private Graph<Actor, DefaultWeightedEdge> grafo;
	
	// METODI
	
	// COSTRUTTORE
	public Simulatore(int nGiorni, Graph<Actor, DefaultWeightedEdge> grafo) 
	{
		this.nGiorni = nGiorni;
		this.grafo = grafo;
	}
	
	// INIZIALIZZARE LA SIMULAZIONE
	public void init()
	{
		this.pause = 0;
		this.intervistati = new HashMap<Integer, Actor>();
		this.attoriDisponibili = new ArrayList<Actor>(grafo.vertexSet());
	}
	
	// ESECUZIONE DELLA SIMULAZIONE
	public void run()
	{
		for(int giorno=0; giorno<this.nGiorni; giorno++)
		{
			Random rand = new Random();
			// rand.nextInt(n)  
			// restituisce un numero intero casuale 
			// compreso fra 0 (incluso) e n (escluso);
			
			// rand.nextDouble() 
			// restituisce un numero in virgola mobile casuale 
			// compreso fra 0 (incluso) e 1 (escluso).
			
			if(giorno == 1 || !this.intervistati.containsKey(giorno - 1))
			{
				// il primo giorno o il giorno dopo una pausa
				// scelgo un attore casualmente
				
				int limiteRand = this.attoriDisponibili.size();
				Actor attore = this.attoriDisponibili.get(rand.nextInt(limiteRand));
				
				// aggiungo agli intervistati
				this.intervistati.put(giorno, attore);
				
				// tolgo dai disponibili
				this.attoriDisponibili.remove(attore);
				
				System.out.println(String.format("Giorno %d: attore casuale (%s)", giorno, attore.toString()));
			
				continue;
				// salta gli if e inizia una nuova iterazione dentro il for
			}
			
			if(giorno >= 3 && this.intervistati.containsKey(giorno-1) && this.intervistati.containsKey(giorno-2)
					&& this.intervistati.get(giorno-1).gender.equals(this.intervistati.get(giorno-2).gender))
			{
				// se per due giorni consecutivi sono stati intervistati attori dello stesso genere
				// al 90% il produttore prende una pausa
				// --> non aggiungo intervistati nella mappa, continue;
				// salto il giorno
				
				if(rand.nextDouble() <= 0.9)
				{
					this.pause++;
					System.out.println(String.format("Giorno %d: pausa", giorno));
					
					continue;
				}
			}
			
			// se arrivo qui:
			// - al 60% scelgo in modo casuale
			// - al 40% mi faccio consigliare
			// ma se non mi consiglia nulla o consiglia uno già intervistato, scelgo in modo casuale
			
			if(rand.nextDouble() <= 0.6)
			{
				// scelgo in modo casuale
				int limiteRand = this.attoriDisponibili.size();
				Actor attore = this.attoriDisponibili.get(rand.nextInt(limiteRand));
				
				// aggiungo agli intervistati
				this.intervistati.put(giorno, attore);
				
				// tolgo dai disponibili
				this.attoriDisponibili.remove(attore);
				
				System.out.println(String.format("Giorno %d: attore casuale (%s)", giorno, attore.toString()));
			
				continue;
			}
			else
			{
				// mi faccio consigliare dall'ultimo intervistato
				Actor ultimo = this.intervistati.get(giorno-1);
				Actor consigliato = this.getConsigliato(ultimo);
				
				// se non mi consiglia nulla o consiglia uno già intervistato, scelgo in modo casuale
				if(consigliato == null || !this.attoriDisponibili.contains(consigliato))
				{
					// scelgo in modo casuale
					int limiteRand = this.attoriDisponibili.size();
					Actor attore = this.attoriDisponibili.get(rand.nextInt(limiteRand));
					
					// aggiungo agli intervistati
					this.intervistati.put(giorno, attore);
					
					// tolgo dai disponibili
					this.attoriDisponibili.remove(attore);
					
					System.out.println(String.format("Giorno %d: attore casuale (%s)", giorno, attore.toString()));
				
					continue;
				}
				else
				{
					// aggiungo quello consigliato
					this.intervistati.put(giorno, consigliato);
					
					// tolgo dai disponibili
					this.attoriDisponibili.remove(consigliato);
					
					System.out.println(String.format("Giorno %d: attore consigliato (%s)", giorno, consigliato.toString()));
					
					continue;
				}
			}
			
			
		}
	}
	
	private Actor getConsigliato(Actor ultimo)
	{
		Actor consigliato = null;
		double peso = 0;
		
		for(Actor vicino: Graphs.neighborListOf(grafo, ultimo))
		{
			DefaultWeightedEdge arco = grafo.getEdge(ultimo, vicino);
			
			if(grafo.getEdgeWeight(arco) > peso)
			{
				peso = grafo.getEdgeWeight(arco);
				consigliato = vicino;
			}
		}
		
		
		return consigliato;
	}
	
	
	// GETTER PER OTTENERE IL RISULTATO DELLA SIMULAZIONE
	public int getPause()
	{
		return this.pause;
	}
	
	public Collection<Actor> getIntervistati()
	{
		return this.intervistati.values();
	}

}
