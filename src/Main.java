import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Parser p = new Parser("https://en.wikipedia.org/wiki/Summer_Olympic_Games#");

        System.out.println(p.mostControversial());

        while (true) {
            try {
                System.out.println("\nChoose an option:");
                System.out.println("1. List all past and present Olympic sports that start with the letter " +
                        "__[capitalized character]__");
                System.out.println("2. List all countries that have participated in the Olympics, but are " +
                        "now considered “obsolete");
                System.out.println("3. List all countries that have won at least ___ silver medals in __[year]__");
                System.out.println("4. List all countries that had podium sweeps in __[year]__");
                System.out.println("5. How many total medals has the __[country]__ won in __[sport]__?");
                System.out.println("6. How many governing bodies of the past or present sports from the Summer " +
                        "Olympics are headquartered in __[country]__?");
                System.out.println("7. Among all Summer Olympics hosted in the __[country]__ since __[year]__, how " +
                        "many countries did the torch relay that covered the longest total distance pass through");
                System.out.println("8. Among all countries that have hosted the Olympics, which country has had the " +
                        "most amount of scandals, controversies, or incidents happen when they were hosting?");
                System.out.println("0. Exit");

                System.out.print("----------------------------------------------------------------------------------" +
                        "----------------------------------------\n\n");


                System.out.print("Your choice: ");

                String input = scanner.nextLine().trim();

                System.out.print("----------------------------------------------------------------------------------" +
                        "----------------------------------------\n");


                switch (input) {
                    case "1":
                        System.out.print("Enter the starting letter: ");
                        String letter = scanner.nextLine().toUpperCase();
                        System.out.println(p.sportStartWith(letter));
                        break;
                    case "2":
                        System.out.println(p.participateObsolete());
                        break;
                    case "3":

                        System.out.print("Enter number of medals: ");
                        int num = Integer.parseInt(scanner.nextLine());
                        System.out.print("Enter year: ");
                        int year = Integer.parseInt(scanner.nextLine());
                        System.out.println(p.silverMetalsByYear(num, year));

                        System.out.println("Please enter valid numbers.");

                        break;
                    case "4":
                        System.out.print("Enter year (e.g. 2012): ");
                        String podiumYear = scanner.nextLine();
                        System.out.println("Podium sweep: " + p.podiumSweep(podiumYear));
                        break;
                    case "5":
                        System.out.print("Enter country: ");
                        String country = scanner.nextLine();
                        System.out.print("Enter sport: ");
                        String sport = scanner.nextLine();
                        System.out.println("Total medals: " + p.totalMedalSport(country, sport));
                        break;
                    case "6":
                        System.out.print("Enter country: ");
                        String hqCountry = scanner.nextLine();
                        System.out.println(p.summerSportHeadQuarter(hqCountry));
                        break;
                    case "7":
                        System.out.print("Enter country: ");
                        String relayCountry = scanner.nextLine();
                        System.out.print("Enter year: ");
                        int relayYear = Integer.parseInt(scanner.nextLine());
                        System.out.println("torch relay that covered the longest " +
                                "total distance covered " + p.countriesLongestRelay(relayCountry, relayYear)
                                + " countries.");
                        System.out.println("Please enter a valid year.");
                        break;
                    case "8":
                        System.out.println(p.mostControversial());
                        break;
                    case "0":
                        System.out.println("Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number between 0 and 7.");
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }

        }
    }
}