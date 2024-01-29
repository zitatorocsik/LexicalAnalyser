import java.io.File;

public class Driver {

    public static void main(String[] args) {
        scanner lexicalanalyser = new scanner("530.4e123-0whileif1");

        for (int i = 0; i < 3; i++) {
            System.out.println(lexicalanalyser.nextToken().toString());

        }

        // File file = new File("lexnegativegrading.src");

    }

}
