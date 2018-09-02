package com.beseech.http;

import com.beseech.model.User;
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

import java.util.ArrayList;
import java.util.List;

public class Api {
    public static class Post {
        public static HttpResponse temp_organizations(String baseUrl, String subdomain) throws Exception {
            String body = String.format("{\"data\": {\"type\": \"temp-organizations\", \"attributes\": {\"subdomain\": \"%s\"} } }", subdomain);
            HttpResponse httpResponse = sendRequest("POST",
                    body,
                    baseUrl + "/temp-organizations",
                    "Content-Type, application/json");
            return httpResponse;
        }
        public static HttpResponse temp_organizations_temp_users(String baseUrl, String tempOrgId, User user) throws Exception {
            String body = String.format("{\"data\": {\"type\": \"temp-users\", \"attributes\": { \"first_name\": \"%s\", \"last_name\": \"%s\", \"email\": \"%s\", \"password\": \"%s\"}}}",
                    user.getFirstName(), user.getLastName(), user.getEmail(), user.getPassword());
            HttpResponse httpResponse = sendRequest("POST",
                    body,
                    baseUrl + "/temp-organizations/" + tempOrgId + "/temp-users",
                    "Content-Type, application/json");

            return httpResponse;
        }
        public static HttpResponse temp_organizations_temp_users_confirm(String baseUrl, String tempOrgId, String tempUserId, String pin) throws Exception {
            String body = String.format("{\"meta\": {\"pin\": \"%s\"}}", pin);
            HttpResponse httpResponse = sendRequest("POST",
                    body,
                    baseUrl + "/temp-organizations/" + tempOrgId + "/temp-users/" + tempUserId + "/confirm",
                    "Content-Type, application/json");

            return httpResponse;
        }
        public static HttpResponse organizations_users_invite(String baseUrl, String orgId, String[] emails, String token) throws Exception {
            String emailList = "";
            for (int i = 0; i < emails.length; i++) {
                emailList += emailList + emails[i];
            }
            System.out.println(String.format("Direct invite user[orgId:%s, email:%s]", orgId, emailList));

            String body = String.format("{\"data\": {\"type\": \"users\", \"attributes\": { \"emails\": [ \"%s\" ] } } }", emailList);
            HttpResponse httpResponse = sendRequest("POST",
                    body,
                    baseUrl + "/organizations/" + orgId + "/users/invite",
                    "Content-Type, application/json", "Authorization, Bearer " + token);
            return httpResponse;
        }
    }

    public static class Get {
        public static HttpResponse qa_users_organizations_users_token(String baseUrl, String orgId, String userId, String qaToken) throws Exception {
            HttpResponse httpResponse = sendRequest("GET",
                    "",
                    baseUrl + "/qa-users/organizations/" + orgId + "/users/" + userId + "/token",
                    "Content-Type, application/json", "Authorization, Bearer " + qaToken);

            return httpResponse;
        }
        public static HttpResponse qa_users_temp_users_pin(String baseUrl, String tempUserId, String qaToken) throws Exception {
            HttpResponse httpResponse = sendRequest("GET",
                    "",
                    baseUrl + "/qa-users/temp-users/" + tempUserId + "/pin",
                    "Content-Type, application/json", "Authorization, Bearer " + qaToken);

            return httpResponse;
        }
        public static HttpResponse users_me(String baseUrl, String token) throws Exception {
            HttpResponse httpResponse = sendRequest("GET",
                    "",
                    baseUrl + "/users/me?include=organizations",
                    "Content-Type, application/json", "Authorization, Bearer " + token);
            return httpResponse;
        }
    }

    public static HttpResponse sendRequest(String requestType, String body, String url,
                                           String... headers) throws Exception {
        try {

            HttpGet getRequest = null;
            HttpPost postRequest;
            HttpPut putRequest;
            HttpDelete delRequest;
            HttpResponse response = null;
            HttpClient client = HttpClientBuilder.create().build();


            // Collecting Headers
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            for (String arg : headers) {

//Considering that you are applying header name and values in String format like this "Header1,Value1"

                nvps.add(new BasicNameValuePair(arg.split(",")[0], arg
                        .split(",")[1]));
            }

            if (requestType.equalsIgnoreCase("GET")) {
                getRequest = new HttpGet(url);
                for (NameValuePair h : nvps) {
                    getRequest.addHeader(h.getName(), h.getValue());
                }
                response = client.execute(getRequest);
            }

            if (requestType.equalsIgnoreCase("POST")) {
                postRequest = new HttpPost(url);
                for (NameValuePair h : nvps) {
                    postRequest.addHeader(h.getName(), h.getValue());
                }

                StringEntity requestEntity = new StringEntity(body, "UTF-8");
                postRequest.setEntity(requestEntity);
                response = client.execute(postRequest);
            }

            if (requestType.equalsIgnoreCase("PUT")) {
                putRequest = new HttpPut(url);
                for (NameValuePair h : nvps) {
                    putRequest.addHeader(h.getName(), h.getValue());
                }
                StringEntity requestEntity = new StringEntity(body, "UTF-8");
                putRequest.setEntity(requestEntity);
                response = client.execute(putRequest);
            }

            if (requestType.equalsIgnoreCase("DELETE")) {
                delRequest = new HttpDelete(url);
                for (NameValuePair h : nvps) {
                    delRequest.addHeader(h.getName(), h.getValue());
                }
                response = client.execute(delRequest);
            }

            return response;

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
