

public class Executa
{

    public Executa()
    {
    }

    public static void main(String args[])
    {
        try
        {
        	if(args.length<1) {
        		return;
        	}
            Process run = Runtime.getRuntime().exec(args[0]);
            Thread.sleep(500);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }
}