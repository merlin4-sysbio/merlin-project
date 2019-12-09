package pt.uminho.ceb.biosystems.merlin.bioapis.externalAPI.ebi.phobius;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Oscar Dias
 *
 */
public class PhobiusParser {

	public static int getNumberOfHelices(List<String>file) {

		int result = 0;

		for (String line:file) {

			String patternString = ".+\\s+(\\d+)\\s+\\d+.+";

			Pattern pattern = Pattern.compile(patternString);
			Matcher matcher = pattern.matcher(line);

			if(matcher.find())
				result = Integer.parseInt(matcher.group(1));
		}

		return result;
	}
}
