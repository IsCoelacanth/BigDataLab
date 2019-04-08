public class Main {

    public static void main(String[] args) {
        String s = "16";

        System.out.println(s.hashCode());
        System.out.println((int)((Math.log10(s.hashCode() & -s.hashCode())/Math.log10(2))));
        System.out.println(s.hashCode().toString());
    }
}
