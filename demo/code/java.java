class Main {
    public static void main(String[] args) throws Exception {
        for (int i = 0; i < 10; i++) {
            Thread.sleep(1000);
            System.out.println("Hello World Java " + i);
            System.err.println("Hello World Java " + i);
        }
    }
}