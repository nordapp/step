The PropertyService is used to set the properties to each mandator.

To stop a mandator use the following command. The '<mandator>' is the
id of the mandator (example: 'IDS')

karaf@root>ob:mandator-stop '<mandator>'

To start the mandator edit the config-file and set
the property 'init' to false. The ConfigAdmin starts the mandator automatic.

This command shows you the started mandator.

karaf@root>ob:mandator-start '<mandator>'