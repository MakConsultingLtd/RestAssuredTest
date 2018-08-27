Feature: Testing TM SandBox API

  Background: API
    Given I set the API endpoint as "https://api.tmsandbox.co.nz/v1/Categories/6327/Details.json?catalogue=false"
    When I send request to the given end point

  @API
  Scenario: AC1 - verifying Name = Carbon credits
    Then I verify the Response status code is 200
    And I verify the Response property called "CategoryId" has the integer value 6327

  @API
  Scenario: AC2 - verifying CanRelist = true
    Then I verify the Response status code is 200
    And I verify the Response property called "CanRelist" has the boolean value "true"

  @API
  Scenario: AC3 - verifying Promotion element has a Name property = Gallery and the description = 2x larger image
    Then I verify the Response status code is 200
    And I verify the Response property has the Promotions element with Property Name equals Gallery has a Description that contains the text "2x larger image"


  @API
  Scenario: Additional test - to verify the response is exactly the same as the baseline response Json file
    Then I verify the Response status code is 200
    And I compare the entire Json Response body is matching the baseline Json file "responseBaseline.json"
