package com.beseech.http;

import com.beseech.context.Context;
import com.beseech.model.Organization;
import com.beseech.model.User;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.List;

public class WebTools {

    public static String createTempOrganization(Context context, String subdomain) throws Exception {
        HttpResponse httpResponse = Api.Post.temp_organizations(context.getApiBaseUrl(), subdomain);

        if (httpResponse.getStatusLine().getStatusCode() > 299) {
            throw new Exception("createTempOrganization httpResponse code is: " + httpResponse.getStatusLine().getStatusCode());
        }
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(responseBody);
        return actualObj.get("data").get("id").toString().replaceAll("\"", "");
    }

    public static String createTempOrganizationTempUser(Context context, String tempOrgId, User user) throws Exception {
        HttpResponse httpResponse = Api.Post.temp_organizations_temp_users(context.getApiBaseUrl(), tempOrgId, user);
        if (httpResponse.getStatusLine().getStatusCode() > 299) {
            throw new Exception("createTempOrganizationTempUser httpResponse code is: " + httpResponse.getStatusLine().getStatusCode());
        }
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(responseBody);
        return actualObj.get("data").get("id").toString().replaceAll("\"", "");
    }

    public static User createTempOrganizationTempUserConfirm(Context context, String tempOrgId, String tempUserId, String pin) throws Exception {
        HttpResponse httpResponse = Api.Post.temp_organizations_temp_users_confirm(context.getApiBaseUrl(), tempOrgId, tempUserId, pin);

        if (httpResponse.getStatusLine().getStatusCode() > 299) {
            throw new Exception("createTempOrganizationTempUserConfirm httpResponse code is: " + httpResponse.getStatusLine().getStatusCode());
        }
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(responseBody);

        User user = new User();
        user.setId(actualObj.get("data").get("id").toString().replaceAll("\"", ""));
        user.setEmail(actualObj.get("data").get("attributes").get("email").toString().replaceAll("\"", ""));
        user.setFirstName(actualObj.get("data").get("attributes").get("first_name").toString().replaceAll("\"", ""));
        user.setLastName(actualObj.get("data").get("attributes").get("last_name").toString().replaceAll("\"", ""));
        user.setToken(actualObj.get("meta").get("token").toString().replaceAll("\"", ""));
        return user;
    }

    public static User createUserWithAnOrganization(Context context, String subdomain, User user) throws Exception {
        System.out.println(String.format("Create user[subdomain:%s, email:%s, password:%s]", subdomain, user.getEmail(), user.getPassword()));
        String tempOrganizationId = createTempOrganization(context, subdomain);
        String tempUserId = createTempOrganizationTempUser(context, tempOrganizationId, user);
        String pin = getPin(context, tempUserId);
        User newUser = createTempOrganizationTempUserConfirm(context, tempOrganizationId, tempUserId, pin);
        newUser.setPassword(user.getPassword());
        return newUser;
    }

    public static ArrayList<User> directInviteUser(Context context, String orgId, String[] emails, String token) throws Exception {
        HttpResponse httpResponse = Api.Post.organizations_users_invite(context.getApiBaseUrl(), orgId, emails, token);

        if (httpResponse.getStatusLine().getStatusCode() > 299) {
            throw new Exception("directInviteUser httpResponse code is: " + httpResponse.getStatusLine().getStatusCode());
        }
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(responseBody);

        ArrayList<User> users = new ArrayList<User>();
        JsonNode jsonUsers = actualObj.get("data");

        for (int i = 0; i < jsonUsers.size(); i++) {
            User user = new User();
            user.setId(jsonUsers.get(i).get("id").toString().replaceAll("\"", ""));
            user.setEmail(jsonUsers.get(i).get("attributes").get("email").toString().replaceAll("\"", ""));
            users.add(user);
        }

        return users;
    }

    public static String getDirectInviteToken(Context context, String orgId, String userId) throws Exception {
        HttpResponse httpResponse = Api.Get.qa_users_organizations_users_token(context.getApiBaseUrl(), orgId, userId, context.getQaToken());
        if (httpResponse.getStatusLine().getStatusCode() > 299) {
            throw new Exception("getDirectInviteToken httpResponse code is: " + httpResponse.getStatusLine().getStatusCode());
        }
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(responseBody);
        return actualObj.get("meta").get("token").toString().replaceAll("\"", "");
    }

    public static String getPin(Context context, String tempUserId) throws Exception {
        HttpResponse httpResponse = Api.Get.qa_users_temp_users_pin(context.getApiBaseUrl(), tempUserId, context.getQaToken());
        if (httpResponse.getStatusLine().getStatusCode() > 299) {
            throw new Exception("getPin httpResponse code is: " + httpResponse.getStatusLine().getStatusCode());
        }
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(responseBody);
        return actualObj.get("meta").get("pin").toString().replaceAll("\"", "");
    }

    public static String getItemFromLocalStorage(WebDriver webDriver, String key) {
        JavascriptExecutor js = ((JavascriptExecutor) webDriver);
        return (String) js.executeScript(String.format(
                "return window.localStorage.getItem('%s');", key));
    }

    public static JsonNode getUsersMe(Context context, String token) throws Exception {
        HttpResponse httpResponse = Api.Get.users_me(context.getApiBaseUrl(), token);
        if (httpResponse.getStatusLine().getStatusCode() > 299) {
            throw new Exception("getPin getUsersMe code is: " + httpResponse.getStatusLine().getStatusCode());
        }
        String responseBody = EntityUtils.toString(httpResponse.getEntity());
        ObjectMapper mapper = new ObjectMapper();
        JsonNode actualObj = mapper.readTree(responseBody);
        return actualObj;
    }

    public static List<Organization> getOrganizatons(Context context, User user) throws Exception {
        List<Organization> organizationList = new ArrayList<Organization>();
        JsonNode node = getUsersMe(context, user.getToken());
        JsonNode includes = node.get("included");
        for (int i = 0; i < includes.size(); i++) {
            JsonNode currentNode = includes.get(i);
            if (!currentNode.get("type").toString().replaceAll("\"", "").equals("organizations")) {
                continue;
            }
            Organization organization = new Organization();
            organization.setAutogenerated(currentNode.get("attributes").get("auto_generated").asInt() == 1);
            organization.setId(currentNode.get("id").toString().replaceAll("\"", ""));
            organization.setName(currentNode.get("attributes").get("name").toString().replaceAll("\"", ""));
            organization.setSubdomain(currentNode.get("attributes").get("subdomain").toString().replaceAll("\"", ""));

            List<String> emailDomains = new ArrayList<String>();
            JsonNode emailDomainsNode = currentNode.get("attributes").get("email_domains");
            for (int j = 0; j < emailDomainsNode.size(); j++) {
                emailDomains.add(emailDomainsNode.get(j).toString().replaceAll("\"", ""));
            }
            organization.setEmailDomains(emailDomains);
            organizationList.add(organization);
        }
        return organizationList;
    }
}
