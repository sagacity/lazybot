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

  Scenario: Register an installation through the server
    Given the following "installed information" coming in from HipChat
      | oauthId | oauthSecret | roomId |
      | 1       | S1          | R1     |

    Then The following installations should be registered
      | oauthId | oauthSecret | roomId |
      | 1       | S1          | R1     |

  Scenario: Unregister an installation through the server
    Given the following installations are registered
      | oauthId |
      | 1       |
      | 2       |

    When there is an unregistration for oauthId 1 coming in from HipChat

    Then The following installations should be registered
      | oauthId |
      | 2       |

  Scenario: incoming message
    Given the following installations are registered
      | oauthId | roomId |
      | 1       | R1     |
      | 2       | R2     |
    And the "start" event is received

    When the following messages come in from HipChat
      | oauthId | message        |
      | 1       | /lazybot Hello |

    Then the following messages are dispatched
      | roomId | message        |
      | R1     | /lazybot Hello |

  Scenario: start and stop with broken plugin
    Given plugin "foo" is broken
    And the following installations are registered
      | oauthId | roomId |
      | 1       | R1     |
      | 2       | R2     |
    Then the installation store should contain 2 installations
    And every installation should contain 1 plugin
