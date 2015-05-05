A. Needs the EventAdmin to be installed

Show all known features in alphabetical order (-o)

karaf@root>feature:list -o

Install the EventAdmin service

karaf@root>feature:install eventadmin

B. The locations with 'data/file/bundle' are set (client-side-script):

- PropertyServiceImpl
- ResourceServiceImpl

C. The end of a session

The session ends by sending an event. Why?

Each session is registered as a service. If the mandator ends each session
of this mandator must end too. The mandator signals this by sending an event.

A single session may end by calling destroy() to the service representing
this session. Then the session throws also an event.