import java.util.Scanner;

public class ParserTester {
    public static void main(String[] args) {

        Parser p = new Parser("https://en.wikipedia.org/wiki/Summer_Olympic_Games#");

        System.out.println(p.sportStartWith("C").toString());
        System.out.println(p.sportStartWith("W").toString());

        System.out.println(p.participateObsolete().toString());

        System.out.println(p.silverMetalsByYear(6, 2016));
        System.out.println(p.silverMetalsByYear(3, 2012));

        System.out.println("podiumSweep: " + p.podiumSweep("2012"));
        System.out.println("podiumSweep: " + p.podiumSweep("2016"));

        System.out.println("totalMedalSport: " + p.totalMedalSport(
                "United States", "Volleyball"));
        System.out.println("totalMedalSport: " + p.totalMedalSport(
                "France", "Archery"));
        System.out.println("totalMedalSport: " + p.totalMedalSport(
                "Austria", "Gymnastics"));
        System.out.println("totalMedalSport: " + p.totalMedalSport(
                "Germany", "Weightlifting"));

        System.out.println(p.summerSportHeadQuarter("Switzerland"));
        System.out.println(p.summerSportHeadQuarter("Germany"));

        System.out.println(p.countriesLongestRelay("United States", 1960));
        System.out.println(p.countriesLongestRelay("Australia", 1960));




    }
}
