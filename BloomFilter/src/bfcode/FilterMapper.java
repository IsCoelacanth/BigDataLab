package bfcode;
import java.lang.System.*;
import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;
import utils.*;

public class FilterMapper extends Mapper<LongWritable, Text, Text, NullWritable> {
    FilterBloom<String> filter;
    @Override
    protected void setup(org.apache.hadoop.mapreduce.Mapper.Context context)
            throws IOException, InterruptedException {
        super.setup(context);
        double falsePositiveProbability = 0.1;
        int expectedNumberOfElements = 100;
        filter = new FilterBloom<String>(falsePositiveProbability, expectedNumberOfElements);
        filter.add("bad service");
        filter.add("iron man");
        filter.add("marvel");
        filter.add("end game");
    }

    protected void map(LongWritable key, Text value, Context context)
            throws java.io.IOException, InterruptedException {

        String[] tokens = value.toString().split(",");
        for(String token :tokens){
	    System.out.println(token);
            if(filter.contains(token)){
                context.write(value, NullWritable.get());
            }
        }
    }
}
