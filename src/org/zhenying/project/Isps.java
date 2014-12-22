package org.zhenying.project;

import java.util.ArrayList;
import java.util.Arrays;

public class Isps {

	public static ArrayList<String> getNewIsps() {
		// holds selected Internet service providers for study
		return new ArrayList<String>(Arrays.asList("att", "verizon", "tmobile",
				"sprint", "uscellular", "cspire", "ntelos", "cellcom",
				"southernlinc", "att, verizon", "att verizon"));
	}

	public static ArrayList<String> getCommonWords() {
		return new ArrayList<String>(Arrays.asList("the", "be", "to", "of",
				"and", "a", "in", "that", "have", "I", "it", "for", "not",
				"on", "with", "he", "as", "you", "do", "at", "this", "but",
				"his", "by", "from", "they", "we", "say", "her", "she", "or",
				"an", "will", "my", "one", "all", "would", "there", "their",
				"what", "so", "up", "out", "if", "about", "who", "get",
				"which", "go", "me", "when", "make", "can", "like", "time",
				"no", "just", "him", "know", "take", "person", "into", "year",
				"your", "good", "some", "could", "them", "see", "other",
				"than", "then", "now", "look", "only", "come", "its", "over",
				"think", "also", "back", "after", "use", "two", "how", "our",
				"work", "first", "well", "way", "even", "new", "want",
				"because", "any", "these", "give", "day", "most", "us", "is",
				"i", "are", "has"));
	}
}