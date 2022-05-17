#!/bin/bash

RELEASE=2.0.0
PACKAGE="all/target/leforemhe.all-$RELEASE.zip"
BUNDLE="core/target/leforemhe.core-$RELEASE.jar"
FILENAME=`basename $PACKAGE`
CURL="curl -u admin:Realdolmen.2O19"
HOST="http://aem.realdolmen.com"
PACKAGE_PATH="/crx/packmgr/service.jsp"
BUNDLE_PATH="/system/console/bundles"

BUNDLEHOST=http://$HOST/system/console/bundles

echo "Installing bundle and packages via CURL command for leforemhe $release ..."

echo $CURL -F file=@$PACKAGE -F name=$FILENAME -F force=true -F install=true $HOST$PACKAGE_PATH
$CURL -F file=@$PACKAGE -F name=$FILENAME -F force=true -F install=true $HOST$PACKAGE_PATH

echo "$CURL -v -F action=install -F bundlestart=start -F bundlestartlevel=20 -F bundlefile=@$BUNDLE $HOST$BUNDLE_PATH"
$CURL -v -F action=install -F bundlestart=start -F bundlestartlevel=20 -F bundlefile=@$BUNDLE $HOST$BUNDLE_PATH

read -p "Press enter to close the window"

