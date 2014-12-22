Comparison-Analysis-of-Different-Telecom-Operators
==================================================

EECS6893 Big Data Analysis Final Project
----
----

<h2>Running instruction: </h2>

<h3>Stand alone mode: </h3>

1. Please first download a warc file from aws-publicdatasets. The sample URI is as below: </br>
s3://aws-publicdatasets/common-crawl/crawl-data/CC-MAIN-2013-48/segments/1386163035819/warc/CC-MAIN-20131204131715-00000-ip-10-33-133-15.ec2.internal.warc.gz</br>
2. Put the file in ./data/input. No need to untar. </br>
3. Annotaion the mathod inputDatasource in class IspsMainFeatures. </br>
4. Add a line like this: FileInputFormat.addInputPath(job, newPath("data/input/textData-01666"));</br>
5. Jar all the classes and use hadoop to envoke it. The output file is in ./data/output/</br>
</br>
<h3>Distributed mode: </h3>

1. In file s3crendential.java, input your aws Access Id and Secret Key. Notice the Secret Key contain "/"s which should be convert to "%2F". </br>
2. Jar all the classes. </br>
3. Create an EMR cluster and upload the Jar. </br>

