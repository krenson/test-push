#!/bin/bash

read -p "Press enter to continue"

echo "Deploying the AEM packages (config, app and content)..."
mvn clean install -PautoDevInstallSinglePackage
echo "Done."

read -p "Press enter to close the window"


