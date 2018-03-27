package utils;

public class BrowserHolder {
  private static ThreadLocal<Browser> browserHolder = new ThreadLocal();
  private static ThreadLocal<Boolean> browserCreatedHolder = new ThreadLocal();

  public BrowserHolder() {
  }

  public static Browser getBrowser() {
    Browser b = browserHolder.get();
    if(b == null) {
      b = new Browser();
      browserHolder.set(b);
      browserCreatedHolder.set(Boolean.valueOf(true));
    }

    return b;
  }

  public static void setBrowser(Browser browser) {
    browserHolder.set(browser);
  }

  public static boolean isBrowserCreated() {
    Boolean created = (Boolean)browserCreatedHolder.get();
    return created == null?false:((Boolean)browserCreatedHolder.get()).booleanValue();
  }

  public static void setBrowserCreated(boolean browserCreated) {
    browserCreatedHolder.set(Boolean.valueOf(browserCreated));
  }

}