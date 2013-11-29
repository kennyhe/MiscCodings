class TestLRU {

    public static void main(String[] args) {
        if (args.length > 0) {
            if (args[0].equals("testdata")) {
                generateTestData();
            }
        }
        // test1(); // Hard coded test case.
        test2(); // Read test cases from stdin
    }


    private static void println(String s) {
        if (s == null) {
            System.out.println("NULL");
        } else {
            System.out.println(s);
        }
    }
    
    private static void generateTestData() {
    
    }

    private static void test1() {
        LRUCache cache = new LRUCache(2); //BOUND 2
        cache.put("a", "2"); // SET a 2
        cache.put("b", "4"); // SET b 4
        println(cache.get("b")); // GET b
        println(cache.get("a", false)); //PEEK a
        cache.put("c", "5"); // SET c 5
        println(cache.get("a")); // GET a
        println(cache.toString()); // DUMP
    }

    // Read test cases from stdin
    private static void test2() {
        try{
            java.io.BufferedReader stdin = new java.io.BufferedReader(new java.io.InputStreamReader(System.in));
            String line = stdin.readLine();
            if (! line.matches("\\d+")) {
                println("Invalid input! Should input a number which indicates the count of commands lines.");
                return;
            } else {
                int rows = Integer.parseInt(line);
                line = stdin.readLine();
                if (! line.matches("BOUND\\s\\d+")) {
                    println("The first command must be BOUND <cache size>!");
                    return;
                } else {
                    LRUCache cache = new LRUCache(Integer.parseInt(line.substring(6)));
                    for (int i = 1; i < rows; i++) {
                        line = stdin.readLine();
                        if (line.matches("BOUND\\s\\d+")) {
                            cache.setBound(Integer.parseInt(line.substring(6)));
                        } else if (line.matches("GET\\s\\S+")) {
                            println(cache.get(line.substring(4)));
                        } else if (line.matches("PEEK\\s\\S+")) {
                            println(cache.get(line.substring(5), false));
                        } else if (line.matches("DUMP")) {
                            println("\n" + cache.toString() + "\n");
                        } else if (line.matches("SET\\s\\S+\\s\\S+")) {
                            String[] parts = line.split("\\s");
                            cache.put(parts[1], parts[2]);
                        } else {
                            println("Syntax error in line " + i + ":" + line);
                            return;
                        }
                    }
                }
            }
        }catch (java.io.IOException e) {
            System.out.println(e); 
        }
    }
}
