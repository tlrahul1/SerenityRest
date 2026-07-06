Feature: Get Products

@getproducts
@Sprint1
Scenario: Verify FakeStore GET Products API Response
Given user call web GET API with "getproducts" endpoint
Then user verifies the web api expected status code as "200"


@getproducts
@Sprint1
Scenario: Verify FakeStore GET Products API Response with query params
Given user call web GET API with "getproducts" endpoint and query params "?limit=5"
Then user verifies the web api expected status code as "200"
