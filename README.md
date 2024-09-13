# RestAssuredPractice

This project uses Rest-Assured and Java to perform automated tests by connecting to mockapi.

## Description

The tests are defined in the Gherkin format, covering the following features:

- Feature: change-phone-number-specific-client
  - Description: Updating the phone number of a specific client.
- Feature: get-list-of-active-resources
  - Description: Updating of active resources.
- Feature: update-and-delete-new-client
  - Description: Creating, updating and deleting a new client.
- Feature: update-last-created-resource
  - Description: Update of the last resource created.

## CONFIGURATION

Before running the tests, make sure you have the following installed:

- Java 17
- Maven

In addition to having a project created in mockapi in order to have an api to consume the necessary information.

After having installed the above mentioned:

- Clone the repository
  git clone https://github.com/tu_usuario/nombre_del_repositorio.git
- Enter the “Constants” file and modify the constants (paths, urls and more) used according to your requirement.
- Install dependencies (by console with "mvn install" or by IDE in POM file)

## EXECUTE TESTS

To run the tests, note that the following tags are used in the project

- @active for the active features
- @smoke for scenarios with main flows
- @clients to refer to the features concerning clients
- @resources to refer to the features concerning resources

Now, in the “TestRunner” file that is inside the “runner” folder, you can edit the name of the labels that have the tests that we want to be executed, by default this: "@clients or @resources" to execute the tests of the features related to the clients and to the resources.

And finally to run the tests, run "TestRunner" file.