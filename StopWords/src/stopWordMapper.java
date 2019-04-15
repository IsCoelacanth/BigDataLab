import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.Iterator;

import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;

public class StopWords extends Mapper<LongWritable, Text, Text, IntWritable> {
    private final static IntWritable one = new IntWritable(1);
    private Text word = new Text();
    private Set stopWords = new HashSet();

    @Override
    protected void setup(Context context) throws IOException, InterruptedException {
        readFile(StopWordSkipper.stopWordsFiles);
    }

    /**
     * map function of Mapper parent class takes a line of text at a time splits to
     * tokens and passes to the context as word along with value as one
     */
    @Override
    protected void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {

        String line = value.toString();
        StringTokenizer tokenizer = new StringTokenizer(line);

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            if (stopWords.contains(token)) {
                context.getCounter(StopWordSkipper.COUNTERS.STOPWORDS).increment(1L);
            } else {
                context.getCounter(StopWordSkipper.COUNTERS.GOODWORDS).increment(1L);
                word.set(token);
                context.write(word, null);
            }
        }

    }

    private void readFile(Path filePath) {
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath.toString()));
            String stopWord = null;
            while ((stopWord = bufferedReader.readLine()) != null) {
                stopWords.add(stopWord.toLowerCase());
            }
            bufferedReader.close();
        } catch (IOException ex) {
            System.err.println("Exception while reading stop words file: " + ex.getMessage());
        }
    }
}
