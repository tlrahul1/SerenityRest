Feature: Create Product

@createproduct
@Sprint1
Scenario: Verify FakeStore POST Create Product API Response
Given user call web POST API with "createproduct" endpoint
Then user verifies the web api expected status code as "201"


#@createproductx
#@Sprint1
#Scenario Outline: Verify web API response for createproduct with negative Scenarios and Bad Request
#When user call web api POST API after modifying "<attribute name>" with "<value>" with endpoint "createproduct","<operation>" and "<Data Type>"
#Then user verifies the web api Expected status code as "<Expected Status Code>" and Response Message as "<Message>"
#Examples:
#| attribute name | value | Expected Status Code | Message         | operation   | Data Type |
#| price          | -10   | 400                   | Invalid price   | changevalue | Integer   |
#| title          |       | 400                   | Title is required | modify    | String    |
