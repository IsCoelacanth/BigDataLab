import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.filecache.DistributedCache;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Counters;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;

@SuppressWarnings("deprecation")
public class stopWords {

    public static Path stopWordsFiles;

    public enum COUNTERS {
        STOPWORDS, GOODWORDS
    }

    public static void main(String[] args) throws Exception {

        Configuration conf = new Configuration();
        GenericOptionsParser parser = new GenericOptionsParser(conf, args);
        args = parser.getRemainingArgs();

        Job job = new Job(conf, "StopWordSkipper");
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);
        job.setJarByClass(stopWords.class);
        job.setMapperClass(stopWordMapper.class);
        job.setNumReduceTasks(0);
        job.setInputFormatClass(TextInputFormat.class);
        job.setOutputFormatClass(TextOutputFormat.class);
        stopWordsFiles = new Path(args[2]);


        FileInputFormat.setInputPaths(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(args[1]));
        job.waitForCompletion(true);
        Counters counters = job.getCounters();
        System.out.printf("Good Words: %d, Stop Words: %d\n", counters.findCounter(COUNTERS.GOODWORDS).getValue(),
                counters.findCounter(COUNTERS.STOPWORDS).getValue());
    }
}
