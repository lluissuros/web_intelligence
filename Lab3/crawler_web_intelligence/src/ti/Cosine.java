// Copyright (C) 2015  Julián Urbano <urbano.julian@gmail.com>
// Distributed under the terms of the MIT License.

package ti;

import java.util.*;

/**
 * Implements retrieval in a vector space with the cosine similarity function and a TFxIDF weight formulation.
 */
public class Cosine implements RetrievalModel
{
	public Cosine()
	{
		// empty
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Tuple<Integer, Double>> runQuery(String queryText, Index index, DocumentProcessor docProcessor)
	{
		// P1
		// Extract the terms from the query
		//System.out.println(index.documents.size());
		ArrayList<String> queryTerms = docProcessor.processText(queryText); 
	
		
		// Calculate the query vector
			//in process
		ArrayList<Tuple<Integer, Double>> queryVector = this.computeVector(queryTerms, index);
		
		
		// Calculate the document similarity
		ArrayList<Tuple<Integer, Double>> results = this.computeScores(queryVector, index);

		return results; // return results
	}

	/**
	 * Returns the list of documents in the specified index sorted by similarity with the specified query vector.
	 *
	 * @param queryVector the vector with query term weights.
	 * @param index       the index to search in.
	 * @return a list of {@link Tuple}s where the first item is the {@code docID} and the second one the similarity score.
	 */
	protected ArrayList<Tuple<Integer, Double>> computeScores(ArrayList<Tuple<Integer, Double>> queryVector, Index index)
	{
		ArrayList<Tuple<Integer, Double>> results = new ArrayList<>();

		// P1
		//<docId, similarity>
		HashMap<Integer, Double> sims = new HashMap<>();
		
		for (Tuple<Integer, Double> term : queryVector) {
			int termId = term.item1;
			for (Tuple<Integer, Double> doc :index.invertedIndex.get(termId)) {
				//get cosine similiaty 
				double weightInQuery = term.item2;
				double weightInDoc = doc.item2;
				double result = weightInQuery * weightInDoc;
				if(sims.containsKey(doc.item1)) {
					//accumulate the previous result to sims[docId]
					sims.put(doc.item1, sims.get(doc.item1) + result);
				} else {
					sims.put(doc.item1, result);
				}
			}
			
		
			double normQueryWeight = 0;
			for (Tuple<Integer, Double> q :queryVector) {
				normQueryWeight = normQueryWeight + Math.pow(q.item2, 2);
			}
			normQueryWeight = Math.sqrt(normQueryWeight);
			
			
			for (int docId : sims.keySet()) {
				double normDocWeight = index.documents.get(docId).item2;
				double cosineSim = sims.get(docId)/(normQueryWeight * normDocWeight);
				results.add(new Tuple<>(docId, cosineSim));
			}			
						
		}
		

		// Sort documents by similarity and return the ranking
		Collections.sort(results, new Comparator<Tuple<Integer, Double>>()
		{
			@Override
			public int compare(Tuple<Integer, Double> o1, Tuple<Integer, Double> o2)
			{
				return o2.item2.compareTo(o1.item2);
			}
		});
		return results;
	}

	/**
	 * Compute the vector of weights for the specified list of terms.
	 *
	 * @param terms the list of terms.
	 * @param index the index
	 * @return a list of {@code Tuple}s with the {@code termID} as first item and the weight as second one.
	 */
	protected ArrayList<Tuple<Integer, Double>> computeVector(ArrayList<String> terms, Index index)
	{
		ArrayList<Tuple<Integer, Double>> vector = new ArrayList<>();

		// P1: compute following the TF.IDF:
		
		// term weights in the documents are already in invertedIndex, and their norm (documents)
		// query weights and their norms should be calculated at query time.
		
		Set<String> set = new HashSet<String>(terms);		
		for (String term : set) {			
			int occurrences = Collections.frequency(terms, term);
			double termFreq = 1 + Math.log10(occurrences);
			
			if (index.vocabulary.containsKey(term)){
				int termID = index.vocabulary.get(term).item1;
				double idf = index.vocabulary.get(term).item2;
				double calculatedWeight = termFreq * idf;
				vector.add(new Tuple<>(termID, calculatedWeight));
			} else {
				//System.out.println(term + " was not found in the docs");
			}
			//System.out.println(" ");
		}
		System.out.println(vector);
		return vector;
	}
}
