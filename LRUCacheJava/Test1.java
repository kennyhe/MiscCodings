class Test1 {
    public static void main(String[] args) {
        System.out.println("1000003\nBOUND 1000000");
        for (int i = 0; i < 1000000; i ++) {
            System.out.println("SET A" + i + " " + i);
        }
        System.out.println("DUMP");
    }
}
        
