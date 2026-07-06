Feature: Create Cart API

  @createcart
  @Sprint1
  Scenario: Verify FakeStore POST Create Cart API Response

    Given user call web POST API with "createcart" endpoint
    Then user verifies the web api expected status code as "201"