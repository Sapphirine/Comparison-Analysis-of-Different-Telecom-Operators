package org.zhenying.project;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.TreeMap;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.MapReduceBase;
import org.apache.hadoop.mapred.OutputCollector;
import org.apache.hadoop.mapred.Reducer;
import org.apache.hadoop.mapred.Reporter;

public class IspsReducer {
	public static class AnalysisReducer extends MapReduceBase implements
			Reducer<IntWritable, Text, Text, Text> {
		private static ArrayList<String> names = Isps.getNewIsps();

		@Override
		public void reduce(IntWritable key, Iterator<Text> values,
				OutputCollector<Text, Text> output, Reporter reporter)
				throws IOException {
			// Name lookup.
			try {
				String isp = names.get(key.get());

				HashMap<String, LongType> wordFreq = new HashMap<String, LongType>();
				HashMap<String, LongType> domainFreq = new HashMap<String, LongType>();
				long totalCount = 0;

				// assemble frequency map
				while (values.hasNext()) {
					String fullVal = values.next().toString();
					String type = fullVal.substring(0, 1);
					String context = fullVal.substring(1);

					// find out this is a domain key or word key.
					if (type.equals("d")) {
						if (!domainFreq.containsKey(context)) {
							domainFreq.put(context, new LongType());
						} else {
							domainFreq.get(context).incr();
						}

						totalCount++;
					} else { // Now its word
						if (!wordFreq.containsKey(context)) {
							wordFreq.put(context, new LongType());
						} else {
							wordFreq.get(context).incr();
						}
					}
				}

				// emit word association info
				findTop(wordFreq, isp, output, "WRD|");
				findTop(domainFreq, isp, output, "DMN|");
				output.collect(new Text("CNT|" + isp + "|"),
						new Text(Long.toString(totalCount)));

			} catch (Exception ex) {
				IspsMainFeatures.LOG.error("Caught Exceptoin", ex);
				reporter.incrCounter("TotalAnalysis Reducer", "Exeptions", 1);
			}
		}

		private static void findTop(HashMap<String, LongType> freqMap,
				String isp, OutputCollector<Text, Text> output, String mark)
				throws IOException {
			// Find top 50 words.
			TreeMap<Long, ArrayList<String>> revMap = new TreeMap<Long, ArrayList<String>>();

			for (String target : freqMap.keySet()) {
				Long freq = new Long(freqMap.get(target).getVal());

				if (!revMap.containsKey(freq)) {
					revMap.put(freq, new ArrayList<String>());
				}
				revMap.get(freq).add(target);
			}

			ArrayList<String> targetsToEmit = new ArrayList<String>();
			for (Long count : revMap.descendingKeySet()) {
				if (targetsToEmit.size() >= 50) {
					break;
				}

				ArrayList<String> targets = revMap.get(count);

				for (String target : targets) {
					if (targetsToEmit.size() >= 50) {
						break;
					} else {
						targetsToEmit.add(target + ":" + count.toString());
					}
				}
			}

			String outputTargets = "";
			for (String target : targetsToEmit) {
				outputTargets += target + ",";
			}

			output.collect(new Text(mark + isp + "|"), new Text(outputTargets));
		}

	}

}
