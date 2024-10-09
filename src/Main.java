public class Main {
    public static void main(String[] args) {
        Application app = new Application();
        try {
            app.start();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}