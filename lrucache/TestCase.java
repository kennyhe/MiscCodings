class TestCase {
    public static void main(String [] args) {
        int x = 0;
        System.out.println("BOUND 64");
        for (int i = 1; i < 1000000; i++) {
            int a = rand(100000);
            if (a % 200 == 0) {
               System.out.println("DUMP");
               System.out.println("BOUND " + (rand(10) + 1) * 20);
               x++;
            }
            if (i > 2000) {
                if (a % 11 == 0) {
                    System.out.println("GET " + randkey());
                }
                if (a % 13 == 0) {
                   System.out.println("PEEK " + randkey());
                }
            }
            if (a % 25 == 0) {
               System.out.println("SET " + randkey() + " " + rands());
            }
        }
    }

    static int rand(int x) {
        return (int)(x * Math.random());
    }

    static String rands() {
        int l = rand(3) + 1;
        char x;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < l; i++) {
            byte c = (byte)rand(6);
            if (c < 3)
                x = (char)('a' + c);
            else
                x = (char)('A' + c - 3);
            sb.append(x);
        }
        return sb.toString();
    }
    static String randkey() {
        int x = rand(10000);
        if (x > 5000) x %= 40;
        else if (x > 3000) x %= 80;
        else if (x > 1000) x %= 160;
        else if (x > 500) x %= 320;
        return "key" + x;
    }
}

