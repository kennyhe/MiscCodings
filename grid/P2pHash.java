
public class P2pHash implements Hash {

    @Override
    public double[] getHash(String seed, int d) {
    	Dbg.verbose("Call Hash");
        int i, j;

        // Create the byte matrix
        int c = seed.length() / d;
        if (seed.length() % d > 0)
            c++;
        
        byte tmp[][] = new byte[c][d];
        
        for (i = 0; i < seed.length(); i++)
            tmp[i / d][i % d] = seed.getBytes()[i];
        
        for (i = seed.length() - (c - 1) * d; i < d - 1; i++)
            tmp[c - 1][i] = 0;
        
        // Reverse the odd numbers
        byte t;
        for (i = 0; i < c; i++) {
            if (i % 2 == 0) {    // odd line, but even index
                for (j = 0; j < d / 2; j++) {
                    t = tmp[i][j];
                    tmp[i][j] = reverse(tmp[i][d - 1 - j]);
                    tmp[i][d - 1 - j] = reverse(t);
                }
                if (d % 2 == 1) {
                    tmp[i][d/2] = reverse(tmp[i][d/2]);
                }
            }
        }
        
        // Get the XOR of the bytes.
        byte[] b = new byte[d];
        for (j = 0; j < d; j++) {
            b[j] = tmp[0][j];
            for (i = 1; i < c; i++) {
                b[j] ^= tmp[i][j];
            }
            Dbg.verbose(Byte.toString(b[j]));
        }
        
        double[] x = new double[d];
        for (j = 0; j < d; j++) {
            x[j] = b[j] / 256.0d;
            if (x[j] < 0)
            	x[j] += 1.0D;
        }
        
        return x;
    }
    
    
    private byte reverse(byte b) {
        byte b0,b1,b2,b3,b4,b5,b6,b7, t;
        
        b0 = (byte) (b & 1);
        b1 = (byte) (b & 2);
        b2 = (byte) (b & 4);
        b3 = (byte) (b & 8);
        b4 = (byte) (b & 16);
        b5 = (byte) (b & 32);
        b6 = (byte) (b & 64);
        b7 = (byte) (b & 128);
        
        t = b0;
        b0 = (byte) (b7 >> 7);
        b7 = (byte) (t << 7);
        t = b1;
        b1 = (byte) (b6 >> 5);
        b6 = (byte) (t << 5);
        t = b2;
        b2 = (byte) (b5 >> 3);
        b5 = (byte) (t << 3);
        t = b3;
        b3 = (byte) (b4 >> 1);
        b4 = (byte) (t << 1);
        
        return (byte) (b0|b1|b2|b3|b4|b5|b6|b7);
    }

}
