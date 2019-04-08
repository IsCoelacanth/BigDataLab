package hadoop;

import java.io.IOException;

import algos.*;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;


public class FMMapper extends Mapper<LongWritable, Text, Text, IntWritable>{
    private static final int EMPTY = 0;
    FlajoletMartin fm;

    @Override
    protected void setup(org.apache.hadoop.mapreduce.Mapper.Context context)
            throws IOException, InterruptedException {
        super.setup(context);
        fm = new FlajoletMartin(4);
    }

    @Override
    public void map(LongWritable key, Text value, Context context)
        throws IOException, InterruptedException {
        String stream = value.toString();
        String id = stream.substring(0, 4);
        stream = stream.substring(5);
        int distinct = fm.uniques(stream);
//        System.out.println(distinct);
        context.write(new Text(id), new IntWritable(distinct));
//        System.out.println(stream);

    }
}