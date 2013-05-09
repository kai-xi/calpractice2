/**
 * 
 */

/**
 * This enum represents the set of constants that represent the Days of the week.
 * @author EmilyChen
 *
 */
public enum Day {
	MONDAY("Mon"),
	TUESDAY("Tue"),
	WEDNESDAY("Wed"),
	THURSDAY("Thu"),
	FRIDAY("Fri"),
	SATURDAY("Sat"),
	SUNDAY("Sun");

	private String text;

	Day(String text)	{
		this.text = text;
	}

	public String getText()	{
		return this.text;
	}

	public static Day fromString(String input)	{
		if (input!=null)	{
			for (Day dayOfWeek : Day.values())	{
				if (dayOfWeek.getText().equalsIgnoreCase(input))	{
					return dayOfWeek;
				}
			}
		}
		return null;
	}
}
