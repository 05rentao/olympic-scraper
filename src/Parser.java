import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Parser {
    URLGetter url;
    private String content;
    Matcher m;

    Parser(String url) {
        this.url = new URLGetter(url.toString());
        updateURL();
    }

    public void updateURL(){
        this.url.printStatusCode();

        ArrayList<String> page = this.url.getContents();
        StringBuilder contentBuilder = new StringBuilder();
        for (String line : page) {
            contentBuilder.append(line);
        }
        this.content = contentBuilder.toString();
    }


    public void newExpression(String expression) {
        Pattern p = Pattern.compile(expression);
        m = p.matcher(this.content);
    }

    public ArrayList<String> groups(int num) {
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

        newExpression("<td[^>]*><a\\b[^>]*>("+ s + "[^<]+)</a></td>");

        return groups(1);
    }

    public ArrayList<String> participateObsolete() {
        newExpression("<a href=\"([^\"]*)\" title=\"List of participating nations at the Summer Olympic Games\"");
        String hierComponent = groups(1).get(0);
        String newUrl = "https://en.wikipedia.org" + hierComponent;
        url = new URLGetter(newUrl);
        updateURL();

        String regex = "<td align=\"left\" bgcolor=\"#e0e0e0\".*?id=\"(\\w{3})\".*?<i>(\\w{3})</i>";
        newExpression(regex);
        // TODO: understand the above equation and finish remaining questions
        return groups(1);


//        ArrayList<String> participate = new ArrayList<>();
//        return participate;
    }

}
// <td align="left" bgcolor="#e0e0e0"><span style="padding-left:1em;" id="ANZ"><i><span class="mw-image-border" typeof="mw:File"><span><img alt="" src="//upload.wikimedia.org/wikipedia/commons/thumb/5/51/Flag_of_Australasian_team_for_Olympic_games.svg/40px-Flag_of_Australasian_team_for_Olympic_games.svg.png" decoding="async" width="22" height="11" class="mw-file-element" srcset="//upload.wikimedia.org/wikipedia/commons/thumb/5/51/Flag_of_Australasian_team_for_Olympic_games.svg/60px-Flag_of_Australasian_team_for_Olympic_games.svg.png 2x" data-file-width="1000" data-file-height="500" /></span></span>&#160;<a href="/wiki/Australasia_at_the_Olympics" title="Australasia at the Olympics">Australasia</a></i></span><a href="#ANZ_note"> <sup>[^]</sup></a></td>
// <td align="left" bgcolor="#e0e0e0"><span style="padding-left:1em;" id="ROC"><i><span class="mw-image-border" typeof="mw:File"><span><img alt="" src="//upload.wikimedia.org/wikipedia/commons/thumb/7/72/Flag_of_the_Republic_of_China.svg/40px-Flag_of_the_Republic_of_China.svg.png" decoding="async" width="22" height="15" class="mw-file-element" srcset="//upload.wikimedia.org/wikipedia/commons/thumb/7/72/Flag_of_the_Republic_of_China.svg/60px-Flag_of_the_Republic_of_China.svg.png 2x" data-file-width="900" data-file-height="600" /></span></span>&#160;<a href="/wiki/Republic_of_China_at_the_Olympics" title="Republic of China at the Olympics">Republic of China</a></i></span><a href="#ROC_note"> <sup>[^]</sup></a></td>
// <td align="left" bgcolor="#e0e0e0"><span style="padding-left:1em;" id="TCH"><i><span class="mw-image-border" typeof="mw:File"><span><img alt="" src="//upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Flag_of_the_Czech_Republic.svg/40px-Flag_of_the_Czech_Republic.svg.png" decoding="async" width="22" height="15" class="mw-file-element" srcset="//upload.wikimedia.org/wikipedia/commons/thumb/c/cb/Flag_of_the_Czech_Republic.svg/60px-Flag_of_the_Czech_Republic.svg.png 2x" data-file-width="900" data-file-height="600" /></span></span>&#160;<a href="/wiki/Czechoslovakia_at_the_Olympics" title="Czechoslovakia at the Olympics">Czechoslovakia</a></i></span><a href="#CZE_note"> <sup>[^]</sup></a></td>
