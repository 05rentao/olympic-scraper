import java.util.ArrayList;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    URLGetter url;
    final URLGetter origUrl;
    private String content;
    Matcher m;
    Pattern p;

    Parser(String url) {
        this.origUrl = new URLGetter(url);
        this.url = new URLGetter(url);
        updateURL();
    }
    
    void resetURL() {
        url = new URLGetter(origUrl.getURL());
        updateURL();
    }

    public void updateURL() {
        this.url.printStatusCode();

        ArrayList<String> page = this.url.getContents();
        StringBuilder contentBuilder = new StringBuilder();
        for (String line : page) {
            contentBuilder.append(line);
        }
        this.content = contentBuilder.toString();
    }


    public void newExpression(String expression) {
        p = Pattern.compile(expression);
        m = p.matcher(this.content);
    }

    public ArrayList<String> groups(int num) throws NoSuchElementException {
        ArrayList<String> groups = new ArrayList<>();

        if (num < 0) {
            throw new IllegalArgumentException("groups: number must be a positive integer");
        }
        if (num == 0) {
            while (m.find()) {
                groups.add(m.group());
            }
        } else {

            while (m.find()) {
                groups.add(m.group(num));
            }
        }
        if (groups.isEmpty()) {
            throw new NoSuchElementException();
        }
        return groups;
    }
    
    

    public ArrayList<String> groups2() {
        ArrayList<String> groups = new ArrayList<>();
        while (m.find()) {
            groups.add(m.group());
        }
        return groups;
    }


    public ArrayList<String> sportStartWith(String s) {
        resetURL();
        if (s.length() > 1 || !s.matches("^[A-Z]$")) {
            throw new IllegalArgumentException("Input must be a single letter A–Z.");
        }
        newExpression("<td[^>]*><a\\b[^>]*>(" + s + "[^<]+)</a></td>");
        return groups(1);
    }

    public ArrayList<String> participateObsolete() {
        navigateWiki("<a href=\"([^\"]*)\" title=\"List of participating nations at the Summer Olympic Games\"");

        // String regex = "<td align=\"left\" bgcolor=\"#e0e0e0\".*?id=\"(\\w{3})\".*?<i>(\\w{3})</i>";
        String regex2 = "<td align=\"left\" bgcolor=\"#e0e0e0\".*?title=[^>]*>([^<]+)</a>";
        // String regex3 = "<td align=\"left\" bgcolor=\"#e0e0e0\"><span .*?id=\"([^\"]+)\">.*?<a [^>]*>([^<]+)</a>";

        newExpression(regex2);
        ArrayList<String> countries = groups(1);
        System.out.println(countries.size());
        return countries;
    }

    void navigateWiki(String regex) {
        newExpression(regex);
        String hierComponent = groups(1).getFirst();
        String newUrl = "https://en.wikipedia.org" + hierComponent;
        url = new URLGetter(newUrl);
        updateURL();
    }

    public ArrayList<String> silverMetalsByYear(int metals, int year) {
        resetURL();
        if (year % 4 != 0) {
            throw new IllegalArgumentException("not a valid year");
        }
        navigateWiki("<a href=\"([^\"]*)\" title=\"" + year + " Summer Olympics medal table\"");
        String regex = "<tr><t.*?title=[^>]*>([^<]+)</a>(?:<sup.*?</sup>)?.{0,1}</th><td>\\d+</td><td>(\\d+)</td>";
        //String regex = "<tr><td>.*?<a href=[^>]+>([^<]+)</a></th><td>\\d+</td><td>(\\d+)</td>";
        newExpression(regex);
        ArrayList<String> country = new ArrayList<>();
        ArrayList<String> silvers = new ArrayList<>();

        while (m.find()) {
            country.add(m.group(1));
            silvers.add(m.group(2));
        }

//        System.out.println(country);
//        System.out.println("size: " + country.size());
//        System.out.println(silvers);
//        System.out.println("size: " + silvers.size());

        ArrayList<String> validCountries = new ArrayList<>();

        for (int i = 0; i < country.size(); i++) {
            if (Integer.parseInt(silvers.get(i)) >= metals) {
                validCountries.add(country.get(i));
            }
        }
        return validCountries;
    }

    public ArrayList<String> podiumSweep(String year) {
        resetURL();
        if (Integer.parseInt(year) % 4 != 0) {
            throw new IllegalArgumentException("not a valid year");
        }

        resetURL();
        navigateWiki("<li><a href=\"([^\"]+)\" title=\"" + year + " Summer Olympics\"");

        getSection("<h3 id=\"Podium_sweeps\">Podium sweeps</h3>(.*?)h2");

        String regex = "<tr><td.*?<td><span .*?title=[^>]*>([^<]+)</a>";
        newExpression(regex);
        ArrayList<String> countries = groups(1);
        // System.out.println(countries.size());
        return countries;
    }

    void getSection(String regexSection) {
        newExpression(regexSection);
        try {
            content = groups(1).getFirst();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("getSection: regexSection not matched");
        }
    }

    public ArrayList<String> totalMedalSport(String country, String sport) {
        resetURL();
        navigateWiki("<li><a href=\"([^\"]+)\" [^>]*>" + sport + "</a></li>");

        getSection("<h[23] id=\"[^\"]+\">Medal table</h[23]>(.*?)<h[23]");

        String regex = "<tr><td>\\d+</td><th.*?title=[^>]*>" + country + "</a>(?:.*?</span>)?" +
                "</th><td>\\d+</td><td>\\d+</td><td>\\d+</td><td>(\\d+)</td></tr>";
        newExpression(regex);
        try {
            ArrayList<String> countries = groups(1);
            // System.out.println(countries.size());
            return countries;
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("country not matched");
        }
    }

    public int summerSportHeadQuarter(String country) {
        resetURL();
        navigateWiki("<a href=\"([^\"]*)\" title=[^>]*>Association of Summer " +
                "Olympic International Federations</a>");

        getSection("<h2 id=\"[^\"]+\">Members</h2>(.*?)<h2");

        String linkRegex = "<tr>\\s*<td><a [^>]+>[^<]*</a></td>\\s*<td><a href=\"([^\"]+)\" title=\"[^\"]+\">([^<]*)</a>";
        newExpression(linkRegex);
        ArrayList<String> links = new ArrayList<>();
        ArrayList<String> name = new ArrayList<>();
        while (m.find()) {
            links.add(m.group(1));
            // System.out.println(m.group(1));
            name.add(m.group(2));
        }

        ArrayList<String> sports = new ArrayList<>();
        for (int i = 0; i < links.size(); i++) {
            String newUrl = "https://en.wikipedia.org" + links.get(i);
            url = new URLGetter(newUrl);
            updateURL();

            m = p.matcher(this.content);
            String sport = name.get(i);
            String hq = "";

            String regexHQ = "<tr><th [^>]*>Headquarters</th>(?:.*?Country</th>)?" +
                    "<td class=\"infobox[^\"]+\">.*?<a href=[^>]+>([^<]+)</a>(?:, ([^<]+))?<";

            newExpression(regexHQ);

            if (m.find()) {
                hq = m.group(1);
                if (m.group(2) != null) {
                    hq = m.group(2);
                }
                // System.out.println("summerSportHeadQuarter for " + sport + ": " + hq);
            } else {
                // System.out.println("summerSportHeadQuarter: cannot match hq for " + sport);
            }

            if (hq.equals(country)) {
                sports.add(sport);
            }
        }
        return sports.size();

    }

    public int countriesLongestRelay(String country, int year) {
        if (year % 4 != 0) {
            throw new IllegalArgumentException("not a valid year");
        }
        resetURL();
        navigateWiki("<a href=\"([^\"]*)\" title=\"List of Olympic torch relays\">Torch relays</a></li>");

        getSection("<h2 id=\"Summer_Olympic_Games\">Summer Olympic Games</h2>(.*?)<h2");

        String regexLinks = "Main article: <a href=\"(/wiki/\\d{4}_Summer_Olympics_torch_relay)\"[^>]*>(\\d{4}) Summer Olympics torch relay";
        // wiki/1980_Summer_Olympics_torch_relay"
        ArrayList<String> host = new ArrayList<>();
        ArrayList<String> links = new ArrayList<>();
        ArrayList<String> years = new ArrayList<>();
        newExpression(regexLinks);

        while (m.find()) {
            links.add(m.group(1));
            years.add(m.group(2));
        }

        // getting the list of countries for each relay separated by comma
        String regexCountries = "Host city</th><td [^>]*>(?:<a href=.*?</a>)*, ([^<]+)</td></tr>.*?Countries visited</th><td class=\"infobox-data\">([^<]+)</td></tr>";
        newExpression(regexCountries);
        ArrayList<String> countries = new ArrayList<>();
        for (int i = 0; i < links.size(); i++) {
            String newUrl = "https://en.wikipedia.org" + links.get(i);
            url = new URLGetter(newUrl);
            updateURL();
            m = p.matcher(this.content);

            String countryLst = "";
            while (m.find()) {
                if (m.group(1).equals(country) && Integer.parseInt(years.get(i)) > year) {
                    host.add(m.group(1));
                    countryLst = m.group(2);
                    years.add(years.get(i));
                }
//                if (countryLst == null) {
//                    System.out.println(years.get(i) + ": " + links.get(i) + " not matched ");
//                }
            }
            countries.add(countryLst);
        }


        // getting and counting individual countries
        String regex = "[^,]+";
        p = Pattern.compile(regex);
        int max = 0;
        String maxYear = null;
        for (int i = 0; i < countries.size(); i++) {
            int count = 0;
            m = p.matcher(countries.get(i));
            while (m.find()) {
                count++;
                if (count > max) {
                    max = count;
                    maxYear = years.get(i);
                }
            }
        }
        System.out.println("maxYear: " + maxYear);
        return max;
    }

    public Map<String, Integer> mostControversial() {
        resetURL();
        navigateWiki("<a href=\"([^\"]*)\" [^>]*>List of Olympic Games scandals and controversies</a></li>");

        TreeMap<String, Integer> freqMap = new TreeMap<>();
        ArrayList<String> countries = new ArrayList<>();
        ArrayList<String> countryListItems = new ArrayList<>();
        String regex = "<div class=\"mw-heading mw-heading3\"><h3[^>]*>.*?Olympics.*?, ([^<,]+)</h3>.*?/div>(.*?)<div";
        newExpression(regex);
        while (m.find()) {
            countries.add(m.group(1));
            countryListItems.add(m.group(2));
        }

        // counting the number of list items for each olympic, and associating it with the country hosting.
        p = Pattern.compile("<li>");
        for (int i = 0; i < countries.size(); i++) {
            content = countryListItems.get(i);
            m = p.matcher(this.content);
            int liCount = 0;
            while (m.find()) {
                liCount++;
            }
            freqMap.put(countries.get(i), freqMap.getOrDefault(countries.get(i), 0) + liCount);
        }
        return freqMap;
    }

}