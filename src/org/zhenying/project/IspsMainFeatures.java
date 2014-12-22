package org.zhenying.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.FileInputFormat;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.JobClient;
import org.apache.hadoop.mapred.JobConf;
import org.apache.hadoop.mapred.SequenceFileInputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.log4j.Logger;

public class IspsMainFeatures extends Configured implements Tool {
	protected static final Logger LOG = Logger
			.getLogger(IspsMainFeatures.class);

	protected static ArrayList<ArrayList<String>> names2D = new ArrayList<ArrayList<String>>();

	static {
		// Create name structure
		for (String singleIspNicknames : Isps.getNewIsps()) {
			ArrayList<String> allIspNicknames = new ArrayList<String>();
			// Normalize and add nickname to list
			for (String ispNickname : singleIspNicknames.split(",")) {
				String t = ispNickname.toLowerCase()
						.replaceAll("[^a-zA-Z0-9 ]", "").trim();
				allIspNicknames.add(t);
			}

			names2D.add(allIspNicknames);
		}
	}

	@Override
	public int run(String[] arg0) throws Exception {
		JobConf job = new JobConf(this.getConf());

		job.setJarByClass(IspsMainFeatures.class);

		inputDatasource(job);
		FileOutputFormat.setOutputPath(job, new Path("data/output"));
		FileOutputFormat.setCompressOutput(job, false);

		job.setInputFormat(SequenceFileInputFormat.class);

		job.setMapOutputKeyClass(IntWritable.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputFormat(TextOutputFormat.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);

		job.setMapperClass(IspsMapper.AnalysisMapper.class);
		job.setReducerClass(IspsReducer.AnalysisReducer.class);

		if (JobClient.runJob(job).isSuccessful()) {
			return 0;
		} else {
			return 1;
		}
	}

	private void inputDatasource(JobConf job) {
		// Local Mode
		// FileInputFormat.addInputPath(job, new
		// Path("data/input/textData-01666"));

		// Grab the valid file list from s3.
		String crendential = s3crendential.getPremission();
		String segmentListFile = "s3n://"
				+ crendential
				+ "aws-publicdatasets/common-crawl/parse-output/valid_segments.txt";

		FileSystem fsInput = null;
		try {
			fsInput = FileSystem.get(new URI(segmentListFile), job);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fsInput.open(new Path(segmentListFile))));

			String segmentId;

			int cnt = 0;
			while ((segmentId = reader.readLine()) != null && cnt < 2) {
				String inputPath = "s3n://"
						+ crendential
						+ "aws-publicdatasets/common-crawl/parse-output/segment/"
						+ segmentId + "/textData-*";
				FileInputFormat.addInputPath(job, new Path(inputPath));

				cnt++;
			}

		} catch (IOException e) {
			System.out.println("IO exception");
		} catch (URISyntaxException e) {
			System.out.println("URI exception");
		}

	}

	public static void main(String[] args) throws Exception {
		Configuration conf = new Configuration();
		IspsMainFeatures inst = new IspsMainFeatures();
		int res = ToolRunner.run(conf, inst, args);
		System.exit(res);
	}
}