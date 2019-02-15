// Copyright (C) 2015  Julián Urbano <urbano.julian@gmail.com>
// Distributed under the terms of the MIT License.

package ti;

import java.util.ArrayList;

/**
 * Implements retrieval in a vector space with the cosine similarity function and a TFxIDF weight formulation,
 * plus pseudorelevance feedback.
 */
public class CosineWithFeedback extends Cosine
{
	protected int feedbackDepth;
	protected double feedbackAlpha;
	protected double feedbackBeta;

	/**
	 * Creates a new retriver with the specified pseudorelevance feedback parameters.
	 *
	 * @param feedbackDepth number of documents to consider relevant.
	 * @param feedbackAlpha relative weight of the original query terms
	 * @param feedbackBeta  relative weight of the expanded terms.
	 */
	public CosineWithFeedback(int feedbackDepth, double feedbackAlpha, double feedbackBeta)
	{
		super();
		this.feedbackDepth = feedbackDepth;
		this.feedbackAlpha = feedbackAlpha;
		this.feedbackBeta = feedbackBeta;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public ArrayList<Tuple<Integer, Double>> runQuery(String queryText, Index index, DocumentProcessor docProcessor)
	{
		// P3
		// Calculate initial results
		// Update query vector
		// Re-run the query

		return null; // return results
	}

	/**
	 * Computes the modified query vector for relevance feedback.
	 *
	 * @param queryVector the original query vector.
	 * @param results     the results with the original query.
	 * @param index       the index to search in.
	 * @return a list of {@code Tuple}s with the {@code termID} as first item and the weight as second one.
	 */
	protected ArrayList<Tuple<Integer, Double>> computeFeedbackVector(ArrayList<Tuple<Integer, Double>> queryVector,
	                                                                  ArrayList<Tuple<Integer, Double>> results,
	                                                                  Index index)
	{
		ArrayList<Tuple<Integer, Double>> weights = new ArrayList<>();

		// P3

		return weights;
	}
}
