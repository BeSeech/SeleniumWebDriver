package com.beseech.test;

import com.beseech.context.Context;
import com.beseech.http.WebTools;
import com.beseech.model.Organization;
import com.beseech.model.TestConfig;
import com.beseech.model.User;
import com.beseech.page.*;
import com.beseech.tools.Config;
import com.beseech.tools.Tools;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.*;
import org.junit.rules.TestName;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.chrome.ChromeDriver;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

//mvn -Dtest=OnBoarding test

public class OnBoarding {
    private Context _context;
    private static TestConfig _testConfig;

    private final static int WAIT_AFTER_SUBMIT_MS = 2000;

    @Rule
    public TestName testName = new TestName();

    @BeforeClass
    public static void loadTestConfig() throws Exception {
        _testConfig = Config.load();
    }

    @Before
    public void setContext() throws Exception {
        System.out.println();
        System.out.println("===== Begin test: " + testName.getMethodName() + " =====");
        _context = new Context(new ChromeDriver(), _testConfig);
        //_context = new Context(new ChromeDriver(), "airslate-rc.xyz", "Api.airslate-rc.xyz/v1");
        //_context = new Context(new ChromeDriver(), "airslate-dev.xyz", "Api.airslate-dev.xyz/v1");
    }

    @After
    public void closeContext() {
        _context.сlose();
        System.out.println("===== Test completed: " + testName.getMethodName() + " =====");
        System.out.println();
    }

    @Test
    public void directInviteForNewUser() throws Exception {
        try {
            User owner = new User(_context.uid);
            owner = WebTools.createUserWithAnOrganization(_context, _context.uid, owner);

            Organization organization = generateOrgToJoin(owner);
            _context.setUrlSubdomainPart(_context.uid);
            User user = new User(UUID.randomUUID().toString());
            String inviteUrl = makeDirectInviteLink(user, organization, owner.getToken());
            _context.getWebDriver().navigate().to(inviteUrl);
            sleep(WAIT_AFTER_SUBMIT_MS);

            User tempUser = new User(UUID.randomUUID().toString());
            DirectJoin directJoin = new DirectJoin(_context);
            directJoin.doDirectJoinUser(tempUser.getFirstName(), tempUser.getLastName(), tempUser.getPassword());
            sleep(WAIT_AFTER_SUBMIT_MS);

            Home home = new Home(_context);
            home.initElements();
            String expectedLogoText = organization.getName().isEmpty() ? organization.getSubdomain() : organization.getName();
            Assert.assertTrue("We're org logo text is: " + expectedLogoText, expectedLogoText.equalsIgnoreCase(home.getLogoText()));
        } catch (Exception e) {
            System.err.println("Error is: " + e.getMessage());
            Tools.takeSnapShot(_context.getWebDriver(), "ErrorScreen/" + testName.getMethodName() + "_ErrorScreen.png");
            throw e;
        }
    }

    @Test
    public void directInviteForRegisteredAnonUser() throws Exception {
        try {
            System.out.println("Creating the organization owner");
            User owner = new User(_context.uid);
            owner = WebTools.createUserWithAnOrganization(_context, _context.uid, owner);

            Organization organization = generateOrgToJoin(owner);

            String uid = UUID.randomUUID().toString();
            System.out.println("Creating the organization member");
            User user = new User(uid);
            user = WebTools.createUserWithAnOrganization(_context, uid, user);

            String inviteUrl = makeDirectInviteLink(user, organization, owner.getToken());
            _context.getWebDriver().navigate().to(inviteUrl);
            sleep(WAIT_AFTER_SUBMIT_MS);

            DirectJoin directJoin = new DirectJoin(_context);
            directJoin.doDirectJoinUser(user.getFirstName(), user.getLastName(), user.getPassword());
            sleep(WAIT_AFTER_SUBMIT_MS);

            sleep(WAIT_AFTER_SUBMIT_MS);
            LoginPage loginPage = new LoginPage(_context);
            loginPage.initElements();
            Assert.assertTrue("The prefilled email is: " + user.getEmail(), user.getEmail().equals(loginPage.getUserNameInput().getText()));
            loginPage.setPasswordInput(user.getPassword());
            loginPage.submit();
            sleep(WAIT_AFTER_SUBMIT_MS);

            sleep(WAIT_AFTER_SUBMIT_MS);
            _context.setUrlSubdomainPart(_context.uid);

            Home home = new Home(_context);
            home.initElements();
            String expectedLogoText = organization.getName().isEmpty() ? organization.getSubdomain() : organization.getName();
            Assert.assertTrue("We're org logo text is: " + expectedLogoText, expectedLogoText.equalsIgnoreCase(home.getLogoText()));
        } catch (Exception e) {
            System.err.println("Error is: " + e.getMessage());
            Tools.takeSnapShot(_context.getWebDriver(), "ErrorScreen/" + testName.getMethodName() + "_ErrorScreen.png");
            throw e;
        }
    }

    @Test
    public void directInviteForLoggedInUser() throws Exception {
        try {
            System.out.println("Creating the organization owner");
            User owner = new User(_context.uid);
            owner = WebTools.createUserWithAnOrganization(_context, _context.uid, owner);

            Organization organization = generateOrgToJoin(owner);

            String uid = UUID.randomUUID().toString();
            System.out.println("Creating the organization member");
            User user = new User(uid);
            user = WebTools.createUserWithAnOrganization(_context, uid, user);

            login(_context, user, true);
            sleep(WAIT_AFTER_SUBMIT_MS);

            _context.setUrlSubdomainPart(_context.uid);

            String inviteUrl = makeDirectInviteLink(user, organization, owner.getToken());
            _context.getWebDriver().navigate().to(inviteUrl);
            sleep(WAIT_AFTER_SUBMIT_MS);

            Home home = new Home(_context);
            home.initElements();
            String expectedLogoText = organization.getName().isEmpty() ? organization.getSubdomain() : organization.getName();
            Assert.assertTrue("We're org logo text is: " + expectedLogoText, expectedLogoText.equalsIgnoreCase(home.getLogoText()));
        } catch (Exception e) {
            System.err.println("Error is: " + e.getMessage());
            Tools.takeSnapShot(_context.getWebDriver(), "ErrorScreen/" + testName.getMethodName() + "_ErrorScreen.png");
            throw e;
        }
    }

    @Test
    public void directFlow() throws Exception {
        Context context = _context;
        User user = new User(context.uid);
        try {
            passLoginPage(context);
            passOnboardingPage(context, context.uid);
            context.setUrlSubdomainPart(context.uid);
            passOnboardingAccountPage(context, user);

            confirmEmail(context, getPinCode(context));
            passOnboardingAboutCompany(context, "Name-" + context.uid);
            passOnboardingAddUser(context);

            Home home = new Home(_context);
            home.initElements();
            Assert.assertNotNull("There is logo text web element", home.logoText);
        } catch (Exception e) {
            System.err.println("Error is: " + e.getMessage());
            Tools.takeSnapShot(_context.getWebDriver(), "ErrorScreen/" + testName.getMethodName() + "_ErrorScreen.png");
            throw e;
        }
    }

    @Test
    public void loginToTheFirstOrg() throws Exception {
        Context context = _context;
        try {
            User user = new User(context.uid);
            user = WebTools.createUserWithAnOrganization(context, context.uid, user);
            Organization organization = generateOrgToJoin(user);

            login(context, user, true);
            sleep(WAIT_AFTER_SUBMIT_MS);

            DomainSelect domainSelect = new DomainSelect(context);
            domainSelect.initElements();
            domainSelect.printOrganizations();
            domainSelect.launchOrganization(0);
            sleep(WAIT_AFTER_SUBMIT_MS);

            Home home = new Home(_context);
            home.initElements();
            String expectedLogoText = organization.getName().isEmpty() ? organization.getSubdomain() : organization.getName();
            Assert.assertNotNull("There is logo text web element", home.logoText);
        } catch (Exception e) {
            System.err.println("Error is: " + e.getMessage());
            Tools.takeSnapShot(_context.getWebDriver(), "ErrorScreen/" + testName.getMethodName() + "_ErrorScreen.png");
            throw e;
        }
    }

    @Test
    public void emailDomainPartJoinNewUser() throws Exception {
        Context context = _context;
        try {
            Organization organization = generateOrgToJoin(null);
            context.setUrlSubdomainPart(context.uid);
            context.getWebDriver().navigate().to(context.getBaseUrl() + "/invite");
            sleep(WAIT_AFTER_SUBMIT_MS);

            User user = new User(UUID.randomUUID().toString());
            EmailJoin emailJoin = new EmailJoin(context);
            emailJoin.doEmailJoinUser(user.getFirstName(), user.getLastName(), user.getPassword(), user.getEmail());
            sleep(WAIT_AFTER_SUBMIT_MS);

            String pin = getPinCode(context);
            confirmEmail(context, pin);

            sleep(WAIT_AFTER_SUBMIT_MS);
            Home home = new Home(_context);
            home.initElements();
            String expectedLogoText = organization.getName().isEmpty() ? organization.getSubdomain() : organization.getName();
            Assert.assertTrue("We're org logo text is: " + expectedLogoText, expectedLogoText.equalsIgnoreCase(home.getLogoText()));
        } catch (Exception e) {
            System.err.println("Error is: " + e.getMessage());
            Tools.takeSnapShot(_context.getWebDriver(), "ErrorScreen/" + testName.getMethodName() + "_ErrorScreen.png");
            throw e;
        }
    }

    private String makeDirectInviteLink(User forUser, Organization toOrganization, String ownerToken) throws Exception {
        ArrayList<User> users = WebTools.directInviteUser(_context, toOrganization.getId(), new String[]{forUser.getEmail()}, ownerToken);
        String token = WebTools.getDirectInviteToken(_context, toOrganization.getId(), users.get(0).getId());

        String email = users.get(0).getEmail();
        email = Base64.getEncoder().encodeToString(email.getBytes());
        String inviteUrl = _context.getBaseInviteUrl(toOrganization.getSubdomain()) + String.format("/invite?token=%s&email=%s&organizationId=%s&subdomain=%s", token, email, toOrganization.getId(), toOrganization.getSubdomain());
        System.out.println("InviteUrl is: " + inviteUrl);
        return inviteUrl;
    }

    private Organization generateOrgToJoin(User byUser) throws Exception {
        Context context = _context;

        User user = byUser;
        if (user == null) {
            user = new User(context.uid);
            user = WebTools.createUserWithAnOrganization(context, context.uid, user);
        }

        List<Organization> organizations = WebTools.getOrganizatons(context, user);
        for (int i = 0; i < organizations.size(); i++) {
            if (!organizations.get(i).getAutogenerated()) {
                return organizations.get(i);
            }
        }
        return null;
    }


    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (Exception e) {
            System.err.println("OnBoarding.sleep: " + e.getMessage());
        }
    }

    private String getPinCode(Context context) {
        try {
            //waiting for saving tempUserId to LocalStorage
            sleep(2000);

            //String tmp = WebTools.getItemFromLocalStorage(context.getWebDriver(), "onboarding");

            JavascriptExecutor js = ((JavascriptExecutor) context.getWebDriver());
            String tmp = (String) js.executeScript("return JSON.stringify(getState())").toString();

            //System.out.println("tmp:" + tmp);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonObject = mapper.readTree(tmp);
            String tempUserId = "";
            if (jsonObject.get("root").get("tempUser") != null) {
                tempUserId = jsonObject.get("root").get("tempUser").toString().replaceAll("\"", "");
            } else {
                tempUserId = jsonObject.get("join").get("tempUser").toString().replaceAll("\"", "");
            }
            return WebTools.getPin(context, tempUserId);
        } catch (Exception e) {
            System.err.println("OnBoarding.getPinCode: " + e.getMessage());
        }
        return "";
    }

    private void login(Context context, User user, Boolean withLoad) {
        LoginPage loginPage = new LoginPage(context);
        if (withLoad) {
            loginPage.load();
        }
        sleep(WAIT_AFTER_SUBMIT_MS);
        loginPage.initElements();
        loginPage.isLoaded();
        loginPage.setUserNameInput(user.getEmail());
        loginPage.setPasswordInput(user.getPassword());
        loginPage.submit();
        sleep(WAIT_AFTER_SUBMIT_MS);
    }

    private void passLoginPage(Context context) {
        LoginPage loginPage = new LoginPage(context);
        loginPage.loadWithInitializationAndCheck();
        loginPage.getStartedClick();
        sleep(WAIT_AFTER_SUBMIT_MS);
    }

    private void passOnboardingPage(Context context, String subdomainText) {
        OnboardingPage onboardingPage = new OnboardingPage(context);
        onboardingPage.initElements();
        onboardingPage.setSubdomainText(subdomainText);
        onboardingPage.submit();
        sleep(WAIT_AFTER_SUBMIT_MS);
    }

    private void passOnboardingAccountPage(Context context, User user) {
        OnboardingAccount onboardingAccountPage = new OnboardingAccount(context);
        onboardingAccountPage.initElements();
        String expectedOnBoardingLink = context.getUrlSubdomainPart() + "." + context.getUrlPostfix();
        Assert.assertTrue("The onboarding link is " + expectedOnBoardingLink,
                onboardingAccountPage.linkToOnBoarding.getText().equals(expectedOnBoardingLink));
        onboardingAccountPage.setFirstName(user.getFirstName());
        onboardingAccountPage.setLastName(user.getLastName());
        onboardingAccountPage.setEmail(user.getEmail());
        onboardingAccountPage.setPassword(user.getPassword());
        onboardingAccountPage.submit();
        sleep(WAIT_AFTER_SUBMIT_MS);
    }

    private void confirmEmail(Context context, String pin) throws Exception {
        if (pin.isEmpty()) {
            throw new Exception("confirmEmail: the pin is empty");
        }
        ConfirmEmail confirmEmailPage = new ConfirmEmail(context);
        confirmEmailPage.initElements();
        confirmEmailPage.setCode(pin);
        confirmEmailPage.submit();
        sleep(WAIT_AFTER_SUBMIT_MS);
    }

    private void passOnboardingAboutCompany(Context context, String name) {
        OnboardingAboutCompany onboardingAboutCompany = new OnboardingAboutCompany(context);
        onboardingAboutCompany.initElements();
        onboardingAboutCompany.setName(name);
        onboardingAboutCompany.submit();
        sleep(WAIT_AFTER_SUBMIT_MS);
    }

    private void passOnboardingAddUser(Context context) {
        OnboardingAddUser onboardingAddUser = new OnboardingAddUser(context);
        onboardingAddUser.initElements();
        onboardingAddUser.submit();
    }

    private void createUser(String email, String password) {
        User user = new User(email, password);

    }


}
