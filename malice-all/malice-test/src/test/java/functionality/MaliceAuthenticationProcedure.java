package functionality;

public class MaliceAuthenticationProcedure {

    private final GwtApplicationChromeDriver driver;

    public MaliceAuthenticationProcedure(GwtApplicationChromeDriver driver) {
        this.driver = driver;
    }

    public void login(String username, String password) {
        driver.sendKeysToElementWithHtmlName("j_username", username);
        driver.sendKeysToElementWithHtmlName("j_password", password);
        driver.clickFirstElementDisplayingText("Entra");
    }

    public void logout() {
        driver.clickFirstElementDisplayingText("LOGOUT");
    }

}
