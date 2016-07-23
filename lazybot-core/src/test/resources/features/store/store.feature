Feature: Store

  Background:
    Given I get an empty store

  Scenario: Can store multiple items
    Given I store value "foo" with key "a"
    And I store value "bar" with key "b"
    Then I should have stored 2 items
    And key "a" should have value "foo"
    And key "b" should have value "bar"

  Scenario: Can store stuff persistently
    Given I store value "foo" with key "a"
    When I get a store
    And key "a" should have value "foo"

  Scenario: Can delete items
    Given I store value "foo" with key "a"
    When I delete key "a"
    Then I should have stored 0 items
