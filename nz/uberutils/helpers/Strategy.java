package nz.uberutils.helpers;

/**
 * Created by IntelliJ IDEA.
 * User: Taylor
 * Date: 2/20/11
 * Time: 11:51 AM
 * Package: nz.uberfalconry;
 */
public interface Strategy
{
    public void execute();

    public boolean isValid();

    public String getStatus();
}
