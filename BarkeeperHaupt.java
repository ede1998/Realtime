import java.util.*;
public class BarkeeperHaupt
// Implementation über Algorithmic State Machine
{
	public static void main (String[] argument)
	{
		Barkeeper
			bk = new Barkeeper ("Bartender");
		Kunde k1 = new Kunde ("Boris", "        ", bk);
		Kunde k2 = new Kunde ("Fedor", "                ", bk);
		Kunde k3 = new Kunde ("Ivano", "                        ", bk);
	}
	public static void jemandTutWas (String offset, String jemand, String was)
	{
		pausiere ();
		System.out.println (offset + jemand + " " + was);
	}
	private static java.util.Random r = new java.util.Random ();
	public static int zufall () { return BarkeeperHaupt.zufall (6); }
	public static int zufall (int n)
	{
		return Math.abs (r.nextInt ()) % n + 1;
	}
	public static void pausiere ()
	{
		try
		{
			Thread.sleep (BarkeeperHaupt.zufall (200));
		}
		catch (InterruptedException e) { }
	}
} // end BarkeeperHaupt
class Barkeeper
{
	final static int NONE = 0;
	final static int WODKA = 2;
	final static int WHISKEY = 1;

	private int state;
	private String name;
	public Barkeeper (String name)
	{
		this.name = name;
		state = NONE;
	}
	synchronized public void whiskey (String name, String offset)
	{
		while (state == WODKA)
		{
			try { wait(); } catch (InterruptedException e) {}
		}
		state = WHISKEY;

		BarkeeperHaupt.jemandTutWas ("", this.name, "nimmt Whiskeyflasche.");
		BarkeeperHaupt.jemandTutWas ("", this.name, "schenkt " + name + " Whiskey ein.");
		BarkeeperHaupt.jemandTutWas (offset, name, "bekommt Whiskey.");
		BarkeeperHaupt.jemandTutWas (offset, name, "hört Prost Whiskey.");
		BarkeeperHaupt.jemandTutWas ("", this.name, "schaut nach Whiskeykunden.");
		BarkeeperHaupt.jemandTutWas ("", this.name, "stellt Flasche Whiskeyflasche ab.");
		BarkeeperHaupt.jemandTutWas ("", this.name, "spült paar Gläser.");
		BarkeeperHaupt.jemandTutWas ("", this.name, "atmet tief durch.");

		state = NONE;
		notify();
	}
	synchronized public void wodka (String name, String offset)
	{
		while (state == WHISKEY)
		{
			try { wait(); } catch (InterruptedException e) {}
		}
		state = WODKA;

		BarkeeperHaupt.jemandTutWas ("", this.name, "nimmt Wodkaflasche.");
		BarkeeperHaupt.jemandTutWas ("", this.name, "schenkt " + name + " Wodka ein.");
		BarkeeperHaupt.jemandTutWas(offset, name, "bekommt Wodka.");
		BarkeeperHaupt.jemandTutWas(offset, name, "hört Prost Wodka.");
		BarkeeperHaupt.jemandTutWas("", this.name, "schaut nach Wodkakunden.");
		BarkeeperHaupt.jemandTutWas("", this.name, "stellt Flasche Wodkaflasche ab.");
		BarkeeperHaupt.jemandTutWas("", this.name, "spült paar Gläser.");
		BarkeeperHaupt.jemandTutWas("", this.name, "atmet tief durch.");

		state = NONE;
		notify();
	}
}
// end Barkeeper
class Kunde extends Thread
{
	private String name;
	private String offset;
	private Barkeeper bk;
	public
		Kunde (String name, String offset, Barkeeper bk)
		{
			this.name = name;
			this.offset = offset;
			this.bk = bk;
			this.start ();
		}
	int n = 0;
	public void run ()
	{
		java.util.Random r = new java.util.Random ();
		BarkeeperHaupt.jemandTutWas (offset, name, "kommt.");
		while (true)
		{
			n++;
			for (int i = 0; i < BarkeeperHaupt.zufall (3); i++)
			{
				BarkeeperHaupt.jemandTutWas (offset, name + n, "wählt.");
			}
			switch (BarkeeperHaupt.zufall (4))
			{
				case 1:
					BarkeeperHaupt.jemandTutWas (offset, name + n, "bestellt Whiskey.");
					bk.whiskey (name + n, offset);
					break;
				case 2:
					BarkeeperHaupt.jemandTutWas (offset, name + n, "bestellt Wodka.");
					bk.wodka (name + n, offset);
					break;
				case 3:
					BarkeeperHaupt.jemandTutWas (offset, name + n, "bestellt Wodka und Whiskey.");
					Thread t1 = new Thread ()
					{
						public void run ()
						{
							bk.wodka (name + n, offset);
						}
					};
					t1.start ();
					bk.whiskey (name + n, offset);
					try { t1.join (); } catch (InterruptedException e) {}
					break;
				case 4:
					BarkeeperHaupt.jemandTutWas (offset, name + n, "bestellt Whiskey und Wodka.");
					Thread
						t2 = new Thread ()
						{
							public void run ()
							{
								bk.whiskey (name + n, offset);
							}
						};
					t2.start ();
					bk.wodka (name + n, offset);
					try { t2.join (); } catch (InterruptedException e) {}
					break;
			}
			BarkeeperHaupt.jemandTutWas (offset, name + n, "hat genug.");
			BarkeeperHaupt.jemandTutWas (offset, name + n, "geht.");
			BarkeeperHaupt.jemandTutWas (offset, name + n, "kommt wieder.");
		}
	}
}
