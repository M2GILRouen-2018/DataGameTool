

public class Main {
     public static void main(String[] args) {
         Provider<Double> provider = new RandomProvider();
         while (provider.hasNext() && provider.count() < 1000) {
             System.out.println(String.format("%d : %.2f", provider.count() + 1, provider.next()));
         }
     }
}
