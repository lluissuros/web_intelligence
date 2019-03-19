// Copyright (C) 2015  Julián Urbano <urbano.julian@gmail.com>
// Distributed under the terms of the MIT License.

package ti;

import ti.Stemmer;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;


/**
 * A processor to extract terms from HTML documents.
 */
public class HtmlProcessor implements DocumentProcessor
{

	// P2:done?
	protected HashSet<String> stopwords;

	/**
	 * Creates a new HTML processor.
	 *
	 * @param pathToStopWords the path to the file with stopwords, or {@code null} if stopwords are not filtered.
	 * @throws IOException if an error occurs while reading stopwords.
	 */
	public HtmlProcessor(File pathToStopWords) throws IOException
	{
		// P2
		//TODO: ¿shall we normalize the stopwords as well??
		if(pathToStopWords != null) {
			List<String> lines = Files.readAllLines(Paths.get(pathToStopWords.toString()), StandardCharsets.UTF_8);
			this.stopwords = new HashSet<String>(lines);
		} 
	
	}

	/**
	 * {@inheritDoc}
	 */
	public Tuple<String, String> parse(String html)
	{
		// P2: done?
		Document document = Jsoup.parse(html);
		String title = document.title(); //Get title
		String body = "";
		if(document.body() != null){
			body = document.body().text();
		}
		return new Tuple<>(title, body); // Return title and body separately
	}

	/**
	 * Process the given text (tokenize, normalize, filter stopwords and stemize) and return the list of terms to index.
	 *
	 * @param text the text to process.
	 * @return the list of index terms.
	 */
	public ArrayList<String> processText(String text)
	{
		
		// P2
		// Tokenizing, normalizing, stopwords, stemming, etc.
		
		ArrayList<String> terms = new ArrayList<>();
		
		//tokenize
		ArrayList<String> tokens = this.tokenize(text);
		
		//normalize
		ArrayList<String> tokensNorm = new ArrayList<>();
		for (String token : tokens) {
			String tokenNorm = this.normalize(token);
			if(tokenNorm !=null) {
				tokensNorm.add(tokenNorm);				
			}
		}
		
		//remove stopwords:
		ArrayList<String> stopWordsRemoved = new ArrayList<>();
		for (String tokenNorm : tokensNorm) {
			if(!this.isStopWord(tokenNorm)) {
				stopWordsRemoved.add(tokenNorm);				
			}
		}
		
		
		//stemming with the provided Stemmer.java
		ArrayList<String> stemmedWords = new ArrayList<>();
		for (String word : stopWordsRemoved) {
			stemmedWords.add(this.stem(word));
		}
	
		/*
		System.out.println("\n log process");
		System.out.println(tokens);
		System.out.println(tokensNorm);
		System.out.println(stopWordsRemoved); 
		System.out.println(stemmedWords); 
		*/
		
		terms = stemmedWords;
	
		return terms;
	}

	/**
	 * Tokenize the given text.
	 *
	 * @param text the text to tokenize.
	 * @return the list of tokens.
	 */
	protected ArrayList<String> tokenize(String text)
	{		
		// P2
		//With the following regexp we will keep also non-latin alphabetic words:
		String[] strings = text.replaceAll("[^\\p{IsAlphabetic}^\\p{IsDigit}]", " ").split("\\s+");
		ArrayList<String> tokens = new ArrayList<String>(Arrays.asList(strings));		

		return tokens;
	}

	/**
	 * Normalize the given term.
	 *
	 * @param text the term to normalize.
	 * @return the normalized term.
	 */
	protected String normalize(String text)
	{
		// P2: done?
		String normalized = null;
		if(!isNumeric(text)) {
			String textLower = text.toLowerCase();
			//normalize and remove ugly unicode:
			normalized = Normalizer.normalize(textLower, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
		}
		return normalized;
	}
	
	/**
	 * Checks whether text is a number.
	 *
	 * @param term the term to check.
	 * @return {@code true} if the term is a number.
	 */
	private boolean isNumeric(String s) { 
	    return s != null && s.matches("[-+]?\\d*\\.?\\d+");  
	}  

	
	/**
	 * Checks whether the given term is a stopword.
	 *
	 * @param term the term to check.
	 * @return {@code true} if the term is a stopword and {@code false} otherwise.
	 */
	protected boolean isStopWord(String term)
	{
		boolean isTopWord = false;

		// P2: done
		if(this.stopwords != null && this.stopwords.contains(term)) {
			isTopWord = true;
		}

		return isTopWord;
	}

	/**
	 * Stem the given term.
	 *
	 * @param term the term to stem.
	 * @return the stem of the term.
	 */
	protected String stem(String term)
	{
		String stem = null;
		
		// P2
		Stemmer stemmer = new Stemmer();
		stemmer.add(term.toCharArray(), term.length());;
		stemmer.stem();
		stem = stemmer.toString();

		return stem;
	}
}
