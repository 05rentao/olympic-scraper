import java.util.ArrayList;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class can try out URL connections
 */
public class ParserTester {


    public static void main(String[] args) {

        Parser p = new Parser("https://en.wikipedia.org/wiki/Summer_Olympic_Games#");

        System.out.println(p.sportStartWith("C").toString());
        System.out.println(p.sportStartWith("W").toString());
        System.out.println(p.participateObsolete().toString());
    }

}
