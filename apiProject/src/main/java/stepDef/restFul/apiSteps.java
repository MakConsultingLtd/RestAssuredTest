package stepDef.restFul;


import com.jayway.restassured.RestAssured;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.specification.RequestSpecification;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.hamcrest.Matchers;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import static java.lang.ClassLoader.getSystemResourceAsStream;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.core.IsCollectionContaining.hasItem;
import static org.hamcrest.core.IsEqual.equalTo;

public class apiSteps {
    private String endpoint;
    protected Response response;
    protected RequestSpecification request;

    private void setEndPoint (String url) {
        this.endpoint = url;
    }

    private String getEndPoint() {
        return this.endpoint;
    }

    private RequestSpecification getRequest() {
        return this.request;
    }

    /* Purpose - To set up the endpoint */
    @Given("^I set the API endpoint as \"([^\"]*)\"$")
    public void I_call_the_end_points(String url) {
        setEndPoint(url);
        this.request = RestAssured.given();
    }

    /* Purpose - To construct the parameters for the request */
    @Given("I construct the following Request parameters$")
    public void setup_request_parameters(Map<String, String> parameters) {
        this.request.params(parameters);
    }

    /* Purpose - To construct the path parameters for the request */
    @Given("I construct the following Request path$")
    public void setup_request_path_parameters(Map<String, String> parameters) {
        this.request.pathParameters(parameters);
    }

    /* Purpose - To send the construced request*/
    @When("I send request to the given end point$")
    public void send_request_to_endpoint() {
        request.log().all();
        response = request.when().get(getEndPoint());
    }

    /* Purpose - To validate the response code*/
    @Then("I verify the Response status code is (\\d+)[ ]*$")
    public void verify_status_code(int statusCode) {
        response.then().statusCode(statusCode);
    }

    /* Purpose - To validate the response header contenting certain property and value */
    @Then("I verify the Response header property called \"([^\"]*)\" has the value \"([^\"]*)\"")
    public void verify_response_header_property_value(String property, String propertyValue) {
        response.then().header(property, equalTo(propertyValue));
    }

    /* Purpose: To identify the Promotion.Name = Gallery, then ensure the relevant description field contains the input text*/
    @Then("I verify the Response property has the Promotions element with Property Name equals Gallery has a Description that contains the text \"([^\"]*)\"")
    public void verify_response_body_property_element(String descriptionText) {
        String jsonResponse = response.body().jsonPath().getString("Promotions.findAll{Promotions -> Promotions.Name == 'Gallery'}.Description");
        assertTrue(jsonResponse.contains(descriptionText));
    }

    /* Purpose: To validate the response having the given property and integer value  */
    @Then("I verify the Response property called \"([^\"]*)\" has the integer value (\\d+)[ ]*$")
    public void verify_response_body_property_integer(String property, int propertyValue) {
        response.then().body(property, equalTo(propertyValue));
    }

    /* Purpose: To validate the response having the given property and boolean value  */
    @Then("I verify the Response property called \"([^\"]*)\" has the boolean value \"([^\"]*)\"")
    public void verify_response_body_property_integer(String property, Boolean propertyValue) {
        response.then().body(property, equalTo(propertyValue));
    }

    /*Purpose: To ensure the response time is less than the given value*/
    @Then("I verify the Response time is less than (\\d+)[ ]*$")
    public void verify_response_time(int timelimit) {
        response.then().time(Matchers.lessThan((long) timelimit));
    }

    /*Purpose: To ensure the response is matching the baseline Json file*/
    @Then("I compare the entire Json Response body is matching the baseline Json file \"([^\"]*)\"$")
    public void verify_response_body_by_file(String baselineJsonFile) throws IOException {
        String actualResponseBody = response.getBody().asString();
        String baselineResponseBody = getFileString(getSystemResourceAsStream(baselineJsonFile));

        try {
            JSONAssert.assertEquals(baselineResponseBody, actualResponseBody, false);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public String getFileString(InputStream inputStream) throws IOException {
        int ch;
        StringBuilder strBuilder = new StringBuilder();
        try {
            while ((ch = inputStream.read()) != -1)
                strBuilder.append((char) ch);
        }
        finally {
            inputStream.close();
        }
        return strBuilder.toString();

    }

}