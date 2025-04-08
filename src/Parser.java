import java.util.ArrayList;
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
        if (s.length() > 1 || !Character.isLetter(s.charAt(0))) {
            throw new IllegalArgumentException("sportStartWith: not a single letter");
        }
//<td style="background: #D3D3D3;"><a href="/wiki/Jeu_de_paume_at_the_1908_Summer_Olympics" title="Jeu de paume at the 1908 Summer Olympics">Jeu de paume</a></td>

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
        navigateWiki("<li><a href=\"([^\"]+)\" title=\"" + year + " Summer Olympics\"");

        getSection("<h3 id=\"Podium_sweeps\">Podium sweeps</h3>(.*?)h2");

        String regex = "<tr><td.*?<td><span .*?title=[^>]*>([^<]+)</a>";
        newExpression(regex);
        ArrayList<String> countries = groups(1);
        System.out.println(countries.size());
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
            System.out.println(countries.size());
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

        String regex = "<tr>\\w*<td><a href=\"[^\"]+\" title=\"[^\"]+\">[^<]*</a</td>\\w+" +
                "<td><a href=\"([^\"]+)\" title=\"[^\"]+\">[^<]*</a></td>";
        newExpression(regex);
        ArrayList<String> links = groups(1);

        ArrayList<String> sports = new ArrayList<>();

        for (String link : links) {
            navigateWiki(link);
            m = p.matcher(this.content);
            String sport = "";
            String hq = "";

            String regexHQ = "";
            // TODO: work on this regex to find headquarters for all sites.
            newExpression(regexHQ);

            if (m.find()) {
                sport = m.group(1);
                hq = m.group(2);
            } else {
                throw new NoSuchElementException("summerSportHeadQuarter: cannot match sport/hq");
            }

            if (hq.equals("Lausanne")) {
                sports.add(sport);
            }
        }

        return sports.size();

    }

}