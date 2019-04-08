package algos;

import java.io.IOException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

public class FlajoletMartin {

    private static final double PHI = 0.77351D;
    private int hashFunctions;
    private HashFunction[] hashes;
    public FlajoletMartin(int numHashFunctions) {
        this.hashFunctions = numHashFunctions;
        hashes = new HashFunction[hashFunctions];
        generateHashes();

    }

    private void generateHashes() {
        for (int i = 0; i < hashFunctions; i++) {
            hashes[i] = new HashFunction(i);
        }
    }

    public int uniques(String str) {
        List<Integer> averageR = new ArrayList<Integer>();
        String[] stream = str.split(" ");
        for (int i = 0; i < hashFunctions; i++){
            int sumR = 0;
            for (String word : stream) {
                sumR = Math.max(getFirstZero(hashes[i].getHash(word)), sumR);
            }
            averageR.add(sumR);
        }

        Collections.sort(averageR);
        double r = 0;
        int avgMid = averageR.size() / 2;
        if (averageR.size() % 2 == 0) {
            r = (averageR.get(avgMid) + averageR.get(avgMid+1))/2;
        } else {
            r = averageR.get(avgMid + 1);
        }
        return (int) (Math.pow(2, r));

    }

    private int getFirstZero(int val) {
        if (val == 0)
            return 0;
        int pos = 0;
        // counting the position of first set bit
        for (int i = 0; i < 64; i++) {
            if ((val & (1 << i))== 0)
                pos++;

            else
                break;
        }
        return pos;
    }


    private static class HashFunction {
        private int numLoop;

        public HashFunction(int loop) {
            this.numLoop = loop;
        }

        public int getHash(Object s) {
            int hash;
            int i = 0;
            do {
                if (s instanceof String) {
                    hash = s.hashCode() * (i+1);
                    s = Integer.toString(hash);
                } else if (s instanceof Number) {
                    hash = String.valueOf(s).hashCode() * (i+1);
                    s = Integer.toString(hash);
                }
                else {
                    String t = s.toString();
                    hash = t.hashCode() *(i+1);
                    s = Integer.toString(hash);
                }
                i += 1;
            } while (i < numLoop);
            return 13 + 7*Math.abs(hash);
        }

    }

}
