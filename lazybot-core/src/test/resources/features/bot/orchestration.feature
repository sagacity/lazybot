Feature: Bot Orchestration

  Scenario: Service start and stop
    Given the following installations are registered
    | oauthId | roomId |
    | 1       | R1     |
    | 2       | R2     |
    Then the installation store should contain 2 installations

    When the "start" event is received
    Then all registered installations should be started with the right context

    When the "stop" event is received
    Then all registered installations should be stopped

  Scenario: unregister an installation
    Given the following installations are registered
      | oauthId | roomId |
      | 1       | R1     |
      | 2       | R2     |

    When the registered installations are unregistered
    Then the plugins should have received an unregister call
    Then the installation store should be empty