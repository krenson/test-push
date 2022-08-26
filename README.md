This is a project for AEM-based Le Forem Horizons Emploi Site.

## Modules

The main parts of the template are:

- core: Java bundle containing all core functionality like OSGi services, listeners or schedulers, as well as component-related Java code such as servlets or request filters.
- ui.apps: contains the /apps (and /etc) parts of the project, ie JS&CSS clientlibs, components, and templates
- ui.content: contains sample content using the components from the ui.apps
- ui.config: contains runmode specific OSGi configs for the project
- ui.frontend: an optional dedicated front-end build mechanism (Angular, React or general Webpack project)
- it.tests: Java based integration tests
- ui.tests: Selenium based UI tests
- all: a single content package that embeds all of the compiled modules (bundles and content packages) including any vendor dependencies

## How to build locally (localhost)

First of all, you need to install the AEM local runtime via aem-sdk:

- https://experienceleague.adobe.com/docs/experience-manager-learn/cloud-service/local-development-environment-set-up/aem-runtime.html?lang=en

In order to use the dispatcher locally, you need to see the dispatcher/README file

To build all the modules run in the project root directory the following command with Maven 3:

    mvn clean install

To build all the modules and deploy the `all` package to a local instance of AEM, run in the project root directory the following command:

    mvn clean install -PautoInstallSinglePackage

Or to deploy it to a publish instance, run

    mvn clean install -PautoInstallSinglePackagePublish

Or alternatively

    mvn clean install -PautoInstallSinglePackage -Daem.port=4503

Or to deploy only the bundle to the author, run

    mvn clean install -PautoInstallBundle

Or to deploy only a single content package, run in the sub-module directory (i.e `ui.apps`)

    mvn clean install -PautoInstallPackage

After that:

- Navigate to Package Manager on your local AEM instance in order to check that all packages have been deployed successfully: http://localhost:4502/crx/packmgr/index.jsp.
- Navigate to AEM Web Console Bundles on your local AEM instance and search leforemhe in order the Java bundle has been deployed susscessfully as well: http://localhost:4502/system/console/.

## How to build on the Dev environment (aem.realdolmen.com)

To build all the modules and deploy packages to aem.realdolmen.com instances of AEM, run in the project root directory the following command:

Working with develop branch:

    mvn clean install -PautoDevInstallPackage
    mvn clean install -PautoDevInstallPackagePublish

Or to deploy only a single content package:

    mvn clean install -PautoDevInstallSinglePackage
    mvn clean install -PautoDevInstallSinglePackagePublish

If you have an issue to install the package via maven command you can use the below script after to run the above command:

    curl-install-package-on-dev.sh

Working with develop branch:

    mvn clean install -PautoDevInstallBundle
    mvn clean install -PautoDevInstallPackage
    mvn clean install -PautoDevInstallPackagePublish

Deploy only a single content package does not work with develop.

There is a script to do the before actions for develop:

    mvn-install-package-on-dev.sh

## Testing

There are three levels of testing contained in the project:

### Unit tests and coverage

This show-cases classic unit testing of the code contained in the bundle. To
test, execute:

    mvn clean verify -pl core

You can find the coverage report here: core/target/jacoco-ut/index.html

### Quality tests

You can run SonarQube in order to get code analysis:

    mvn clean verify sonar:sonar -Psonar

You can get the report via https://sonarqube.realdolmen.com

## ClientLibs

The frontend module is made available using an [AEM ClientLib](https://helpx.adobe.com/experience-manager/6-5/sites/developing/using/clientlibs.html). When executing the NPM build script, the app is built and the [`aem-clientlib-generator`](https://github.com/wcm-io-frontend/aem-clientlib-generator) package takes the resulting build output and transforms it into such a ClientLib.

A ClientLib will consist of the following files and directories:

- `css/`: CSS files which can be requested in the HTML
- `css.txt` (tells AEM the order and names of files in `css/` so they can be merged)
- `js/`: JavaScript files which can be requested in the HTML
- `js.txt` (tells AEM the order and names of files in `js/` so they can be merged
- `resources/`: Source maps, non-entrypoint code chunks (resulting from code splitting), static assets (e.g. icons), etc.

## Maven settings

The project comes with the auto-public repository configured. To setup the repository in your Maven settings, refer to:

    http://helpx.adobe.com/experience-manager/kb/SetUpTheAdobeMavenRepository.html

## Cloud Manager seetings

    https://docs.adobe.com/content/help/en/experience-manager-cloud-manager/using/introduction-to-cloud-manager.html

### Password-Protected Maven Repository Support

Reference this from the .cloudmanager/maven/settings.xml file:

    aio cloudmanager:set-pipeline-variables PIPELINEID --secret CUSTOM_MYCO_REPOSITORY_PASSWORD secretword

### Pipeline Variables

Current variables can be listed:

    aio cloudmanager:list-pipeline-variables PIPELINEID

To set a variable using the CLI, run a command like:

    aio cloudmanager:set-pipeline-variables PIPELINEID --variable MY_CUSTOM_VARIABLE test

### Installing Additional System Packages

You can find an example in the profile section on the root pom.xml file.

Installing a system package in this manner does not install it in the runtime environment used for running Adobe Experience Manager. If you need a system package installed on the AEM environment, contact your Customer Success Engineers (CSE).

### Pushing Your Branch and run Adobe Cloud Manager pipeline for release v1 and v2 (deprecated)

NOTA: It is recommended pushing the code via Azure DevOps pipeline. See next section.
The specific URL, along with your credentials, will be provided to your by your Customer Success Engineering during Cloud Manager onboarding.

Just one time:

    git remote add adobe https://git.cloudmanager.adobe.com/realdolmenptrsd/RealdolmenGFIProgram-p33330/

Then, for each deploy on stage is required to run:

    git checkout master
    git merge develop
    git push
    git push adobe

After that, you can go to Cloud Manager and build the pipeline:

    https://experience.adobe.com/#/@forem/cloud-manager/home.html/program/19705

In order to create a tag, you can run:

    git tag X.Y.Z
    git push --tag
    git push adobe --tag

### Integration between GitLab repository and Azure DevOps repository (temporally)

NOTA: It is required this integration while gitlab repository is not turn off

In order to move the GitLab repository to Azure DevOps repository will be required maintain both
repository during a short time to sync both repositories.

Just one time:

    git remote leforemhe https://leforemhe@dev.azure.com/leforemhe/Canaux%20Num%C3%A9riques%20-%20Adobe%20DXP/_git/Adobe-DXP

Then, for each deploy on stage is required to run:

    git checkout develop
    git push leforemhe

### Running Azure DevOps pipelines (non-production and production)

#### Non-production pipeline

It is required to have access to https://dev.azure.com/leforemhe/Canaux%20Num%C3%A9riques%20-%20Adobe%20DXP/_build

After that, you can choose Adobe-DXP non-production pipeline and run it manually which build the project and pushing
the code in the Adobe Git repository (develop branch) and the Cloud Manager non-production pipeline will be triggered
automatically.

Adobe-DXP non-production pipeline is also triggered automatically when code is pushed in
the Azure DevOps repository on the develop branch via a completed pull request.

Check if Cloud Manager non-production pipeline has been successful
via https://experience.adobe.com/#/@forem/cloud-manager/home.html/program/19705

#### Production pipeline

In order to deploy in production is required to use the Azure DevOps production pipeline
which pushing the code in the Adobe Git repository (master branch) and then the Cloud
Manager production pipeline should be triggered manually.

Check if the Cloud Manager production pipeline has been successful
via https://experience.adobe.com/#/@forem/cloud-manager/home.html/program/19705

## Fast Development

You can use those options to skip some modules:

    -DskipTests=true
    -Dskip.ui.frontend=true
    -Dskip.aemanalyser=true

### Frontend (ui.frontend and ui.apps)

It has been added fast frontend development:

    cd ui.frontend
    npm run watch
    npm run aemsync

### Frontend (ui.content)

If you change the content of your local AEM instance you can use IntelliVault->Push to CRX (or Repo tool) with your http://localhost:4502 author instance

Also, you can run the next command:

    cd ui.content
    mvn clean install -PautoInstallPackage -DskipTests=true

You can skip ui.frontend with:

    mvn clean install -PautoInstallPackage -DskipTests=true -Dskip.ui.frontend=true

### Bundle (Java)

    cd core
    mvn clean install -PautoInstallBundle -DskipTests=true

### Configuration (OSGi)

    cd ui.config
    mvn clean install -PautoInstallPackage -DskipTests=true

## Security checklist

You can see the status:

- https://adobe.sharepoint.com/sites/amsaembasic/etspubli/SitePages/CQMS_Runbook.aspx

References:

- https://experienceleague.adobe.com/docs/experience-manager-64/administering/security/security-checklist.html?lang=en#main-security-measures​
- https://experienceleague.adobe.com/docs/experience-manager-dispatcher/using/getting-started/security-checklist.html?lang=en#use-the-latest-version-of-dispatcher​

## leforemhe Servlets

- RTE
  - Example: https://author-forem-prod.adobemsbasic.com/content/leforemhe/fr/partenaires/jcr:content.vanitypath.leforemhe.json
- Image core component:
  - The AdaptiveImageServlet is responsible for image processing and streaming when image core component is used and look like _/image.coreimg._/\*

## Set up local AEM Runtime

- Download the aem-sdk software to install both the author and publish instances : https://experience.adobe.com/#/downloads/content/software-distribution/en/aemcloud.html
- Remember installing all dependencies and libraries on the publish instance as well which is located into packages/ui.apps.packages. See the README file about it as well.

### Set up local AEM Author service

- Create the folder ~/aem-sdk/author
- Copy the Quickstart JAR file to ~/aem-sdk/author and rename it to aem-author-p4502.jar
- Start the local AEM Author Service: java -jar aem-author-p4502.jar (password: admin)
- Access the local AEM Author Service at http://localhost:4502 in a Web browser

### Set up local AEM Publish service

- Create the folder ~/aem-sdk/publish
- Copy the Quickstart JAR file to ~/aem-sdk/publish and rename it to aem-publish-p4503.jar
- Start the local AEM Author Service: java -jar aem-publish-p4503.jar (password: admin)
- Access the local AEM Author Service at http://localhost:4503 in a Web browser

### Replication agent

- Login to the Author service and navigate to http://localhost:4502/etc/replication/agents.author.html.
- Click Default Agent (publish) to open the default Replication agent.
- Click Edit to open the agent’s configuration.
- Under the Settings tab, enabled the check true
- Under the Transport tab, update the following fields:  
   URI - http://localhost:4503/bin/receive?sling:authRequestLogin=1
  User/password - admin/admin
- Click Ok to save the configuration and enable the Default Replication Agent.

You can now make changes to content on the Author service and publish them to the Publish service.

### Stopping a local AEM runtime

In order to stop a local AEM runtime, either AEM Author or Publish service, open the command line window that was used to start the AEM Runtime, and tap Ctrl-C

### Starting a local AEM runtime again

In order to start a local AEM runtime, either AEM Author or Publish service, open a command-line window:

~/aem-sdk/author/bin/start.bat

~/aem-sdk/publish/bin/start.bat

### Monitoring a local AEM runtime

In order to monitor a local AEM runtime, either AEM Author or Publish service, open a command-line Git Bash:

~/aem/author/logs/tail -f error.log

~/aem/publish/logs/tail -f error.log

### Local Dispatcher Runtime

Docker Desktop on windows is required.

See dispatcher/README file.

### Storybook configuration

#### Running it locally

Requirements:

    https://visualstudio.microsoft.com/vs/older-downloads/#visual-studio-2019-and-other-products
    https://www.python.org/downloads/release/python-3101/
    Node 10.13.0
    In Window server (dev environment) was required to define the variable SET VCTargetsPath=C:\Program Files (x86)\Microsoft Visual Studio\2019\Professional\MSBuild\Microsoft\VC\v160\

    cd ui.frontendv2

There are two packages that must be installed only locally, so do not add it on the package.json file:

    npm install -g @storybook/aem-cli
    npm install -g @storybook/aem

After that you can run storybook:

    npm install
    npm run storybook

Install fiddler

    https://www.telerik.com/download/fiddler/fiddler4

When installed Fiddler

    Go to tools > HOSTS..
    Enable remapping
    Add this line: localhost:8080 publish.realdolmen.com
    Save
    Keep Fiddler running when using Storybook

Go to http://localhost:4501

#### Running in on dev environment

Requirements:

    https://visualstudio.microsoft.com/vs/older-downloads/#visual-studio-2019-and-other-products
    https://www.python.org/downloads/release/python-3101/
    Node 14.0.0
    In Window server (dev environment) was required to define the variable SET VCTargetsPath=C:\Program Files (x86)\Microsoft Visual Studio\2019\Professional\MSBuild\Microsoft\VC\v160\

The repository is located on the server: C:\Users\.AZU_Admin\AEM\aem-leforemhe-site

It has been created a storybook.bat file to stop and start it automatically. There is a link
on the Window Desktop.

You need to connect with the virtual machine via Remote Desktop Connexion with the
IP 40.68.132.238 and user .AZU_Admin. So, when new stories have been merged on develop branch,
then is required to run the storybook.bat file on the dev environment, which will pull
the develop branch, will stop the storybook server and start it from scratch again.

Go to http://publish.realdolmen.com:8081/storybook

## Releases leforemhe

0.0.1 In progress
