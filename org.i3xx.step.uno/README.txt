The locations with 'data/step/bin/bundle' are set (server-side-script):

- DeployServiceImpl
- EngineBaseServiceImpl
- InitializeMandatorImpl

The locations with 'data/page/file/bundle' are set (client-side-script):

- DeployServiceImpl

Here are a few examples of commands:

Cleans and resets the sessions of the mandator VDV.

karaf@root>ob:uno-cleanup 'VDV' true

Creates a new session of the mandator 'VDV' with the user 'root' and
reports the session number. You can set a session number after the user
('VDV' 'root' '15').

karaf@root>ob:uno-session-login 'VDV' root

Runs a step in the session of the mandator 'VDV'. The mandator must contain
exact one workflow.

karaf@root>ob:uno-session-run 'VDV' -7554802676215120015

Calls a function in the session of the mandator 'VDV'. Note: The call uses
the symbolic name of a card. The card contains the call to the script runtime.
Card-SymbolicName: group-1.artifact-3.hello
Card-Function: i3xx.group-1.artifact-3.hello
The script defines the function 'i3xx.group-1.artifact-3.hello'

karaf@root>ob:uno-session-call 'VDV' -7554802676215120015 'group-1.artifact-3.hello' true

Logout from the session of the mandator 'VDV'.

karaf@root>ob:uno.session-logout 'VDV' -7554802676215120015

And clear the session of the mandator 'VDV'

karaf@root>ob:uno.session-clear 'VDV' -7554802676215120015


