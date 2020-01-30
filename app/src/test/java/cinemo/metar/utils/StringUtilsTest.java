package cinemo.metar.utils;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Shahbaz Hashmi on 2020-01-29.
 */
public class StringUtilsTest {


    @Test
    public void trim_htmlString_expected() {
        String result = StringUtils.getBetweenString("<td><a href=\"A302.TXT\">A302.TXT</a></td>", "<td><a href=\"A302.TXT\">", "</a></td>");
        System.err.print(result);
        assertNotNull(result);
    }

    @Test
    public void trim_anchorTag_textExpected() {
        String result = StringUtils.getAnchorTagText("<td><a href=\"A302.TXT\">A302.TXT</a></td>");
        System.err.print(result);
        assertNotNull(result);
    }

    @Test
    public void removeHtmlTags_textExpected() {
        String result = StringUtils.removeHtmlTags("<td align=\"right\">30-Jan-2020 04:35");
        System.err.print(result);
        assertNotNull(result);
    }
}