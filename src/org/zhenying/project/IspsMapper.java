package org.zhenying.project;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.Mapper;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reporter;

public class IspsMapper {
	protected static ArrayList<String> commonWords = Isps.getCommonWords();
	
	public static class AnalysisMapper extends MapReduceBase implements
			Mapper<Text, Text, IntWritable, Text> {
		private final String _counterGroup = "Custom Mapper Counters";

		@Override
		public void map(Text key, Text value,
				OutputCollector<IntWritable, Text> output, Reporter reporter)
				throws IOException {

			reporter.incrCounter(this._counterGroup, "Records In", 1);

			try {
				String pageText = value.toString();
				pageText = pageText.replaceAll("[^a-zA-Z0-9 ]", "").replaceAll(
						"\\s+", " ");

				if (pageText == null || pageText == "") {
					// Empty
					reporter.incrCounter(this._counterGroup, "Skipped", 1);
					return;
				}

				// Extract domain
				URI uri = new URI(key.toString());
				String domain = uri.getHost();
				domain = domain.startsWith("www.") ? domain.substring(4) : domain;

				for (int i = 0; i < IspsMainFeatures.names2D.size(); i++) {
					// if any of the name is mentioned, count as one.
					for (String nickname : IspsMainFeatures.names2D.get(i)) {
						if (!pageText.contains(nickname)) {
							continue;
						}

						// emit domain
						output.collect(new IntWritable(i), new Text("d"
								+ domain));
						// emit uncommon words
						for (String word : pageText.split(" ")) {
							word = word.toLowerCase().trim();

							if (!commonWords.contains(word)) {
								output.collect(new IntWritable(i), new Text("w"
										+ word));
							}
						}

						break;
					}
				}
			} catch (URISyntaxException uriEx) {
				// Unreadable URI character
				reporter.incrCounter(this._counterGroup, "URIException", 1);
			} catch (NullPointerException nullEx) {
				// Not a valid URI
				reporter.incrCounter(this._counterGroup, "NullException", 1);
			} catch (Exception ex) {
				IspsMainFeatures.LOG.error("Caught Exception", ex);
				reporter.incrCounter(this._counterGroup, "Exception", 1);
			} 

		}
	}

}
