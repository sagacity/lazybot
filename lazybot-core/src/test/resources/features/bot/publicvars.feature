Feature: Public variables

  Scenario: Basic combining
    Given for plugin "foo" I set variable "a" to "1"
    And for plugin "bar" I set variable "b" to "2"
    Then the combined variables should be
      | a | 1 |
      | b | 2 |

  Scenario: Overwriting
    Given for plugin "foo" I set variable "a" to "1"
    And for plugin "bar" I set variable "a" to "5"
    Then the combined variables should be
      | a | 5 |

  Scenario: Overwriting
    Given for plugin "foo" I set variable "a" to "1"
    And for plugin "foo" I set variable "b" to "2"
    And for plugin "bar" I set variable "a" to "<removed>"
    Then the combined variables should be
      | b | 2 |